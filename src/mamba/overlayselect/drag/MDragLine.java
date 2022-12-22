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
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import mamba.base.MambaShape;
import mamba.base.engine.MEngine;

/**
 *
 * @author jmburu
 */
public class MDragLine extends MDragVoid{
    
    private final DoubleProperty strokeWidth;
    private final ObjectProperty<Color> strokeColor;  
    private final DoubleProperty dashSize;
    private final DoubleProperty gapSize;
    
    private Point2D start = Point2D.ZERO;
    private Point2D end = Point2D.ZERO;
    
    public MDragLine(MambaShape<MEngine> ownerShape)
    {
        super(ownerShape);
        
        strokeWidth = new SimpleDoubleProperty(1.5);
        strokeColor = new SimpleObjectProperty(Color.BLACK);           
        dashSize = new SimpleDoubleProperty(3);
        gapSize = new SimpleDoubleProperty(5);
    }
    
    public void setStart(double x, double y)
    {
        start = new Point2D(x, y);
    }  
    
    public void setStart(Point2D start)
    {
        this.start = start;
    }  
    
    public void setEnd(double x, double y)
    {
        end = new Point2D(x, y);
    }
    
    public void setEnd(Point2D end)
    {
        this.end = end;
    }

    @Override
    public void draw() {
        getGraphicsContext().save();
        //apply transform first
        this.shapeToGlobalTransform().transformGraphicsContext(getGraphicsContext());
                
        //draw shape, this is just local coordinates           
        getGraphicsContext().setStroke(strokeColor.get());
        getGraphicsContext().setLineWidth(strokeWidth.doubleValue());
        
        getGraphicsContext().setLineDashes(dashSize.get(), gapSize.get());        
        getGraphicsContext().strokeLine(start.getX(), start.getY(), end.getX(), end.getY());
                
        getGraphicsContext().restore(); //reset transforms and any other configurations
    }

}
