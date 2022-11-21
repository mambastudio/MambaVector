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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import mamba.base.math.MBound;

/**
 *
 * @author jmburu
 */
public class MDragLine extends MDrag{
    private Line dashedLine;

    @Override
    protected ObservableList<Shape> initDrag() {
        dashedLine = new Line();
        dashedLine.setStrokeWidth(1.5);
        dashedLine.getStrokeDashArray().addAll(3d, 5d);
        this.setCursor(Cursor.DEFAULT);
        return FXCollections.observableArrayList(dashedLine);
    }

    @Override
    public double getWidth() {
        MBound bound = new MBound();
        bound.include(new Point2D(dashedLine.getStartX(), dashedLine.getStartY()));
        bound.include(new Point2D(dashedLine.getEndX(), dashedLine.getEndY()));
        return bound.getWidth();
    }

    @Override
    public double getHeight() {
        MBound bound = new MBound();
        bound.include(new Point2D(dashedLine.getStartX(), dashedLine.getStartY()));
        bound.include(new Point2D(dashedLine.getEndX(), dashedLine.getEndY()));
        return bound.getHeight();
    }

    @Override
    public double getX() {
        return dashedLine.getStartX();
    }
        
    @Override
    public void setX(double x) {
        dashedLine.setStartX(x);
    }
    
    public double getEndX()
    {
        return dashedLine.getEndX();
    }
    
    public void setEndX(double x)
    {
        dashedLine.setEndX(x);
    }

    @Override
    public double getY() 
    {
        return dashedLine.getStartY();
    }

    @Override
    public void setY(double y) 
    {
        dashedLine.setStartY(y);
    }
    
    public double getEndY()
    {
        return dashedLine.getEndY();
    }

    public void setEndY(double y)
    {
        dashedLine.setEndY(y);
    }
    
    public void setStart(double x, double y)
    {
        setX(x);
        setY(y);
    }    
    
    public void setEnd(double x, double y)
    {
        setEndX(x);
        setEndY(y);
    }
}
