/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.overlayselect.drag;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;

/**
 *
 * @author user
 */
public class MDragSquare extends MDrag {
    
    private Rectangle rectangle;
    private double translateX = 0, translateY = 0; 
    
    public MDragSquare()
    {
        super();
    }
   
    public MDragSquare(double size)
    {
      //  super(Cursor.HAND);
        rectangle.setWidth(size);
        rectangle.setHeight(size);
        
    }
    
    @Override
    protected ObservableList<Shape> initDrag() {
        rectangle = new Rectangle();
        rectangle.setWidth(16);
        rectangle.setHeight(16);
        rectangle.setStrokeType(StrokeType.INSIDE);  
        rectangle.setFill(Color.LIGHTBLUE);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(1);
        
       
        
        return FXCollections.observableArrayList(rectangle);
    }

    @Override
    public double getWidth() {
        return this.getBoundsInParent().getWidth();
    }

    @Override
    public double getHeight() {
        return this.getBoundsInParent().getHeight();
    }

    @Override
    public double getX() {
        return translateX;
    }

    @Override
    public void setX(double x) {
        this.translateX = x; 
        rectangle.setTranslateX((int)translateX - getWidth()/2);
    }

    @Override
    public double getY() {
        return translateY;
    }

    @Override
    public void setY(double y) {
        this.translateY = y;
        rectangle.setTranslateY((int)translateY - getHeight()/2);
    }
    
    
    
}
