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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import mamba.base.engine.MEngine;
import mamba.base.engine.shape.MCircle;
import mamba.base.engine.shape.MRectangle;
import mamba.components.BackgroundPane;
import mamba.components.ResizeableCanvas;

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
    StackPane baseDrawPanel;
    @FXML
    VBox propertyPanel;
    
    private final BackgroundPane backgroundPanel = new BackgroundPane();
    private final ResizeableCanvas renderCanvas = new ResizeableCanvas(500, 500);
    private final MEngine engine2D = new MEngine();
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        baseDrawPanel.getChildren().addAll(backgroundPanel, renderCanvas);
        renderCanvas.setEngine2D(engine2D);
        renderCanvas.setPropertyDisplayPanel(propertyPanel);
              
        /*
        engine2D.selectedObjectProperty().addListener((o, ov, nv)->{
            
            if(nv == null)
                propertyPanel.getChildren().clear();
            else
            {
                propertyPanel.getChildren().clear();
                ObservableList<MBeanPropertyItem> properties = MBeanPropertyUtility.getProperties(nv);
                MBeanPropertySheet propertySheet = new MBeanPropertySheet();
                propertySheet.setFactory(new MDefaultEditorFactory());
                propertySheet.init(properties);
                propertyPanel.getChildren().add(propertySheet);
                
            }
        });
        */
    }    
    
    public void addCircle(ActionEvent e)
    {        
        engine2D.addShape(new MCircle());        
    }
    
    public void addRectangle(ActionEvent e)
    {        
        engine2D.addShape(new MRectangle());        
    }
    
    public void clearAll(ActionEvent e)
    {
        engine2D.removeAll();
        propertyPanel.getChildren().clear();
    }
    
}
