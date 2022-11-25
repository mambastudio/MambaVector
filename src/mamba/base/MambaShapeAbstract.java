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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Effect;
import mamba.base.math.MTransform;
import mamba.base.math.MTransformGeneric;
import mamba.overlayselect.drag.MDrag;

/**
 *
 * @author jmburu
 * @param <Engine2D>
 */
public abstract class MambaShapeAbstract<Engine2D extends MambaEngine2D> implements MambaShape<Engine2D> {
    private MTransformGeneric localTransform;
    private Engine2D engine2D;
    private GraphicsContext graphicsContext;
    protected ObservableList<MambaShape<Engine2D>> children;
    private Effect effect;
    protected ObservableList<MDrag> dragHandles;
    
    protected MambaShapeAbstract()
    {
        localTransform = new MTransform();
        effect = null;
        children = FXCollections.emptyObservableList();
        dragHandles = FXCollections.observableArrayList();
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
    
    //if this is a child, get local to parent and vice versa
    @Override
    public MTransformGeneric localToParentTransform()
    {
        if(this.hasParent())
            return this.getLocalTransform().createConcatenation(getParent().getLocalTransform());
        else
            return this.getLocalTransform();
    }
    
    @Override
    public MTransformGeneric parentToLocalTransform()
    {
        MTransformGeneric localToParent = localToParentTransform();
        return new MTransform(localToParent.getInverseMatrix(), localToParent.getMatrix());
    }
        
    //get transforms to global and vice versa
    @Override
    public MTransformGeneric localToGlobalTransform()
    {
        MTransformGeneric transform;
        if(this.hasParent())
            transform = getLocalTransform().createConcatenation(getParent().localToParentTransform());
        else
            transform = getLocalTransform();        
        return transform.createConcatenation(this.getEngine2D().getTransform());
    }
    
    @Override
    public MTransformGeneric globalToLocalTransform()
    {
        MTransformGeneric transform = localToGlobalTransform();
        return new MTransform(transform.getInverseMatrix(), transform.getMatrix());
    }
    
    public Bounds getGlobalBounds()
    {
        return localToGlobalTransform(getShapeBound());
    }
    
    public Point2D localToParentTransform(Point2D point)
    {
        return localToParentTransform().transform(point);
    }
    
    public Point2D parentToLocalTransform(Point2D point)
    {
        return parentToLocalTransform().transform(point);
    }
    
    public Bounds localToParentTransform(Bounds bound)
    {
        return localToParentTransform().transform(bound);
    }
    
    public Bounds parentToLocalTransform(Bounds bound)
    {
        return parentToLocalTransform().transform(bound);
    }
    
    public Point2D localToGlobalTransform(Point2D point)
    {
        return localToGlobalTransform().transform(point);
    }
    
    public Point2D globalToLocalTransform(Point2D point)
    {
        return globalToLocalTransform().transform(point);
    }
    
    public Bounds localToGlobalTransform(Bounds bound)
    {
        return localToGlobalTransform().transform(bound);
    }
    
    public Bounds globalToLocalTransform(Bounds bound)
    {
        return globalToLocalTransform().transform(bound);
    }
}
