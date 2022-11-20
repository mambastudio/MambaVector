/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.base.engine.shape.attributes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import mamba.base.engine.shape.MPath;
import static mamba.base.engine.shape.MPath.PathToMove.MOVE_TO;
import mamba.overlayselect.drag.MDrag;
import mamba.overlayselect.drag.MDragCircle;

/**
 *
 * @author user
 */
public class MPathPoint implements MPathPointGeneric{
    MPath path;
    MPathTypeGeneric pathType;
    Point2D point;
   
    MDrag drag;
    MDrag dragC1;
    MDrag dragC2;
    
    //for calculating control points (control drags too) relative to drag point
    double tC1;
    Point2D dC1;
    double tC2;
    Point2D dC2;    
    
    public MPathPoint(MPath path)
    {
        this.path = path;
        this.pathType = MOVE_TO;
        this.point = Point2D.ZERO;
        initControlDrags();
    }
    
    public MPathPoint(MPath path, MPathTypeGeneric pathType, Point2D point)
    {
        this.path = path;
        this.pathType = pathType;
        this.point = pointInvTransform(point);
        initControlDrags();
    }
    
    private void initControlDrags()
    {
        tC1 = 0;
        dC1 = Point2D.ZERO;
        tC2 = 0;
        dC2 = Point2D.ZERO;
    }
    
    @Override
    public MPathTypeGeneric getPathType()
    {
        return pathType;
    }
    
    public Point2D getPoint()
    {
        return point;
    }
    
    public void setPoint(Point2D point)
    {
        this.point = point;
    }
    
    private Point2D pointInvTransform(Point2D point)
    {
        Point2D p = path.getTransform().inverseTransform(point);      
        return p;
    }
    
    private Point2D pointTransform(Point2D point)
    {
        return path.getTransform().transform(point);
    }
    
    @Override
    public void updateDragHandles() {                
        drag.setX(pointTransform(point).getX());
        drag.setY(pointTransform(point).getY());             
    }

    @Override
    public ObservableList<MDrag> getDragHandles() {
        MDragCircle pointDrag = new MDragCircle(Color.GREENYELLOW);
        pointDrag.setX(pointTransform(point).getX());
        pointDrag.setY(pointTransform(point).getY());     
        drag = pointDrag;
        pointDrag.setOnMouseDragged(e->{
            Point2D p = new Point2D(e.getX(), e.getY());   //in global coordinates             
            point = this.pointInvTransform(p);   //transform to local coordinates

            path.updateDragHandles(null);                
            path.getEngine2D().draw();
        });

        pointDrag.setOnMouseMoved(e->{
            pointDrag.setCursor(Cursor.HAND);
        });
        return FXCollections.observableArrayList(pointDrag);
    }
    
    private ObservableList<MDrag> getControlDragHandles()
    {
        //color
        MDragCircle c1 = new MDragCircle(Color.CHOCOLATE);
        //O + tD
        c1.setX(pointTransform(point).getX() + dC1.multiply(tC1).getX()); 
        c1.setY(pointTransform(point).getY() + dC1.multiply(tC1).getY());     
        dragC1 = c1;
        c1.setOnMouseDragged(e->{
            Point2D p = new Point2D(e.getX(), e.getY());   //in global coordinates             
            Point2D ep = this.pointInvTransform(p);   //transform to local coordinates
            
            //update relative control points data
            tC1 = ep.distance(point);
            dC1 = (tC1 > 0) ? ep.subtract(point).normalize() : Point2D.ZERO;
            tC2 = tC1;
            dC2 = dC1.multiply(-1);

            path.updateDragHandles(null);                
            path.getEngine2D().draw();
        });

        c1.setOnMouseMoved(e->{
            c1.setCursor(Cursor.HAND);
        });
        
        //color
        MDragCircle c2 = new MDragCircle(Color.CHOCOLATE);
        //O + tD
        c2.setX(pointTransform(point).getX()); 
        c2.setY(pointTransform(point).getY());     
        dragC2 = c2;
        c2.setOnMouseDragged(e->{
            Point2D p = new Point2D(e.getX(), e.getY());   //in global coordinates             
            Point2D eP = this.pointInvTransform(p);   //transform to local coordinates
            
            path.updateDragHandles(null);                
            path.getEngine2D().draw();
        });

        c2.setOnMouseMoved(e->{
            c2.setCursor(Cursor.HAND);
        });
        return FXCollections.observableArrayList(c1);
    }
}
