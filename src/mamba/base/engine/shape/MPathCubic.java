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
package mamba.base.engine.shape;

import java.util.Objects;
import java.util.Optional;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import static javafx.scene.shape.StrokeLineCap.BUTT;
import mamba.base.engine.shape.attributes.MCubicPoint;
import mamba.base.engine.shape.attributes.MSpline;
import mamba.base.math.MBound;
import mamba.overlayselect.drag.MDrag;
import mamba.util.MSplineUtility;
import mamba.util.MIntersection;

/**
 *
 * @author user
 */
public class MPathCubic extends MSpline<MCubicPoint>{
    
    private final ObjectProperty<Color> lineColor;
    private final DoubleProperty lineWidth;
    private final BooleanProperty isClosed;
    private final ObjectProperty<Color> fillColor;
    private final BooleanProperty fillPath;
    private final ObjectProperty<StrokeLineCap> lineCap;
    private final BooleanProperty dashedLine;
    private final DoubleProperty dashSize;
    private final DoubleProperty gapSize;
    
    private final BooleanProperty isBezierEdit;
    
    public MPathCubic()
    {
        lineColor = new SimpleObjectProperty(Color.BLACK);
        lineWidth = new SimpleDoubleProperty(2);
        fillColor = new SimpleObjectProperty(Color.BLACK);
        isClosed = new SimpleBooleanProperty(false);
        fillPath = new SimpleBooleanProperty(false);
        dashedLine = new SimpleBooleanProperty(false);
        dashSize = new SimpleDoubleProperty(5);
        gapSize = new SimpleDoubleProperty(5);
        lineCap = new SimpleObjectProperty(BUTT);
        
        isBezierEdit = new SimpleBooleanProperty(false);
        isBezierEdit.addListener((o, ov, nv)->{
            getEngine2D().getSelectionModel().refreshDragHandlesAndDraw(); //refresh overlay
        });
        
    }
        
    @Override
    public void draw() {
        getGraphicsContext().save();
        //apply transform first
        this.shapeToGlobalTransform().transformGraphicsContext(getGraphicsContext());
        
        getGraphicsContext().setLineWidth(lineWidth.get());
        // Set the Color
        getGraphicsContext().setStroke(lineColor.get());  
        
        //draw shape, this is just local coordinates         
        getGraphicsContext().setLineCap(lineCap.get());   
        
        if(dashedLine.get())
            getGraphicsContext().setLineDashes(dashSize.get(), gapSize.get());
        
        getGraphicsContext().setFill(fillColor.get());
        
        getGraphicsContext().beginPath(); 
        
        for(MCubicPoint point : getList())
        {
            boolean isFirst = this.isFirst(point);
            if(isFirst)
                getGraphicsContext().moveTo(
                        point.getPoint().getX(), 
                        point.getPoint().getY());            
            else
            {
                getGraphicsContext().bezierCurveTo(
                        point.getC1().getX(), 
                        point.getC1().getY(), 
                        point.getC2().getX(), 
                        point.getC2().getY(), 
                        point.getPoint().getX(), 
                        point.getPoint().getY());
            }
        }                        
        
        if(this.isClosed.get())
            getGraphicsContext().closePath();
        
        getGraphicsContext().setEffect(getEffect());     
        if(this.fillPath.get())
            getGraphicsContext().fill();
        getGraphicsContext().setEffect(null);        
        getGraphicsContext().stroke();
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
    public void add(MCubicPoint point)
    {
        if(!this.isEmpty())
        {
            MCubicPoint lastPoint = this.getLast();
            //get mirror of c2 of last point
            point.setC1(MSplineUtility.getMirrorControlPoint(lastPoint.getPoint(), lastPoint.getC2()));
        }
        point.setSpline(this); //very important
        super.add(point);
    }

    @Override
    public BoundingBox getShapeBound() {
        MBound shapeBound = new MBound();
        getList().forEach(point -> {        
            shapeBound.include(point.getPoint());
        });        
        return shapeBound.getBoundingBox();
    }

    @Override
    public boolean isComplete() {
        return size() > 0;
    }

    @Override
    public ObservableList<MDrag> initDragHandles() {
        ObservableList<MDrag> handles = FXCollections.observableArrayList();
        for(MCubicPoint point: getList())
        {
            handles.addAll(point.initDragHandles());
        }
        return handles;
    }

    @Override
    public void updateDragHandles() {
        for(MCubicPoint point: getList())
        {
            point.updateDragHandles();
        }    
    }

    @Override
    public boolean containsGlobalPoint(Point2D globalPoint) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public BooleanProperty getIsBezierEdit()
    {
        return isBezierEdit;
    }
        
    public boolean isBezierEdit()
    {
        return isBezierEdit.get();
    }
    
    @Override
    public String toString()
    {
        return "Path: \n" +getList();
    }    

    @Override
    public Optional<MCubicPoint> containsDrag(MDrag drag) {
        Objects.requireNonNull(drag, "drag should be non-null");
        for(MCubicPoint point : getList())        
            if(point.containsDrag(drag))
                Optional.of(point);
        return Optional.empty();        
    }
}
