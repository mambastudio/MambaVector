/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.base.engine.shape.attributes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import mamba.base.engine.shape.MPath;
import static mamba.base.engine.shape.MPath.PathToMove.MOVE_TO;
import mamba.overlayselect.drag.MDrag;
import mamba.overlayselect.drag.MDragC;
import mamba.overlayselect.drag.MDragLine;

/**
 *
 * @author user
 */
public class MPathPoint implements MPathPointGeneric{
    MPath path;
    MPathTypeGeneric pathType;
    
    Point2D shapePoint;
    
    MDrag drag;
    MDrag dragC1;
    MDrag dragC2; //mirror or reflection of dragC1
    MDragLine dragLine;
    
    //for calculating control points (control drags too) relative to drag shapePoint
    //To delete
    double tC;
    Point2D dirC;
    
    public MPathPoint(MPath path)
    {
        this.path = path;
        this.pathType = MOVE_TO;
        this.shapePoint = Point2D.ZERO;
        initShapeControlDrags();
    }
    
    public MPathPoint(MPath path, MPathTypeGeneric pathType, Point2D shapePoint)
    {
        this.path = path;
        this.pathType = pathType;        
        this.shapePoint = shapePoint;
        initShapeControlDrags();
    }
    
    private void initShapeControlDrags()
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
    public Point2D getShapePoint()
    {
        return shapePoint;
    }
    
    @Override
    public void setShapePoint(Point2D point)
    {
        this.shapePoint = point;
    }
    
    
    @Override
    public void updateDragHandles() {         
        if(drag == null) //if drag handles have not been initialised for the first time
           initDragHandles();
        
        drag.setPosition(path.shapeToGlobalTransform(shapePoint));        
        Point2D pC1 = getShapeControl();
        Point2D pC2 = getMirrorShapeControl();
        
        //if editing bezier control points
        if(path.isBezierEdit())
        {
            dragC1.setPosition(path.shapeToGlobalTransform(pC1));
            dragC2.setPosition(path.shapeToGlobalTransform(pC2));
            
            dragLine.setStart(dragC1.getPosition());
            dragLine.setEnd(dragC2.getPosition());
        }
    }

    @Override
    public ObservableList<MDrag> initDragHandles() {
        MDrag pointDrag = new MDragC(path);
        pointDrag.setPosition(path.shapeToGlobalTransform(shapePoint));        
        drag = pointDrag;
        pointDrag.setOnMouseDrag(e->{
            Point2D p = new Point2D(e.getX(), e.getY());   //in global coordinates             
            shapePoint = path.globalToShapeTransform(p);   //transform to local coordinates

            path.updateDragHandles();                
            path.getEngine2D().draw();
        });

        ObservableList<MDrag> drags = FXCollections.observableArrayList(pointDrag);
        
        //if editing bezier control points
        if(path.isBezierEdit())
        {
            drags.setAll(initDragControlHandles());
            drags.add(pointDrag);
        }
        
        return drags;
    }
    
    private ObservableList<MDrag> initDragControlHandles()
    {
        //color
        MDrag c1 = new MDragC(path);
        //O + tD and when you initialise drag position
        Point2D ctrl_1 = getShapeControl();
        c1.setPosition(path.shapeToGlobalTransform(ctrl_1)); 
        dragC1 = c1;
        
        c1.setOnMouseDrag(e->{
            Point2D p = new Point2D(e.getX(), e.getY());   //in global coordinates             
            Point2D ep = path.globalToShapeTransform(p);   //transform to local coordinates
            
            //update relative control points data
            this.updateControlPointData(ep, false);
            
            path.updateDragHandles();                
            path.getEngine2D().draw();
        });
        
        //color
        MDrag c2 = new MDragC(path);
        //O + tD
        Point2D ctrl_2 = getMirrorShapeControl();
        c2.setPosition(path.shapeToGlobalTransform(ctrl_2));               
        dragC2 = c2;
        
        c2.setOnMouseDrag(e->{
            Point2D p = new Point2D(e.getX(), e.getY());   //in global coordinates             
            Point2D ep = this.path.globalToShapeTransform(p);   //transform to local coordinates
            
            //update relative control points data
            this.updateControlPointData(ep, true);
            
            path.updateDragHandles();                
            path.getEngine2D().draw();
        });
        
        dragLine = new MDragLine(path);
        dragLine.setStart(c1.getPosition());
        dragLine.setEnd(c2.getPosition());
        return FXCollections.observableArrayList(dragLine, c1, c2);
    }
    
    @Override
    public Point2D getShapeControl()
    {
        return shapePoint.add(dirC.multiply(tC));
    }
    
    @Override
    public Point2D getMirrorShapeControl()
    {
        return shapePoint.add(dirC.multiply(-1d).multiply(tC)); //opposite
    }
    
    public void updateControlPointData(Point2D ep, boolean isMirror)
    {
        //update relative control points data
        tC = ep.distance(shapePoint);
        dirC = (tC > 0) ? ep.subtract(shapePoint).normalize() : Point2D.ZERO;  
        //negate if it's mirror
        dirC = isMirror ? dirC.multiply(-1) : dirC;
    }
        
    public boolean isInBezierRange()
    {
        return tC > 0.5;
    }
    
    @Override
    public boolean contains(MDrag drag)
    {
        return this.drag == drag;
    }
}
