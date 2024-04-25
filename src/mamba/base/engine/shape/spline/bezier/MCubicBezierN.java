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
package mamba.base.engine.shape.spline.bezier;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import mamba.base.engine.shape.spline.MSplineN;
import mamba.base.engine.shape.spline.MSplinePoint;
import mamba.base.math.MBound;

/**
 *
 * @author jmburu
 * 
 * FIXME IN COMMENT
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
 * to be obtained in previous bezier point.
 */
 
public class MCubicBezierN extends MBezierN<MSplineN<MCubicBezierN>>
{
    protected final MSplinePoint p1; 
    protected final MSplinePoint p2;
    
    public MCubicBezierN(MSplinePoint p1, MSplinePoint p2)
    {
        this.p1 = p1;
        this.p2 = p2;
    }
    
    public MCubicBezierN(Point2D p1, Point2D p2)
    {
        this.p1 = new MSplinePoint(p1);
        this.p2 = new MSplinePoint(p2);
    }

    @Override
    public Bounds getBound() {
        MBound shapeBound = new MBound();
        shapeBound.include(p1.getPoint(), p2.getPoint());
        return shapeBound.getBoundingBox();
    }
}
