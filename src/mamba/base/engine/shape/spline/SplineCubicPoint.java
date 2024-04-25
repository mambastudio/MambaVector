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

import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import mamba.base.math.MTransform;
import mamba.overlayselect.drag.MDrag;
import mamba.overlayselect.drag.MDragC;
import mamba.util.MSplineUtility;

/**
 *
 * @author user
 */
public final class SplineCubicPoint implements SplinePoint, SplineDragHandles<SplineCubic> {
    private MDrag drag;
    private final ObjectProperty<Point2D> pointProperty = new SimpleObjectProperty();  //local space    
    private final ObjectProperty<Point2D> controlProperty = new SimpleObjectProperty(); //local space
    
    //make sure to setSpline spline before/after adding to spline
    public SplineCubicPoint(Point2D p) {
        setPoint(p);
        controlProperty.set(new Point2D(p.getX(), p.getY()));
    }
    
    //when you add in spline
    //make sure to setSpline spline before/after adding to spline
    public SplineCubicPoint(Point2D p, Point2D c) {
        setPoint(p);
        controlProperty.set(c);
    }
    
    //control points for path point (called from initDragHandles)
    private ObservableList<MDrag> initDragControlHandles()
    {
        return FXCollections.emptyObservableList();
    }

    @Override
    public ObservableList<MDrag> initDragHandles() {
       drag = new MDragC(getSpline());
        drag.setPosition(getSpline().shapeToGlobalTransform(getPoint()));   
        
        ObservableList<MDrag> drags = FXCollections.observableArrayList(drag);
        
        //update bezier point and control points (all drag nodes will be updated in the updateDragHandles())
        drag.setOnMouseDrag(e->{
            //get previous point
            Point2D prevP = new Point2D(getPoint().getX(), getPoint().getY());   
                        
            //new bezier point after drag
            Point2D p = new Point2D(e.getX(), e.getY());   //in global coordinates    
            Point2D pLocal = getSpline().globalToShapeTransform(p);   //transform to local coordinates
            setPoint(pLocal);   
            
            //get transform of current translated bezier point
            MTransform translate = MTransform.translate(pLocal.subtract(prevP));
            
            //update the controls based on the new bezier point
           // MSplineUtility.applyTransformToMidPointControls(translate, this, getSpline().getNext(this));
           
            updateDragHandles();
            getSpline().getEngine2D().draw();
        });
        
        //if editing bezier control points
        if(getSpline().isBezierEdit())
        {
            drags.setAll(initDragControlHandles());
            drags.add(drag);
        }
        
        return drags; 
    }

    @Override
    public void updateDragHandles() {
        if(drag == null) //if drag handles have not been initialised for the first time
           initDragHandles();
        
        drag.setPosition(getSpline().shapeToGlobalTransform(getPoint()));        
               
        //if editing bezier control points
        if(getSpline().isBezierEdit())
        {
            
        }
    }

    @Override
    public boolean containsDrag(MDrag drag) {
        Objects.requireNonNull(drag, "provide a non-null drag");
        return this.drag == drag;
    }

    @Override
    public Point2D getPoint() {
        return pointProperty.get();
    }

    @Override
    public void setPoint(Point2D point) {
        pointProperty.set(point);
    }
    
    public Point2D getC1()
    {
        return getPoint();
    }
    
    public Point2D getC2()
    {
        return getPoint();
    }
}
