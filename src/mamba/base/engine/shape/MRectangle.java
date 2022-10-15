/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.base.engine.shape;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;
import mamba.base.MambaShape;
import mamba.base.engine.MEngine;
import mamba.overlayselect.drag.MDrag;
import mamba.overlayselect.drag.MDragCircle;
import mamba.overlayselect.drag.MDragSquare;
import mamba.util.MBound2;

/**
 *
 * @author jmburu
 */
public final class MRectangle implements MambaShape<MEngine>{

    private MEngine engine2D;
    private GraphicsContext graphicContext;
    
    private Point2D offset;
    
    private final DoubleProperty width;
    private final DoubleProperty height;
    
    private final DoubleProperty arcWidth;
    private final DoubleProperty arcHeight;
    
    private final ObjectProperty<Color> solidColor;
    private final DoubleProperty strokeWidth;
    private final ObjectProperty<Color> strokeColor;
        
    private Transform transform;
    
    private Effect effect = null;
    
    ObservableList<MDrag> dragHandles = FXCollections.observableArrayList();
    
    private final ObservableList<MambaShape<MEngine>> children = FXCollections.emptyObservableList();
    
    private final StringProperty nameProperty;
    
    public MRectangle()
    {
        offset = new Point2D(0, 0);
        
        width = new SimpleDoubleProperty(50);
        height = new SimpleDoubleProperty(50);
        
        arcWidth = new SimpleDoubleProperty(0);
        arcHeight = new SimpleDoubleProperty(0);
        
        solidColor = new SimpleObjectProperty(Color.YELLOW);
        strokeWidth = new SimpleDoubleProperty(0.001);
        strokeColor = new SimpleObjectProperty(Color.BLACK);
        
        //to be at positon (0, 0), Transform.translate(width.get()/2, height.get()/2), since origin is at the middle
        transform = Transform.translate(width.get()/2, height.get()/2); //
        
        nameProperty = new SimpleStringProperty();
    }
    
    public MRectangle(
            double x,
            double y,
            double width, 
            double height,
            double rx, 
            double ry
    )
    {
        this();
        this.translate(new Point2D(x + width/2, y + height/2));
        this.width.set(width);
        this.height.set(height);
        this.arcWidth.set(rx);
        this.arcHeight.set(ry);
    }
    
    public MRectangle(
            double x,
            double y,
            double width, 
            double height,
            double rx, 
            double ry,
            Color fill,
            Color stroke,
            double stroke_width
    )
    {
        this(x, y, width, height, rx, ry);
        this.solidColor.set(fill);
        this.strokeColor.set(stroke);
        this.strokeWidth.set(stroke_width);
    }
    
    @Override
    public Transform getTransform() {
       return transform;
    }

    @Override
    public void setTransform(Transform transform) {
        this.transform = transform;
    }

    @Override
    public void translate(Point2D p) {
        Point2D tp = p.subtract(offset);
        this.transform = Transform.translate(tp.getX(), tp.getY());
    }

    @Override
    public Point2D getTranslate() {
        return this.transform.transform(new Point2D(0, 0));
    }

    @Override
    public void setOffset(Point2D offset) {
        this.offset = offset;
    }

