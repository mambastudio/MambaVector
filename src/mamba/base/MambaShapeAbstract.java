/*
 * The MIT License
 *
 * Copyright 2022 jmburu.
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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Effect;
import javafx.scene.input.MouseEvent;
import mamba.base.math.MTransform;
import mamba.base.math.MTransformGeneric;
import mamba.overlayselect.drag.MDrag2;

/**
 *
 * @author jmburu
 * @param <Engine2D>
 * 
 * local< - >shape
 * parent*< - >local< - >shape
 * global< - >parent*< - >local< - >shape
 * 
 * Shape coordinates are in shape boundary, and if the shape is transformed, the newly transformed
 * coordinates is the local coordinates; 
 * Hence 
 *      - local->shape transform
 *      - shape->local transform
 *  
 * Local coordinates are in local boundary, and if the local space/coordinates are transformed based on parent local transform (layout), if it exists, 
 * the newly transformed coordinates are in the parent local coordinates;
 * Hence 
 *      - local->parent transform
 *      - parent->local transform
 * 
 * Global coordinates don't require much explanation, but they are located in the engine class
 *      - global->shape transform = global/world coordinates to shape coordinate
 *      - shape->global transform = shape coordinate to global/world coordinates
 * 
 * NB: Local coordinates are the shape coordinates of the parent if any
 */
public abstract class MambaShapeAbstract<Engine2D extends MambaEngine2D> implements MambaShape<Engine2D> {
       
    private MTransformGeneric localTransform;
    private Engine2D engine2D;
    private GraphicsContext graphicsContext;
    protected ObservableList<MambaShape<Engine2D>> children;
    private Effect effect;
    protected ObservableList<MDrag2> dragHandles;
    protected final Map<EventType<? extends MouseEvent>, Consumer<MouseEvent>> mouseEventConsumer;
    
    protected MambaShapeAbstract()
    {
        localTransform = new MTransform();
        effect = null;
        children = FXCollections.emptyObservableList();
        dragHandles = FXCollections.observableArrayList();
        mouseEventConsumer = new HashMap();
    }
    
    @Override
    public MTransformGeneric getLocalTransform()
    {
        return localTransform;
    }
    
    @Override
    public void setLocalTransform(MTransformGeneric localTransform)
    {
        this.localTransform = localTransform;
    }    
    
    @Override
    public Engine2D getEngine2D() {
        return engine2D;
    }

    @Override
    public void setEngine(Engine2D engine2D) {
        this.engine2D = engine2D;
    }
    
    @Override
    public void setGraphicContext(GraphicsContext context) {
        this.graphicsContext = context;
    }

    @Override
    public GraphicsContext getGraphicsContext() {
        return this.graphicsContext;
    }
        
    @Override
    public ObservableList<MambaShape<Engine2D>> getChildren() {
        return children;
    }
    
    @Override
    public Effect getEffect()
    {
        return effect;
    }
    
    @Override
    public void setEffect(Effect effect)
    {
        this.effect = effect;
    }
    
    
    public void setOnMouseDrag(Consumer<MouseEvent> consume)
    {
        this.mouseEventConsumer.put(MouseEvent.MOUSE_DRAGGED, consume);
    }
       
    public void processMouseEvent(MouseEvent e)
    {
        if(this.mouseEventConsumer.containsKey(e.getEventType()))
            this.mouseEventConsumer.get(e.getEventType()).accept(e);
    }
    
    /**
     * (shape -> local) -> parent* -> world
     * @return      
     */        

    @Override
    public MTransformGeneric shapeToGlobalTransform()
    {
        MTransformGeneric transform;
        if(this.hasParent())
            transform = getLocalTransform().createConcatenation(getParent().shapeToGlobalTransform());
        else
            transform = getLocalTransform();                
        return transform.createConcatenation(getEngine2D().getTransform());
    }
    
    @Override
    public MTransformGeneric globalToShapeTransform()
    {
        MTransformGeneric shapeToGlobalTransform = shapeToGlobalTransform();
        return shapeToGlobalTransform.inverseTransform();
    }
    
    //after shape transformed or ignore concantenation of local transform
    @Override
    public MTransformGeneric localToGlobalTransform()
    {        
        if(this.hasParent())
            return getParent().shapeToGlobalTransform();       //calls engine transform irregardless 
        else
            return getEngine2D().getTransform(); //engine transform is not called anywhere, hence call it here
    }
    
    @Override
    public MTransformGeneric globalToLocalTransform()
    {
        return localToGlobalTransform().inverseTransform();
    }
    
    @Override
    public Point2D shapeToLocalTransform(Point2D shapePointCoord)
    {
        return localTransform.transform(shapePointCoord);
    }
    
    @Override
    public Bounds shapeToLocalTransform(Bounds shapeBoundCoord)
    {
        return localTransform.transform(shapeBoundCoord);
    }
    
    @Override
    public Point2D localToShapeTransform(Point2D localPointCoord)
    {
        return localTransform.inverseTransform(localPointCoord);
    }
    
    @Override
    public Bounds localToShapeTransform(Bounds localBoundCoord)
    {
        return localTransform.inverseTransform(localBoundCoord);
    }
    
    @Override
    public Point2D shapeToGlobalTransform(Point2D shapePointCoord)
    {
        return shapeToGlobalTransform().transform(shapePointCoord);
    }  
    
    @Override
    public Bounds shapeToGlobalTransform(Bounds shapeBoundCoord)
    {
        return shapeToGlobalTransform().transform(shapeBoundCoord);
    }  
    
    @Override
    public Point2D globalToShapeTransform(Point2D globalPointCoord)
    {
        return globalToShapeTransform().transform(globalPointCoord);
    } 
    
    @Override
    public Bounds globalToShapeTransform(Bounds globalBoundCoord)
    {
        return globalToShapeTransform().transform(globalBoundCoord);
    } 
}
