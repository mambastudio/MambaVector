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
import javafx.scene.Cursor;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import mamba.base.MambaShape;
import static mamba.base.MambaShape.ShapeState.EXPERT;
import mamba.base.MambaSupplierVoid;
import mamba.base.engine.MEngine;
import mamba.overlayselect.MDragHandle;

/**
 *
 * @author user
 */
public class MCircle implements MambaShape<MEngine> {
    private GraphicsContext graphicContext;
    
    private ShapeState shapeState = ShapeState.DISPLAY;
    
    private final DoubleProperty anchorX = new SimpleDoubleProperty(0);
    private final DoubleProperty anchorY = new SimpleDoubleProperty(0);
    
    private final DoubleProperty width;
    private final DoubleProperty height;
    private final ObjectProperty<Color> solidColor;
    private final DoubleProperty strokeWidth;
    private final ObjectProperty<Color> strokeColor;
    
    private final DoubleProperty translateX = new SimpleDoubleProperty(50);
    private final DoubleProperty translateY = new SimpleDoubleProperty(50);
    
    private final ObjectProperty<BoundingBox> boundingBox = new SimpleObjectProperty();
    
    private Point2D offset = new Point2D(0, 0);
    
    private MEngine engine2D;
    
    ObservableList<MDragHandle> dragHandles = FXCollections.observableArrayList();
   
    public MCircle()
    {
        width = new SimpleDoubleProperty(90);
        height = new SimpleDoubleProperty(90);
        solidColor = new SimpleObjectProperty(Color.YELLOW);
        strokeWidth = new SimpleDoubleProperty(0.001);
        strokeColor = new SimpleObjectProperty(Color.BLACK);
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
        
        graphicContext.setStroke(strokeColor.get());
        graphicContext.setLineWidth(strokeWidth.doubleValue());
        graphicContext.strokeOval(anchorX.doubleValue() + translateX.doubleValue(), 
                                  anchorY.doubleValue() + translateY.doubleValue(), 
                                  width.doubleValue(), height.doubleValue());
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
    
    public double getStrokeWidth()
    {        
        return this.strokeWidth.doubleValue();
    }
    
    public void setStrokeWidth(double strokeWidth)
    {       
        this.strokeWidth.set(strokeWidth);
        this.updateBounds();
        this.getEngine2D().draw();
      
    }
    
    public DoubleProperty strokeWidthProperty()
    {
        return strokeWidth;
    }
    
    public void setStrokeColor(Color strokeColor)
    {       
        this.strokeColor.set(strokeColor);
        this.updateBounds();
        this.getEngine2D().draw();
        
    }
    
    public Color getStrokeColor()
    {
        return this.strokeColor.get();
    }
    
    public ObjectProperty<Color> strokeColorProperty(){
        return strokeColor;
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
    public ObservableList<MDragHandle> getDragHandles()
    {
        if(dragHandles.isEmpty())
        {
            MDragHandle c1 = new MDragHandle(5, Cursor.DEFAULT);            
            c1.setX(getBounds().getMinX() - 5);
            c1.setY(getBounds().getMinY() - 5);
            dragHandles.add(c1);
            
            
            MDragHandle c2 = new MDragHandle(5, Cursor.DEFAULT);
            c2.setX(getBounds().getMaxX() - 5);
            c2.setY(getBounds().getMaxY() - 5);
            dragHandles.add(c2);
                        
            MDragHandle c3 = new MDragHandle(5, Cursor.DEFAULT);
            c3.setX(getBounds().getMinX() - 5);
            c3.setY(getBounds().getMaxY() - 5);
            dragHandles.add(c3);
                       
            MDragHandle c4 = new MDragHandle(5, Cursor.DEFAULT);
            c4.setX(getBounds().getMaxX() - 5);
            c4.setY(getBounds().getMinY() - 5);
            dragHandles.add(c4);
            c4.setOnMousePressed(e->{
               Point2D p = new Point2D(e.getX(), e.getY()); 
               System.out.println(p);
            });
            c4.setOnMouseDragged(e->{
                Point2D p = new Point2D(e.getX(), e.getY()); 
                double newWidth = p.getX() - this.getBounds().getMinX();
                setWidth(newWidth);
                updateDragHandles(null);
            });
        }
                
        return dragHandles;
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
                anchorX.doubleValue() + translateX.doubleValue() - strokeWidth.doubleValue()/2, 
                anchorY.doubleValue() + translateY.doubleValue() - strokeWidth.doubleValue()/2, 
                width.doubleValue()   + strokeWidth.doubleValue(), 
                height.doubleValue()  + strokeWidth.doubleValue()));
    }

    //called from resizable canvas
    @Override
    public void updateDragHandles(MDragHandle referenceHandle) {
        MDragHandle c1 = dragHandles.get(0);
        c1.setX(getBounds().getMinX() - 5);
        c1.setY(getBounds().getMinY() - 5);
        
        MDragHandle c2 = dragHandles.get(1);
        c2.setX(getBounds().getMaxX() - 5);
        c2.setY(getBounds().getMaxY() - 5);
       
        MDragHandle c3 = dragHandles.get(2);
        c3.setX(getBounds().getMinX() - 5);
        c3.setY(getBounds().getMaxY() - 5);
        
        MDragHandle c4 = dragHandles.get(3);
        c4.setX(getBounds().getMaxX() - 5);
        c4.setY(getBounds().getMinY() - 5);        
    }
}
