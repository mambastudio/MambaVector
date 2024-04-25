/*
 * The MIT License
 *
 * Copyright 2024 user.
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

import java.util.ArrayList;
import java.util.Optional;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mamba.base.MambaShapeAbstract;
import mamba.base.engine.MEngine;
import mamba.overlayselect.drag.MDrag;

/**
 *
 * @author user
 * @param <SPoint>
 */
public abstract class Spline<SPoint extends SplinePoint> extends MambaShapeAbstract<MEngine>{
    private final ObservableList<SPoint> pointList;
    
    public Spline()
    {
        pointList = FXCollections.observableArrayList();
    }
    
    public int getIndex(SPoint point)
    {
        return pointList.indexOf(point);
    }
    
    public void add(SPoint point)
    {
        pointList.add(point);
    }
    
    public void addAll(SPoint... pointArray)
    {
        for(SPoint point : pointArray)
            add(point);
    }
    
    public void addAll(ArrayList<SPoint> pointList)
    {
        for(SPoint point : pointList)
            add(point);
    }
    
    public void remove(SPoint point)
    {
        pointList.remove(point);
    }
    
    public boolean contains(SPoint point)
    {
        return pointList.contains(point);
    }
    
    public boolean absent(SPoint point)
    {
        return !pointList.contains(point);
    }
    
    public boolean hasNext(SPoint point)
    {
        return !isLast(point);
    }
    
    public SPoint getNext(SPoint point)
    {
        if(absent(point) || isLast(point))
            return null;
               
        return pointList.get(pointList.indexOf(point) + 1);
    }
    
    public SPoint getPrevious(SPoint point)
    {
        if(absent(point))
            return null;
        
        if(isFirst(point))
            return getLast();   
        
        return pointList.get(pointList.indexOf(point) - 1);
    }
    
    protected boolean isLast(SPoint point)
    {
        int index = pointList.indexOf(point);
        return (pointList.size()-1) == index;
    }
    
    protected SPoint getLast()
    {
        if(!pointList.isEmpty())
            return pointList.get(pointList.size()-1);
        else return null;
    }
    
    protected boolean isFirst(SPoint point)
    {
        int index = pointList.indexOf(point);
        return index == 0;
    }
    
    protected SPoint getFirst()
    {
        if(!pointList.isEmpty())
            return pointList.get(0);
        else return null;
    }
        
    public ObservableList<SPoint> getList()
    {
        return pointList;
    }
    
    public int size()
    {
        return pointList.size();
    }
    
    public boolean isEmpty()
    {
        return pointList.isEmpty();
    }
    
    public abstract Optional<SPoint> containsDrag(MDrag drag);
    public abstract BooleanProperty getIsBezierEdit();
    public abstract boolean isBezierEdit();
        
    @Override
    public boolean isPath()
    {
        return true;
    }
}
