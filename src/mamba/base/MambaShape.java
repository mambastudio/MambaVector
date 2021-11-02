/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.base;

import javafx.beans.property.DoubleProperty;
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

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
    public BoundingBox getBounds();
    public boolean contains(Point2D p);    
    public ShapeState getState();
    public void setState(ShapeState shapeState);
    
    default String getSVGString()
    {
        return null;
    }
}
