/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.base;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Effect;
import javafx.scene.transform.Transform;
import static mamba.base.MambaShape.ShapeState.ANIMATION;
import static mamba.base.MambaShape.ShapeState.DISPLAY;
import static mamba.base.MambaShape.ShapeState.EXPERT;
import static mamba.base.MambaShape.ShapeState.SELECT;
import mamba.overlayselect.MDragHandle;

/**
 *
 * @author user
 * @param <Engine2D>
 */
public interface MambaShape<Engine2D extends MambaEngine2D> {
    public enum ShapeState{DISPLAY, SELECT, ANIMATION, EXPERT};
    
    public Transform getTransform();
    public void setTransform(Transform transform);
        
    public void translate(Point2D p);
    public Point2D getTranslate();
    
    public void setOffset(Point2D offset);
    public Point2D getOffset();
    default void resetOffset()
    {
        setOffset(new Point2D(0, 0));
    }
        
    public Engine2D getEngine2D();
    public void setEngine(Engine2D engine2D);
    public void setGraphicContext(GraphicsContext context);
    public GraphicsContext getGraphicsContext();
    public void draw();
        
    public BoundingBox getBounds();
        
    //not only by bounds but by specific shape
    public boolean contains(Point2D p); 
    
    public ShapeState getState();
    public void setState(ShapeState shapeState);
        
    default boolean isDisplay()
    {
        return getState() == DISPLAY;
    }
    
    default boolean isSelect()
    {
        return getState() == SELECT;
    }
    
    default boolean isAnimation()
    {
        return getState() == ANIMATION;
    }
    
    default boolean isExpert()
    {
        return getState() == EXPERT;
    }
    
    default String getSVGString()
    {
        return null;
    }
    
    default ObservableList<MDragHandle> getDragHandles()
    {
        return FXCollections.emptyObservableList();
    }
    
    public void updateDragHandles(MDragHandle referenceHandle);
    
    default Effect getEffect()
    {
        return null;
    }
    
    default void setEffect(Effect effect)
    {
        
    }
    
}
