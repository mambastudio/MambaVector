/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import javax.imageio.ImageIO;
import jfx.gallery.GalleryDialog;
import mamba.base.MambaShape;
import mamba.base.engine.MEngine;
import mamba.base.engine.shape.MCircle;
import mamba.base.engine.shape.MEllipse;
import mamba.base.engine.shape.MImage;
import mamba.base.engine.shape.MLine;
import mamba.base.engine.shape.MPath;
import mamba.base.engine.shape.MPath.PathTo;
import mamba.base.engine.shape.MRectangle;
import mamba.base.parser.svg.SVGDocument;
import mamba.base.parser.svg.SVGParser;
import mamba.components.BackgroundPane;
import mamba.components.ResizeableCanvas;
import mamba.overlayselect.MSelectionModel;
import mamba.snapshot.NodeSnapshot;
import mamba.treeview.RecursiveTreeItem;
import mamba.util.MDisableStateNodes;
import mamba.util.MambaUtility;

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
    Pane rootPane;
    
    @FXML
    HBox hEditBox;
    @FXML
    VBox vShapeBox;
    
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
    @FXML
    TreeView<MambaShape> layerTreeView;    
    @FXML
    Button groupShapes;
    @FXML
    Button ungroupShapes;
    
    @FXML
    ToggleGroup editToolGroup;
    @FXML
    ToggleButton penTool;
    @FXML
    ToggleButton eraserTool;
    @FXML
    ToggleButton bendTool;
   
    
    private final BackgroundPane backgroundPanel = new BackgroundPane();
    private ResizeableCanvas renderCanvas;
    private final Group selectionLayer = new Group();
        private final MEngine engine2D = new MEngine();    
    private final FileChooser fileChooser = new FileChooser();
    
    private final MDisableStateNodes pathEditButtons = new MDisableStateNodes();
    
    private final GalleryDialog gallery = new GalleryDialog(800, 500);
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO    
        renderCanvas = new ResizeableCanvas(this, 500, 500);  
        
        baseDrawPanel.getChildren().addAll(backgroundPanel, renderCanvas, selectionLayer);
              
        //ensure they grow according to base draw panel
        backgroundPanel.prefWidthProperty().bind(baseDrawPanel.widthProperty());
        backgroundPanel.prefHeightProperty().bind(baseDrawPanel.heightProperty());        
        renderCanvas.prefWidthProperty().bind(baseDrawPanel.widthProperty());
        renderCanvas.prefHeightProperty().bind(baseDrawPanel.heightProperty());
                
        //add properties and selection model first
        renderCanvas.setPropertyDisplayPanel(propertyPanel);
        renderCanvas.setEffectPropertyDisplayPanel(effectPropertyDisplayPanel);
        engine2D.setSelectionModel(new MSelectionModel(selectionLayer));       
        renderCanvas.setEngine2D(engine2D);
        
        
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
        
        TreeItem<MambaShape> rootItem = new RecursiveTreeItem<>(
                engine2D.getRoot(),
                //engine2D.getRoot()::getDisplay,
                engine2D.getRoot()::get);
        layerTreeView.setRoot(rootItem);
        layerTreeView.setShowRoot(false);
        
        layerTreeView.getSelectionModel().selectedItemProperty().addListener((o, ov, nv)->{
            TreeItem<MambaShape> selected = nv;
            
            if(selected != null && selected.getParent() != null)
            {
                renderCanvas.select(selected.getValue());
            }
        });
        
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("SVG files", "*.svg")
        );
        
        pathEditButtons.addNodes(eraserTool, penTool, bendTool, groupShapes, ungroupShapes);
        pathEditButtons.disable();
        
        engine2D.getSelectionModel().getSelectionProperty().addListener((o, ov, nv)->{
            if(nv != null && nv instanceof MPath)
            {
                pathEditButtons.enable();
                if(this.editToolGroup.getSelectedToggle() == null)
                    this.applyCursorEdit(null);
                else
                    this.applyCursorEdit(this.editToolGroup.getSelectedToggle().getUserData());
            }               
            else
            {
                pathEditButtons.disable();
                this.applyCursorEdit(null);
            }
        });
        
        //Path edit tools
        penTool.setUserData("pentool");        
        eraserTool.setUserData("erasertool");        
        editToolGroup.selectedToggleProperty().addListener((o, ov, nv)->{
            if(nv != null)
            {
                this.applyCursorEdit(nv.getUserData());
                vShapeBox.setDisable(true);
                hEditBox.setDisable(true);
            }
            else
            {
                this.applyCursorEdit(null);
                vShapeBox.setDisable(false);
                hEditBox.setDisable(false);
            }
        });
        
        bendTool.selectedProperty().addListener((o, ov, nv)->{
            if(nv != null && engine2D.getSelected() instanceof MPath)
            {
                MPath path = (MPath)engine2D.getSelected();
                path.getIsBezierEdit().set(nv);
            }
        });
   
    }    
    
    public void addCircle(ActionEvent e)
    {        
        engine2D.addShape(new MCircle());   
        //buildTreeItems(layerTreeView, engine2D.getShapes());
    }
    
    public void addRectangle(ActionEvent e)
    {        
        engine2D.addShape(new MRectangle());   
        //buildTreeItems(layerTreeView, engine2D.getShapes());
    }
    
    public void addEllipse(ActionEvent e)
    {        
        engine2D.addShape(new MEllipse());   
        //buildTreeItems(layerTreeView, engine2D.getShapes());
    }
    
    public void addLine(ActionEvent e)
    {        
        engine2D.addShape(new MLine());   
        //buildTreeItems(layerTreeView, engine2D.getShapes());
    }
    
    public void addImage(ActionEvent e)
    {
        Optional<List<Path>> option = gallery.showAndWait(rootPane.getScene().getWindow());
        if(option.isPresent())
        {
            for(Path path : option.get())
            {
                MImage image = new MImage(path);
                engine2D.addShape(image);
            }
        }
    }
    
    public void addPath(ActionEvent e)
    {
        engine2D.addShape(new MPath());   
    }
    
    public void clearAll(ActionEvent e)
    {
        engine2D.removeAll();
        propertyPanel.getChildren().clear();
        effectPropertyDisplayPanel.getChildren().clear();
        //buildTreeItems(layerTreeView, engine2D.getShapes());
    }
    
    public void selectLayerTreeView(MambaShape shape)
    {
        TreeItem item = MambaUtility.searchTreeItem(layerTreeView.getRoot(), shape);
        layerTreeView.getSelectionModel().select(item);
    }
    
    public void open(ActionEvent e)
    {
        File svgFile = fileChooser.showOpenDialog(renderCanvas.getScene().getWindow());
        if(svgFile != null)
        {
            SVGParser parser = new SVGParser(svgFile);
            SVGDocument svg = parser.parseSVG();
            engine2D.setAll(svg.elements);
            
        }
    }
    
    public void saveImage(ActionEvent e)
    {
        FileChooser fileChooser = new FileChooser();
                
        //Set extension filter
        FileChooser.ExtensionFilter extFilter = 
                new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(renderCanvas.getScene().getWindow());

        if(file != null){
            try {
                WritableImage writableImage = new WritableImage((int)renderCanvas.getWidth(), (int)renderCanvas.getHeight());
                renderCanvas.snapshot(null, writableImage);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                ImageIO.write(renderedImage, "png", file);
            } catch (IOException ex) {
                Logger.getLogger(BuilderController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
            
    }
    
    public void transferSelectedToBack(ActionEvent e)
    {
        this.selectLayerTreeView(engine2D.transferSelectionToBack());
    }
    
    public void transferSelectedToFront(ActionEvent e)
    {
        this.selectLayerTreeView(engine2D.transferSelectionToFront());
    }
    
    public void deleteSelected(ActionEvent e)
    {
        engine2D.deleteSelected();
    }
    
    public void exit(ActionEvent e)
    {
        System.exit(0);
    }
    
    protected void applyCursorEdit(Object object)
    {
        if(object == null)
            renderCanvas.setCursor(Cursor.DEFAULT);
        else if(object.equals("pentool"))
            renderCanvas.setCursor(new ImageCursor(NodeSnapshot.getPhosphorIcon("pen-nib", 24, 90)));
        else if(object.equals("erasertool"))
            renderCanvas.setCursor(new ImageCursor(NodeSnapshot.getPhosphorIcon("eraser", 24, 90)));        
        else
            renderCanvas.setCursor(Cursor.DEFAULT);
    }
    
    public boolean isPenToolSelected()
    {
        if(pathEditButtons.isEnabled())
            return penTool.isSelected();
        return false;
    }
    
    public boolean isEraserToolSelected()
    {
        if(pathEditButtons.isEnabled())
            return eraserTool.isSelected();
        return false;
    }
}
