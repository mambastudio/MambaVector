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
package mamba.base.engine.shape.attributes;

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
 * @param <P>
 */
public abstract class MPathSpline<P extends MPathPointGeneric> extends MambaShapeAbstract<MEngine>{
    private final ObservableList<P> points;
    
    protected MPathSpline()
    {
        points = FXCollections.observableArrayList();
    }
    
    //for ui editing (this are beans type methods)
    public abstract void setIsClosed(boolean isClosed);    
    public abstract boolean getIsClosed();
    public abstract BooleanProperty isClosedProperty();
    
    public P get(int index)
    {
        return points.get(index);
    }
    
    public ObservableList<P> getList()
    {
        return points;
    }
    
    public int size()
    {
        return points.size();
    }
   
    public void add(P point)
    {
        points.add(point);
    }
    
    public boolean contains(P point)
    {
        return points.contains(point);
    }
    
    public boolean absent(P point)
    {
        return !points.contains(point);
    }
    
    public boolean hasNext(P point)
    {
        return !isLast(point);
    }
    
    public P getNext(P point)
    {
        if(absent(point))
            return null;
        
        if(isLast(point))
            return getFirst();        
       
        return points.get(points.indexOf(point) + 1);
    }
    
    public P getPrevious(P point)
    {
        if(absent(point))
            return null;
        
        if(isFirst(point))
            return getLast();   
        
        return points.get(points.indexOf(point) - 1);
    }
    
    protected boolean isLast(P point)
    {
        int index = points.indexOf(point);
        return (points.size()-1) == index;
    }
    
    protected P getLast()
    {
        if(points.size() > 0)
            return points.get(points.size()-1);
        else return null;
    }
    
    protected boolean isFirst(P point)
    {
        int index = points.indexOf(point);
        return index == 0;
    }
    
    protected P getFirst()
    {
        if(points.size() > 0)
            return points.get(0);
        else return null;
    }
    
    public Optional<P> getPoint(MDrag drag)
    {
        for(int i = 0; i<points.size(); i++)
            if(points.get(i).contains(drag))
                return Optional.ofNullable(points.get(i));
        return Optional.empty();
    }
    
    protected void remove(P p)
    {
        points.remove(p);
    }
}
