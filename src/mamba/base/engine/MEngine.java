/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.base.engine;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import mamba.base.MambaEngine2D;
import mamba.base.MambaShape;

/**
 *
 * @author user
 */
public class MEngine implements MambaEngine2D {
    private GraphicsContext graphicContext;
    private final List<MambaShape> listShapes;
    private ObjectProperty<MambaShape> selectedShape = null;
    private ObjectProperty<MambaShape> expertShape = null;
    
    public MEngine()
    {
        graphicContext = null;
        listShapes = new ArrayList<>();
        selectedShape = new SimpleObjectProperty();
        expertShape = new SimpleObjectProperty();
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
        if(isSelected())
            selectedShape.get().drawSelect();
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
        selectedShape.set(null);
        draw();
    }

    @Override
    public void remove(MambaShape shape) {
        listShapes.remove(shape);            
        draw();
    }

    @Override
    public MambaShape hit(Point2D p) {        
        for(int i = listShapes.size()-1; i>-1; i--)
        {
            if(listShapes.get(i).contains(p))
            {
                MambaShape shape = listShapes.get(i);                
                return shape;
            }
        }        
        return null;
    }
    
    @Override
    public MambaShape hitSelect(Point2D p) {        
        for(int i = listShapes.size()-1; i>-1; i--)
        {
            if(listShapes.get(i).contains(p))
            {
                MambaShape shape = listShapes.get(i);
                this.selectedShape.set(shape);
                return shape;
            }
        }
        this.selectedShape.set(null);
        return null;
    }

    @Override
    public MambaShape getSelected() {
        return selectedShape.get();
    }

    @Override
    public void setSelected(MambaShape shape) {
        this.selectedShape.set(shape);
    }
    
    public boolean isSelected()
    {
        return selectedShape.get()!=null;
    }

    @Override
    public ObjectProperty<MambaShape> selectedObjectProperty() {
        return selectedShape;
    }
    
}
