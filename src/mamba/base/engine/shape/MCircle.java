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
import mamba.overlayselect.drag.MDragSquare;
import mamba.util.MBound2;

/**
 *
 * @author user
 */
public class MCircle implements MambaShape<MEngine>{
    private MEngine engine2D;
    private GraphicsContext graphicContext;
    
    private Point2D offset;
    
    private final DoubleProperty radius;
    private final ObjectProperty<Color> solidColor;
    private final DoubleProperty strokeWidth;
    private final ObjectProperty<Color> strokeColor;
        
    private Effect effect = null;
    
    private Transform transform;
    
    private final StringProperty nameProperty;
    
    ObservableList<MDrag> dragHandles = FXCollections.observableArrayList();
    
    private final ObservableList<MambaShape<MEngine>> children = FXCollections.emptyObservableList();
    
    public MCircle()
    {
        offset = new Point2D(0, 0);
        
        radius = new SimpleDoubleProperty(45);
        solidColor = new SimpleObjectProperty(Color.YELLOW);
        strokeWidth = new SimpleDoubleProperty(0.001);
        strokeColor = new SimpleObjectProperty(Color.BLACK);
        
        transform = Transform.translate(50, 50); 
        
        nameProperty = new SimpleStringProperty("Circle");
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
        graphicContext.setEffect(effect);
        graphicContext.fillOval(
                -radius.doubleValue() + strokeWidth.doubleValue()/2, 
                -radius.doubleValue() + strokeWidth.doubleValue()/2, 
                radius.doubleValue() * 2 - strokeWidth.doubleValue(), 
                radius.doubleValue() * 2 - strokeWidth.doubleValue());
        
        graphicContext.setStroke(strokeColor.get());
        graphicContext.setLineWidth(strokeWidth.doubleValue());
        graphicContext.strokeOval(-radius.doubleValue() + strokeWidth.doubleValue()/2, 
                                  -radius.doubleValue() + strokeWidth.doubleValue()/2, 
                                  radius.doubleValue()*2  - strokeWidth.doubleValue(), 
                                  radius.doubleValue()*2 - strokeWidth.doubleValue());
        
        graphicContext.setEffect(null);
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
    
    /*
        - when mouse pressed
            - set current pressed point //not of any use here
        - when mouse dragging
            - get current bounds
            - calculate new bounds based on new drag position and current bounds
            - modify shape based on new bounds/or user defined modification
            - updated drag nodes based on new bounds
            - draw shape
    */
    
    @Override
    public ObservableList<MDrag> getDragHandles()
    {
        if(dragHandles.isEmpty())
        {       
            MDragSquare c1 = new MDragSquare();            
            c1.setX(getBounds().getMinX());
            c1.setY(getBounds().getMinY());
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
                double nRadius = nbound.getMaxExtentRadius(); //new radius
                
                setRadius((int)nRadius);  
                
                updateDragHandles(null);                
                engine2D.draw();

            });

            c1.setOnMouseMoved(e->{
                c1.setCursor(Cursor.HAND);
            });
            
            MDragSquare c2 = new MDragSquare();
            c2.setX(getBounds().getMaxX());
            c2.setY(getBounds().getMaxY());
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
                double nRadius = nbound.getMaxExtentRadius(); //new radius
               
                setRadius((int)nRadius);    
                
                updateDragHandles(null);       
                
                engine2D.draw();
            });

            c2.setOnMouseMoved(e->{
                c2.setCursor(Cursor.HAND);
            });
            
            MDragSquare c3 = new MDragSquare();
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
                double nRadius = nbound.getMaxExtentRadius(); //new radius
               
                setRadius((int)nRadius);

                updateDragHandles(null);            

                engine2D.draw();
            });

            c3.setOnMouseMoved(e->{
                c3.setCursor(Cursor.HAND);
            });

            MDragSquare c4 = new MDragSquare();
            c4.setX(getBounds().getMaxX());
            c4.setY(getBounds().getMinY());
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
                double nRadius = nbound.getMaxExtentRadius(); //new radius
               
                setRadius((int)nRadius);

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

    @Override
    public void updateDragHandles(MDrag referenceHandle) {
        
        //TODO
        MDrag c1 = dragHandles.get(0);
        c1.setX(getBounds().getMinX());
        c1.setY(getBounds().getMinY());
        
        
        MDrag c2 = dragHandles.get(1);
        c2.setX(getBounds().getMaxX());
        c2.setY(getBounds().getMaxY());
       
       
        MDrag c3 = dragHandles.get(2);
        c3.setX(getBounds().getMinX());
        c3.setY(getBounds().getMaxY());
        
        MDrag c4 = dragHandles.get(3);        
        c4.setX(getBounds().getMaxX());
        c4.setY(getBounds().getMinY());  
        
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
        return "Circle";
    }
}
