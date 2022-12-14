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
import javafx.scene.Cursor;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import mamba.BuilderController;
import mamba.base.MambaCanvas;
import mamba.base.MambaShape;
import mamba.base.engine.MEngine;
import mamba.base.engine.shape.MPath;
import mamba.base.math.MTransform;
import mamba.beans.MBeanPropertyItem;
import mamba.beans.MBeanPropertySheet;
import mamba.beans.MBeanPropertyUtility;
import mamba.beans.editors.MDefaultEditorFactory;
import mamba.overlayselect.drag.MDrag2;
import mamba.util.MIntersection;
import mamba.util.MObservableStack;
import mamba.util.MambaUtility;
import mamba.util.MouseClick;
import mamba.util.MultipleKeyCombination;

/**
 *
 * @author user
 */
public final class ResizeableCanvas extends Region implements MambaCanvas<MEngine, VBox>
{
    private final Canvas canvas;
    private MEngine engine2D = null;
    private MouseClick mouseClick = new MouseClick();
      
    private VBox propertyDisplayPanel = null;
    
    private VBox effectPropertyDisplayPanel = null;
    private ComboBox<Effect> effectTypeComboBox = null;
    private ChangeListener<Effect> effectTypeComboBoxListener = null;
    
    private MObservableStack preparatoryStack = null;
    
    private final BuilderController controller;   
    
    Delta dragDelta = new Delta();
        
               
    public ResizeableCanvas(BuilderController controller, double width, double height) 
    {
        this.preparatoryStack = new MObservableStack(); 
        this.controller = controller; 
        
        //set the width and height of this and the canvas as the same
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
            if(engine2D != null)
                engine2D.draw();
        });
        canvas.heightProperty().addListener((o, oV, nv)->{
            if(engine2D != null)
                engine2D.draw();
        });
        
        //effect combobox for shape
        effectTypeComboBoxListener = (o, ov, nv)-> setShapeEffect(nv);       
        
        this.setOnMouseClicked(this::mouseClicked);
        this.setOnMouseDragged(this::mouseDragged);
        this.setOnMousePressed(this::mousePressed);
        this.setOnMouseReleased(this::mouseReleased);
        this.setOnScroll(this::mouseScrolled);   
        this.setOnMouseMoved(this::mouseMoved);
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
    
    
    public void mouseClicked(MouseEvent e)
    {
        mouseClick.isDoubleClick(500);
    }
    
    public void mouseMoved(MouseEvent e)
    {
        //put cursor if over
        setMouseCursorOnDragHandle(new Point2D(e.getX(), e.getY()));
    }
    
    public void mousePressed(MouseEvent e)
    {            
        dragDelta.setPoint(new Point2D(e.getX(), e.getY()));  
        Point2D p = new Point2D(e.getX(), e.getY());  
      
        if(!engine2D.getSelectionModel().intersectDragHandles(p))
        {
            MIntersection isect = new MIntersection();
            engine2D.intersect(p, isect);
            engine2D.setSelected(isect.shape);
        }        
    }
    
    public void mouseReleased(MouseEvent e)
    {
        this.setCursor(Cursor.DEFAULT);
        this.engine2D.getSelectionModel().removeDragHandleSelected();
    }
    
    public void mouseDragged(MouseEvent e)
    {
        dragDelta.setPoint(new Point2D(e.getX(), e.getY()));        
                      
        if(new MultipleKeyCombination(KeyCode.CONTROL).match())
        {            
            this.setCursor(Cursor.MOVE);
            translate(dragDelta.getDelta());
        }
        else if(engine2D.getSelectionModel().isDragHandleSelected())
        {            
            engine2D.getSelectionModel().getDragHandleSelected().processMouseEvent(e);
        }
        else if(engine2D.getSelectionModel().isSelected()) //translate shape
        {
            Point2D deltaVector = dragDelta.getDelta(); //this is a vector
            MambaShape shape = engine2D.getSelectionModel().getSelected();
            
            Point2D deltaScaledVector = shape.globalToLocalTransform().deltaTransform(deltaVector);            
            shape.setLocalTransform(shape.getLocalTransform().createConcatenation(MTransform.translate(deltaScaledVector)));
            shape.updateDragHandles();
            
            engine2D.draw();           
        }       
    }
    
    public void mouseScrolled(ScrollEvent e)
    {
        double deltaY = e.getDeltaY()* 0.1;
        double scale = deltaY > 0 ? 1.1 : 0.9;
        
        Point2D mousePoint = new Point2D(e.getX(), e.getY());
        Point2D scalePoint = new Point2D(scale, scale);
        
        //put cursor if over
        setMouseCursorOnDragHandle(mousePoint);
        
        if(new MultipleKeyCombination(KeyCode.CONTROL).match())
        {           
            zoom(mousePoint, scalePoint);
        }    
        e.consume();
        
    }
    
    //https://medium.com/@benjamin.botto/zooming-at-the-mouse-coordinates-with-affine-transformations-86e7312fd50b
    private void zoom(Point2D point, Point2D scale)
    {                
        MTransform zoom = engine2D.getTransform(). //get existing engine transform first
                createConcatenation(MTransform.scale(scale, point)).asMTransform();
        
        engine2D.setTransform(zoom);             
    }
    
    private void translate(Point2D delta)
    { 
        MTransform translate = engine2D.getTransform(). //get existing engine transform first
                createConcatenation(MTransform.translate(delta)).asMTransform(); 
        
        engine2D.setTransform(translate);
    }
    
    private void setMouseCursorOnDragHandle(Point2D p)
    {
        boolean isInDrag = false;
            
        if(engine2D.isSelected())
            for(MDrag2 drag : engine2D.getSelectionModel().getSelectedShapeDragHandles())
                isInDrag |= drag.containsGlobalPoint(p);

        if(isInDrag)
            this.setCursor(Cursor.HAND);
        else
            this.setCursor(Cursor.DEFAULT);
    }
    
    private class Delta
    {
        private Point2D pressed = Point2D.ZERO;
        private Point2D delta = Point2D.ZERO;
        
        public void setPoint(Point2D p)
        {
            delta = p.subtract(pressed);
            pressed = p;
        }
        
        public Point2D getDelta()
        {
            return delta;
        }
        
    }
}
