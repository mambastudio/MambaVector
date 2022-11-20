/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.components;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.effect.Blend;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DisplacementMap;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.MotionBlur;
import javafx.scene.effect.Reflection;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import mamba.BuilderController;
import mamba.base.MambaCanvas;
import mamba.base.MambaShape;
import mamba.base.engine.MEngine;
import mamba.base.engine.shape.MPath;
import mamba.beans.MBeanPropertyItem;
import mamba.beans.MBeanPropertySheet;
import mamba.beans.MBeanPropertyUtility;
import mamba.beans.editors.MDefaultEditorFactory;
import mamba.util.MObservableStack;
import mamba.util.MambaUtility;

/**
 *
 * @author user
 */
public final class ResizeableCanvas extends Region implements MambaCanvas<MEngine, VBox>
{
    private final Canvas canvas;
    private MEngine engine2D = null;
    private long lastClickTime = 0;
      
    private VBox propertyDisplayPanel = null;
    
    private VBox effectPropertyDisplayPanel = null;
    private ComboBox<Effect> effectTypeComboBox = null;
    private ChangeListener<Effect> effectTypeComboBoxListener = null;
    
    private MObservableStack preparatoryStack = null;
    
    private final BuilderController controller;   
    
    
        
               
    public ResizeableCanvas(BuilderController controller, double width, double height) 
    {
        this.preparatoryStack = new MObservableStack(); 
        this.controller = controller; 
        
        //set the width and height of this and the canvas as the same
        setWidth(width);
        setHeight(height);
        canvas = new Canvas(width, height);
        //add the canvas as a child
        getChildren().add(canvas);
        //bind the canvas width and height to the region
        canvas.widthProperty().bind(this.widthProperty());
        canvas.heightProperty().bind(this.heightProperty());
        canvas.widthProperty().addListener((o, oV, nv)->{
            engine2D.draw();
        });
        canvas.heightProperty().addListener((o, oV, nv)->{
            engine2D.draw();
        });
        
        //effect combobox for shape
        effectTypeComboBoxListener = (o, ov, nv)-> setShapeEffect(nv);       
        
        this.setOnMouseClicked(this::mouseClicked);
        this.setOnMouseDragged(this::mouseDragged);
        this.setOnMousePressed(this::mousePressed);
    }
    
    private void setShapeEffect(Effect effect)
    {
        if(engine2D.isSelected())
        {
            engine2D.getSelected().setEffect(effect);
            initEffectPropertySheet(engine2D.getSelected());
            engine2D.draw();
        }
        else
        {
            initEffectPropertySheet(null);
        }
    }
    
    public GraphicsContext getGraphicsContext2D() {
        return canvas.getGraphicsContext2D();
    }

    @Override
    public void setEngine2D(MEngine engine2D) {
        this.engine2D = engine2D;
        this.engine2D.setGraphicsContext(getGraphicsContext2D());
        //what happens if shape is selected -> init properties, effect editors
        this.engine2D.getSelectionModel().getSelectionProperty().addListener((o, ov, nv)->{
            if(nv != null)
            {
                initPropertySheet(engine2D.getSelected());                
                initEffectList(engine2D.getSelected().getEffect()); //init fresh combobox effect list
                controller.selectLayerTreeView(engine2D.getSelected());
            }
            else
            {
                initPropertySheet(null);                
                initEffectList(null); //init fresh combobox effect list
                controller.selectLayerTreeView(null);
            }
        });
    }

    @Override
    public MEngine getEngine2D() {
        return engine2D;
    }

    public void mouseClicked(MouseEvent e)
    {
        boolean isDoubleClick = isDoubleClick(500);
    }
    
    public void mousePressed(MouseEvent e)
    {            
        if(engine2D.isSelected() && engine2D.getSelectionModel().getSelected() instanceof MPath)
        {            
            if(controller.isPenToolSelected())
            {
                MPath path = (MPath) engine2D.getSelectionModel().getSelected();
                path.addLineTo(e.getX(), e.getY());
                return;
            }
        }
            
        //new selection
        Point2D p = new Point2D(e.getX(), e.getY());
        engine2D.hitSelect(p);
        
        //init new shape for dragging 
        if(engine2D.isSelected())
        {            
            engine2D.getSelected().setOffset(p.subtract(engine2D.getSelected().getTranslate()));               
            engine2D.draw();
        }
        else
            engine2D.draw();
        
        
    }
    
    public void mouseDragged(MouseEvent e)
    {
        if(e.isPrimaryButtonDown() && engine2D.isSelected())
        {        
            if(engine2D.isSelected() && engine2D.getSelectionModel().getSelected() instanceof MPath)
                if(controller.isPenToolSelected())
                    return;
            
            Point2D p = new Point2D(e.getX(), e.getY());
            engine2D.getSelected().translate(p);    
            engine2D.getSelected().updateDragHandles(null);
            Platform.runLater(()->engine2D.draw());           
        }        
    }
    
