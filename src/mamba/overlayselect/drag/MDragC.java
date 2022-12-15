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
package mamba.overlayselect.drag;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import mamba.base.MambaShape;
import mamba.base.engine.MEngine;
import mamba.base.math.MBound;
import mamba.util.MIntersection;

/**
 *
 * @author jmburu
 */
public class MDragC extends  MDrag {
    
    private final ObjectProperty<Color> solidColor;
    private final DoubleProperty strokeWidth;
    private final ObjectProperty<Color> strokeColor;
    private final DoubleProperty width;
    private final DoubleProperty height;
    private final ObjectProperty<Point2D> position;
    
    private final ObjectProperty<Point2D> location;
    private final ObjectProperty<Point2D> fraction;
    
    
    public MDragC(MambaShape<MEngine> ownerShape)
    {
        super(ownerShape);
        
        solidColor = new SimpleObjectProperty(Color.LIGHTBLUE);
        strokeWidth = new SimpleDoubleProperty(1);
        strokeColor = new SimpleObjectProperty(Color.BLACK);    
        width = new SimpleDoubleProperty(8);
        height = new SimpleDoubleProperty(8);
        
        position = new SimpleObjectProperty(Point2D.ZERO);
        fraction = new SimpleObjectProperty(Point2D.ZERO);
        
        location = new SimpleObjectProperty(position.get().subtract(new Point2D(width.doubleValue()/2, height.doubleValue()/2)));
        position.addListener((o, ov, nv)->{
            location.set(nv.subtract(new Point2D(width.doubleValue()/2, height.doubleValue()/2)));
        });        
    }

    @Override
    public void draw() {
        getGraphicsContext().save();
              
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
        boolean intersect = getShapeBound().contains(parentPoint);
        if(intersect)
            isect.shape = this;
        return intersect;            
    }

    @Override
    public boolean intersect(Bounds parentBound, MIntersection isect) {
        boolean intersect = getShapeBound().contains(parentBound);
        if(intersect)
            isect.shape = this;
        return intersect;           
    }

    @Override
    public Bounds getShapeBound() {
        Point2D min = new Point2D(location.get().getX(), location.get().getY());
        Point2D max = new Point2D(location.get().getX() + width.doubleValue(), location.get().getY() + height.doubleValue());
        MBound bound = new MBound();
        bound.include(min);
        bound.include(max);       
        return (BoundingBox)(bound.getBoundingBox());
    }

    @Override
    public boolean containsGlobalPoint(Point2D p) {
        return getShapeBound().contains(p);
    }

    @Override
    public Point2D getPosition() {
        return position.get();
    }

    @Override
    public void setPosition(Point2D position) {
        this.position.set(position);
    }

    @Override
    public void setPosition(double x, double y) {
        this.position.set(new Point2D(x, y));
    }

    @Override
    public Point2D getFraction() {
        return fraction.get();
    }

    @Override
    public void setFraction(Point2D fraction) {
        this.fraction.set(fraction);
    }

    @Override
    public void setFraction(double fractionX, double fractionY) {
        this.fraction.set(new Point2D(fractionX, fractionY));
    }
    
}
