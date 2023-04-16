/*
 * The MIT License
 *
 * Copyright 2023 user.
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

import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import mamba.base.engine.shape.MPathCubic;
import mamba.base.engine.shape.attributes.bezier.MCubicBezier;
import mamba.overlayselect.drag.MDrag;
import mamba.overlayselect.drag.MDragC;

/**
 *
 * @author user
 */
public class MCubicPoint extends MCubicBezier implements MSplineDragHandles<MCubicPoint, MPathCubic>{   
    MDrag drag;        
    
    //make sure to setSpline spline before/after adding to spline
    public MCubicPoint(Point2D p) {
        super(p);
    }
    
    //when you add in spline
    //make sure to setSpline spline before/after adding to spline
    public MCubicPoint(Point2D p, Point2D c2)
    {
        super(p, c2);
    }
    
    //make sure to setSpline spline before/after adding to spline
    public MCubicPoint(Point2D p, Point2D c1, Point2D c2)
    {
        super(p, c1, c2);
    }

    @Override
    public ObservableList<MDrag> initDragHandles() {
        drag = new MDragC(getSpline());
        drag.setPosition(getSpline().shapeToGlobalTransform(getPoint()));   
        
        ObservableList<MDrag> drags = FXCollections.observableArrayList(drag);
        
        //update bezier point and control points (all drag nodes will be updated in the updateDragHandles())
        drag.setOnMouseDrag(e->{
                        
            //new bezier point after drag
            Point2D p = new Point2D(e.getX(), e.getY());   //in global coordinates             
            setPoint(getSpline().globalToShapeTransform(p));   //transform to local coordinates
                                   
            updateDragHandles();
            getSpline().getEngine2D().draw();
        });
        
        //if editing bezier control points
        if(getSpline().isBezierEdit())
        {
            
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
    public boolean containsDrag(MDrag drag)
    {
        Objects.requireNonNull(drag, "provide a non-null drag");
        return this.drag == drag;
    }
}
