/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.effect.Effect;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.util.StringConverter;
import mamba.base.engine.MEngine;
import mamba.base.engine.shape.MCircle;
import mamba.base.engine.shape.MRectangle;
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
    
    @FXML
    VBox effectPropertyDisplayPanel;
    @FXML
    ComboBox<Effect> effectTypeComboBox;
    @FXML
    Button effectButtonRemove;
    
    @FXML
    ComboBox<Paint> paintTypeComboBox;
    
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
        renderCanvas.setEffectPropertyDisplayPanel(effectPropertyDisplayPanel);
        renderCanvas.setEffectTypeComboBox(effectTypeComboBox);        
        effectTypeComboBox.setConverter(new StringConverter<Effect>(){
            @Override
            public String toString(Effect t) {
                if(t != null)
                    return t.getClass().getSimpleName();
                else
                    return "NULL";
            }

            @Override
            public Effect fromString(String string) {
                return null;
            }
            
        });
        
        engine2D.setSelectionModel(new MSelectionModel(selectionLayer));       
        
        effectButtonRemove.setOnAction(e->{
            effectTypeComboBox.setValue(null);           
        });
        
        
        ObservableList<Paint> paintTypeList = FXCollections.observableArrayList();  
        paintTypeList.add(Color.BLACK);
        paintTypeList.add(new LinearGradient(0,  0,   1,   0, true, CycleMethod.NO_CYCLE, new Stop[] { new Stop(0, Color.BLACK), new Stop(1, Color.RED)}));
        paintTypeList.add(new RadialGradient(0, .1, 100, 100,   20, false, CycleMethod.NO_CYCLE, new Stop(0, Color.RED), new Stop(1, Color.BLACK)));
        paintTypeComboBox.setConverter(new StringConverter<Paint>(){
            @Override
            public String toString(Paint t) {
                if(t != null)
                    return t.getClass().getSimpleName();
                else
                    return "NULL";
            }

            @Override
            public Paint fromString(String string) {
                return null;
            }            
        });
        paintTypeComboBox.setItems(paintTypeList);
        paintTypeComboBox.getSelectionModel().select(0);
        
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
        effectPropertyDisplayPanel.getChildren().clear();
    }
    
}