    public void select(MambaShape shape)
    {
        //is shape in engine
        if(MambaUtility.searchMambaShape(engine2D.getRoot(), shape) == null)
            return;
        
        //remove selection drawing first
        MambaShape previousSelected = engine2D.getSelected();        
        if(previousSelected != null){            
            engine2D.draw();
        }
        
        //new selection
        engine2D.setSelected(shape);
        
        //init new shape for dragging and init select state
        if(engine2D.isSelected())
        {            
            //engine2D.getSelected().setOffset(p.subtract(engine2D.getSelected().getTranslate()));               
            engine2D.draw();
        }
        else
            engine2D.draw();
    }
    
    public void initPropertySheet(MambaShape shape)
    {
        if(shape != null)
        {
            propertyDisplayPanel.getChildren().clear();
            ObservableList<MBeanPropertyItem> properties = MBeanPropertyUtility.getProperties(shape, ()-> engine2D.draw());
            MBeanPropertySheet propertySheet = new MBeanPropertySheet();            
            propertySheet.setFactory(new MDefaultEditorFactory());
            propertySheet.init(properties);
            propertyDisplayPanel.getChildren().add(propertySheet);
        }
        else
        {
            propertyDisplayPanel.getChildren().clear();
            effectTypeComboBox.setValue(null);
        }        
    }
    
    public void initEffectPropertySheet(MambaShape shape)
    {
       
        if(shape != null)
        {                        
            effectPropertyDisplayPanel.getChildren().clear();
            if(shape.getEffect() != null)
            {
                ObservableList<MBeanPropertyItem> properties = MBeanPropertyUtility.getProperties(shape.getEffect(), ()-> engine2D.draw());
                MBeanPropertySheet propertySheet = new MBeanPropertySheet();                
                propertySheet.setFactory(new MDefaultEditorFactory());
                propertySheet.init(properties);
                
                
                effectPropertyDisplayPanel.getChildren().add(propertySheet);
            }            
        }
        else
        {
            effectPropertyDisplayPanel.getChildren().clear();
        }
        
    }
        
    private boolean isDoubleClick(long intervalRangeMsec)
    {
        long currentClickTime = System.currentTimeMillis();
        long diff = 0;       
        if(lastClickTime!=0 && currentClickTime!=0)
            diff = currentClickTime - lastClickTime;        
        lastClickTime = currentClickTime;
        
        return diff < intervalRangeMsec && intervalRangeMsec > 0;
    }

    @Override
    public void setPropertyDisplayPanel(VBox propertyDisplayPanel) {
        this.propertyDisplayPanel = propertyDisplayPanel;
    }

    public void setEffectPropertyDisplayPanel(VBox effectPropertyDisplayPanel)
    {
        this.effectPropertyDisplayPanel = effectPropertyDisplayPanel;
    }
    
    public void setEffectTypeComboBox(ComboBox<Effect> effectTypeComboBox)
    {
        //in case we add same listener
        if(this.effectTypeComboBox != null)
            detachEffectComboBoxListener();
        
        this.effectTypeComboBox = effectTypeComboBox;       
        attachEffectComboBoxListener();
        initEffectList(null);
    }
    
    private void detachEffectComboBoxListener()
    {
        this.effectTypeComboBox.valueProperty().removeListener(effectTypeComboBoxListener);
    }
    
    private void attachEffectComboBoxListener()
    {
        this.effectTypeComboBox.valueProperty().addListener(effectTypeComboBoxListener);
    }
    
    private void initEffectList(Effect defaultEffect)
    {
        ObservableList<Effect> effectValueList = FXCollections.observableArrayList();     
        effectValueList.removeAll(effectValueList);
        effectValueList.add(new DropShadow());
        effectValueList.add(new BoxBlur());
        effectValueList.add(new Reflection());
        effectValueList.add(new Blend());
        effectValueList.add(new Bloom());
        effectValueList.add(new MotionBlur());
        effectValueList.add(new InnerShadow());
        effectValueList.add(new ColorAdjust());
        effectValueList.add(new Glow());
        effectValueList.add(new DisplacementMap());
        
        if(defaultEffect != null)
        {            
            int i = 0;
            for(Effect effect: effectValueList)
            {
                if(effect.getClass().equals(defaultEffect.getClass()))
                    effectValueList.set(i, defaultEffect);
                i++;
            }            
        }
        
        effectTypeComboBox.setItems(effectValueList);
        
        if(defaultEffect != null)
        {
            int i = 0;
            for(Effect effect: effectValueList)
            {
                if(effect.getClass().equals(defaultEffect.getClass()))
                    effectTypeComboBox.getSelectionModel().select(i);
                i++;
            }
        }
    }
}
