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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    
    //for controlling morphing of underlying shape
    private ObservableList<MambaShape> anchorShapes = null; 
    
    public MEngine()
    {
        graphicContext = null;
        listShapes = new ArrayList<>();
        selectedShape = new SimpleObjectProperty();
        anchorShapes = FXCollections.observableArrayList(); //currently empty
        
        selectedShape.addListener((o, ov, nv) ->{
            if(!nv.isExpert())
                initAnchorShapes();
        });
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
        {
            if(!selectedShape.get().isExpert())
                selectedShape.get().drawSelect();
            anchorShapes.forEach(shape->{
                shape.draw();
            });
        }
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

    //never used anywhere
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
        for(int i = anchorShapes.size()-1; i>-1; i--)
        {
            if(anchorShapes.get(i).contains(p))
            {
                MambaShape shape = anchorShapes.get(i);
                this.selectedShape.set(shape);
                return shape;
            }
        }
        
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
    
    public void initAnchorShapes()
    {
        anchorShapes.removeAll(anchorShapes);
            if(selectedShape.get() != null)
                anchorShapes.addAll(selectedShape.get().getAnchorShapes());
    }

    
}
