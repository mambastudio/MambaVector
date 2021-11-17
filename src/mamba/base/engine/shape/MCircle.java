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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import mamba.base.MambaShape;
import static mamba.base.MambaShape.ShapeState.EXPERT;
import mamba.base.MambaSupplierVoid;
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
    private final DoubleProperty lineWidth = new SimpleDoubleProperty(0.001);
    private final ObjectProperty<Color> lineColor = new SimpleObjectProperty(Color.BLACK);
    
    private final DoubleProperty translateX = new SimpleDoubleProperty(50);
    private final DoubleProperty translateY = new SimpleDoubleProperty(50);
    
    private final ObjectProperty<BoundingBox> boundingBox = new SimpleObjectProperty();
    
    private Point2D offset = new Point2D(0, 0);
    
    private MEngine engine2D;
    
    private MambaSupplierVoid supplierVoid = null;
   
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
        
        graphicContext.setStroke(lineColor.get());
        graphicContext.setLineWidth(lineWidth.doubleValue());
        graphicContext.strokeOval(anchorX.doubleValue() + translateX.doubleValue(), 
                                  anchorY.doubleValue() + translateY.doubleValue(), 
                                  width.doubleValue(), height.doubleValue());
        graphicContext.restore();
    }

    @Override
    public void drawSelect() {
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

    @Override
    public BoundingBox getBounds() {
        return getBoundsProperty().get();
    }

    @Override
    public boolean contains(Point2D p) {        
        return getBoundsProperty().get().contains(p);
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
        engine2D.initAnchorShapes();
        this.updateBounds();
        this.getEngine2D().draw();
        
    }
    
    public double getWidth()
    {        
        return this.width.doubleValue();
    }
    
    public void setHeight(double height)
    {       
        this.height.set(height);
        engine2D.initAnchorShapes();
        this.updateBounds();
        this.getEngine2D().draw();
        
    }
    
    public double getHeight()
    {
        return this.height.doubleValue();
    }
    
    public void setSolidColor(Color solidColor)
    {       
        this.solidColor.set(solidColor);
        engine2D.initAnchorShapes();
        this.updateBounds();
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
    
    public double getLineWidth()
    {        
        return this.lineWidth.doubleValue();
    }
    
    public void setLineWidth(double lineWidth)
    {       
        this.lineWidth.set(lineWidth);
        engine2D.initAnchorShapes();
        this.updateBounds();
        this.getEngine2D().draw();
      
    }
    
    public DoubleProperty lineWidthProperty()
    {
        return lineWidth;
    }
    
    public void setLineColor(Color lineColor)
    {       
        this.lineColor.set(lineColor);
        engine2D.initAnchorShapes();
        this.updateBounds();
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
        this.updateBounds();
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
    public ObservableList<MambaShape> getAnchorShapes()
    {
        ObservableList<MambaShape> anchorShapes = FXCollections.observableArrayList();
               
        MCircle c1 = getAnchorShape(getBounds().getMinX() - 5, getBounds().getMinY() - 5);
        c1.setInitPropertiesCall(()->{
            translateX.set(getBounds().getMinX() - 5);
            translateY.set(getBounds().getMinY() - 5);
        });
        anchorShapes.add(c1);
        
        MCircle c2 = getAnchorShape(getBounds().getMaxX() - 5, getBounds().getMaxY() - 5);
        c2.setInitPropertiesCall(()->{
            translateX.set(getBounds().getMaxX() - 5);
            translateY.set(getBounds().getMaxY() - 5);
        });
        anchorShapes.add(c2);
        
        MCircle c3 = getAnchorShape(getBounds().getMinX() - 5, getBounds().getMaxY() - 5);
        c3.setInitPropertiesCall(()->{
            translateX.set(getBounds().getMinX() - 5);
            translateY.set(getBounds().getMaxY() - 5);
        });
        anchorShapes.add(c3);
                
        MCircle c4 = getAnchorShape(getBounds().getMaxX() - 5, getBounds().getMinY() - 5);
        c4.setInitPropertiesCall(()->{
            translateX.set(getBounds().getMaxX() - 5);
            translateY.set(getBounds().getMinY() - 5);
        });
        anchorShapes.add(c4);
                
        return anchorShapes;
    }
    
    private MCircle getAnchorShape(double x, double y)
    {
        MCircle anchorCircle = new MCircle();
        anchorCircle.width.set(10);
        anchorCircle.height.set(10);
        anchorCircle.translateX.set(x);
        anchorCircle.translateY.set(y);
        anchorCircle.solidColor.setValue(Color.web("#8099FF"));
        anchorCircle.lineWidth.setValue(1);
        anchorCircle.setState(EXPERT);
        anchorCircle.setGraphicContext(getGraphicsContext());
        anchorCircle.setEngine(getEngine2D());
        return anchorCircle;
    }

    @Override
    public void initProperties() {
        if(supplierVoid != null)
            supplierVoid.run();
    }

    @Override
    public void setInitPropertiesCall(MambaSupplierVoid supplierVoid) {
        this.supplierVoid = supplierVoid;
    }

    @Override
    public ObjectProperty<BoundingBox> getBoundsProperty() {
        if(boundingBox.get() == null)
            updateBounds();
        return boundingBox;
    }

    @Override
    public void updateBounds() {
        boundingBox.set(new BoundingBox(
                anchorX.doubleValue() + translateX.doubleValue() - lineWidth.doubleValue()/2, 
                anchorY.doubleValue() + translateY.doubleValue() - lineWidth.doubleValue()/2, 
                width.doubleValue()   + lineWidth.doubleValue(), 
                height.doubleValue()  + lineWidth.doubleValue()));
    }
}
