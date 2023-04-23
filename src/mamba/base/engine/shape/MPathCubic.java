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
    
    public MPathCubic()
    {
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
        
        add(new MCubicPoint(Point2D.ZERO, Point2D.ZERO));
        add(new MCubicPoint(new Point2D(100, 100), new Point2D(100, 100)));
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
        getGraphicsContext().save();
        //apply transform first
        this.shapeToGlobalTransform().transformGraphicsContext(getGraphicsContext());
        
        getGraphicsContext().setLineWidth(lineWidthProperty.get());
        // Set the Color
        getGraphicsContext().setStroke(lineColorProperty.get());  
        
        //draw shape, this is just local coordinates         
        getGraphicsContext().setLineCap(lineCapProperty.get());   
        
        if(dashedLineProperty.get())
            getGraphicsContext().setLineDashes(dashSizeProperty.get(), gapSizeProperty.get());
        
        getGraphicsContext().setFill(fillColorProperty.get());
        
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
        
        if(this.isClosedProperty.get())
            getGraphicsContext().closePath();
        
        getGraphicsContext().setEffect(getEffect());     
        if(this.fillPathProperty.get())
            getGraphicsContext().fill();
        getGraphicsContext().setEffect(null);        
        getGraphicsContext().stroke();
        
        getGraphicsContext().restore();
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
            MSplineUtility.chainCubicPoints(lastPoint, point);
        }
        point.setSpline(this); //very important
        super.add(point);
        
        if(this.hasEngine2D())
            this.getEngine2D().draw();
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
        return "Path";
    }    

    @Override
    public Optional<MCubicPoint> containsDrag(MDrag drag) {
        Objects.requireNonNull(drag, "drag should be non-null");
        for(MCubicPoint point : getList())        
            if(point.containsDrag(drag))
                return Optional.of(point);
        return Optional.empty();        
    }
}
