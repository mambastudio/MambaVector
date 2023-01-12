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
package mamba.base.engine.shape.attributes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import mamba.base.engine.shape.MPath2;
import mamba.base.engine.shape.attributes.bezier.MCubicBezier;
import mamba.overlayselect.drag.MDrag;
import mamba.overlayselect.drag.MDragC;
import mamba.overlayselect.drag.MDragLine;

/**
 *
 * @author jmburu
 */
public class MCubicBezierDrag {
    MCubicBezier bezier;
    MPath2 spline;
                
    MDrag drag;    
    MDragLine dragLine;
    
    //for calculating control points (control drags too) relative to drag shapePoint
    //To delete
    double tC;
    Point2D dirC;
    
    public MCubicBezierDrag(MCubicBezier bezier)
    {
        this.bezier = bezier;
        this.spline = bezier.getSpline();
        if(this.spline == null)
            throw new NullPointerException("spline is not present");
    }
    
    public ObservableList<MDrag> initDragHandles() {
        MDrag pointDrag = new MDragC(spline);
        pointDrag.setPosition(spline.shapeToGlobalTransform(bezier.getPoint()));    
        drag = pointDrag;
        pointDrag.setOnMouseDrag(e->{
            Point2D p = new Point2D(e.getX(), e.getY());   //in global coordinates             
            shapePoint = path.globalToShapeTransform(p);   //transform to local coordinates

            path.updateDragHandles();                
            path.getEngine2D().draw();
        });

        ObservableList<MDrag> drags = FXCollections.observableArrayList(pointDrag);
        
        //if editing bezier control points
        if(path.isBezierEdit())
        {
            drags.setAll(initDragControlHandles());
            drags.add(pointDrag);
        }
        
        return drags;
    }
    
}
