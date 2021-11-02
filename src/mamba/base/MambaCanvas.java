/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.base;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;

/**
 *
 * @author user
 * @param <Engine2D>
 * @param <PropertyDisplayPanel>
 */
public interface MambaCanvas<Engine2D extends MambaEngine2D, PropertyDisplayPanel extends Pane> {
    public void setEngine2D(Engine2D engine2D);
    public Engine2D getEngine2D();
    public MambaShape hit(Point2D point);
    public void setPropertyDisplayPanel(PropertyDisplayPanel propertyDisplayPanel);
}
