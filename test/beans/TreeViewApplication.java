/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import mamba.base.MambaHierarchyData;
import mamba.treeview.RecursiveTreeItem;

/**
 *
 * @author jmburu
 */
public class TreeViewApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    
    
       
   
    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();       
        TreeView<StringDisplay> treeView = new TreeView<>();
        
        StringDisplay rootData = new StringDisplay("Joe");       
        rootData.add("Mwangi");
        rootData.add("Mburu");
        
        
        TreeItem<StringDisplay> rootItem = new RecursiveTreeItem<>(
                rootData, rootData::getDisplay, rootData::get);
        treeView.setRoot(rootItem);
        
        Button addButton = new Button("Add");
        addButton.setOnAction(e->{
            rootData.add(("Joe " +(int)(Math.random()*100)));
        });
        
        root.getChildren().addAll(addButton, treeView);        
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("TreeView Demo");
        primaryStage.show();
        
        //System.out.println(rootNode.findParent(sDisplay));
    }
    
    public class StringDisplay implements MambaHierarchyData
    {

       
        private final StringProperty name;
        private final ObservableList<StringDisplay> children = FXCollections.observableArrayList();
        
        public StringDisplay(String string)
        {
            name = new SimpleStringProperty(string);
        }
        
        public String getName()
        {
            return name.get();
        }
        
        public void setName(String string)
        {
            name.set(string);
        }
        
        public StringProperty propertyName()
        {
            return name;
        }
        
        public void add(String string)
        {
            getChildren().add(new StringDisplay(string));
        }
        
        public Shape getDisplay(StringDisplay display)
        {
            if(children.isEmpty())
            {
                Circle circle = new Circle(10);
                circle.setFill(Color.AQUAMARINE);
                return circle;
            }
            else
            {
                Rectangle rectangle = new Rectangle(10, 10);
                rectangle.setFill(Color.BLUEVIOLET);
                return rectangle;
            }
        }
        
        @Override
        public ObservableList<StringDisplay> getChildren() {
            return children;
        }
                
        @Override
        public String toString()
        {
            return name.get();
        }
        
        
    }
    
    
}
