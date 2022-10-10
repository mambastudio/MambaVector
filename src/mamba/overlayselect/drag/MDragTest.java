/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.overlayselect.drag;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;

/**
 *
 * @author user
 */
public class MDragTest extends MDrag{
    Rectangle rectangle;
    double translateX, translateY; 
    
    public MDragTest()
    {
        super();
        translateX = 0;
        translateY = 0;
    }
    
    @Override
    protected ObservableList<Shape> initDrag() {
        rectangle = new Rectangle();
        rectangle.setWidth(8); //should be even to avoid blur in the division at setX & setY
        rectangle.setHeight(8); //should be even to avoid blur in the division at setX & setY
        rectangle.setFill(Color.LIGHTBLUE); 
        rectangle.setStrokeType(StrokeType.INSIDE);
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
        rectangle.setTranslateX(translateX - getWidth()/2);
    }

    @Override
    public double getY() {
        return translateY;
    }

    @Override
    public void setY(double y) {
        this.translateY = y;
        rectangle.setTranslateY(translateY - getHeight()/2);
    }
    
}
