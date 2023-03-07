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
import mamba.base.math.MTransform;
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
    
       
    public MPathPoint(MPath path)
    {
        this.path = path;
        this.pathType = MOVE_TO;
        this.pathPointLocal = Point2D.ZERO;
        this.pathControlLocal_1 = Point2D.ZERO;
        this.pathControlLocal_2 = Point2D.ZERO;
    }
    
    public MPathPoint(MPath path, MPathTypeGeneric pathType, Point2D shapePoint)
    {
        this.path = path;
        this.pathType = pathType;        
        this.pathPointLocal = shapePoint;
        this.pathControlLocal_1 = new Point2D(shapePoint.getX(), shapePoint.getY());
        this.pathControlLocal_2 = new Point2D(shapePoint.getX(), shapePoint.getY());
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
               
        //if editing bezier control points
        if(path.isBezierEdit())
        {
            dragC1.setPosition(path.shapeToGlobalTransform(pathControlLocal_1));
            dragC2.setPosition(path.shapeToGlobalTransform(pathControlLocal_2));
            
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
            
            //store current state of bezier point      
            Point2D pathPointLocalPrev = new Point2D(pathPointLocal.getX(), pathPointLocal.getY());
            
            //new bezier point after drag
            Point2D p = new Point2D(e.getX(), e.getY());   //in global coordinates             
            pathPointLocal = path.globalToShapeTransform(p);   //transform to local coordinates
            
            //get transform of current translated bezier point
            MTransform translate = MTransform.translate(pathPointLocal.subtract(pathPointLocalPrev));
            
            //update the controls based on the new bezier point           
            pathControlLocal_1 = translate.transform(pathControlLocal_1);
            pathControlLocal_2 = translate.transform(pathControlLocal_2);
            
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
        dragC1 = new MDragC(path);     
        dragC1.setPosition(path.shapeToGlobalTransform(pathPointLocal)); 
        dragC1.setOnMouseDrag(e->{
            Point2D p = new Point2D(e.getX(), e.getY());   //in global coordinates             
            
            pathControlLocal_1 = path.globalToShapeTransform(p);   //transform to local coordinates            
            pathControlLocal_2 = this.getMirrorControlPoint(pathPointLocal, pathControlLocal_1);
                        
            path.updateDragHandles();                
            path.getEngine2D().draw();
        });
        
        //color
        dragC2 = new MDragC(path);
        dragC2.setPosition(path.shapeToGlobalTransform(pathControlLocal_2));   
        dragC2.setOnMouseDrag(e->{
            Point2D p = new Point2D(e.getX(), e.getY());   //in global coordinates             
            
            pathControlLocal_2 = path.globalToShapeTransform(p);   //transform to local coordinates            
            pathControlLocal_1 = this.getMirrorControlPoint(pathPointLocal, pathControlLocal_2);
            
            path.updateDragHandles();                
            path.getEngine2D().draw();
        });
        
        dragLine = new MDragLine(path);
        dragLine.setStart(dragC1.getPosition());
        dragLine.setEnd(dragC2.getPosition());
        return FXCollections.observableArrayList(dragLine, dragC1, dragC2);
    }
    
    @Override
    public Point2D getFirstControl()
    {
        return pathControlLocal_1;
    }
    
    @Override
    public Point2D getSecondControl()
    {
        return pathControlLocal_2; //opposite
    }
        
    public Point2D getMirrorControlPoint(Point2D bezierPoint, Point2D control1)
    {
        //The Continuity of Splines (YouTube - Time: 20:00) by Freya Holmér
        return bezierPoint.multiply(2).subtract(control1);
    }
    
    @Override
    public boolean contains(MDrag drag)
    {
        return this.drag == drag;
    }
}
