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

import java.util.Objects;
import javafx.geometry.Point2D;
import mamba.base.engine.shape.MPathCubic;

/**
 *
 * @author user
 */
public class MCubicBezier implements MBezier<MPathCubic>{
    private MPathCubic spline;
    
    
    /**
     * Cubic Bezier in graphics is defined by a current point and two control points 
     * (c1, c2).
     *     
     * In a spline, c1 belongs to the previous point, and c2 for current point, 
     * but both belong in the same bezier class here which has a single 
     * point (current point). Therefore, c1, has to be obtained in the previous
     * point.
     * 
     * Hence, cubic bezier class here is a reference to the current point 
     * (bezier point). It therefore means, to get control points like c1, it has 
     * to obtain it in previous bezier point.
     */
    
    
    private Point2D point;  //local space
    private Point2D c1;
    private Point2D c2;
        
    public MCubicBezier(Point2D p)
    {
        point = p;
        c1 = null;
        c2 = null;
    }
    
    //when you add in spline
    public MCubicBezier(Point2D p, Point2D c2)
    {
        this.point = p;       
        this.c1 = null;
        this.c2 = c2;
    }
    
    public MCubicBezier(Point2D p, Point2D c1, Point2D c2)
    {
        this.point = p;       
        this.c1 = c1;
        this.c2 = c2;
    }
    
    public void set(Point2D p, Point2D c1, Point2D c2)
    {
        this.point = p;       
        this.c1 = c1;
        this.c2 = c2;
    }
    
    public Point2D getPoint()
    {
        return point;
    }
    
    public void setPoint(Point2D point)
    {
        this.point = point;
    }
           
    public Point2D getC1()
    {
        return c1;
    }
        
    public void setC1(Point2D c1)
    {
        this.c1 = c1;
    }
        
    public Point2D getC2()
    {
        return c2;
    }
        
    public void setC2(Point2D c2)
    {
        this.c2 = c2;
    }
        
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("\n p ").append(point).append("\n");       
        builder.append("c1 ").append(c1).append("\n");
        builder.append("c2 ").append(c2).append("\n");
        return builder.toString();
    }

    @Override
    public void setSpline(MPathCubic spline) {
        this.spline = spline;
    }

    @Override
    public MPathCubic getSpline() {
        return spline;
    }

    public boolean isIsolated()
    {
        return Objects.isNull(c1) && Objects.isNull(c2);
    }
}
