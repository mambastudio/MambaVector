/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.base.engine;

import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Transform;
import mamba.base.MambaEngine2D;
import mamba.base.MambaShape;
import mamba.base.engine.shape.groups.MRoot;
import mamba.base.math.MTransform;
import mamba.base.math.MTransformGeneric;
import mamba.overlayselect.MSelectionModel;
import mamba.util.MIntersection;

/**
 *
 * @author user
 */
public class MEngine implements MambaEngine2D {
    private GraphicsContext graphicContext = null;        
    private MSelectionModel selectionModel = null;
    private MRoot rootShape = null;
    
    private MTransformGeneric transform;
    
    public MEngine()
    {
        graphicContext = null;
        rootShape = new MRoot();
        transform = new MTransform();
    }

    @Override
    public GraphicsContext getGraphicsContext() {
        return graphicContext;
    }

    @Override
    public void setGraphicsContext(GraphicsContext graphicContext) {
        this.graphicContext = graphicContext;
    }

    @Override
    public void draw() {
        graphicContext.clearRect(0, 0, Float.MAX_VALUE, Float.MAX_VALUE);                
        rootShape.draw();
    }

    @Override
    public void addShape(MambaShape shape) {
        rootShape.addShape(shape);
        shape.setGraphicContext(graphicContext);
        shape.setEngine(this);
        this.selectionModel.set(shape);
        draw();
    }

    @Override
    public void removeAll() {
        rootShape.clear();
        selectionModel.clear();
        draw();
    }

    @Override
    public void setAll(List shapes) {
        rootShape.clear();
        selectionModel.clear();
        
        List<MambaShape> listShapes = (List<MambaShape>)shapes;
        for(MambaShape shape : listShapes)
        {
            shape.setGraphicContext(graphicContext);
            shape.setEngine(this);
            rootShape.addShape(shape);
        }
        draw();
    }
        
    @Override
    public MambaShape hitSelect(Point2D p) {  
        
        MIntersection isect = new MIntersection();
        if(rootShape.intersect(p, isect))
        {
            MambaShape shape = isect.shape;
            selectionModel.set(shape);               
            return shape;
        }
        this.selectionModel.clear();
        return null;
    }

    @Override
    public MambaShape getSelected() {
        return selectionModel.getSelected();
    }

    @Override
    public void setSelected(MambaShape shape) {
        this.selectionModel.set(shape);
    }
    
    public boolean isSelected()
    {
        return selectionModel.isSelected();
    }
  
    @Override
    public void setSelectionModel(MSelectionModel selectionModel) {
        this.selectionModel = selectionModel;
    }


    @Override
    public MambaShape getRoot() {
        return rootShape;
    }

    public MambaShape transferSelectionToBack()
    {
        if(selectionModel.isSelected())
        {
            //FIXME (does not include inner nodes)
            MambaShape selected = selectionModel.getSelected();
            selectionModel.clear();
            rootShape.getChildren().remove(selected);
            rootShape.getChildren().add(0, selected);            
            draw();
            return selected;
        }
        return null;
    }
    
    public MambaShape transferSelectionToFront()
    {
        if(selectionModel.isSelected())
        {
            //FIXME (does not include inner nodes)
            MambaShape selected = selectionModel.getSelected();
            selectionModel.clear();
            rootShape.getChildren().remove(selected);
            rootShape.addShape(selected);                      
            draw();
            return selected;
        }
        return null;
    }
    
    public void deleteSelected()
    {
        if(selectionModel.isSelected())
        {
            rootShape.getChildren().remove(selectionModel.getSelected());
            selectionModel.clear();
            draw();
        }
    }

    @Override
    public MSelectionModel getSelectionModel() {
        return selectionModel;
    }

    @Override
    public MTransformGeneric getTransform() {
        return transform;
    }

    @Override
    public void setTransform(MTransformGeneric transform) {
        this.transform = transform;
        if(isSelected())
            getSelectionModel().refreshOverlay();        
    }
}
