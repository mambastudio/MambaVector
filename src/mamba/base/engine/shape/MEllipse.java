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
public class MEllipse implements MambaShape<MEngine>{
    
    private MEngine engine2D;
    private GraphicsContext graphicContext;
    
    private Point2D offset;
    
    //center of ellipse
    private final DoubleProperty centerX;
    private final DoubleProperty centerY;
    
    //radius of ellipse
    private final DoubleProperty radiusX;
    private final DoubleProperty radiusY;
    
    //fill color for ellipse
    private final ObjectProperty<Color> fillColor;
    
    //stroke width, color
    private final DoubleProperty strokeWidth;
    private final ObjectProperty<Color> strokeColor;
    
    private Transform transform;    
    private Effect effect;
    
    private final ObservableList<MDrag> dragHandles = FXCollections.observableArrayList();
    
    private final ObservableList<MambaShape<MEngine>> children = FXCollections.emptyObservableList();
    
    private final StringProperty nameProperty;
    
    public MEllipse()
    {
        offset = new Point2D(0, 0);
        
        centerX = new SimpleDoubleProperty(0);
        centerY = new SimpleDoubleProperty(0);
        
        radiusX = new SimpleDoubleProperty(25);
        radiusY = new SimpleDoubleProperty(15);
        
        fillColor = new SimpleObjectProperty(Color.YELLOW);
        
        strokeWidth = new SimpleDoubleProperty(0.001);
        strokeColor = new SimpleObjectProperty(Color.BLACK);
        
        //to be at positon (0, 0)
        transform = Transform.translate(radiusX.get(), radiusY.get()); //
        
        nameProperty = new SimpleStringProperty("Ellipse");
    }
    
    public void setFillColor(Color fillColor)
    {       
        this.fillColor.set(fillColor);               
    }
    
    public Color getFillColor()
    {
        return this.fillColor.get();
    }
    
    public ObjectProperty<Color> fillColorProperty()
    {
        return fillColor;
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
    public ShapeType getType() {
        return ShapeType.SHAPE;
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
        graphicContext.setFill(fillColor.get());
        graphicContext.setEffect(effect);             
        graphicContext.fillOval(
                centerX.doubleValue() - radiusX.doubleValue(), 
                centerY.doubleValue() - radiusY.doubleValue(), 
                radiusX.doubleValue() * 2, radiusY.doubleValue() * 2);
        
        graphicContext.setStroke(strokeColor.get());
        graphicContext.setLineWidth(strokeWidth.doubleValue());
        graphicContext.strokeOval(
                centerX.doubleValue() - radiusX.doubleValue(), 
                centerY.doubleValue() - radiusY.doubleValue(), 
                radiusX.doubleValue() * 2, radiusY.doubleValue() * 2);
        
        graphicContext.setEffect(null);
        graphicContext.restore(); //reset transforms and any other configurations
    }

    @Override
    public BoundingBox getBounds() {
        Point2D min = new Point2D(centerX.doubleValue() - radiusX.doubleValue(), centerY.doubleValue() - radiusY.doubleValue());
        Point2D max = new Point2D(centerX.doubleValue() + radiusX.doubleValue(), centerY.doubleValue() + radiusY.doubleValue());
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
            Point2D min = new Point2D(centerX.doubleValue() - radiusX.doubleValue(), centerY.doubleValue() - radiusY.doubleValue());
            Point2D max = new Point2D(centerX.doubleValue() + radiusX.doubleValue(), centerY.doubleValue() + radiusY.doubleValue());
            MBound2 bound = new MBound2();
            bound.include(min);
            bound.include(max);       
            return bound.contains(invP);
        } catch (NonInvertibleTransformException ex) {
            Logger.getLogger(MEllipse.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public void updateDragHandles(MDrag referenceHandle) {
        
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
                double nRadiusX = nbound.getWidth()/2; //new height
                double nRadiusY = nbound.getHeight()/2; //new height
                
                radiusX.set((int)nRadiusX);  //int cast is to avoid blurry antialiasing
                radiusY.set((int)nRadiusY); 
                
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
                double nRadiusX = nbound.getWidth()/2; //new height
                double nRadiusY = nbound.getHeight()/2; //new height
                
                radiusX.set((int)nRadiusX);  //int cast is to avoid blurry antialiasing
                radiusY.set((int)nRadiusY); 
                                
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
                double nRadiusX = nbound.getWidth()/2; //new height
                double nRadiusY = nbound.getHeight()/2; //new height
                
                radiusX.set((int)nRadiusX);  //int cast is to avoid blurry antialiasing
                radiusY.set((int)nRadiusY); 

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
                double nRadiusX = nbound.getWidth()/2; //new height
                double nRadiusY = nbound.getHeight()/2; //new height
                
                radiusX.set((int)nRadiusX);  //int cast is to avoid blurry antialiasing
                radiusY.set((int)nRadiusY); 

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
    public StringProperty getNameProperty() {
        return nameProperty;
    }

    @Override
    public String getName() {
        return nameProperty.get();
    }

    @Override
    public ObservableList<MambaShape<MEngine>> getChildren() {
        return children;
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
    public String toString()
    {
        if(getName() == null || getName().isEmpty())
            return "Ellipse";
        else
            return getName();
    }
}
