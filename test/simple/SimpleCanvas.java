/*
 * The MIT License
 *
 * Copyright 2022 jmburu.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package simple;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import mamba.base.MambaCanvas;
import mamba.base.engine.MEngine;
import mamba.base.math.MTransform;
import mamba.overlayselect.drag.MDrag2;
import mamba.util.MIntersection;
import mamba.util.MultipleKeyCombination;

/**
 *
 * @author jmburu
 */
public class SimpleCanvas extends Region implements MambaCanvas<MEngine, VBox>{
    
    private MEngine engine2D = null;
    private final Canvas canvas;
    
    Delta dragDelta = new Delta();
    
    public SimpleCanvas()
    {
        double width = 50, height = 50;
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
        
        this.setOnMouseClicked(this::mouseClicked);
        this.setOnMouseDragged(this::mouseDragged);
        this.setOnMousePressed(this::mousePressed);
        this.setOnMouseReleased(this::mouseReleased);
        this.setOnScroll(this::mouseScrolled);   
        this.setOnMouseMoved(this::mouseMoved);
       
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
    
    public GraphicsContext getGraphicsContext2D() {
        return canvas.getGraphicsContext2D();
    }

    @Override
    public void setPropertyDisplayPanel(VBox propertyDisplayPanel) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void mouseClicked(MouseEvent e)
    {
       
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
        else if(engine2D.getSelectionModel().isSelected())
        {
           // MTransform translate = engine2D.getTransform(). //get existing engine transform first
           //     createConcatenation(MTransform.translate(dragDelta)).asMTransform();
        
           // engine2D.setTransform(translate);
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
                createConcatenation(MTransform.translate(delta)).asMTransform(); //delta here is a scalar
        
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
