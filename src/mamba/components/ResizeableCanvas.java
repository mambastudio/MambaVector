/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.components;

import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import mamba.base.MambaCanvas;
import mamba.base.MambaShape;
import mamba.base.MambaShape.ShapeState;
import static mamba.base.MambaShape.ShapeState.DISPLAY;
import static mamba.base.MambaShape.ShapeState.EXPERT;
import mamba.base.engine.MEngine;
import mamba.beans.MBeanPropertyItem;
import mamba.beans.MBeanPropertySheet;
import mamba.beans.MBeanPropertyUtility;
import mamba.beans.editors.MDefaultEditorFactory;

/**
 *
 * @author user
 */
public class ResizeableCanvas extends Region implements MambaCanvas<MEngine, VBox>
{
    private final Canvas canvas;
    private MEngine engine2D = null;
    private long lastClickTime = 0;
      
    private VBox propertyDisplayPanel = null;
           
    public ResizeableCanvas(double width, double height) 
    {
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
        
        this.setOnMouseClicked(this::mouseClicked);
        this.setOnMouseDragged(this::mouseDragged);
        this.setOnMousePressed(this::mousePressed);
        this.setOnMouseReleased(this::mouseReleased);
    }
    
    public GraphicsContext getGraphicsContext2D() {
        return canvas.getGraphicsContext2D();
    }

    @Override
    public void setEngine2D(MEngine engine2D) {
        this.engine2D = engine2D;
        this.engine2D.setGraphicsContext(getGraphicsContext2D());
    }

    @Override
    public MEngine getEngine2D() {
        return engine2D;
    }

    @Override
    public MambaShape hit(Point2D point) { 
        MambaShape shape = engine2D.hit(point);
        System.out.println(engine2D.getSelected());
        return shape;
    }
    
    public void mouseClicked(MouseEvent e)
    {
        boolean isDoubleClick = isDoubleClick(500);
    }
    
    public void mousePressed(MouseEvent e)
    {        
        
        
        //remove selection drawing first
        MambaShape previousSelected = engine2D.getSelected();        
        if(previousSelected != null){
            previousSelected.setState(ShapeState.DISPLAY);
            engine2D.draw();
        }
        
        //new selection
        Point2D p = new Point2D(e.getX(), e.getY());
        engine2D.hitSelect(p);
        
        //init new shape for dragging and init select state
        if(engine2D.isSelected())
        {
            engine2D.getSelected().setState(ShapeState.SELECT); //set selection
            engine2D.getSelected().setOffset(p.subtract(engine2D.getSelected().getPosition()));            
            engine2D.draw();
        }
        else
            engine2D.draw();
        
        //property of new selected shape
        initPropertySheet(engine2D.getSelected());
    }
    
    public void mouseReleased(MouseEvent e)
    {        
        //tempShape = null;
    }
    
    public void mouseDragged(MouseEvent e)
    {
        if(e.isPrimaryButtonDown() && engine2D.isSelected())
        {        
            Point2D p = new Point2D(e.getX(), e.getY());
            engine2D.getSelected().translate(p.getX(), p.getY());
            engine2D.draw();
        }
        
    }
    
    public void initPropertySheet(MambaShape shape)
    {
        if(shape != null && shape.getState() != DISPLAY)
        {
            propertyDisplayPanel.getChildren().clear();
            ObservableList<MBeanPropertyItem> properties = MBeanPropertyUtility.getProperties(shape);
            MBeanPropertySheet propertySheet = new MBeanPropertySheet();
            propertySheet.setFactory(new MDefaultEditorFactory());
            propertySheet.init(properties);
            propertyDisplayPanel.getChildren().add(propertySheet);
        }
        else
            propertyDisplayPanel.getChildren().clear();
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

 
}
