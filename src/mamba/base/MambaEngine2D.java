/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.base;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import mamba.overlayselect.MSelectionModel;

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
    
    public MShape getSelected();
    public MShape hitSelect(Point2D p);
    public void setSelected(MShape shape);
    
    public void setSelectionModel(MSelectionModel selectionModel);
}
