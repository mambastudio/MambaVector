/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.overlayselect;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mamba.base.MambaShape;
import mamba.overlayselect.drag.MDrag2;

/**
 *
 * @author user
 */
public class MSelectionModel {
    //either remove or add components to it for editing shape   
    ObjectProperty<MambaShape> selectedShapeProperty;
    ObservableList<MDrag2> selectedShapeDragHandles;
        
    //set overlay group for adding editing nodes/components
    public MSelectionModel()
    {
        this.selectedShapeProperty = new SimpleObjectProperty();
        this.selectedShapeDragHandles = FXCollections.observableArrayList();
        
        selectedShapeProperty.addListener((o, ov, nv)->{
            if(nv != null)
            {
                selectedShapeDragHandles.setAll(selectedShapeProperty.get().initDragHandles());
                nv.getEngine2D().draw();
            }
            else
            {
                selectedShapeDragHandles.removeAll(selectedShapeDragHandles);
                if(ov != null)
                    ov.getEngine2D().draw();
            }
        });
    }
    
    public void set(MambaShape shape)
    {       
        if(shape != null)
        {
            if(shape == this.selectedShapeProperty)
                return;            
            this.selectedShapeProperty.set(shape);            
        }
        else
            clear();             
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
        selectedShapeProperty.get().getEngine2D().draw();
    }
    
}
