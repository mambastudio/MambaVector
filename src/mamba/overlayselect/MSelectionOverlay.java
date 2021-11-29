/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.overlayselect;

import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import mamba.base.MambaShape;

/**
 *
 * @author user
 */
public class MSelectionOverlay extends Region{
    boolean selectionRectangleVisible = true;
    boolean dragHandlesVisible = true;
    final MambaShape monitoredShape;
    
    double diameter = 6;
    double radius = diameter / 2.0;
    Rectangle selectionRectangle = new Rectangle();
        
    
    public MSelectionOverlay(MambaShape shape) {
        this.monitoredShape = shape;       
        setPickOnBounds(false);
        this.getChildren().addAll(shape.getDragHandles());
    }
    

    // records relative x and y co-ordinates.
    private class Delta {
        double x;
        double y;
        double minX;
        double maxX;
        double minY;
        double maxY;
    }
}
