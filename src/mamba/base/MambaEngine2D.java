/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.base;

import java.util.List;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import mamba.base.math.MTransformGeneric;
import mamba.overlayselect.MSelectionModel;
import mamba.util.MIntersection;

/**
 *
 * @author user
 * @param <MShape>
 */
public interface MambaEngine2D<MShape extends MambaShape> {
    
    public MTransformGeneric getTransform();
    public void setTransform(MTransformGeneric transform);
        
    public GraphicsContext getGraphicsContext();
    public void setGraphicsContext(GraphicsContext graphicContext);
    
    public void draw();
    
    //shape addition and removal
    public void addShape(MShape shape);
    public void removeAll();   
    public void setAll(List<MShape> shapes);
    
    //shape selection and selection model
    public MShape getSelected();    
    public void setSelected(MShape shape);
    public void setSelectionModel(MSelectionModel selectionModel);
    public MSelectionModel getSelectionModel();
    
    //intersection
    public MShape hitSelect(Point2D p); //to be deprecated
    default boolean intersect(Point2D p, MIntersection isect)
    {
        return getRoot().intersect(p, isect);
    }
    
    default boolean intersect(Bounds localBound, MIntersection isect)
    {
        return false;
    }
    
    //root shape in the hierarchy
    public MShape getRoot();       
}
