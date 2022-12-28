/*
 * The MIT License
 *
 * Copyright 2022 user.
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
package mamba.base.engine.shape.attributes.bezier;

import javafx.geometry.Point2D;
import mamba.base.engine.shape.MPath2;

/**
 *
 * @author user
 */
public class MQuadraticBezier implements MBezier<MPath2>{

    private Point2D point;    
    private Point2D control;
    
    public MQuadraticBezier()
    {
        point = Point2D.ZERO;              
        control = Point2D.ZERO;
    }
    
    public MQuadraticBezier(Point2D point, Point2D control)
    {
        this.point = point;              
        this.control = control;
    }
    
    public Point2D getPoint()
    {
        return point;
    }
    
    public void setPoint(Point2D point)
    {
        this.point = point;
    }
    
    public Point2D getControl()
    {
        return control;
    }
    
    public void setControl(Point2D control)
    {
        this.control = control;
    }
    
    
    
}
