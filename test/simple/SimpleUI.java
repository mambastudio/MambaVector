/*
 * The MIT License
 *
 * Copyright 2022 jmburu.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package simple;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import mamba.base.engine.MEngine;
import mamba.base.engine.shape.MCircle2;
import mamba.components.BackgroundPane;
import mamba.overlayselect.MSelectionModel;
import mamba.util.MambaUtility;

/**
 *
 * @author jmburu
 */
public class SimpleUI extends Application{
    
    private final BackgroundPane background = new BackgroundPane();
    private final SimpleCanvas canvas = new SimpleCanvas();
    private final Group selectionLayer = new Group();
    private final MEngine engine2D = new MEngine();    
    
    @Override
    public void start(Stage primaryStage) {
        canvas.setEngine2D(engine2D);
        
        engine2D.setSelectionModel(new MSelectionModel(selectionLayer));       
        MCircle2 circle = new MCircle2();
        engine2D.addShape(circle);
        
        MCircle2 circle2 = new MCircle2();
        circle2.setSolidColor(Color.BLUE);
        
       // circle2.setLocalTransform(MTransform.scale(2, 2));
        circle2.setLocation(60, 60);
        engine2D.addShape(circle2);
        engine2D.setSelected(circle2);
             
        Pane root = new Pane(background, canvas, selectionLayer);     
        
        //ensure they grow according to base draw panel
        background.prefWidthProperty().bind(root.widthProperty());
        background.prefHeightProperty().bind(root.heightProperty());        
        canvas.prefWidthProperty().bind(root.widthProperty());
        canvas.prefHeightProperty().bind(root.heightProperty());
                
        Scene scene = new Scene(new StackPane(root), 800, 650);        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();       
        
        //https://stackoverflow.com/questions/29064225/how-to-create-a-javafx-keycombination-with-three-or-more-keys
        //register keys
        scene.setOnKeyPressed(e->{
            MambaUtility.getKeyCodes().add(e.getCode());
        });
        scene.setOnKeyReleased(e->{
            MambaUtility.getKeyCodes().remove(e.getCode());
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