    @Override
    public Point2D getOffset() {
        return offset;
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
    public GraphicsContext getGraphicsContext() {
        return this.graphicContext;
    }

    @Override
    public void draw() {
        graphicContext.save();
        //apply transform first
        graphicContext.setTransform(
                transform.getMxx(), transform.getMyx(), transform.getMxy(),
                transform.getMyy(), transform.getTx(), transform.getTy());
        
        //draw shape, this is just local coordinates 
        graphicContext.setFill(solidColor.get());
        graphicContext.setEffect(effect);                
        graphicContext.fillRoundRect(
                -width.doubleValue()/2  + strokeWidth.doubleValue()/2, 
                -height.doubleValue()/2  + strokeWidth.doubleValue()/2, 
                width.doubleValue()  - strokeWidth.doubleValue(), 
                height.doubleValue() - strokeWidth.doubleValue(), 
                arcWidth.doubleValue(), 
                arcHeight.doubleValue());
                
        graphicContext.setStroke(strokeColor.get());
        graphicContext.setLineWidth(strokeWidth.doubleValue());
        graphicContext.strokeRoundRect(-width.doubleValue()/2  + strokeWidth.doubleValue()/2, 
                -height.doubleValue()/2  + strokeWidth.doubleValue()/2, 
                width.doubleValue()  - strokeWidth.doubleValue(), 
                height.doubleValue() - strokeWidth.doubleValue(), 
                arcWidth.doubleValue(), 
                arcHeight.doubleValue());
        
        
        
        graphicContext.setEffect(null);
        graphicContext.restore(); //reset transforms and any other configurations
    }

    @Override
    public BoundingBox getBounds() {
        Point2D min = new Point2D(-width.doubleValue()/2, -height.doubleValue()/2);
        Point2D max = new Point2D(width.doubleValue()/2, height.doubleValue()/2);
        MBound2 bound = new MBound2();
        bound.include(min);
        bound.include(max);       
        return (BoundingBox) transform.transform(bound.getBoundingBox());
    }

    @Override
    public boolean contains(Point2D p) {
        try {
            //transform p to local coordinates
            Point2D invP = transform.inverseTransform(p);
            Point2D min = new Point2D(-width.doubleValue()/2, -height.doubleValue()/2);
            Point2D max = new Point2D(width.doubleValue()/2, height.doubleValue()/2);
            MBound2 bound = new MBound2();
            bound.include(min);
            bound.include(max);       
            return bound.contains(invP);
        } catch (NonInvertibleTransformException ex) {
            Logger.getLogger(MRectangle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public double getWidth()
    {
        return width.doubleValue();
    }
    
    public void setWidth(double width)
    {
        this.width.set((int)width);
        
        updateDragHandles(null);
    }
    
    public DoubleProperty widthProperty()
    {
        return width;
    }
    
    public double getHeight()
    {
        return height.doubleValue();
    }
    
    public void setHeight(double height)
    {
        this.height.set((int)height);
        
        updateDragHandles(null);
    }
    
    public DoubleProperty heightProperty()
    {
        return height;
    }
    
    public void setSolidColor(Color solidColor)
    {       
        this.solidColor.set(solidColor);        
        
    }
    
    public Color getSolidColor()
    {
        return this.solidColor.get();
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
    }
    
    public DoubleProperty strokeWidthProperty()
    {
        return strokeWidth;
    }
    
    public void setStrokeColor(Color strokeColor)
    {       
        this.strokeColor.set(strokeColor);
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
    public Effect getEffect()
    {
        return effect;
    }
    
    @Override
    public void setEffect(Effect effect)
    {
        this.effect = effect;
    }
    

    @Override
    public void updateDragHandles(MDrag referenceHandle) {
        //TODO
        MDrag c1 = dragHandles.get(0);    //top left
        c1.setX(getBounds().getMinX());
        c1.setY(getBounds().getMinY());        
        
        MDrag c2 = dragHandles.get(1);    //bottom right
        c2.setX(getBounds().getMaxX());
        c2.setY(getBounds().getMaxY());       
       
        MDrag c3 = dragHandles.get(2);    //bottom left
        c3.setX(getBounds().getMinX());
        c3.setY(getBounds().getMaxY());
        
        MDrag c4 = dragHandles.get(3);    //top right  
        c4.setX(getBounds().getMaxX());
        c4.setY(getBounds().getMinY());
        
        MDrag c5 = dragHandles.get(4);         
        double c5_new_offset_x  = c5.getOffsetPercentX() * getBounds().getWidth();  //offset from right boundary
        
        //apply arc size
        arcWidth.set(Math.abs(c5_new_offset_x));
        arcHeight.set(Math.abs(c5_new_offset_x));
        
        c5.setX(getBounds().getMaxX() + c5_new_offset_x); //variable along x-axis
        c5.setY(getBounds().getMinY() + this.getHeight()/2); //never changes along y-axis in terms of proportion
    }
    
     @Override
    public ObservableList<MDrag> getDragHandles()
    {
        if(dragHandles.isEmpty())
        {       
            MDragSquare c1 = new MDragSquare();       //top left     
            c1.setX(getBounds().getMinX());
            c1.setY(getBounds().getMinY());
            dragHandles.add(c1);

            c1.setOnMouseDragged(e->{

                Point2D p = new Point2D(e.getX(), e.getY());

                MBound2 nbound = new MBound2();
                MBound2 cbound = new MBound2();

                cbound.include(getBounds());          //current bounds 
                nbound.include(p, cbound.getPoint(2));      //new bounds
                double nWidth = nbound.getWidth(); //new height
                double nHeight = nbound.getHeight(); //new height
                
                setWidth((int)nWidth);  //int cast is to avoid blurry antialiasing
                setHeight((int)nHeight); 
                
                updateDragHandles(null);                
                engine2D.draw();

            });

            c1.setOnMouseMoved(e->{
                c1.setCursor(Cursor.HAND);
            });
            
            MDragSquare c2 = new MDragSquare(); //bottom right
            c2.setX(getBounds().getMaxX());
            c2.setY(getBounds().getMaxY());
            dragHandles.add(c2);

            c2.setOnMouseDragged(e->{
                Point2D p = new Point2D(e.getX(), e.getY());

                MBound2 nbound = new MBound2();
                MBound2 cbound = new MBound2();

                cbound.include(getBounds());          //current bounds 
                nbound.include(p, cbound.getPoint(0));      //new bounds
               
                double nWidth = nbound.getWidth(); //new height
                double nHeight = nbound.getHeight(); //new height
                
                setWidth((int)nWidth);  
                setHeight((int)nHeight); 
                
                updateDragHandles(null);       
                
                engine2D.draw();
            });

            c2.setOnMouseMoved(e->{
                c2.setCursor(Cursor.HAND);
            });
            
            MDragSquare c3 = new MDragSquare(); //bottom left
            c3.setX(getBounds().getMinX());
            c3.setY(getBounds().getMaxY());
            dragHandles.add(c3);

            c3.setOnMousePressed(e->{
               Point2D p = new Point2D(e.getX(), e.getY()); 
               setOffset(Point2D.ZERO);    
            });

            c3.setOnMouseDragged(e->{

                Point2D p = new Point2D(e.getX(), e.getY());

                MBound2 nbound = new MBound2();
                MBound2 cbound = new MBound2();

                cbound.include(getBounds());       //current bounds             
                nbound.include(p, cbound.getPoint(1));      //new bounds
                double nWidth = nbound.getWidth(); //new height
                double nHeight = nbound.getHeight(); //new height
                
                setWidth((int)nWidth);  
                setHeight((int)nHeight); 

                updateDragHandles(null);            

                engine2D.draw();
            });

            c3.setOnMouseMoved(e->{                             
                c3.setCursor(Cursor.HAND);
            });

            MDragSquare c4 = new MDragSquare();  //top right
            c4.setX(getBounds().getMaxX());
            c4.setY(getBounds().getMinY());
            dragHandles.add(c4);

            c4.setOnMouseDragged(e->{

                Point2D p = new Point2D(e.getX(), e.getY());

                MBound2 nbound = new MBound2();
                MBound2 cbound = new MBound2();

                cbound.include(getBounds());       //current bounds             
                nbound.include(p, cbound.getPoint(3));      //new bounds
                double nWidth = nbound.getWidth(); //new height
                double nHeight = nbound.getHeight(); //new height
                
                setWidth((int)nWidth);  
                setHeight((int)nHeight); 

                updateDragHandles(null);             

                engine2D.draw();
            });

            //c4 on mouse moved
            c4.setOnMouseMoved(e->{
                c4.setCursor(Cursor.HAND);
            });
            
            //not that rounded corners or arcs are edited as equal size, whic needs to be independent arc width and arc heigh editing
            //TODO
            MDrag c5 = new MDragCircle();            
            c5.setX(this.getBounds().getMaxX() + arcWidth.doubleValue());
            c5.setY(this.getBounds().getMinY() + this.getHeight()/2);
            
            c5.setOffsetX(arcWidth.doubleValue(), this.getBounds().getWidth());
            
            dragHandles.add(c5);
            
            //for arc size calculations
            c5.setOnMouseDragged(e->{
                Point2D p = new Point2D(e.getX(), e.getY());           
                
                double offsetXFromRightBoundary = p.getX() - this.getBounds().getMaxX();  //positive or negative size depending on relative position
                double limitXFromRightBoundary  = this.getBounds().getWidth()/2; //limit, which is 1/2 width of the rectangle
                
                if(Math.abs(offsetXFromRightBoundary) < limitXFromRightBoundary) //notice we use abs(...)              
                    c5.setOffsetX(offsetXFromRightBoundary, this.getBounds().getWidth()); //set offset and xOffset percentage along x-axis
                else
                    c5.setOffsetX(Math.copySign(limitXFromRightBoundary, offsetXFromRightBoundary), this.getBounds().getWidth()); //shouldn't be greater than absolute limit
                
                updateDragHandles(null);             

                engine2D.draw();
            });

            //c5 on mouse moved
            c5.setOnMouseMoved(e->{
                c5.setCursor(Cursor.HAND);
            });
        }
        
        return dragHandles;               
    }

    @Override
    public StringProperty getNameProperty() {
        return nameProperty;
    }

    @Override
    public String getName() {
        return nameProperty.get();
    }

    @Override
    public ShapeType getType() {
        return ShapeType.SHAPE;
    }
    
    @Override
    public ObservableList<MambaShape<MEngine>> getChildren() {
        return children;
    }
    
    @Override
    public String toString()
    {
        return "Rectangle";
    }
}
