/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.base;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import mamba.base.math.MTransformGeneric;
import mamba.overlayselect.drag.MDrag;
import mamba.util.MIntersection;

/**
 *
 * @author user
 * @param <Engine2D>
 */
public interface MambaShape<Engine2D extends MambaEngine2D> extends MambaHierarchyData<MambaShape<Engine2D>> {
    
    public enum ShapeType{CONTAINER, SHAPE, SHAPE_POLY};
    public enum ShapeState{PREPAROTRY, EDIT, DISPLAY};
    
    public MTransformGeneric getTransform();
    public void setTransform(MTransformGeneric transform);
        
    public void translate(Point2D p);
    public Point2D getTranslate();
    
    //mouse offset when dragging
    public void setOffset(Point2D offset);
    public Point2D getOffset();
    
    default void resetOffset()
    {
        setOffset(new Point2D(0, 0));
    }
    
    public ShapeType getType();
    
    
    default boolean addShape(MambaShape shape)
    {
        return false;
    }
    
    default ObservableList<MambaShape> getShapeList()
    {
        return FXCollections.emptyObservableList();
    }
        
    public Engine2D getEngine2D();
    public void setEngine(Engine2D engine2D);
    public void setGraphicContext(GraphicsContext context);
    public GraphicsContext getGraphicsContext();
    public void draw();
        
    public BoundingBox getBounds();
        
    //not only by bounds but by specific shape
    public boolean contains(Point2D p); 
    
    public default boolean intersect(Point2D p, MIntersection isect)
    {
        if(contains(p))
        {
            isect.shape = this;
            return true;
        }
        return false;
    }
        
    default String getSVGString()
    {
        return null;
    }
    
    default ObservableList<MDrag> getDragHandles()
    {
        return FXCollections.emptyObservableList();
    }
    
    public void updateDragHandles(MDrag referenceHandle);
    
    default Effect getEffect()
    {
        return null;
    }
    
    default void setEffect(Effect effect)
    {
        
    }
    
    public StringProperty getNameProperty();
    public String getName();    
    
    default Shape getDisplay(MambaShape shape)
    {        
        Rectangle rectangle = new Rectangle(10, 10);
        rectangle.setFill(Color.BLUEVIOLET);
        return rectangle;        
    }
    
    default MambaShape copy()
    {
        return null;
    }
    
    default boolean isComplete()
    {
        return true;
    }
    
    default boolean isPath()
    {
        return false;
    }
}
