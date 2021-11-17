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
import mamba.base.MambaSupplierVoid;
import mamba.base.engine.MEngine;

/**
 *
 * @author user
 */
public class MRectangle implements MambaShape<MEngine>{
    
    private GraphicsContext graphicContext;
    
    private ShapeState shapeState = ShapeState.DISPLAY;
    
    private final DoubleProperty anchorX = new SimpleDoubleProperty(0);
    private final DoubleProperty anchorY = new SimpleDoubleProperty(0);
    
    //editable properties
    private final DoubleProperty width = new SimpleDoubleProperty(90);
    private final DoubleProperty height = new SimpleDoubleProperty(90);
    private final ObjectProperty<Color> solidColor = new SimpleObjectProperty(Color.YELLOW);
    private final DoubleProperty lineWidth = new SimpleDoubleProperty(0.001);
    private final ObjectProperty<Color> lineColor = new SimpleObjectProperty(Color.BLACK);
    private final DoubleProperty arcWidth = new SimpleDoubleProperty(0);
    private final DoubleProperty arcHeight = new SimpleDoubleProperty(0);
    
    private final DoubleProperty translateX = new SimpleDoubleProperty(50);
    private final DoubleProperty translateY = new SimpleDoubleProperty(50);
    
    private Point2D offset = new Point2D(0, 0);
    
    private MEngine engine2D;
    
    public MRectangle()
    {
        
    }
    
    @Override
    public GraphicsContext getGraphicsContext() {
        return graphicContext;
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

    @Override
    public DoubleProperty widthProperty() {
        return width;
    }

    @Override
    public DoubleProperty heightProperty() {
        return height;
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
    
    public ObjectProperty<Color> solidColorProperty(){
        return solidColor;
    }
    
    public void setArcWidth(double width)
    {
        this.arcWidth.set(width);
        this.getEngine2D().draw();
    }
    
    public double getArcWidth()
    {        
        return this.arcWidth.doubleValue();
    }
    
    public DoubleProperty arcWidthProperty(){
        return arcWidth;
    }
    
    public void setArcHeight(double height)
    {
        this.arcHeight.set(height);
        this.getEngine2D().draw();
    }
    
    public double getArcHeight()
    {        
        return this.arcHeight.doubleValue();
    }
    
    public DoubleProperty arcHeightProperty(){
        return arcHeight;
    }
    
    public double getLineWidth()
    {        
        return this.lineWidth.doubleValue();
    }
    
    public void setLineWidth(double lineWidth)
    {       
        this.lineWidth.set(lineWidth);
        this.getEngine2D().draw();
    }
    
    public DoubleProperty lineWidthProperty()
    {
        return lineWidth;
    }
    
    public void setLineColor(Color lineColor)
    {       
        this.lineColor.set(lineColor);
        this.getEngine2D().draw();
    }
    
    public Color getLineColor()
    {
        return this.lineColor.get();
    }
    
    public ObjectProperty<Color> lineColorProperty(){
        return lineColor;
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

    @Override
    public MEngine getEngine2D() {
        return engine2D;
    }

    @Override
    public void setEngine(MEngine engine2D) {
        this.engine2D = engine2D;
    }

    @Override
    public void setGraphicContext(GraphicsContext context) {
        this.graphicContext = context;
    }


    @Override
    public void draw() {
         //graphicContext
        graphicContext.save();
        graphicContext.setFill(solidColor.get());
        graphicContext.fillRoundRect(anchorX.doubleValue() + translateX.doubleValue(), 
                                anchorY.doubleValue() + translateY.doubleValue(), 
                                width.doubleValue(), height.doubleValue(),
                                arcWidth.doubleValue(), arcHeight.doubleValue());
        graphicContext.setStroke(lineColor.get());
        graphicContext.setLineWidth(lineWidth.doubleValue());
        graphicContext.strokeRoundRect(anchorX.doubleValue() + translateX.doubleValue(), 
                                  anchorY.doubleValue() + translateY.doubleValue(), 
                                  width.doubleValue(), height.doubleValue(),
                                  arcWidth.doubleValue(), arcHeight.doubleValue());
        
        graphicContext.restore();
    }

    @Override
    public void drawSelect() {
        if(shapeState == SELECT)
        {
            graphicContext.save();
            graphicContext.setStroke(Color.rgb(230, 230, 230));
            graphicContext.setLineWidth(0.5);
            graphicContext.strokeRect(anchorX.doubleValue() + translateX.doubleValue() - lineWidth.doubleValue()/2, 
                                  anchorY.doubleValue() + translateY.doubleValue() - lineWidth.doubleValue()/2, 
                                  width.doubleValue() + lineWidth.doubleValue(), height.doubleValue() + lineWidth.doubleValue());
            graphicContext.setStroke(Color.rgb(80, 80, 80));
            graphicContext.setLineWidth(2);
            graphicContext.setLineDashes(5);
            graphicContext.strokeRect(anchorX.doubleValue() + translateX.doubleValue() - lineWidth.doubleValue()/2, 
                                  anchorY.doubleValue() + translateY.doubleValue() - lineWidth.doubleValue()/2, 
                                  width.doubleValue() + lineWidth.doubleValue(), height.doubleValue() + lineWidth.doubleValue());
            graphicContext.restore();
        }
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
                                anchorX.doubleValue() + translateX.doubleValue() - lineWidth.doubleValue()/2, 
                                anchorY.doubleValue() + translateY.doubleValue() - lineWidth.doubleValue()/2, 
                                width.doubleValue() + lineWidth.doubleValue(), height.doubleValue() + lineWidth.doubleValue());
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
    public void recalculateProperties() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRecalculatePropertiesCall(MambaSupplierVoid msv) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
