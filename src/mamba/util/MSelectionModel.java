/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.util;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import mamba.base.MambaShape;
import mamba.overlayselect.drag.MDrag2;
import mamba.overlayselect.drag.MDragShape;
import mamba.util.MIntersection;

/**
 *
 * @author user
 */
public class MSelectionModel {
    //either remove or add components to it for editing shape   
    ObjectProperty<MambaShape> selectedShapeProperty;
    ObjectProperty<MDrag2> selectedDragHandleProperty;
    
    //delected shape drag handles
    ObservableList<MDrag2> selectedShapeDragHandleList;
        
    //set overlay group for adding editing nodes/components
    public MSelectionModel()
    {
        this.selectedShapeProperty = new SimpleObjectProperty();
        this.selectedDragHandleProperty = new SimpleObjectProperty();
        this.selectedShapeDragHandleList = FXCollections.observableArrayList();
        
        selectedShapeProperty.addListener((o, ov, nv)->{
            if(nv != null)
            {               
                selectedShapeDragHandleList.setAll(selectedShapeProperty.get().initDragHandles());  
                nv.updateDragHandles();
                nv.getEngine2D().draw();                
            }
            else
            {
                selectedShapeDragHandleList.removeAll(selectedShapeDragHandleList);
                if(ov != null)
                    ov.getEngine2D().draw();
            }
        });
    }
    
    public void set(MambaShape shape)
    {       
        if(shape != null)
        {
            if(shape == this.selectedShapeProperty.get())
                return;            
            this.selectedShapeProperty.set(shape);            
        }
        else
            clear();             
    }
    
    public boolean intersectDragHandles(Point2D canvasPoint)
    {       
        if(selectedShapeDragHandleList.isEmpty())
            return false;
        
        MIntersection isect = new MIntersection();
        for(MDrag2 drag: selectedShapeDragHandleList)
            if(drag.intersect(canvasPoint, isect))
            {
                selectedDragHandleProperty.set((MDrag2)isect.shape);
                return true;
            }
        selectedDragHandleProperty.set(null);        
        return false;
    }
    
    public boolean isDragHandleSelected()
    {
        return selectedDragHandleProperty.get() != null;
    }
    
    public void removeDragHandleSelected()
    {
        selectedDragHandleProperty.set(null);
    }
    
    public MDrag2 getDragHandleSelected()
    {
        return selectedDragHandleProperty.get();
    }

    public void clear() {
        
        selectedShapeProperty.set(null);
    }
    
    public boolean isSelected()
    {
        return selectedShapeProperty.get() != null;
    }

    public boolean isEmpty()  {
        return selectedShapeProperty.get() == null;
    }

    public boolean contains(MambaShape shape) {
        return this.selectedShapeProperty.get() == shape;
    }
    
    public MambaShape getSelected()
    {
        return selectedShapeProperty.get();
    }
    
    public ObjectProperty<MambaShape> getSelectionProperty()
    {
        return selectedShapeProperty;
    }
    
    public void refreshDragHandles()
    {        
        selectedShapeProperty.get().updateDragHandles();        
    }
    
    public void refreshDragHandlesAndDraw()
    {        
        selectedShapeProperty.get().updateDragHandles();
        selectedShapeProperty.get().getEngine2D().draw();
    }
    
    public ObservableList<MDrag2> getSelectedShapeDragHandles()
    {
        return FXCollections.unmodifiableObservableList(selectedShapeDragHandleList);
    }
}
