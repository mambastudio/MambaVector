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
import mamba.overlayselect.MDragHandle;
import mamba.util.MBound2;

/**
 *
 * @author jmburu
 */
public class MRectangle implements MambaShape<MEngine>{

    private ShapeState shapeState = ShapeState.DISPLAY;
    
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
    
    ObservableList<MDragHandle> dragHandles = FXCollections.observableArrayList();
    
    public MRectangle()
    {
        offset = new Point2D(0, 0);
        
        width = new SimpleDoubleProperty(45);
        height = new SimpleDoubleProperty(45);
        
        arcWidth = new SimpleDoubleProperty(0);
        arcHeight = new SimpleDoubleProperty(0);
        
        solidColor = new SimpleObjectProperty(Color.YELLOW);
        strokeWidth = new SimpleDoubleProperty(0.001);
        strokeColor = new SimpleObjectProperty(Color.BLACK);
        
        transform = Transform.translate(50, 50); 
        
        
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
        graphicContext.setStroke(strokeColor.get());
        graphicContext.setLineWidth(strokeWidth.doubleValue());
        graphicContext.fillRoundRect(
                -width.doubleValue()/2  + strokeWidth.doubleValue()/2, 
                -height.doubleValue()/2  + strokeWidth.doubleValue()/2, 
                width.doubleValue()  - strokeWidth.doubleValue(), 
                height.doubleValue() - strokeWidth.doubleValue(), 
                arcWidth.doubleValue(), 
                arcHeight.doubleValue());
        
        graphicContext.setEffect(null);
        graphicContext.setStroke(strokeColor.get());
        graphicContext.setLineWidth(strokeWidth.doubleValue());
        
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

    @Override
    public ShapeState getState() {
        return shapeState;
    }

    @Override
    public void setState(ShapeState shapeState) {
        this.shapeState = shapeState;
    }
    
    public double getWidth()
    {
        return width.doubleValue();
    }
    
    public void setWidth(double width)
    {
        this.width.set(width);
        
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
        this.height.set(height);
        
        updateDragHandles(null);
    }
    
    public DoubleProperty heightProperty()
    {
        return height;
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
    public void updateDragHandles(MDragHandle referenceHandle) {
        //TODO
        MDragHandle c1 = dragHandles.get(0);
        c1.setX(getBounds().getMinX() - 5);
        c1.setY(getBounds().getMinY() - 5);
        
        
        MDragHandle c2 = dragHandles.get(1);
        c2.setX(getBounds().getMaxX());
        c2.setY(getBounds().getMaxY());
       
       
        MDragHandle c3 = dragHandles.get(2);
        c3.setX(getBounds().getMinX() - 5);
        c3.setY(getBounds().getMaxY());
        
        MDragHandle c4 = dragHandles.get(3);        
        c4.setX(getBounds().getMaxX());
        c4.setY(getBounds().getMinY() - 5);  
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

            c1.setOnMousePressed(e->{
               Point2D p = new Point2D(e.getX(), e.getY()); 
               setOffset(Point2D.ZERO);    
            });

            c1.setOnMouseDragged(e->{

                Point2D p = new Point2D(e.getX(), e.getY());

                MBound2 nbound = new MBound2();
                MBound2 cbound = new MBound2();

                cbound.include(getBounds());          //current bounds 
                nbound.include(p, cbound.getPoint(2));      //new bounds
                double nWidth = nbound.getWidth(); //new height
                double nHeight = nbound.getHeight(); //new height
                
                setWidth((int)nWidth);  
                setHeight((int)nHeight); 
                
                updateDragHandles(null);                
                engine2D.draw();

            });

            c1.setOnMouseMoved(e->{
                c1.setCursor(Cursor.HAND);
            });
            
            MDragHandle c2 = new MDragHandle(5, Cursor.DEFAULT);
            c2.setX(getBounds().getMaxX() - 5);
            c2.setY(getBounds().getMaxY() - 5);
            dragHandles.add(c2);

            c2.setOnMousePressed(e->{
               Point2D p = new Point2D(e.getX(), e.getY()); 
               setOffset(Point2D.ZERO);     
            });

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
            
            MDragHandle c3 = new MDragHandle(5, Cursor.DEFAULT);
            c3.setX(getBounds().getMinX() - 5);
            c3.setY(getBounds().getMaxY() - 5);
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

            MDragHandle c4 = new MDragHandle(5, Cursor.DEFAULT);
            c4.setX(getBounds().getMaxX() - 5);
            c4.setY(getBounds().getMinY() - 5);
            dragHandles.add(c4);

            c4.setOnMousePressed(e->{
               Point2D p = new Point2D(e.getX(), e.getY()); 
               setOffset(Point2D.ZERO);    
            });

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
        }
                
        return dragHandles;       
        
    }
}
