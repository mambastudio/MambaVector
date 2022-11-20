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
package mamba.base.math;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

/**
 *
 * @author user
 */
public class MTransformMap implements MTransformGeneric{
    
    public enum TransformTypeKey{SCALE, TRANSLATE};
    
    private final ObservableMap<String, MTransform> map;
    
    public MTransformMap()
    {
        map = FXCollections.observableHashMap();
    }

    public void put(String key, MTransform value)
    {
        map.put(key, value);
    }
    
    public void put(TransformTypeKey key, MTransform value)
    {
        this.put(key.name(), value);
    }
    
    public MTransform get(String key)
    {
        return map.get(key);
    }
    
    @Override
    public Point2D transform(Point2D point)
    {
        Point2D p = new Point2D(point.getX(), point.getY());
        
        for(MTransform transform : map.values())
            p = transform.transform(p);
        
        return p;
    }
    
    @Override
    public Point2D inverseTransform(Point2D point)
    {
        Point2D p = new Point2D(point.getX(), point.getY());
        
        List<MTransform> toReverse = new ArrayList(map.values());
        Collections.reverse(toReverse);
        
        for(MTransform transform : toReverse)
            p = transform.inverseTransform(p);
        return p;
    }

    @Override
    public Point2D deltaTransform(Point2D point) {
        Point2D p = new Point2D(point.getX(), point.getY());
        
        for(MTransform transform : map.values())
            p = transform.deltaTransform(p);
        
        return p;
    }

    @Override
    public Point2D deltaInverseTransform(Point2D point) {
        Point2D p = new Point2D(point.getX(), point.getY());
        
        List<MTransform> toReverse = new ArrayList(map.values());
        Collections.reverse(toReverse);
        
        for(MTransform transform : toReverse)
            p = transform.deltaInverseTransform(p);
        return p;
    }

    @Override
    public void transformGraphicsContext(GraphicsContext graphicsContext) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Bounds transform(Bounds bound) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Bounds inverseTransform(Bounds bound) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MTransform createConcatenation(MTransformGeneric transform) {
        MTransform t = new MTransform(getMatrix(), getInverseMatrix());
        return transform.createConcatenation(t);
    }

    @Override
    public MMatrix4 getMatrix() {
        MMatrix4 matrix = new MMatrix4();
        for(MTransform transform : map.values())
            matrix = matrix.createConcatenation(transform.getMatrix());
        return matrix;
    }

    @Override
    public MMatrix4 getInverseMatrix() {
        MMatrix4 matrix = new MMatrix4();
        List<MTransform> toReverse = new ArrayList(map.values());
        Collections.reverse(toReverse);        
        for(MTransform transform : toReverse)
            matrix = matrix.createConcatenation(transform.getInverseMatrix());
        return matrix;
    }
}
