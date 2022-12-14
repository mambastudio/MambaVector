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
package mamba.base.engine.shape;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import mamba.base.MambaShapeAbstract;
import mamba.base.engine.MEngine;
import mamba.base.math.MBound;
import mamba.base.math.MTransform;
import mamba.overlayselect.drag.MDrag;
import mamba.overlayselect.drag.MDragC;
import mamba.overlayselect.drag.MDragShape;
import mamba.util.MIntersection;

/**
 *
 * @author jmburu
 */
public class MCircle extends MambaShapeAbstract<MEngine>
{
    private final ObjectProperty<Color> solidColor;
    private final DoubleProperty strokeWidth;
    private final ObjectProperty<Color> strokeColor;
    private final DoubleProperty width;
    private final DoubleProperty height;
    
    public final ObjectProperty<Point2D> location;
    
    public MCircle()
    {
        solidColor = new SimpleObjectProperty(Color.YELLOW);
        strokeWidth = new SimpleDoubleProperty(0.001);
        strokeColor = new SimpleObjectProperty(Color.BLACK);    
        width = new SimpleDoubleProperty(50);
        height = new SimpleDoubleProperty(50);
        
        location = new SimpleObjectProperty(Point2D.ZERO);        
    }
    
    
    @Override
    public void draw() {
        getGraphicsContext().save();
        //apply transform first
        this.shapeToGlobalTransform().transformGraphicsContext(getGraphicsContext());
      
        //draw shape, this is just local coordinates 
        getGraphicsContext().setFill(solidColor.get());
        getGraphicsContext().setEffect(getEffect());
        getGraphicsContext().fillOval(
                location.get().getX() + strokeWidth.doubleValue()/2, 
                location.get().getY() + strokeWidth.doubleValue()/2, 
                width.doubleValue() - strokeWidth.doubleValue(), 
                height.doubleValue() - strokeWidth.doubleValue());
        
        getGraphicsContext().setStroke(strokeColor.get());
        getGraphicsContext().setLineWidth(strokeWidth.doubleValue());
        getGraphicsContext().strokeOval(
                location.get().getX() + strokeWidth.doubleValue()/2, 
                location.get().getY() + strokeWidth.doubleValue()/2, 
                width.doubleValue()  - strokeWidth.doubleValue(), 
                height.doubleValue() - strokeWidth.doubleValue());
        
        getGraphicsContext().setEffect(null);
        getGraphicsContext().restore(); //reset transforms and any other configurations
    }

    @Override
    public boolean intersect(Point2D parentPoint, MIntersection isect) {
        Point2D shapeSpacePoint = this.getLocalTransform().inverseTransform(parentPoint);
        //simple check
        Bounds bound = getShapeBound();
        if(bound.contains(shapeSpacePoint))
        {
            isect.shape = this;
            return true;
        }
        return false;        
    }

    @Override
    public boolean intersect(Bounds parentBound, MIntersection isect) {
        Bounds shapeSpaceBound = getLocalTransform().inverseTransform(parentBound);        
        return getShapeBound().contains(shapeSpaceBound);
    }

    @Override
    public Bounds getShapeBound() {
        Point2D min = new Point2D(location.get().getX(), location.get().getY());
        Point2D max = new Point2D(location.get().getX() + width.doubleValue(), location.get().getY() + height.doubleValue());
        MBound bound = new MBound();
        bound.include(min);
        bound.include(max);       
        return bound.getBoundingBox();
    }

    @Override
    public boolean isComplete() {
        return true;
    }

