/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.base.engine;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import mamba.base.MambaEngine2D;
import mamba.base.MambaShape;
import mamba.overlayselect.MSelectionModel;

/**
 *
 * @author user
 */
public class MEngine implements MambaEngine2D {
    private GraphicsContext graphicContext;
    private final List<MambaShape> listShapes;
    
    MSelectionModel selectionModel = null;
    
    public MEngine()
    {
        graphicContext = null;
        listShapes = new ArrayList<>();        
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
                
        listShapes.forEach(shape -> {
            shape.draw();
        });        
    }

    @Override
    public void addShape(MambaShape shape) {
        listShapes.add(shape);
        shape.setGraphicContext(graphicContext);
        shape.setEngine(this);
        draw();
    }

    @Override
    public void removeAll() {
        listShapes.clear();
        selectionModel.clear();
        draw();
    }

    @Override
    public void remove(MambaShape shape) {
        listShapes.remove(shape);            
        draw();
    }

        
    @Override
    public MambaShape hitSelect(Point2D p) {  
               
        for(int i = listShapes.size()-1; i>-1; i--)
        {
            if(listShapes.get(i).contains(p))
            {
                
                MambaShape shape = listShapes.get(i);
                selectionModel.set(shape);               
                return shape;
            }
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

    
}
