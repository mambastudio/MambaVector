/*
 * The MIT License
 *
 * Copyright 2023 jmburu.
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
package mamba.base.engine.shape.spline;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;

/**
 *
 * @author jmburu
 */
public class MSplinePoint {
    private final ObjectProperty<Point2D> controlProperty = new SimpleObjectProperty();
    private final ObjectProperty<Point2D> pointProperty = new SimpleObjectProperty();
    
    public MSplinePoint(Point2D p)
    {
        pointProperty.set(p);      
    }
    
    //when you add in spline
    public MSplinePoint(Point2D p, Point2D control)
    {
        this.pointProperty.set(p);               
        this.controlProperty.set(control);
    }
    
    public Point2D getPoint()
    {
        return pointProperty.get();
    }
    
    public void setPoint(Point2D point)
    {
        pointProperty.set(point);
    }
    
    public ObjectProperty<Point2D> pointProperty()
    {
        return pointProperty;
    }
    
    public Point2D getControl()
    {
        return controlProperty.get();
    }
    
    public void setControl(Point2D control)
    {
        controlProperty.set(control);
    }
    
    public ObjectProperty<Point2D> controlProperty()
    {
        return controlProperty;
    }
}
