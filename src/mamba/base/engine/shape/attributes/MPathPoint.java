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
import mamba.overlayselect.drag.MDragLine;

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
    MDrag dragC2; //mirror or reflection of dragC1
    MDragLine dragLine;
    
    //for calculating control points (control drags too) relative to drag point
    double tC;
    Point2D dirC;
    
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
        tC = 0;
        dirC = Point2D.ZERO;        
    }
    
    @Override
    public MPathTypeGeneric getPathType()
    {
        return pathType;
    }
    
    @Override
    public Point2D getPoint()
    {
        return point;
    }
    
    @Override
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
        
        Point2D pC1 = getControl();
        Point2D pC2 = getMirrorControl();
        
        //if editing bezier control points
        if(path.isBezierEdit())
        {
            dragC1.setX(pointTransform(pC1).getX());
            dragC1.setY(pointTransform(pC1).getY());
            dragC2.setX(pointTransform(pC2).getX());
            dragC2.setY(pointTransform(pC2).getY());

            dragLine.setStart(dragC1.getX(), dragC1.getY());
            dragLine.setEnd(dragC2.getX(), dragC2.getY());
        }
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
        ObservableList<MDrag> drags = FXCollections.observableArrayList(pointDrag);
        
        //if editing bezier control points
        if(path.isBezierEdit())
            drags.addAll(getControlDragHandles());
        
        return drags;
    }
    
    private ObservableList<MDrag> getControlDragHandles()
    {
        //color
        MDragCircle c1 = new MDragCircle(Color.CHOCOLATE);
        //O + tD and when you initialise drag position
        Point2D ctrl_1 = getControl();
        c1.setX(pointTransform(ctrl_1).getX()); 
        c1.setY(pointTransform(ctrl_1).getY());     
        dragC1 = c1;
        
        c1.setOnMouseDragged(e->{
            Point2D p = new Point2D(e.getX(), e.getY());   //in global coordinates             
            Point2D ep = this.pointInvTransform(p);   //transform to local coordinates
            
            //update relative control points data
            this.updateControlPointData(ep, false);
            
            path.updateDragHandles(null);                
            path.getEngine2D().draw();
        });

        c1.setOnMouseMoved(e->{
            c1.setCursor(Cursor.HAND);
        });
        
        //color
        MDragCircle c2 = new MDragCircle(Color.CHOCOLATE);
        //O + tD
        Point2D ctrl_2 = getMirrorControl();
        c2.setX(pointTransform(ctrl_2).getX()); 
        c2.setY(pointTransform(ctrl_2).getY());     
        dragC2 = c2;
        
        c2.setOnMouseDragged(e->{
            Point2D p = new Point2D(e.getX(), e.getY());   //in global coordinates             
            Point2D ep = this.pointInvTransform(p);   //transform to local coordinates
            
            //update relative control points data
            this.updateControlPointData(ep, true);
            
            path.updateDragHandles(null);                
            path.getEngine2D().draw();
        });

        c2.setOnMouseMoved(e->{
            c2.setCursor(Cursor.HAND);
        });
        
        dragLine = new MDragLine();
        dragLine.setStart(c1.getX(), c1.getY());
        dragLine.setEnd(c2.getX(), c2.getY());
        return FXCollections.observableArrayList(dragLine, c1, c2);
    }
    
    @Override
    public Point2D getControl()
    {
        return point.add(dirC.multiply(tC));
    }
    
    @Override
    public Point2D getMirrorControl()
    {
        return point.add(dirC.multiply(-1d).multiply(tC)); //opposite
    }
    
    public void updateControlPointData(Point2D ep, boolean isMirror)
    {
        //update relative control points data
        tC = ep.distance(point);
        dirC = (tC > 0) ? ep.subtract(point).normalize() : Point2D.ZERO;  
        //negate if it's mirror
        dirC = isMirror ? dirC.multiply(-1) : dirC;
    }
        
    public boolean isInBezierRange()
    {
        return tC > 0.5;
    }
}
