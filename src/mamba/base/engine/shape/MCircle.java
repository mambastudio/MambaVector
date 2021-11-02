/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.base.engine.shape;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import mamba.base.MambaShape;
import static mamba.base.MambaShape.ShapeState.SELECT;
import mamba.base.engine.MEngine;

/**
 *
 * @author user
 */
public class MCircle implements MambaShape<MEngine> {
    private GraphicsContext graphicContext;
    
    private ShapeState shapeState = ShapeState.DISPLAY;
    
    private final DoubleProperty anchorX = new SimpleDoubleProperty(0);
    private final DoubleProperty anchorY = new SimpleDoubleProperty(0);
    
    private final DoubleProperty width = new SimpleDoubleProperty(90);
    private final DoubleProperty height = new SimpleDoubleProperty(90);
    private final ObjectProperty<Color> solidColor = new SimpleObjectProperty(Color.YELLOW);
    
    private final DoubleProperty translateX = new SimpleDoubleProperty(50);
    private final DoubleProperty translateY = new SimpleDoubleProperty(50);
    
    private Point2D offset = new Point2D(0, 0);
    
    private MEngine engine2D;
   
    public MCircle()
    {
        
    }
    
    @Override
    public GraphicsContext getGraphicsContext() {
        return graphicContext;
    }

    @Override
    public void draw() {
        //graphicContext
        graphicContext.save();
        graphicContext.setFill(solidColor.get());
        graphicContext.fillOval(anchorX.doubleValue() + translateX.doubleValue(), 
                                anchorY.doubleValue() + translateY.doubleValue(), 
                                width.doubleValue(), height.doubleValue());
        graphicContext.restore();
    }

    @Override
    public void drawSelect() {
        graphicContext.save();
        graphicContext.setStroke(Color.rgb(230, 230, 230));
        graphicContext.setLineWidth(0.5);
        graphicContext.strokeRect(anchorX.doubleValue() + translateX.doubleValue(), 
                                  anchorY.doubleValue() + translateY.doubleValue(), 
                                   width.doubleValue(), height.doubleValue());
        graphicContext.setStroke(Color.rgb(80, 80, 80));
        graphicContext.setLineWidth(2);
        graphicContext.setLineDashes(5);
        graphicContext.strokeRect(anchorX.doubleValue() + translateX.doubleValue(), 
                                anchorY.doubleValue() + translateY.doubleValue(), 
                                width.doubleValue(), height.doubleValue());
        graphicContext.restore();
    }

    @Override
    public BoundingBox getBounds() {
        return new BoundingBox( anchorX.doubleValue() + translateX.doubleValue(), 
                                anchorY.doubleValue() + translateY.doubleValue(), 
                                width.doubleValue(), 
                                height.doubleValue());
    }

    @Override
    public boolean contains(Point2D p) {
        BoundingBox bounds = new BoundingBox(
                anchorX.doubleValue() + translateX.doubleValue(), 
                anchorY.doubleValue() + translateY.doubleValue(), 
                width.doubleValue(), height.doubleValue());
        return bounds.contains(p);
    }

    @Override
    public ShapeState getState() {
        return shapeState;
    }

    @Override
    public void setState(ShapeState shapeState) {
        this.shapeState = shapeState;
    }

    @Override
    public void setGraphicContext(GraphicsContext graphicContext) {
        this.graphicContext = graphicContext;
    }

    @Override
    public MEngine getEngine2D() {
        return engine2D;
    }

    @Override
    public void setEngine(MEngine engine2D) {
        this.engine2D = engine2D;
    }
    
    public void setWidth(double width)
    {
        this.width.set(width);
        this.getEngine2D().draw();
    }
    
    public double getWidth()
    {        
        return this.width.doubleValue();
    }
    
    public void setHeight(double height)
    {       
        this.height.set(height);
        this.getEngine2D().draw();
    }
    
    public double getHeight()
    {
        return this.height.doubleValue();
    }
    
    public void setSolidColor(Color solidColor)
    {       
        this.solidColor.set(solidColor);
        this.getEngine2D().draw();
    }
    
    public Color getSolidColor()
    {
        return this.solidColor.get();
    }

    @Override
    public DoubleProperty widthProperty() {
        return width;
    }

    @Override
    public DoubleProperty heightProperty() {
        return height;
    }
    
    public ObjectProperty<Color> solidColorProperty(){
        return solidColor;
    }


    @Override
    public void translate(double x, double y) {
        this.translateX.set(x - offset.getX());
        this.translateY.set(y - offset.getY());
    }

    @Override
    public Point2D getPosition() {
        return new Point2D( anchorX.doubleValue() + translateX.doubleValue(), 
                            anchorY.doubleValue() + translateY.doubleValue());
    }

    @Override
    public void setOffset(Point2D offset) {
        this.offset = offset;
    }
    
    
}