    @Override
    public ObservableList<MDrag> initDragHandles() {
        if(dragHandles.isEmpty())
        {                              
            MDragC c1 = new MDragC(this);       
            c1.setPosition(getGlobalBounds().getMinX(), getGlobalBounds().getMinY());            
            dragHandles.add(c1);            

            c1.setOnMouseDrag(e->{
                Point2D p = new Point2D(e.getX(), e.getY());

                MBound cBound = new MBound(getShapeBound());
                Point2D cShapePoint = this.globalToShapeTransform(p); //to shape coordinates    
                MBound nShapeBound = new MBound(cShapePoint, cBound.getMax());
                                
                location.set(nShapeBound.getMin());
                width.set(nShapeBound.getWidth());
                height.set(nShapeBound.getHeight());
                
                updateDragHandles();                
                getEngine2D().draw();               
            });
                        
            MDragC c2 = new MDragC(this);            
            c2.setPosition(getGlobalBounds().getMaxX(), getGlobalBounds().getMaxY());            
            dragHandles.add(c2);
            
            c2.setOnMouseDrag(e->{
                Point2D p = new Point2D(e.getX(), e.getY());
                
                MBound cBound = new MBound(getShapeBound());
                Point2D cShapePoint = this.globalToShapeTransform(p); //to shape coordinates    
                MBound nShapeBound = new MBound(cBound.getUpperLeft(), cShapePoint); //simple reflection of point
                                
                location.set(nShapeBound.getMin());
                width.set(nShapeBound.getWidth());
                height.set(nShapeBound.getHeight());
                
                updateDragHandles();             
                getEngine2D().draw();
            });
            
            MDragC c3 = new MDragC(this);            
            c3.setPosition(getGlobalBounds().getMinX(), getGlobalBounds().getMaxY());           
            dragHandles.add(c3);

            c3.setOnMouseDrag(e->{
                Point2D p = new Point2D(e.getX(), e.getY());
                
                MBound cBound = new MBound(getShapeBound());
                Point2D cShapePoint = this.globalToShapeTransform(p); //to shape coordinates    
                MBound nShapeBound = new MBound(cShapePoint, cBound.getUpperRight()); //simple reflection of point
               
                location.set(nShapeBound.getMin());
                width.set(nShapeBound.getWidth());
                height.set(nShapeBound.getHeight());

                updateDragHandles(); 
                getEngine2D().draw();
            });
            
            MDragC c4 = new MDragC(this);            
            c4.setPosition(getGlobalBounds().getMaxX(), getGlobalBounds().getMinY());           
            dragHandles.add(c4);
            c4.setOnMouseDrag(e->{

                Point2D p = new Point2D(e.getX(), e.getY());

                MBound cBound = new MBound(getShapeBound());
                Point2D cShapePoint = this.globalToShapeTransform(p); //to shape coordinates    
                MBound nShapeBound = new MBound(cShapePoint, cBound.getLowerLeft()); //simple reflection of point
               
                location.set(nShapeBound.getMin());
                width.set(nShapeBound.getWidth());
                height.set(nShapeBound.getHeight());
                
                updateDragHandles();           
                getEngine2D().draw();
            });
        }       
      
        return dragHandles;       
    }

    @Override
    public void updateDragHandles() {    
        
        MDragShape c1 = dragHandles.get(0);
        c1.setPosition(getGlobalBounds().getMinX(), getGlobalBounds().getMinY());
        
        MDragShape c2 = dragHandles.get(1);
        c2.setPosition(getGlobalBounds().getMaxX(), getGlobalBounds().getMaxY());
        
        MDragShape c3 = dragHandles.get(2);
        c3.setPosition(getGlobalBounds().getMinX(), getGlobalBounds().getMaxY());
              
        MDragShape c4 = dragHandles.get(3);        
        c4.setPosition(getGlobalBounds().getMaxX(), getGlobalBounds().getMinY());
    }
  
    @Override
    public boolean containsGlobalPoint(Point2D globalPoint) {        
        //transform p to shape space coordinates
        Point2D shapeSpacePoint = globalToShapeTransform(globalPoint);
        //simple check
        Bounds bound = getShapeBound();
        return bound.contains(shapeSpacePoint);
    }
    
    public double getStrokeWidth()
    {        
        return this.strokeWidth.doubleValue();
    }
    
    public void setStrokeWidth(double strokeWidth)
    {       
        this.strokeWidth.set(strokeWidth);  
    }
    
    public DoubleProperty strokeWidthProperty()
    {
        return strokeWidth;
    }
    
    public void setStrokeColor(Color strokeColor)
    {       
        this.strokeColor.set(strokeColor);
        this.getEngine2D().draw();        
    }
    
    public Color getStrokeColor()
    {
        return this.strokeColor.get();
    }
    
    public ObjectProperty<Color> strokeColorProperty()
    {
        return strokeColor;
    }
    
    public void setSolidColor(Color solidColor)
    {       
        this.solidColor.set(solidColor);    
    }
    
    public Color getSolidColor()
    {
        return this.solidColor.get();
    }
    
    public ObjectProperty<Color> solidColorProperty()
    {
        return solidColor;
    }
    
    public void setLocation(double x, double y)
    {
        this.setLocalTransform(MTransform.translate(x, y));
    }
    
}
