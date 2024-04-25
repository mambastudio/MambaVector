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
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import static javafx.scene.shape.StrokeLineCap.BUTT;
import mamba.base.MambaShapeAbstract;
import mamba.base.engine.MEngine;
import mamba.base.math.MBound;
import mamba.overlayselect.drag.MDrag;
import mamba.util.MIntersection;

/**
 *
 * @author jmburu
 * @param <Bezier>
 */
public class MSplineN<Bezier extends MBezierN> extends MambaShapeAbstract<MEngine> {
    
    private final ObservableList<Bezier> bezierList;
    
    private final ObjectProperty<Color> lineColorProperty;
    private final DoubleProperty lineWidthProperty;
    private final BooleanProperty isClosedProperty;
    private final ObjectProperty<Color> fillColorProperty;
    private final BooleanProperty fillPathProperty;
    private final ObjectProperty<StrokeLineCap> lineCapProperty;
    private final BooleanProperty dashedLineProperty;
    private final DoubleProperty dashSizeProperty;
    private final DoubleProperty gapSizeProperty;
    
    private final BooleanProperty isBezierEdit;
        
    public MSplineN()
    {
        bezierList = FXCollections.observableArrayList();       
        
        lineColorProperty   = new SimpleObjectProperty(Color.BLACK);
        lineWidthProperty   = new SimpleDoubleProperty(2);
        fillColorProperty   = new SimpleObjectProperty(Color.BLACK);
        isClosedProperty    = new SimpleBooleanProperty(false);
        fillPathProperty    = new SimpleBooleanProperty(false);
        dashedLineProperty  = new SimpleBooleanProperty(false);
        dashSizeProperty    = new SimpleDoubleProperty(5);
        gapSizeProperty     = new SimpleDoubleProperty(5);
        lineCapProperty     = new SimpleObjectProperty(BUTT);
        
        isBezierEdit = new SimpleBooleanProperty(false);
        isBezierEdit.addListener((o, ov, nv)->{
            getEngine2D().getSelectionModel().refreshDragHandlesAndDraw(); //refresh overlay
        });        
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
        bezierList.forEach(bezier -> {
            add(bezier);
        });
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
    
    public Color getLineColor()
    {
        return lineColorProperty.get();
    }
    
    public void setLineColor(Color color)
    {
        this.lineColorProperty.set(color);
    }
        
    public ObjectProperty<Color> lineColorProperty()
    {
        return lineColorProperty;
    }
    
    public double getLineWidth()
    {
        return lineWidthProperty.get();
    }
    
    public void setLineWidth(double lineWidth)
    {
        this.lineWidthProperty.set(lineWidth);
    }
    
    public DoubleProperty lineWidthProperty()
    {
        return lineWidthProperty;
    } 
    
    public Color getFillColor()
    {
        return fillColorProperty.get();
    }
    
    public void setFillColor(Color fillColor)
    {
        this.fillColorProperty.set(fillColor);
    }
    
    public ObjectProperty<Color> fillColorProperty()
    {
        return fillColorProperty;
    }  
    
    public boolean getIsClosed()
    {
        return isClosedProperty.get();
    }
    
    public void setIsClosed(boolean isClosed)
    {
        this.isClosedProperty.set(isClosed);
    }
    
    public BooleanProperty isClosedProperty()
    {
        return isClosedProperty;
    }  
    
    public boolean getFillPath()
    {
        return fillPathProperty.get();
    }
    
    public void setFillPath(boolean fillPath)
    {
        this.fillPathProperty.set(fillPath);
    }
    
    public BooleanProperty fillPathProperty()
    {
        return fillPathProperty;
    } 
    
    public boolean getDashedLine()
    {
        return dashedLineProperty.get();
    }
    
    public void setDashedLine(boolean dashedLine)
    {
        this.dashedLineProperty.set(dashedLine);
    }
    
    public BooleanProperty dashedLineProperty()
    {
        return dashedLineProperty;
    } 
    
    public double getDashSize()
    {
        return dashSizeProperty.get();
    }
    
    public void setDashSize(double dashSize)
    {
        this.dashSizeProperty.set(dashSize);
    }
    
    public DoubleProperty dashSizeProperty()
    {
        return dashSizeProperty;
    }  
    
    public double getGapSize()
    {
        return gapSizeProperty.get();
    }
    
    public void setGapSize(double gapSize)
    {
        this.gapSizeProperty.set(gapSize);
    }
    
    public DoubleProperty gapSizeProperty()
    {
        return gapSizeProperty;
    }  
    
    public StrokeLineCap getLineCap()
    {
        return lineCapProperty.get();
    }
    
    public void setLineCap(StrokeLineCap cap)
    {
        this.lineCapProperty.set(cap);
    }
    
    public ObjectProperty<StrokeLineCap> lineCapProperty()
    {
        return lineCapProperty;
    }    

    @Override
    public void draw() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean intersect(Point2D parentPoint, MIntersection isect) {
        //transform p to local coordinates
        Point2D invP = this.localToShapeTransform(parentPoint);
        Bounds bound = getShapeBound();
        if(bound.contains(invP))
        {
            isect.shape = this;
            return true;
        }
        else 
            return false;
    }

    @Override
    public boolean intersect(Bounds parentBound, MIntersection isect) {
        //transform p to local coordinates
        Bounds invBound = this.localToShapeTransform(parentBound);
        Bounds bound = getShapeBound();        
        if(bound.contains(invBound))
        {
            isect.shape = this;
            return true;
        }
        else 
            return false;
    }

    @Override
    public Bounds getShapeBound() {
        MBound shapeBound = new MBound();
        getList().forEach(bezier -> {        
            shapeBound.include(bezier.getBound());
        });        
        return shapeBound.getBoundingBox();
    }

    @Override
    public boolean isComplete() {
        return size() > 0;
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
