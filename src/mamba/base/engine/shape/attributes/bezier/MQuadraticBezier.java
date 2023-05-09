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
import mamba.base.engine.shape.MPathCubic;
import mamba.util.MSplineUtility;

/**
 *
 * @author user
 */
public class MQuadraticBezier implements MBezier<MPathCubic>{

    private Point2D point;    
    private Point2D c1;
    
    public MQuadraticBezier()
    {
        point = Point2D.ZERO;              
        c1 = Point2D.ZERO;
    }
    
    public MQuadraticBezier(Point2D point, Point2D control)
    {
        this.point = point;              
        this.c1 = control;
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
        return c1;
    }
    
    public void setControl(Point2D control)
    {
        this.c1 = control;
    }
    
    public MCubicBezier convertToBezier(Point2D previousPoint)
    {        
        return MSplineUtility.convertQuadraticToCubic(previousPoint, this);
    }
    
}
