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

import mamba.base.engine.shape.attributes.bezier.MBezier;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import mamba.base.MambaShapeAbstract;
import mamba.base.engine.MEngine;

/**
 *
 * @author user
 * @param <Bezier>
 */
public abstract class MSpline<Bezier extends MBezier> extends MambaShapeAbstract<MEngine>{
    private final ObservableList<Bezier> bezierList;
    
    private Point2D startPoint;
    
    protected MSpline()
    {
        bezierList = FXCollections.observableArrayList();
        startPoint = Point2D.ZERO;
    }
    
    public void setStartPoint(Point2D startPoint)
    {
        this.startPoint = startPoint;
    }
    
    public Point2D getStartPoint()
    {
        return this.startPoint;
    }
    
    public int getIndex(Bezier bezier)
    {
        return bezierList.indexOf(bezier);
    }
    
    public void add(Bezier bezier)
    {
        bezierList.add(bezier);
        bezier.set(this);
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
        bezier.set(null);
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
        if(absent(bezier))
            return null;
        
        if(isLast(bezier))
            return getFirst();        
       
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
}
