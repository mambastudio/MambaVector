/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.overlayselect.drag;

import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Shape;

/**
 *
 * @author user
 * @param <S>
 */
public abstract class MDrag<S extends Shape> extends Group {
           
    /**
     * Offset coordinates below are not used directly for position (use getPosition instead).
     * This is used in some calculation for editing purpose and hence have no meaning across all shapes.
     * For example, the rectangle use these coordinate but not circle.
     * 
     * Drags such as mouse drag and mouse pressed are handle in shape
     * 
    **/
    private double offsetX, offsetY;
    private double offset_percX, offset_percY; //(0 - 1 range), store current state if possible but not usually assured this is current - just use offsetX
           
    public MDrag()
    {
        this(Cursor.HAND);        
    }
    
    public MDrag(Cursor dragCursor)
    {
        this.setCursor(dragCursor);
        ObservableList<S> drag = initDrag();    
        super.getChildren().addAll(drag);
    }
    
    protected abstract ObservableList<S> initDrag();       
    
    //required to use directly to underlying shapes to avoid use of transforms
    public abstract double getWidth();
    public abstract double getHeight();
    public abstract double getX();
    public abstract void setX(double x);
    public abstract double getY();
    public abstract void setY(double y);
    
    @Override
    public ObservableList<Node> getChildren()
    {
        return this.getChildrenUnmodifiable();
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
