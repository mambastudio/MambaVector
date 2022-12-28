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
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import mamba.base.MambaShape;
import mamba.base.MambaShapeAbstract;
import mamba.base.engine.MEngine;
import mamba.base.math.MBound;
import mamba.overlayselect.drag.MDrag;
import mamba.overlayselect.drag.MDragC;
import mamba.overlayselect.drag.MDragShape;
import mamba.util.MIntersection;

/**
 *
 * @author jmburu
 */
public final class MRectangle extends MambaShapeAbstract<MEngine>{
        
    private final DoubleProperty width;
    private final DoubleProperty height;
    
    private final DoubleProperty arcWidth;
    private final DoubleProperty arcHeight;
    
    private final ObjectProperty<Color> solidColor;
    private final DoubleProperty strokeWidth;
    private final ObjectProperty<Color> strokeColor;
    
    public final ObjectProperty<Point2D> location;
            
    private Effect effect = null;
    
    public MRectangle()
    {        
        width = new SimpleDoubleProperty(50);
        height = new SimpleDoubleProperty(50);
        
        arcWidth = new SimpleDoubleProperty(0);
        arcHeight = new SimpleDoubleProperty(0);
        
        solidColor = new SimpleObjectProperty(Color.YELLOW);
        strokeWidth = new SimpleDoubleProperty(0.001);
        strokeColor = new SimpleObjectProperty(Color.BLACK);
                
        location = new SimpleObjectProperty(Point2D.ZERO);        
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
    public void draw() {
        getGraphicsContext().save();
        //apply transform first
        this.shapeToGlobalTransform().transformGraphicsContext(getGraphicsContext());
        
        //draw shape, this is just local coordinates 
        getGraphicsContext().setFill(solidColor.get());
        //stroke line
        getGraphicsContext().setStroke(strokeColor.get());
        getGraphicsContext().setLineWidth(strokeWidth.doubleValue());
        getGraphicsContext().strokeRoundRect(
                location.get().getX() + strokeWidth.doubleValue()/2, 
                location.get().getY() + strokeWidth.doubleValue()/2, 
                width.doubleValue() - strokeWidth.doubleValue(), 
                height.doubleValue() - strokeWidth.doubleValue(),
                arcWidth.doubleValue(), 
                arcHeight.doubleValue());
        
        getGraphicsContext().setEffect(effect);                
        getGraphicsContext().fillRoundRect(
                location.get().getX() + strokeWidth.doubleValue()/2, 
                location.get().getY() + strokeWidth.doubleValue()/2, 
                width.doubleValue()  - strokeWidth.doubleValue(), 
                height.doubleValue() - strokeWidth.doubleValue(), 
                arcWidth.doubleValue(), 
                arcHeight.doubleValue());                
        
        getGraphicsContext().setEffect(null);
        getGraphicsContext().restore(); //reset transforms and any other configurations
    }

    @Override
    public Bounds getShapeBound() {
        Point2D min = new Point2D(location.get().getX(), location.get().getY());
        Point2D max = new Point2D(location.get().getX() + width.doubleValue(), location.get().getY() + height.doubleValue());
        MBound bound = new MBound();
        bound.include(min);
        bound.include(max);       
        return bound.getBoundingBox();
    }

    @Override
    public boolean intersect(Point2D parentPoint, MIntersection isect) {
        Point2D shapeSpacePoint = this.getLocalTransform().inverseTransform(parentPoint);
        //simple check
        Bounds bound = getShapeBound();
        if(bound.contains(shapeSpacePoint))
        {
            isect.shape = this;
            return true;
        }
        return false;        
    }

    @Override
    public boolean intersect(Bounds parentBound, MIntersection isect) {
        Bounds shapeSpaceBound = getLocalTransform().inverseTransform(parentBound);        
        return getShapeBound().contains(shapeSpaceBound);
    }
    
    @Override
    public boolean isComplete() {
        return true;
    }

    public double getWidth()
    {
        return width.doubleValue();
    }
    
    public void setWidth(double width)
    {
        this.width.set((int)width);        
        updateDragHandles();
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
        updateDragHandles();
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
    public void updateDragHandles() {
        //TODO
        MDragShape c1 = dragHandles.get(0);
        c1.setPosition(getGlobalBounds().getMinX(), getGlobalBounds().getMinY());
        
        MDragShape c2 = dragHandles.get(1);
        c2.setPosition(getGlobalBounds().getMaxX(), getGlobalBounds().getMaxY());
        
        MDragShape c3 = dragHandles.get(2);
        c3.setPosition(getGlobalBounds().getMinX(), getGlobalBounds().getMaxY());
              
        MDragShape c4 = dragHandles.get(3);        
        c4.setPosition(getGlobalBounds().getMaxX(), getGlobalBounds().getMinY());
        
        MDrag c5 = dragHandles.get(4);         
        double c5_x_offset  = c5.getFractionX() * getShapeBound().getWidth();  //offset from right boundary        
        //apply arc size
        arcWidth.set(Math.abs(c5_x_offset));
        arcHeight.set(Math.abs(c5_x_offset));        
        Point2D c5position = new Point2D(
                getShapeBound().getMaxX() + c5_x_offset,          //right side of rectangle
                getShapeBound().getMinY() + this.getHeight()/2    //never changes along y-axis in terms of proportion
        );        
        c5.setPosition(this.shapeToGlobalTransform(c5position));      //apply scale transforms
        
    }
    
    @Override
    public ObservableList<MDrag> initDragHandles()
    {
        if(dragHandles.isEmpty())
        {                              
            MDragC c1 = new MDragC(this);       
            c1.setPosition(getGlobalBounds().getMinX(), getGlobalBounds().getMinY());            
            dragHandles.add(c1);            

            c1.setOnMouseDrag(e->{
                Point2D p = new Point2D(e.getX(), e.getY());

                MBound cBound = new MBound(getShapeBound());
                Point2D cShapePoint = this.globalToShapeTransform(p); //to shape coordinates    
                MBound nShapeBound = new MBound(cShapePoint, cBound.getMax());
                                
                location.set(nShapeBound.getMin());
                width.set(nShapeBound.getWidth());
                height.set(nShapeBound.getHeight());
                
                updateDragHandles();                
                getEngine2D().draw();               
            });
                        
            MDragC c2 = new MDragC(this);            
            c2.setPosition(getGlobalBounds().getMaxX(), getGlobalBounds().getMaxY());            
            dragHandles.add(c2);
            
            c2.setOnMouseDrag(e->{
                Point2D p = new Point2D(e.getX(), e.getY());
                
                MBound cBound = new MBound(getShapeBound());
                Point2D cShapePoint = this.globalToShapeTransform(p); //to shape coordinates    
                MBound nShapeBound = new MBound(cBound.getUpperLeft(), cShapePoint); //simple reflection of point
                                
                location.set(nShapeBound.getMin());
                width.set(nShapeBound.getWidth());
                height.set(nShapeBound.getHeight());
                
                updateDragHandles();             
                getEngine2D().draw();
            });
            
            MDragC c3 = new MDragC(this);            
            c3.setPosition(getGlobalBounds().getMinX(), getGlobalBounds().getMaxY());           
            dragHandles.add(c3);

            c3.setOnMouseDrag(e->{
                Point2D p = new Point2D(e.getX(), e.getY());
                
                MBound cBound = new MBound(getShapeBound());
                Point2D cShapePoint = this.globalToShapeTransform(p); //to shape coordinates    
                MBound nShapeBound = new MBound(cShapePoint, cBound.getUpperRight()); //simple reflection of point
               
                location.set(nShapeBound.getMin());
                width.set(nShapeBound.getWidth());
                height.set(nShapeBound.getHeight());

                updateDragHandles(); 
                getEngine2D().draw();
            });
            
            MDragC c4 = new MDragC(this);            
            c4.setPosition(getGlobalBounds().getMaxX(), getGlobalBounds().getMinY());           
            dragHandles.add(c4);
            c4.setOnMouseDrag(e->{

                Point2D p = new Point2D(e.getX(), e.getY());

                MBound cBound = new MBound(getShapeBound());
                Point2D cShapePoint = this.globalToShapeTransform(p); //to shape coordinates    
                MBound nShapeBound = new MBound(cShapePoint, cBound.getLowerLeft()); //simple reflection of point
               
                location.set(nShapeBound.getMin());
                width.set(nShapeBound.getWidth());
                height.set(nShapeBound.getHeight());
                
                updateDragHandles();           
                getEngine2D().draw();
                
            });
            //not that rounded corners or arcs are edited as equal size, whic needs to be independent arc width and arc heigh editing
            //TODO
            MDrag c5 = new MDragC(this);            
            c5.setPosition(
                    this.getGlobalBounds().getMaxX() + arcWidth.doubleValue(),
                    this.getGlobalBounds().getMinY() + this.getHeight()/2);
            dragHandles.add(c5);            
            
            //for arc size calculations
            c5.setOnMouseDrag(e->{
                Point2D gp = new Point2D(e.getX(), e.getY());       
                Point2D sp = this.globalToShapeTransform(gp);
                
                double offsetXFromRightBoundary = sp.getX() - this.getShapeBound().getMaxX();  //positive or negative size depending on relative position
                double deltaFractionX = offsetXFromRightBoundary/this.getShapeBound().getWidth();
                
                double limitXFromRightBoundary  = 1; //limit, which is the fraction is a whole number
                
                if(Math.abs(deltaFractionX) < limitXFromRightBoundary) //notice we use abs(...)              
                    c5.setFraction(deltaFractionX, 0); //set offset and xOffset percentage along x-axis
                else
                    c5.setFraction(Math.copySign(limitXFromRightBoundary, deltaFractionX), 0); //shouldn't be greater than absolute limit
                
                updateDragHandles();        
                getEngine2D().draw();
            });

            
        }       
      
        return dragHandles;     
    }
    
    @Override
    public boolean containsGlobalPoint(Point2D globalPoint) {
        //transform p to shape space coordinates
        Point2D shapeSpacePoint = globalToShapeTransform(globalPoint);
        //simple check
        Bounds bound = getShapeBound();
        return bound.contains(shapeSpacePoint);
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
