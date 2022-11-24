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
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import mamba.base.MambaShape;
import mamba.base.MambaShapeAbstract;
import mamba.base.engine.MEngine;
import mamba.overlayselect.drag.MDrag;
import mamba.overlayselect.drag.MDragSquare;
import mamba.base.math.MBound;
import mamba.base.math.MTransform;
import mamba.util.MIntersection;

/**
 *
 * @author user
 */
public class MCircle extends MambaShapeAbstract<MEngine>{
    
    private GraphicsContext graphicContext;
        
    private final DoubleProperty radius;
    private final ObjectProperty<Color> solidColor;
    private final DoubleProperty strokeWidth;
    private final ObjectProperty<Color> strokeColor;
        
    private Effect effect = null;
    
    
    
    private final ObservableList<MambaShape<MEngine>> children = FXCollections.emptyObservableList();
    
    public MCircle()
    {        
        radius = new SimpleDoubleProperty(45);
        solidColor = new SimpleObjectProperty(Color.YELLOW);
        strokeWidth = new SimpleDoubleProperty(0.001);
        strokeColor = new SimpleObjectProperty(Color.BLACK);    
        this.setLocalTransform(MTransform.translate(50, 50));
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
        getLocalTransform().transformGraphicsContext(graphicContext);
        
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
    public boolean containsGlobalPoint(Point2D p) {
       
        //transform p to local coordinates
        Point2D invP = getLocalTransform().inverseTransform(p);
        Point2D min = new Point2D(-radius.doubleValue(), -radius.doubleValue());
        Point2D max = new Point2D(radius.doubleValue(), radius.doubleValue());
        MBound bound = new MBound();
        bound.include(min);
        bound.include(max);
        return bound.contains(invP);
        
    }
    
    public double getRadius()
    {
        return radius.doubleValue();
    }
    
    public void setRadius(double radius)
    {
        this.radius.set(radius);        
        updateDragHandles();
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
    public ObservableList<MDrag> initDragHandles()
    {
        if(dragHandles.isEmpty())
        {       
            MDragSquare c1 = new MDragSquare();            
            c1.setX(getGlobalBounds().getMinX());
            c1.setY(getGlobalBounds().getMinY());
            dragHandles.add(c1);

            c1.setOnMousePressed(e->{
               Point2D p = new Point2D(e.getX(), e.getY());                
            });

            c1.setOnMouseDragged(e->{

                Point2D p = new Point2D(e.getX(), e.getY());

                MBound nbound = new MBound();
                MBound cbound = new MBound();

                cbound.include(getGlobalBounds());          //current bounds 
                nbound.include(p, cbound.getPoint(2));      //new bounds
                double nRadius = nbound.getMaxExtentRadius(); //new radius
                
                setRadius((int)nRadius);  
                
                updateDragHandles();                
                getEngine2D().draw();

            });

            c1.setOnMouseMoved(e->{
                c1.setCursor(Cursor.HAND);
            });
            
            MDragSquare c2 = new MDragSquare();
            c2.setX(getGlobalBounds().getMaxX());
            c2.setY(getGlobalBounds().getMaxY());
            dragHandles.add(c2);

            c2.setOnMousePressed(e->{
               Point2D p = new Point2D(e.getX(), e.getY());                
            });

            c2.setOnMouseDragged(e->{

                Point2D p = new Point2D(e.getX(), e.getY());

                MBound nbound = new MBound();
                MBound cbound = new MBound();

                cbound.include(getGlobalBounds());          //current bounds 
                nbound.include(p, cbound.getPoint(0));      //new bounds
                double nRadius = nbound.getMaxExtentRadius(); //new radius
               
                setRadius((int)nRadius);    
                
                updateDragHandles();       
                
                getEngine2D().draw();
            });

            c2.setOnMouseMoved(e->{
                c2.setCursor(Cursor.HAND);
            });
            
            MDragSquare c3 = new MDragSquare();
            c3.setX(getGlobalBounds().getMinX());
            c3.setY(getGlobalBounds().getMaxY());
            dragHandles.add(c3);

            c3.setOnMousePressed(e->{
               Point2D p = new Point2D(e.getX(), e.getY());               
            });

            c3.setOnMouseDragged(e->{

                Point2D p = new Point2D(e.getX(), e.getY());

                MBound nbound = new MBound();
                MBound cbound = new MBound();

                cbound.include(getGlobalBounds());       //current bounds             
                nbound.include(p, cbound.getPoint(1));      //new bounds
                double nRadius = nbound.getMaxExtentRadius(); //new radius
               
                setRadius((int)nRadius);

                updateDragHandles();            

                getEngine2D().draw();
            });

            c3.setOnMouseMoved(e->{
                c3.setCursor(Cursor.HAND);
            });

            MDragSquare c4 = new MDragSquare();
            c4.setX(getGlobalBounds().getMaxX());
            c4.setY(getGlobalBounds().getMinY());
            dragHandles.add(c4);

            c4.setOnMousePressed(e->{
               Point2D p = new Point2D(e.getX(), e.getY());             
            });

            c4.setOnMouseDragged(e->{

                Point2D p = new Point2D(e.getX(), e.getY());

                MBound nbound = new MBound();
                MBound cbound = new MBound();

                cbound.include(getGlobalBounds());       //current bounds             
                nbound.include(p, cbound.getPoint(3));      //new bounds
                double nRadius = nbound.getMaxExtentRadius(); //new radius
               
                setRadius((int)nRadius);

                updateDragHandles();             

                getEngine2D().draw();
            });

            //c4 on mouse moved
            c4.setOnMouseMoved(e->{
                c4.setCursor(Cursor.HAND);
            });
        }
                
        return dragHandles;       
        
    }

    @Override
    public void updateDragHandles() {
        
        //TODO
        MDrag c1 = dragHandles.get(0);
        c1.setX(getGlobalBounds().getMinX());
        c1.setY(getGlobalBounds().getMinY());
                
        MDrag c2 = dragHandles.get(1);
        c2.setX(getGlobalBounds().getMaxX());
        c2.setY(getGlobalBounds().getMaxY());
              
        MDrag c3 = dragHandles.get(2);
        c3.setX(getGlobalBounds().getMinX());
        c3.setY(getGlobalBounds().getMaxY());
        
        MDrag c4 = dragHandles.get(3);        
        c4.setX(getGlobalBounds().getMaxX());
        c4.setY(getGlobalBounds().getMinY());          
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
    public ObservableList<MambaShape<MEngine>> getChildren() {
        return children;
    }

    @Override
    public String toString()
    {
        return "Circle";
    }
    
    @Override
    public boolean intersect(Point2D localPoint, MIntersection isect) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean intersect(Bounds localBound, MIntersection isect) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BoundingBox getShapeBound() {
        Point2D min = new Point2D(-radius.doubleValue(), -radius.doubleValue());
        Point2D max = new Point2D(radius.doubleValue(), radius.doubleValue());
        MBound bound = new MBound();
        bound.include(min);
        bound.include(max);       
        return (BoundingBox)(bound.getBoundingBox());
    }

    @Override
    public boolean isComplete() {
        return true;
    }
}
