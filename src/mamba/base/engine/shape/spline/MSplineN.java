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

import mamba.base.engine.shape.spline.bezier.MBezierN;
import java.util.ArrayList;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import mamba.base.MambaShapeAbstract;
import mamba.base.engine.MEngine;
import mamba.overlayselect.drag.MDrag;
import mamba.util.MIntersection;

/**
 *
 * @author jmburu
 * @param <Bezier>
 */
public class MSplineN<Bezier extends MBezierN> extends MambaShapeAbstract<MEngine> {
    
    private final ObservableList<Bezier> bezierList;
        
    public MSplineN()
    {
        bezierList = FXCollections.observableArrayList();       
    }
        
    public int getIndex(Bezier bezier)
    {
        return bezierList.indexOf(bezier);
    }
    
    public void add(Bezier bezier)
    {
        bezierList.add(bezier);
        bezier.setSpline(this);
    }
    
    public void addAll(Bezier... bezierArray)
    {
        for(Bezier bezier : bezierArray)
            add(bezier);
    }
    
    public void addAll(ArrayList<Bezier> bezierList)
    {
        for(Bezier bezier : bezierList)
            add(bezier);
    }
    
    public void remove(Bezier bezier)
    {
        bezierList.remove(bezier);
        bezier.setSpline(null);
    }
    
    public boolean contains(Bezier bezier)
    {
        return bezierList.contains(bezier);
    }
    
    public boolean absent(Bezier bezier)
    {
        return !bezierList.contains(bezier);
    }
    
    public boolean hasNext(Bezier bezier)
    {
        return !isLast(bezier);
    }
    
    public Bezier getNext(Bezier bezier)
    {
        if(absent(bezier) || isLast(bezier))
            return null;
               
        return bezierList.get(bezierList.indexOf(bezier) + 1);
    }
    
    public Bezier getPrevious(Bezier point)
    {
        if(absent(point))
            return null;
        
        if(isFirst(point))
            return getLast();   
        
        return bezierList.get(bezierList.indexOf(point) - 1);
    }
    
    protected boolean isLast(Bezier point)
    {
        int index = bezierList.indexOf(point);
        return (bezierList.size()-1) == index;
    }
    
    protected Bezier getLast()
    {
        if(bezierList.size() > 0)
            return bezierList.get(bezierList.size()-1);
        else return null;
    }
    
    protected boolean isFirst(Bezier point)
    {
        int index = bezierList.indexOf(point);
        return index == 0;
    }
    
    protected Bezier getFirst()
    {
        if(bezierList.size() > 0)
            return bezierList.get(0);
        else return null;
    }
        
    public ObservableList<Bezier> getList()
    {
        return bezierList;
    }
    
    public int size()
    {
        return bezierList.size();
    }
    
    public boolean isEmpty()
    {
        return bezierList.isEmpty();
    }
    
    public Optional<Bezier> containsDrag(MDrag drag)
    {
        return null;
    }
        
    @Override
    public boolean isPath()
    {
        return true;
    }

    @Override
    public void draw() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean intersect(Point2D parentPoint, MIntersection isect) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean intersect(Bounds parentBound, MIntersection isect) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Bounds getShapeBound() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isComplete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ObservableList<MDrag> initDragHandles() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateDragHandles() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean containsGlobalPoint(Point2D globalPoint) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
