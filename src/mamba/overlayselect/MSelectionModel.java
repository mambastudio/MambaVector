/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.overlayselect;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Group;
import mamba.base.MambaShape;

/**
 *
 * @author user
 */
public class MSelectionModel {
    
    //group selection layer is always on top of canvas. 
    //either remove or add components to it for editing shape
    Group selectionLayer;    
    ObjectProperty<MambaShape> selectedShapeProperty;
    
    //selection overlay, where controls are
    MSelectionOverlay selectionOverlay;
    
    //set overlay group for adding editing nodes/components
    public MSelectionModel(Group layoutBoundsOverlay)
    {
        this.selectionLayer = layoutBoundsOverlay;
        this.selectedShapeProperty = new SimpleObjectProperty();
    }
    
    public void set(MambaShape shape)
    {
       
        if(shape != null)
        {
            if(shape == this.selectedShapeProperty)
                return;
            selectionOverlay = new MSelectionOverlay(shape);
            selectionLayer.getChildren().clear();
            selectionLayer.getChildren().add(selectionOverlay);
            this.selectedShapeProperty.set(shape);
            
        }
        else
            clear();
             
    }
    
  

    public void clear() {

        selectionLayer.getChildren().clear();
        selectionOverlay = null;
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
    
    public void refreshOverlay()
    {
        if(selectionOverlay != null)
        {
            selectionOverlay.refresh();           
        }
    }
    
    public void disableSelectionOverlay(boolean disable)
    {
        selectionOverlay.setDisable(disable);
    }
}
