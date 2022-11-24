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
import mamba.base.math.MTransformGeneric;
import mamba.overlayselect.drag.MDrag;
import mamba.util.MIntersection;

/**
 *
 * @author jmburu
 */
public class MCircle2 extends MambaShapeAbstract<MEngine>
{
    private final ObjectProperty<Color> solidColor;
    private final DoubleProperty strokeWidth;
    private final ObjectProperty<Color> strokeColor;
    private final DoubleProperty width;
    private final DoubleProperty height;
    
    private final double radius = 1; //always 1, any other size will be done in the transform
    
    private MTransform scale = MTransform.scale(new Point2D(50, 50));
    private final MTransform translate = MTransform.translate(50, 50);
    
    public MCircle2()
    {
        solidColor = new SimpleObjectProperty(Color.YELLOW);
        strokeWidth = new SimpleDoubleProperty(0.001);
        strokeColor = new SimpleObjectProperty(Color.BLACK);    
        width = new SimpleDoubleProperty(50);
        height = new SimpleDoubleProperty(50);
        
        width.addListener((o, ov, nv)->{
            scale = MTransform.scale(new Point2D(width.doubleValue()/2.d, height.doubleValue()/2.d));
        });
        height.addListener((o, ov, nv)->{
            scale = MTransform.scale(new Point2D(width.doubleValue()/2.d, height.doubleValue()/2.d));
        });
        
        scale = MTransform.scale(new Point2D(width.doubleValue()/2.d, height.doubleValue()/2.d));
    }
    
    @Override
    public MTransformGeneric getLocalTransform()
    {
        return scale.createConcatenation(translate);
    }
       
    @Override
    public void draw() {
        getGraphicsContext().save();
        //apply transform first
        getLocalTransform().transformGraphicsContext(getGraphicsContext());
        
        //draw shape, this is just local coordinates 
        getGraphicsContext().setFill(solidColor.get());
        getGraphicsContext().setEffect(getEffect());
        getGraphicsContext().fillOval(
                -radius + strokeWidth.doubleValue()/2, 
                -radius + strokeWidth.doubleValue()/2, 
                radius * 2 - strokeWidth.doubleValue(), 
                radius * 2 - strokeWidth.doubleValue());
        
        getGraphicsContext().setStroke(strokeColor.get());
        getGraphicsContext().setLineWidth(strokeWidth.doubleValue());
        getGraphicsContext().strokeOval(-radius + strokeWidth.doubleValue()/2, 
                                  -radius + strokeWidth.doubleValue()/2, 
                                  radius*2  - strokeWidth.doubleValue(), 
                                  radius*2 - strokeWidth.doubleValue());
        
        getGraphicsContext().setEffect(null);
        getGraphicsContext().restore(); //reset transforms and any other configurations
    }

    @Override
    public boolean intersect(Point2D parentPoint, MIntersection isect) {
        Point2D shapeSpacePoint = getLocalTransform().inverseTransform(parentPoint);
        //simple check
        Point2D min = new Point2D(-radius, -radius);
        Point2D max = new Point2D(radius, radius);
        MBound bound = new MBound();
        bound.include(min);
        bound.include(max);
        return bound.contains(shapeSpacePoint);        
    }

    @Override
    public boolean intersect(Bounds parentBound, MIntersection isect) {
        Bounds shapeSpaceBound = getLocalTransform().inverseTransform(parentBound);        
        return getShapeBound().contains(shapeSpaceBound);
    }

    @Override
    public Bounds getShapeBound() {
        Point2D min = new Point2D(-radius, -radius);
        Point2D max = new Point2D(radius, radius);
        MBound bound = new MBound();
        bound.include(min);
        bound.include(max);       
        return (BoundingBox)(bound.getBoundingBox());
    }

    @Override
    public boolean isComplete() {
        return true;
    }

    @Override
    public ObservableList<MDrag> initDragHandles() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateDragHandles() {        
        MDrag c1 = dragHandles.get(0);
        c1.setX(getGlobalBounds().getMinX());
        c1.setY(getGlobalBounds().getMinY());
                
        MDrag c2 = dragHandles.get(1);
        c2.setX(getGlobalBounds().getMaxX());
        c2.setY(getGlobalBounds().getMaxY());
              
        MDrag c3 = dragHandles.get(2);
        c3.setX(getGlobalBounds().getMinX());
        c3.setY(getGlobalBounds().getMaxY());
        
        MDrag c4 = dragHandles.get(3);        
        c4.setX(getGlobalBounds().getMaxX());
        c4.setY(getGlobalBounds().getMinY());          
    }
  
    @Override
    public boolean containsGlobalPoint(Point2D globalPoint) {        
        //transform p to shape space coordinates
        Point2D shapeSpacePoint = globalToLocalTransform(globalPoint);
        //simple check
        Point2D min = new Point2D(-radius, -radius);
        Point2D max = new Point2D(radius, radius);
        MBound bound = new MBound();
        bound.include(min);
        bound.include(max);
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
    
}
