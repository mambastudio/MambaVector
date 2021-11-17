/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.overlayselect;

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

    public MDragHandle(double size, Cursor dragCursor) {

        this.dragCursor = dragCursor;

        setWidth(size);
        setHeight(size);
        
        this.setFill(Color.rgb(0, 128, 0, 0.6));
        this.setStroke(Color.GREEN);
        this.setStrokeWidth(1);
        this.setStrokeType(StrokeType.OUTSIDE);
      
    }

    public Cursor getDragCursor() {
        return dragCursor;
    }
    
    
}
