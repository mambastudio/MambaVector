/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.base;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import static mamba.base.MambaShape.ShapeState.ANIMATION;
import static mamba.base.MambaShape.ShapeState.DISPLAY;
import static mamba.base.MambaShape.ShapeState.EXPERT;
import static mamba.base.MambaShape.ShapeState.SELECT;

/**
 *
 * @author user
 * @param <Engine2D>
 */
public interface MambaShape<Engine2D extends MambaEngine2D> {
    public enum ShapeState{DISPLAY, SELECT, ANIMATION, EXPERT};
        
    DoubleProperty widthProperty();
    DoubleProperty heightProperty();
    
    public void translate(double x, double y);
    public Point2D getPosition();
    public void setOffset(Point2D offset);
        
    public Engine2D getEngine2D();
    public void setEngine(Engine2D engine2D);
    public void setGraphicContext(GraphicsContext context);
    public GraphicsContext getGraphicsContext();
    public void draw();
    public void drawSelect();
    
    public ObjectProperty<BoundingBox> getBoundsProperty();
    public BoundingBox getBounds();
    public void updateBounds();
    
    public boolean contains(Point2D p);    
    public ShapeState getState();
    public void setState(ShapeState shapeState);
    
    public void initProperties();
    
    public void setInitPropertiesCall(MambaSupplierVoid supplierVoid);
    
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
    
    default ObservableList<MambaShape> getAnchorShapes()
    {
        return FXCollections.emptyObservableList();
    }
}
