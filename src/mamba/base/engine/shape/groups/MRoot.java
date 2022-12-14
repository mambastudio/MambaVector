/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.base.engine.shape.groups;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import mamba.base.MambaShape;
import mamba.base.MambaShapeAbstract;
import mamba.base.engine.MEngine;
import mamba.base.math.MBound;
import mamba.overlayselect.drag.MDrag;
import mamba.util.MIntersection;
import static mamba.util.Reversed.reversed;

/**
 *
 * @author jmburu
 */
public class MRoot extends MambaShapeAbstract<MEngine>{
    
    private MEngine engine2D;
    private GraphicsContext graphicContext;    
    
    public MRoot()
    {
        this.children = FXCollections.observableArrayList();
    }
    
    public MRoot(MambaShape<MEngine>... shapes)
    {
        this();
    }
   
    @Override
    public MEngine getEngine2D() {
        return engine2D;
    }

    @Override
    public void setEngine(MEngine engine2D) {
        this.engine2D = engine2D;
    }

    @Override
    public void setGraphicContext(GraphicsContext context) {
        this.graphicContext = context;
    }

    @Override
    public GraphicsContext getGraphicsContext() {
        return graphicContext;
    }

    @Override
    public void draw() {
        for(MambaShape shape : children)
            shape.draw();
    }

    @Override
    public ObservableList getChildren() {
        return children;
    }
    
    @Override
    public boolean intersect(Point2D localTransformedPoint, MIntersection isect)
    {
        Point2D shapeTransformedPoint = this.getLocalTransform().inverseTransform(localTransformedPoint);
        for(MambaShape shape : reversed(children))
            if(shape.intersect(shapeTransformedPoint, isect)) //return first hit
            {                
                return true;
            }
        return false;
    }
    
    @Override
    public boolean intersect(Bounds localTransformedBound, MIntersection isect) {
        Bounds shapeTransformedBound = this.getLocalTransform().inverseTransform(localTransformedBound);
        for(MambaShape shape : reversed(children))
            if(shape.intersect(shapeTransformedBound, isect)) //return first hit
            {                
                return true;
            }
        return false;
    }
    
    @Override
    public Bounds getShapeBound() {
        MBound shapeBound = new MBound();
        for(MambaShape shape : reversed(children))
           shapeBound.include(shape.getShapeBound());
        return shapeBound.getBoundingBox();
    }
    
    public boolean addShape(MambaShape shape)
    {
        return getChildren().add(shape);        
    }
    
    public boolean addShape(List<MambaShape> shapes)
    {
        return getChildren().addAll(shapes);
    }
    
    @Override
    public boolean isComplete() {
        return true;
    }
    
    @Override
    public ObservableList<MDrag> initDragHandles() {
        return FXCollections.emptyObservableList();
    }
    
    @Override
    public void updateDragHandles() {
        //empty drag handles
    }
    
    @Override
    public boolean containsGlobalPoint(Point2D globalPoint) {
        //transform p to shape space coordinates
        Point2D shapeTransformedPoint = globalToShapeTransform(globalPoint);
        //simple check
        Bounds bound = getShapeBound();
        return bound.contains(shapeTransformedPoint);
    }
    
    @Override
    public boolean canHaveChildren()
    {
        return true;
    }
    
    @Override
    public String toString()
    {
        return "Root";
    }    
}
