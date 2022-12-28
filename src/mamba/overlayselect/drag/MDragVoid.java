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
package mamba.overlayselect.drag;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import mamba.base.MambaShape;
import mamba.base.engine.MEngine;
import mamba.base.math.MBound;
import mamba.util.MIntersection;

/**
 *
 * @author user
 */
public abstract class MDragVoid extends MDrag{
    
    public MDragVoid(MambaShape<MEngine> ownerShape) {
        super(ownerShape);
    }
    
    @Override
    public boolean intersect(Point2D parentPoint, MIntersection isect) {
        return false;
    }

    @Override
    public boolean intersect(Bounds parentBound, MIntersection isect) {
        return false;
    }

    @Override
    public Bounds getShapeBound() {
        return new MBound().getBoundingBox();
    }

    @Override
    public boolean containsGlobalPoint(Point2D globalPoint) {
        return false;
    }

    @Override
    public Point2D getPosition() {
        return Point2D.ZERO;
    }

    @Override
    public void setPosition(Point2D position) {
      
    }

    @Override
    public void setPosition(double x, double y) {
        
    }

    @Override
    public Point2D getFraction() {
        return Point2D.ZERO;
    }

    @Override
    public void setFraction(Point2D fraction) {
        
    }

    @Override
    public void setFraction(double x, double y) {
        
    }
    
}
