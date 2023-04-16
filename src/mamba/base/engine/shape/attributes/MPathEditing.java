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

import javafx.geometry.Point2D;
import mamba.base.engine.MEngine;
import mamba.base.engine.shape.MPathCubic;

/**
 *
 * @author user
 */
public class MPathEditing {
    MEngine engine;    
    
    public MPathEditing(MEngine engine)
    {        
        this.engine = engine;
    }
    
    public boolean addLine(Point2D globalPoint, boolean isPenToolSelected)
    {
        if(engine.getSelectionModel().isPathEditingMode()) //true if selected shape is a path
        {
            if(isPenToolSelected)
            {
                MSpline path = (MSpline) engine.getSelectionModel().getSelected();
                Point2D tP = path.globalToShapeTransform(globalPoint); //very important
                
                Point2D p = new Point2D(tP.getX(), tP.getY());
                Point2D c = new Point2D(tP.getX(), tP.getY());
                
                path.add(new MCubicPoint(p, c));
                return true;
            }            
        }
        return false;
    }
    
    public boolean removePoint(Point2D globalPoint, boolean isEraserToolSelected)
    {
        if(engine.getSelectionModel().isPathEditingMode()) //true if selected shape is a path
        {
            if(isEraserToolSelected && engine.getSelectionModel().isDragHandleSelected())
            {
                MPathCubic path = (MPathCubic) engine.getSelectionModel().getSelected();
                //containsDrag(MDrag drag) returns Optional<MCubicPoint> which we can now remove from path
                path.remove(path.containsDrag(engine.getSelectionModel().getDragHandleSelected()).get());      
            }
        }
        return false;
    }
}
