/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.base.engine.shape;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import mamba.base.MambaShape;
import mamba.base.MambaShapeAbstract;
import mamba.base.engine.MEngine;
import mamba.overlayselect.drag.MDrag;
import mamba.base.math.MBound;
import mamba.overlayselect.drag.MDragC;
import mamba.overlayselect.drag.MDragShape;
import mamba.util.MIntersection;

/**
 *
 * @author user
 */
public class MImage extends MambaShapeAbstract<MEngine>{      
    private final DoubleProperty width;
    private final DoubleProperty height;
    
    private final ObjectProperty<Image> image;
    
    private final ObjectProperty<Point2D> location;
    
    public MImage(Path path)
    {              
        image = new SimpleObjectProperty(getImage(path));        
        
        width = new SimpleDoubleProperty(250);
        height = new SimpleDoubleProperty(250);
        
        nameProperty.set("image");       
        
        location = new SimpleObjectProperty(Point2D.ZERO);        
    }

    @Override
    public void draw() {
        getGraphicsContext().save();
        //apply transform first
        this.shapeToGlobalTransform().transformGraphicsContext(getGraphicsContext());
        getGraphicsContext().setEffect(getEffect());     
        
        //draw shape, this is just local coordinates 
        getGraphicsContext().drawImage(
                image.get(), 
                location.get().getX(), 
                location.get().getY(), 
                width.doubleValue(), 
                height.doubleValue());        
        
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
        }
        
        return dragHandles;               
    }
   
    @Override
    public String toString()
    {
        return "Image";
    }
    
    private Image getImage(Path path)
    {
        Image im = null;
        try {
            im = new Image(new FileInputStream(path.toFile()));
        } catch (FileNotFoundException ex) {            
            Logger.getLogger(MImage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return im;
    }

    @Override
    public boolean containsGlobalPoint(Point2D globalPoint) {
        //transform p to shape space coordinates
        Point2D shapeSpacePoint = globalToShapeTransform(globalPoint);
        //simple check
        Bounds bound = getShapeBound();
        return bound.contains(shapeSpacePoint);
    }
}
