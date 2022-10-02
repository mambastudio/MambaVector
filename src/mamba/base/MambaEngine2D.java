/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.base;

import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import mamba.overlayselect.MSelectionModel;
import mamba.util.MIntersection;

/**
 *
 * @author user
 * @param <MShape>
 */
public interface MambaEngine2D<MShape extends MambaShape> {
    
    public GraphicsContext getGraphicsContext();
    public void setGraphicsContext(GraphicsContext graphicContext);
    
    public void draw();
    public void addShape(MShape shape);
    public void removeAll();
    public void remove(MShape shape);
    
    public void addShape(List<MShape> shapes);
    public void setAll(List<MShape> shapes);
    
    public MShape getSelected();
    public MShape hitSelect(Point2D p);
    public void setSelected(MShape shape);
    default boolean intersect(Point2D p, MIntersection isect)
    {
        return getRoot().intersect(p, isect);
    }
    
    public void setSelectionModel(MSelectionModel selectionModel);
    public MSelectionModel getSelectionModel();
    
    public List<MShape> getShapes();
    
    public MShape getRoot();
    
    
}
