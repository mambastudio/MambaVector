/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import mamba.base.engine.MEngine;
import mamba.base.engine.shape.MCircle;
import mamba.components.BackgroundPane;
import mamba.components.ResizeableCanvas;
import mamba.overlayselect.MSelectionModel;

/**
 * FXML Controller class
 *
 * @author user
 */
public class BuilderController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    Pane baseDrawPanel;
    @FXML
    VBox propertyPanel;
    
    private final BackgroundPane backgroundPanel = new BackgroundPane();
    private final ResizeableCanvas renderCanvas = new ResizeableCanvas(500, 500);
    private final Group selectionLayer = new Group();
    private final MEngine engine2D = new MEngine();
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO    
                
        baseDrawPanel.getChildren().addAll(backgroundPanel, renderCanvas, selectionLayer);
        
        //ensure they grow according to base draw panel
        backgroundPanel.prefWidthProperty().bind(baseDrawPanel.widthProperty());
        backgroundPanel.prefHeightProperty().bind(baseDrawPanel.heightProperty());        
        renderCanvas.prefWidthProperty().bind(baseDrawPanel.widthProperty());
        renderCanvas.prefHeightProperty().bind(baseDrawPanel.heightProperty());
        
        renderCanvas.setEngine2D(engine2D);
        renderCanvas.setPropertyDisplayPanel(propertyPanel);
        
        engine2D.setSelectionModel(new MSelectionModel(selectionLayer));              
        
    }    
    
    public void addCircle(ActionEvent e)
    {        
        engine2D.addShape(new MCircle());        
    }
    
    public void addRectangle(ActionEvent e)
    {        
        //engine2D.addShape(new MRectangle());        
    }
    
    public void clearAll(ActionEvent e)
    {
        engine2D.removeAll();
        propertyPanel.getChildren().clear();
    }
    
}
