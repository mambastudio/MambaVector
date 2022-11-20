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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import mamba.base.MambaShape;
import mamba.base.engine.MEngine;
import mamba.overlayselect.drag.MDrag;
import mamba.overlayselect.drag.MDragSquare;
import mamba.base.math.MBound;
import mamba.base.math.MTransform;
import mamba.base.math.MTransformGeneric;

/**
 *
 * @author user
 */
public class MImage implements MambaShape<MEngine>{
    
    private MEngine engine2D;
    private GraphicsContext graphicContext;
    
    private Point2D offset;
    
    private final DoubleProperty width;
    private final DoubleProperty height;
    
    private final ObjectProperty<Image> image;
    
    private MTransformGeneric transform;
    
    private Effect effect = null;
    
    ObservableList<MDrag> dragHandles = FXCollections.observableArrayList();
    
    private final ObservableList<MambaShape<MEngine>> children = FXCollections.emptyObservableList();
    
    private final StringProperty nameProperty;
    
    public MImage(Path path)
    {
        offset = new Point2D(0, 0);
                
        image = new SimpleObjectProperty(getImage(path));        
        
        width = new SimpleDoubleProperty(250);
        height = new SimpleDoubleProperty(250);
        
        //to be at positon (0, 0), Transform.translate(width.get()/2, height.get()/2), since origin is at the middle
        transform = MTransform.translate(width.get()/2, height.get()/2); //
        
        nameProperty = new SimpleStringProperty();
    }

    @Override
    public MTransformGeneric getTransform() {
        return transform;
    }

    @Override
    public void setTransform(MTransformGeneric transform) {
        this.transform = transform;
    }

    @Override
    public void translate(Point2D p) {
        Point2D tp = p.subtract(offset);
        this.transform = MTransform.translate(tp.getX(), tp.getY());
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
        transform.transformGraphicsContext(graphicContext);        
        graphicContext.setEffect(effect);     
        
        //draw shape, this is just local coordinates 
        graphicContext.drawImage(
                image.get(), 
                -width.doubleValue()/2, 
                -height.doubleValue()/2, 
                width.doubleValue(), 
                height.doubleValue());        
        
        graphicContext.setEffect(null);
        graphicContext.restore(); //reset transforms and any other configurations
    }

    @Override
    public BoundingBox getBounds() {
        Point2D min = new Point2D(-width.doubleValue()/2, -height.doubleValue()/2);
        Point2D max = new Point2D(width.doubleValue()/2, height.doubleValue()/2);
        MBound bound = new MBound();
        bound.include(min);
        bound.include(max);       
        return (BoundingBox) transform.transform(bound.getBoundingBox());
    }

    @Override
    public boolean contains(Point2D p) {       
        //transform p to local coordinates
        Point2D invP = transform.inverseTransform(p);
        Point2D min = new Point2D(-width.doubleValue()/2, -height.doubleValue()/2);
        Point2D max = new Point2D(width.doubleValue()/2, height.doubleValue()/2);
        MBound bound = new MBound();
        bound.include(min);
        bound.include(max);       
        return bound.contains(invP);       
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

                MBound nbound = new MBound();
                MBound cbound = new MBound();

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

                MBound nbound = new MBound();
                MBound cbound = new MBound();

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

                MBound nbound = new MBound();
                MBound cbound = new MBound();

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

                MBound nbound = new MBound();
                MBound cbound = new MBound();

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

    @Override
    public StringProperty getNameProperty() {
        return nameProperty;
    }

    @Override
    public String getName() {
        return nameProperty.get();
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
}
