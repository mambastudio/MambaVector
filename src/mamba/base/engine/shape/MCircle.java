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
import javafx.scene.effect.DropShadow;
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
 * @author user
 */
public class MCircle implements MambaShape<MEngine>{
    
    private ShapeState shapeState = ShapeState.DISPLAY;
    
    private MEngine engine2D;
    private GraphicsContext graphicContext;
    
    private Point2D offset;
    
    private final DoubleProperty radius;
    private final ObjectProperty<Color> solidColor;
    private final DoubleProperty strokeWidth;
    private final ObjectProperty<Color> strokeColor;
    
    private Effect dropShadow = null;
    
    private Transform transform;
    
    ObservableList<MDragHandle> dragHandles = FXCollections.observableArrayList();
    
    public MCircle()
    {
        offset = new Point2D(0, 0);
        
        radius = new SimpleDoubleProperty(45);
        solidColor = new SimpleObjectProperty(Color.YELLOW);
        strokeWidth = new SimpleDoubleProperty(0.001);
        strokeColor = new SimpleObjectProperty(Color.BLACK);
        
        transform = Transform.translate(50, 50); 
        
        
    }

    @Override
    public Transform getTransform() {
        return this.transform;
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
        graphicContext.setEffect(dropShadow);
        graphicContext.fillOval(
                -radius.doubleValue() + strokeWidth.doubleValue()/2, 
                -radius.doubleValue() + strokeWidth.doubleValue()/2, 
                radius.doubleValue() * 2 - strokeWidth.doubleValue(), 
                radius.doubleValue() * 2 - strokeWidth.doubleValue());
        graphicContext.setEffect(null);
        graphicContext.setStroke(strokeColor.get());
        graphicContext.setLineWidth(strokeWidth.doubleValue());
        graphicContext.strokeOval(-radius.doubleValue() + strokeWidth.doubleValue()/2, 
                                  -radius.doubleValue() + strokeWidth.doubleValue()/2, 
                                  radius.doubleValue()*2  - strokeWidth.doubleValue(), 
                                  radius.doubleValue()*2 - strokeWidth.doubleValue());
        
        
        
        graphicContext.restore(); //reset transforms and any other configurations
    }

    @Override
    public BoundingBox getBounds() {
        Point2D min = new Point2D(-radius.doubleValue(), -radius.doubleValue());
        Point2D max = new Point2D(radius.doubleValue(), radius.doubleValue());
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
            Point2D min = new Point2D(-radius.doubleValue(), -radius.doubleValue());
            Point2D max = new Point2D(radius.doubleValue(), radius.doubleValue());
            MBound2 bound = new MBound2();
            bound.include(min);
            bound.include(max);
            return bound.contains(invP);
        } catch (NonInvertibleTransformException ex) {
            Logger.getLogger(MCircle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public double getRadius()
    {
        return radius.doubleValue();
    }
    
    public void setRadius(double radius)
    {
        this.radius.set(radius);
        
        updateDragHandles(null);
    }
    
    public DoubleProperty radiusProperty()
    {
        return radius;
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

    @Override
    public ShapeState getState() {
        return this.shapeState;
    }

    @Override
    public void setState(ShapeState shapeState) {
        this.shapeState = shapeState;
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
               c1.setCurrentPressedPoint(p);   
            });

            c1.setOnMouseDragged(e->{

                Point2D p = new Point2D(e.getX(), e.getY());

                MBound2 nbound = new MBound2();
                MBound2 cbound = new MBound2();

                cbound.include(getBounds());          //current bounds 
                nbound.include(p, cbound.getPoint(2));      //new bounds
                double nRadius = nbound.getMaxExtentRadius(); //new radius
                
                setRadius((int)nRadius);  
                
                updateDragHandles(null);
                c1.setCurrentPressedPoint(p);
                
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
               c2.setCurrentPressedPoint(p);   
            });

            c2.setOnMouseDragged(e->{

                Point2D p = new Point2D(e.getX(), e.getY());

                MBound2 nbound = new MBound2();
                MBound2 cbound = new MBound2();

                cbound.include(getBounds());          //current bounds 
                nbound.include(p, cbound.getPoint(0));      //new bounds
                double nRadius = nbound.getMaxExtentRadius(); //new radius
               
                setRadius((int)nRadius);    
                
                updateDragHandles(null);
                c2.setCurrentPressedPoint(p);        
                
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
               c3.setCurrentPressedPoint(p);   
            });

            c3.setOnMouseDragged(e->{

                Point2D p = new Point2D(e.getX(), e.getY());

                MBound2 nbound = new MBound2();
                MBound2 cbound = new MBound2();

                cbound.include(getBounds());       //current bounds             
                nbound.include(p, cbound.getPoint(1));      //new bounds
                double nRadius = nbound.getMaxExtentRadius(); //new radius
               
                setRadius((int)nRadius);

                updateDragHandles(null);
                c3.setCurrentPressedPoint(p);               

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
               c4.setCurrentPressedPoint(p);   
            });

            c4.setOnMouseDragged(e->{

                Point2D p = new Point2D(e.getX(), e.getY());

                MBound2 nbound = new MBound2();
                MBound2 cbound = new MBound2();

                cbound.include(getBounds());       //current bounds             
                nbound.include(p, cbound.getPoint(3));      //new bounds
                double nRadius = nbound.getMaxExtentRadius(); //new radius
               
                setRadius((int)nRadius);

                updateDragHandles(null);
                c4.setCurrentPressedPoint(p);               

                engine2D.draw();
            });

            //c4 on mouse moved
            c4.setOnMouseMoved(e->{
                c4.setCursor(Cursor.HAND);
            });
        }
                
        return dragHandles;       
        
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
    public Effect getEffect()
    {
        return dropShadow;
    }
    
    @Override
    public void setEffect(Effect effect)
    {
        this.dropShadow = effect;
    }
    
}
