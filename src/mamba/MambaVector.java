package mamba;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import mamba.util.MultipleKeyCombination;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author user
 */
public class MambaVector extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Builder.fxml"));
        Parent root = loader.load();
        
        //three quarter size of the screen monitor
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();        
        Scene scene = new Scene(root, screenBounds.getWidth() * 0.95, screenBounds.getHeight() * 0.85);
        
        //https://stackoverflow.com/questions/29064225/how-to-create-a-javafx-keycombination-with-three-or-more-keys
        //https://bitbucket.org/kogs/multiplekeycombi/src/master/
        //register keys
        MultipleKeyCombination.setupMultipleKeyCombination(scene);        
        
        primaryStage.setScene(scene);
       // primaryStage.setMaximized(true);
        primaryStage.show();
    }
    
    
    
    public static void main(String[] args) {
        launch(args);
    }
}
