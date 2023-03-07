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
    
    Point2D pathPointLocal;
    Point2D pathControlLocal_1;
    Point2D pathControlLocal_2;
    
    MDrag drag;
    MDrag dragC1;
    MDrag dragC2; //mirror or reflection of dragC1
    MDragLine dragLine;
    
    //for calculating control points (control drags too) relative to drag pathPointLocal
    //To delete
   double tC;
    Point2D dirC;
    
    public MPathPoint(MPath path)
    {
        this.path = path;
        this.pathType = MOVE_TO;
        this.pathPointLocal = Point2D.ZERO;
        this.pathControlLocal_1 = Point2D.ZERO;
        this.pathControlLocal_2 = Point2D.ZERO;
        initShapeControlDrags();
    }
    
    public MPathPoint(MPath path, MPathTypeGeneric pathType, Point2D shapePoint)
    {
        this.path = path;
        this.pathType = pathType;        
        this.pathPointLocal = shapePoint;
        this.pathControlLocal_1 = Point2D.ZERO;
        this.pathControlLocal_2 = Point2D.ZERO;
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
        return pathPointLocal;
    }
    
    @Override
    public void setShapePoint(Point2D point)
    {
        this.pathPointLocal = point;
    }
    
    
    @Override
    public void updateDragHandles() {         
        if(drag == null) //if drag handles have not been initialised for the first time
           initDragHandles();
        
        drag.setPosition(path.shapeToGlobalTransform(pathPointLocal));        
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
        drag = new MDragC(path);
        drag.setPosition(path.shapeToGlobalTransform(pathPointLocal));        
        
        //update bezier point and control points (all drag nodes will be updated in the updateDragHandles())
        drag.setOnMouseDrag(e->{
            
            //get current distribution of control points from the current bezier point (distance and direction)
            //can this be presented as a matrix in future?
            Point2D dir1 = pathControlLocal_1.subtract(pathPointLocal).normalize();
            double t1 = pathPointLocal.distance(pathControlLocal_1);
            Point2D dir2 = pathControlLocal_2.subtract(pathPointLocal).normalize();
            double t2 = pathPointLocal.distance(pathControlLocal_2);
            
            //new bezier point after drag
            Point2D p = new Point2D(e.getX(), e.getY());   //in global coordinates             
            pathPointLocal = path.globalToShapeTransform(p);   //transform to local coordinates
            
            //update the controls based on the new bezier point
            //pc = p + td
            pathControlLocal_1 = pathPointLocal.add(dir1.multiply(t1));
            pathControlLocal_2 = pathPointLocal.add(dir2.multiply(t2));
            
            updateDragHandles();
            path.getEngine2D().draw();
        });

        ObservableList<MDrag> drags = FXCollections.observableArrayList(drag);
        
        //if editing bezier control points
        if(path.isBezierEdit())
        {
            drags.setAll(initDragControlHandles());
            drags.add(drag);
        }
        
        return drags;
    }
    
    //control points for path point (called from initDragHandles)
    private ObservableList<MDrag> initDragControlHandles()
    {
        //color
        MDrag c1 = new MDragC(path);
        //O + tD and when you initialise drag position       
        c1.setPosition(path.shapeToGlobalTransform(pathPointLocal)); 
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
        c2.setPosition(path.shapeToGlobalTransform(pathPointLocal));               
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
        return pathPointLocal.add(dirC.multiply(tC));
    }
    
    @Override
    public Point2D getMirrorShapeControl()
    {
        return pathPointLocal.add(dirC.multiply(-1d).multiply(tC)); //opposite
    }
    
    public void updateControlPointData(Point2D ep, boolean isMirror)
    {
        //update relative control points data
        tC = ep.distance(pathPointLocal);
        dirC = (tC > 0) ? ep.subtract(pathPointLocal).normalize() : Point2D.ZERO;  
        //negate if it's mirror
        dirC = isMirror ? dirC.multiply(-1) : dirC;
    }
        
    public boolean isInBezierRange()
    {
        return tC > 0.5;
    }
    
    public Point2D getMirrorControlPoint(Point2D point, Point2D control1)
    {
        //The Continuity of Splines (YouTube - Time: 20:00) by Freya Holmér
        return point.multiply(2).subtract(control1);
    }
    
    @Override
    public boolean contains(MDrag drag)
    {
        return this.drag == drag;
    }
}
