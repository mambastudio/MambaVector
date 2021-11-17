/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.overlayselect;

import javafx.scene.Group;
import mamba.base.MambaShape;

/**
 *
 * @author user
 */
public class MSelectionModel {
    Group selectionLayer;
    
    MambaShape shape;
    
    public MSelectionModel(Group layoutBoundsOverlay)
    {
        this.selectionLayer = layoutBoundsOverlay;
    }
    
    public void set(MambaShape shape)
    {
       
        if(shape != null)
        {
            if(shape == this.shape)
                return;
            MSelectionOverlay selectionOverlay = new MSelectionOverlay(shape);
            selectionLayer.getChildren().clear();
            selectionLayer.getChildren().add(selectionOverlay);
            this.shape = shape;
            
        }
        else
            clear();
             
    }
    
  

    public void clear() {

        selectionLayer.getChildren().clear();
        shape = null;
    }
    
    public boolean isSelected()
    {
        return shape != null;
    }

    public boolean isEmpty()  {
        return shape == null;
    }

    public boolean contains(MambaShape shape) {
        return this.shape == shape;
    }
    
    public MambaShape getSelected()
    {
        return shape;
    }
}
