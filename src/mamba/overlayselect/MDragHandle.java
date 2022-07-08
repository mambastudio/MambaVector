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
        Bounds b = localToParent(getBoundsInLocal());
        return new Point2D(b.getMinX(), b.getMinY());
    }    
    
    public double getOffsetX()
    {
        return offsetX;
    }
    
    public void setOffsetX(double offsetX)
    {
        this.offsetX = offsetX;
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
    
    public double getOffsetY()
    {
        return offsetY;
    }
    
    public void setOffsetY(double offsetY)
    {
        this.offsetY = offsetY;
    }
    
    public void setOffsetY(double offsetY, double height)
    {
        this.offsetY = offsetY;
        this.offset_percY = offsetY/height;
    }
    
}
