/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.overlayselect;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/**
 *
 * @author user
 */
public class MDragHandle extends Rectangle {
    Cursor dragCursor;
    
    /**
     * Offset coordinates below are not used directly for position (use getPosition instead).
     * This is used in some calculation for editing purpose and hence have no meaning across all shapes.
     * For example, the rectangle use these coordinate but not circle.
     * 
    **/
    double offsetX, offsetY;
    double offset_percX, offset_percY; //(0 - 1 range), store current state if possible but not usually assured this is current - just use offsetX
    
    public MDragHandle(double size, Cursor dragCursor) {

        this.dragCursor = dragCursor;

        setWidth(size);
        setHeight(size);
        
        this.setFill(Color.LIGHTBLUE);
        this.setStroke(Color.BLACK);
        this.setStrokeWidth(1);
        this.setStrokeType(StrokeType.OUTSIDE);      
    }
    
    public MDragHandle(double size, Color fill, Cursor dragCursor) {

        this.dragCursor = dragCursor;

        setWidth(size);
        setHeight(size);
        
        this.setFill(fill);
        this.setStroke(Color.BLACK);
        this.setStrokeWidth(1);
        this.setStrokeType(StrokeType.OUTSIDE);      
    }
    
    public Cursor getDragCursor() {
        return dragCursor;
    }
    
    public Point2D getPosition()
    {
        double x = getX() + getWidth()/2;
        double y = getY() + getHeight()/2;        
        return new Point2D(x, y);
    }    
    
    public void setPositionX(double x)
    {
        double width = this.getWidth();
        this.setX((int)(x - width/2)); //int cast is to avoid blur filter for antialiasing during drawing
    }
    
    public void setPositionY(double y)
    {
        double height = this.getHeight();
        this.setY((int)(y - height/2));
    }
    
    public void setOffsetX(double offsetX, double widthBound)
    {
        this.offsetX = offsetX;
        this.offset_percX = offsetX/widthBound;
    }
    
    public double getOffsetPercentX()
    {
        return offset_percX;
    }
    
    public void setOffsetY(double offsetY, double heightBound)
    {
        this.offsetY = offsetY;
        this.offset_percY = offsetY/heightBound;
    }
    
    public double getOffsetPercentY()
    {
        return offset_percY;
    }
}
