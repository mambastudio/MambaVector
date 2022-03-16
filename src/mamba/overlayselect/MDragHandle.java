/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.overlayselect;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import mamba.base.MambaShape;
import mamba.util.MBound2;

/**
 *
 * @author user
 */
public class MDragHandle extends Rectangle {
    Cursor dragCursor;
    
    public MDragHandle(double size, Cursor dragCursor) {

        this.dragCursor = dragCursor;

        setWidth(size);
        setHeight(size);
        
        this.setFill(Color.LIGHTBLUE);
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
}
