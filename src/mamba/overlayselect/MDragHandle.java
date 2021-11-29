/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.overlayselect;

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
    Point2D currentPressed;

    public MDragHandle(double size, Cursor dragCursor) {

        this.dragCursor = dragCursor;

        setWidth(size);
        setHeight(size);
        
        this.setFill(Color.WHITE);
        this.setStroke(Color.BLACK);
        this.setStrokeWidth(1);
        this.setStrokeType(StrokeType.OUTSIDE);
      
        this.currentPressed = new Point2D(0, 0);
    }
    
    public void setCurrentPressedPoint(Point2D p)
    {
        this.currentPressed = p;
    }
    
    public Point2D getCurrentPressedPoint()
    {
        return this.currentPressed;
    }

    public Cursor getDragCursor() {
        return dragCursor;
    }
    
    
}
