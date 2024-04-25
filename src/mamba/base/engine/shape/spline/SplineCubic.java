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

import java.util.Optional;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import static javafx.scene.shape.StrokeLineCap.BUTT;
import mamba.base.engine.shape.attributes.MCubicPoint;
import mamba.overlayselect.drag.MDrag;
import mamba.util.MIntersection;

/**
 *
 * @author user
 */
public class SplineCubic extends Spline<SplineCubicPoint> {
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
    
    public SplineCubic()
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
        
        add(new SplineCubicPoint(Point2D.ZERO));
        add(new SplineCubicPoint(new Point2D(100, 100)));
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
        
        getList().forEach(point -> {
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
        });                        
        
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
    public Bounds getShapeBound() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean isComplete() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ObservableList<MDrag> initDragHandles() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void updateDragHandles() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean containsGlobalPoint(Point2D globalPoint) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Optional<SplineCubicPoint> containsDrag(MDrag drag) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public BooleanProperty getIsBezierEdit() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean isBezierEdit() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
