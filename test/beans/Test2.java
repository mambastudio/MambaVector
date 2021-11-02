/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import mamba.beans.editors.MNumericField;

/**
 *
 * @author user
 */
public class Test2 extends Application {
    
    @Override
    public void start(Stage primaryStage) {
      
        StackPane root = new StackPane();
        
        Scene scene = new Scene(root, 400, 250);
        
        MNumericField field = new MNumericField(Double.TYPE);
        field.valueProperty().addListener((o, ov, nv)->{
            System.out.println(nv);
        });
        
        root.getChildren().add(field);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
