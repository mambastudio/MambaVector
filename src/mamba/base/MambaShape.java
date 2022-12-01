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
package mamba.base;

import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Effect;
import mamba.base.math.MTransformGeneric;
import mamba.overlayselect.drag.MDrag2;
import mamba.util.MIntersection;

/**
 *
 * @author user
 * @param <Engine2D>
 * 
 * 
 * A shape can be anything; a circle, rectangle, path, image, text, etc.
 *  
 * A parent shape can be a layout actually
 * 
 */
public interface MambaShape<Engine2D extends MambaEngine2D> extends MambaHierarchyData<MambaShape<Engine2D>> 
{
    public MTransformGeneric getLocalTransform();
    public void setLocalTransform(MTransformGeneric transform);
    
    //if this is a child, get local to parent and vice versa
    public MTransformGeneric localToParentTransform();
    public MTransformGeneric parentToLocalTransform();
        
    //get transforms to global and vice versa
    public MTransformGeneric localToGlobalTransform();
    public MTransformGeneric globalToLocalTransform();
    
    public Engine2D getEngine2D();
    public void setEngine(Engine2D engine2D);
    
    public void setGraphicContext(GraphicsContext context);
    public GraphicsContext getGraphicsContext();
    
    //where we do the drawing
    public void draw();
    
    //intersection of bounds and shape (this applies to the top-down hierarchy intersection)
    public boolean intersect(Point2D parentPoint, MIntersection isect);
    public boolean intersect(Bounds parentBound, MIntersection isect);
    
    public default boolean hasParent()
    {
        return getParent() != null;
    }
    
    //good for traversing up the hierarchy
    public default MambaShape<Engine2D> getParent()
    {
        return null;
    }
    
    //local bounds (you can use the transforms above to transform bounds)
    public Bounds getShapeBound();
    
    //some shapes are being edited before fully fledged such as a path that starts with a point
    public boolean isComplete();
    
   
     //for ui editor such as mouse editing (utilises the global transforms)
    public ObservableList<MDrag2> initDragHandles();
    public void updateDragHandles();
    
    //effects to be used here
    public Effect getEffect();    
    public void setEffect(Effect effect);
    
    //bounds of shape
    public boolean containsGlobalPoint(Point2D globalPoint); 
        
    //clone or copy 
    default MambaShape copy()
    {
        return null;
    }
            
    //just a helper for the editor to do extra stuff (blocking any other interactions)
    default boolean isPath()
    {
        return false;
    }
}
