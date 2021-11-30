/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.overlayselect;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import mamba.base.MambaShape;

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
        
        this.setFill(Color.LIGHTBLUE);
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
    
    public static ObservableList<MDragHandle> getDefaultResizeDragHandles(MambaShape shape)
    {        
        ObservableList<MDragHandle> dragHandles = FXCollections.observableArrayList();
        
        MDragHandle c1 = new MDragHandle(5, Cursor.DEFAULT);            
        c1.setX(shape.getBounds().getMinX() - 5);
        c1.setY(shape.getBounds().getMinY() - 5);
        dragHandles.add(c1);

        c1.setOnMousePressed(e->{
           Point2D p = new Point2D(e.getX(), e.getY());                

           Point2D off = p.subtract(shape.getPosition()); //good to put offset in place                         
           shape.setOffset(off);

           c1.setCurrentPressedPoint(p);               
        });

        c1.setOnMouseDragged(e->{
            Point2D p = new Point2D(e.getX(), e.getY()); 

            double newWidth     = shape.getBounds().getMaxX() - p.getX();
            double newHeight    = shape.getBounds().getMaxY() - p.getY();

            shape.translate(p.getX() + shape.getOffset().getX(), p.getY() + shape.getOffset().getY());

            shape.setWidth(newWidth);
            shape.setHeight(newHeight);

            shape.updateDragHandles(null);
            c1.setCurrentPressedPoint(p);               

        });

        c1.setOnMouseMoved(e->{
            c1.setCursor(Cursor.HAND);
        });


        MDragHandle c2 = new MDragHandle(5, Cursor.DEFAULT);
        c2.setX(shape.getBounds().getMaxX() - 5);
        c2.setY(shape.getBounds().getMaxY() - 5);
        dragHandles.add(c2);

        c2.setOnMousePressed(e->{
           Point2D p = new Point2D(e.getX(), e.getY());                

           Point2D off = p.subtract(shape.getPosition()); //good to put offset in place                         
           shape.setOffset(off);

           c2.setCurrentPressedPoint(p);               
        });

        c2.setOnMouseDragged(e->{
            Point2D p = new Point2D(e.getX(), e.getY());

            double newWidth = p.getX() - shape.getBounds().getMinX();
            shape.setWidth(newWidth);

            double newHeight = p.getY() - shape.getBounds().getMinY();
            shape.setHeight(newHeight);

            shape.updateDragHandles(null);
        });

        c2.setOnMouseMoved(e->{
            c2.setCursor(Cursor.HAND);
        });

        MDragHandle c3 = new MDragHandle(5, Cursor.DEFAULT);
        c3.setX(shape.getBounds().getMinX() - 5);
        c3.setY(shape.getBounds().getMaxY() - 5);
        dragHandles.add(c3);

        c3.setOnMousePressed(e->{
           Point2D p = new Point2D(e.getX(), e.getY());                

           Point2D off = p.subtract(shape.getPosition()); //good to put offset in place                         
           shape.setOffset(off);

           c3.setCurrentPressedPoint(p);               
        });

        c3.setOnMouseDragged(e->{
            Point2D p = new Point2D(e.getX(), e.getY()); 
            Point2D pc = shape.getPosition();

            double newWidth = shape.getBounds().getMaxX() - p.getX(); //new width
            shape.setWidth(newWidth);                

            double newHeight = p.getY() - pc.getY();                
            shape.setHeight(newHeight);

            //ensure position y never changes
            shape.translate(p.getX() + shape.getOffset().getX(), pc.getY() + shape.getOffset().getY());

            shape.updateDragHandles(null);
            c3.setCurrentPressedPoint(p);               

        });

        c3.setOnMouseMoved(e->{
            c3.setCursor(Cursor.HAND);
        });

        MDragHandle c4 = new MDragHandle(5, Cursor.DEFAULT);
        c4.setX(shape.getBounds().getMaxX() - 5);
        c4.setY(shape.getBounds().getMinY() - 5);
        dragHandles.add(c4);

        c4.setOnMousePressed(e->{
           Point2D p = new Point2D(e.getX(), e.getY());                

           Point2D off = p.subtract(shape.getPosition()); //good to put offset in place                         
           shape.setOffset(off);

           c4.setCurrentPressedPoint(p);               
        });

        c4.setOnMouseDragged(e->{
            Point2D p = new Point2D(e.getX(), e.getY()); 
            Point2D p2 = c4.getCurrentPressedPoint();
            Point2D pc = shape.getPosition();

            double newWidth = p.getX() - shape.getBounds().getMinX();
            shape.setWidth(newWidth);

            double changeY = p2.getY() - p.getY();    //always positive if going up, and negative if going down            
            double newHeight = shape.getHeight() + changeY;               
            shape.setHeight(newHeight);                

            //ensures position x never changes
            shape.translate(pc.getX() + shape.getOffset().getX(), p.getY() + shape.getOffset().getY());

            shape.updateDragHandles(null);
            c4.setCurrentPressedPoint(p);
        });
        //c4 on mouse moved
        c4.setOnMouseMoved(e->{
            c4.setCursor(Cursor.HAND);
        });
        
        return dragHandles;
    }
}
