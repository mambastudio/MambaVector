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

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import mamba.base.MambaCanvas;
import mamba.base.engine.MEngine;
import mamba.base.math.MBound;
import mamba.base.math.MTransform;

/**
 *
 * @author jmburu
 */
public class SimpleCanvas extends Region implements MambaCanvas<MEngine, VBox>{
    
    private MEngine engine2D = null;
    private final Canvas canvas;
    
    public SimpleCanvas()
    {
        double width = 50, height = 50;
        //set the width and height of this and the canvas as the same
        setWidth(width);
        setHeight(height);
        canvas = new Canvas(width, height);
        //add the canvas as a child
        getChildren().add(canvas);
        //bind the canvas width and height to the region
        canvas.widthProperty().bind(this.widthProperty());
        canvas.heightProperty().bind(this.heightProperty());
        canvas.widthProperty().addListener((o, oV, nv)->{
            if(engine2D != null)
                engine2D.draw();
        });
        canvas.heightProperty().addListener((o, oV, nv)->{
            if(engine2D != null)
                engine2D.draw();
        });
        
        this.setOnMouseClicked(this::mouseClicked);
        this.setOnMouseDragged(this::mouseDragged);
        this.setOnMousePressed(this::mousePressed);
        this.setOnScroll(this::mouseScrolled);
    }

    @Override
    public void setEngine2D(MEngine engine2D) {
        this.engine2D = engine2D;
        this.engine2D.setGraphicsContext(getGraphicsContext2D());
    }

    @Override
    public MEngine getEngine2D() {
        return engine2D;
    }
    
    public GraphicsContext getGraphicsContext2D() {
        return canvas.getGraphicsContext2D();
    }

    @Override
    public void setPropertyDisplayPanel(VBox propertyDisplayPanel) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void mouseClicked(MouseEvent e)
    {
       
    }
    
    public void mousePressed(MouseEvent e)
    {            
                
    }
    
    public void mouseDragged(MouseEvent e)
    {
        
    }
    
    public void mouseScrolled(ScrollEvent e)
    {
        double deltaY = e.getDeltaY()* 0.1;
        double scale;
        if(deltaY > 0)
            scale = 1.5;
        else
            scale = 0.5;
        
        engine2D.setTransform(engine2D.getTransform().createConcatenation(MTransform.scale(scale, scale)));        
        if(engine2D.isSelected())
            engine2D.getSelectionModel().refreshOverlay();
        engine2D.draw();
    }
    
}
