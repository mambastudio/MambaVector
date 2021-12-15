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
    
    public Point2D getPosition()
    {
        Bounds b = localToParent(getBoundsInLocal());
        return new Point2D(b.getMinX(), b.getMinY());
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
           shape.setOffset(Point2D.ZERO);    
           c1.setCurrentPressedPoint(p);   
        });
        
        c1.setOnMouseDragged(e->{
            
            Point2D p = new Point2D(e.getX(), e.getY());
            
            MBound2 nbound = new MBound2();
            MBound2 cbound = new MBound2();
            
            cbound.include(shape.getBounds());          //current bounds 
            nbound.include(p, cbound.getPoint(2));      //new bounds
            
            shape.translate(p);                 

           // shape.setWidth(nbound.getWidth());
           // shape.setHeight(nbound.getHeight());

            shape.updateDragHandles(null);
            c1.setCurrentPressedPoint(p);               

        });

        c1.setOnMouseMoved(e->{
            c1.setCursor(Cursor.HAND);
        });
        
        /*
        MDragHandle c1 = new MDragHandle(5, Cursor.DEFAULT);            
        c1.setX(shape.getBounds().getMinX() - 5);
        c1.setY(shape.getBounds().getMinY() - 5);
        dragHandles.add(c1);

        c1.setOnMousePressed(e->{
           Point2D p = new Point2D(e.getX(), e.getY()); 
           shape.setOffset(Point2D.ZERO);    
           c1.setCurrentPressedPoint(p);   
        });

        c1.setOnMouseDragged(e->{
            
            Point2D p = new Point2D(e.getX(), e.getY());
            
            MBound2 nbound = new MBound2();
            MBound2 cbound = new MBound2();
            
            cbound.include(shape.getBounds());          //current bounds 
            nbound.include(p, cbound.getPoint(2));      //new bounds
            
            shape.setPosition(
                    p.getX(), 
                    p.getY());

           // shape.setWidth(nbound.getWidth());
           // shape.setHeight(nbound.getHeight());

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
           shape.setOffset(Point2D.ZERO);    
           c2.setCurrentPressedPoint(p);   
        });
        
        c2.setOnMouseDragged(e->{
            
            Point2D p = new Point2D(e.getX(), e.getY());
            
            MBound2 nbound = new MBound2();
            MBound2 cbound = new MBound2();
            
            cbound.include(shape.getBounds());          //current bounds             
            nbound.include(cbound.getMin(), p);      //new bounds
            
//            shape.setWidth(nbound.getWidth());
          //  shape.setHeight(nbound.getHeight());
                     
            shape.updateDragHandles(null);
            c2.setCurrentPressedPoint(p);               

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
           shape.setOffset(Point2D.ZERO);    
           c3.setCurrentPressedPoint(p);   
        });
        
        c3.setOnMouseDragged(e->{
            
            Point2D p = new Point2D(e.getX(), e.getY());
            
            MBound2 nbound = new MBound2();
            MBound2 cbound = new MBound2();
            
            cbound.include(shape.getBounds());       //current bounds             
            nbound.include(p, cbound.getPoint(1));      //new bounds
            
            shape.setPosition(
                    nbound.getMin().getX(), 
                    nbound.getMin().getY());
            
          //  shape.setWidth(nbound.getWidth());
         //   shape.setHeight(nbound.getHeight());
                     
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
           shape.setOffset(Point2D.ZERO);    
           c4.setCurrentPressedPoint(p);   
        });
        
        c4.setOnMouseDragged(e->{
            
            Point2D p = new Point2D(e.getX(), e.getY());
            
            MBound2 nbound = new MBound2();
            MBound2 cbound = new MBound2();
            
            cbound.include(shape.getBounds());       //current bounds             
            nbound.include(p, cbound.getPoint(3));      //new bounds
            
            shape.setPosition(
                    nbound.getMin().getX(), 
                    nbound.getMin().getY());
            
         //   shape.setWidth(nbound.getWidth());
        //    shape.setHeight(nbound.getHeight());
                     
            shape.updateDragHandles(null);
            c4.setCurrentPressedPoint(p);               

        });
        
        //c4 on mouse moved
        c4.setOnMouseMoved(e->{
            c4.setCursor(Cursor.HAND);
        });
        */
        return dragHandles;
    }
}
