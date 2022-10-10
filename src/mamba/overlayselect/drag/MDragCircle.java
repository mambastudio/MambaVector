/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.overlayselect.drag;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;

/**
 *
 * @author user
 */
public class MDragCircle extends MDrag{
    Circle circle;
    
    @Override
    protected ObservableList<Shape> initDrag() {
        circle = new Circle();
        circle.setRadius(4);
        circle.setFill(Color.BISQUE);
        circle.setStrokeType(StrokeType.INSIDE);  
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(1);        
        return FXCollections.observableArrayList(circle);
    }

    @Override
    public double getWidth() {
        return circle.getRadius()*2;
    }

    @Override
    public double getHeight() {
        return circle.getRadius()*2;
    }

    @Override
    public double getX() {
        return circle.getCenterX(); //the addition is due to getPosition... in super class
    }

    @Override
    public void setX(double x) {
        circle.setCenterX((int)(x )); //the addition is due to getPosition... in super class
    }

    @Override
    public double getY() {
        return circle.getCenterY(); //the addition is due to getPosition... in super class
    }

    @Override
    public void setY(double y) {
        circle.setCenterY((int)(y)); //the addition is due to getPosition... in super class
    }
    
}
