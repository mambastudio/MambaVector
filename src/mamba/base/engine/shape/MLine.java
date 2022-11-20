/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.base.engine.shape;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
import javafx.scene.shape.StrokeLineCap;
import static javafx.scene.shape.StrokeLineCap.BUTT;
import javafx.scene.transform.NonInvertibleTransformException;
import mamba.base.MambaShape;
import mamba.base.engine.MEngine;
import mamba.overlayselect.drag.MDrag;
import mamba.overlayselect.drag.MDragSquare;
import mamba.base.math.MBound;
import mamba.base.math.MTransform;
import mamba.base.math.MTransformGeneric;
import mamba.util.MIntersection;

/**
 *
 * @author user
 */
public class MLine implements MambaShape<MEngine>{
  
    private MEngine engine2D;
    private GraphicsContext graphicContext;
    
    private Point2D offset;
    
    //line end points always in local coordinates
    private Point2D p1;   
    private Point2D p2;
    
    //stroke width, color
    private final DoubleProperty strokeWidth;
    private final ObjectProperty<Color> strokeColor;
    private final ObjectProperty<StrokeLineCap> lineCap;
    private final BooleanProperty dashedLine;
    private final DoubleProperty dashSize;
    private final DoubleProperty gapSize;
    
    private MTransformGeneric transform;    
    private Effect effect;
    
    private final ObservableList<MDrag> dragHandles = FXCollections.observableArrayList();
    
    private final ObservableList<MambaShape<MEngine>> children = FXCollections.emptyObservableList();
    
    private final StringProperty nameProperty;
    
    public MLine()
    {
        offset = new Point2D(0, 0);
        
        p1 = new Point2D(10, 10);
        p2 = new Point2D(60, 60);
        
        strokeWidth = new SimpleDoubleProperty(4);
        strokeColor = new SimpleObjectProperty(Color.BLACK);
        
        //to be at positon (p1, p2)
        transform = MTransform.translate(0, 0); 
        
        nameProperty = new SimpleStringProperty("Line");
        dashedLine = new SimpleBooleanProperty(false);
        dashSize = new SimpleDoubleProperty(5);
        gapSize = new SimpleDoubleProperty(5);
        lineCap = new SimpleObjectProperty(BUTT);
    }
    
    public void setStrokeWidth(double strokeWidth)
    {
        this.strokeWidth.set(strokeWidth);
    }
    
    public double getStrokeWidth()
    {
        return this.strokeWidth.get();
    }
    
    public DoubleProperty strokeWidthProperty()
    {
        return this.strokeWidth;
    }
       
    public void setLineCap(StrokeLineCap lineCap)
    {
        this.lineCap.set(lineCap);
    }
    
    public StrokeLineCap getLineCap()
    {
        return this.lineCap.get();
    }
    
    public ObjectProperty<StrokeLineCap> lineCapProperty()
    {
        return this.lineCap;
    }
    
    public void setStrokeColor(Color strokeColor)
    {
        this.strokeColor.set(strokeColor);
    }
    
    public Color getStrokeColor()
    {
        return this.strokeColor.get();
    }
    
    public ObjectProperty<Color> strokeColorProperty()
    {
        return this.strokeColor;
    }
    
    public void setDashSize(double dashSize)
    {
        this.dashSize.set(dashSize);
    }
    
    public double getDashSize()
    {
        return this.dashSize.get();
    }
    
    public DoubleProperty dashSizeProperty()
    {
        return this.dashSize;
    }
    
    public void setGapSize(double gapSize)
    {
        this.gapSize.set(gapSize);
    }
    
    public double getGapSize()
    {
        return this.gapSize.get();
    }
    
    public DoubleProperty gapSizeProperty()
    {
        return this.gapSize;
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
                
        //draw shape, this is just local coordinates         
        graphicContext.setLineCap(lineCap.get());        
        graphicContext.setStroke(strokeColor.get());
        graphicContext.setLineWidth(strokeWidth.doubleValue());
        
        if(dashedLine.get())
            graphicContext.setLineDashes(dashSize.get(), gapSize.get());
        
        graphicContext.strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        
        
        graphicContext.setEffect(null);
        graphicContext.restore(); //reset transforms and any other configurations
    }

    @Override
    public BoundingBox getBounds() {
        MBound bound = new MBound();
        bound.include(p1);
        bound.include(p2);   
        return (BoundingBox) transform.transform(bound.getBoundingBox());
    }

    @Override
    public boolean contains(Point2D p) {       
        //transform p to local coordinates
        Point2D invP = transform.inverseTransform(p);            
        MBound bound = new MBound();

        //expand a bit to maximum 5 on x and y in order to have minimum bound that is not less than 5
        //future plans is to implement oriented bounding box
        bound.include(new Point2D(p1.getX()-5, p1.getY()-5), new Point2D(p1.getX() + 5, p1.getY() + 5));
        bound.include(new Point2D(p2.getX()-5, p2.getY()-5), new Point2D(p2.getX() + 5, p2.getY() + 5));
        bound.include(p1);
        bound.include(p2);       
        return bound.contains(invP);       
    }

    @Override
    public void updateDragHandles(MDrag referenceHandle) {
        
        MDrag c1 = dragHandles.get(0);   
        c1.setX(getGlobalP1().getX());
        c1.setY(getGlobalP1().getY());    
              
        MDrag c2 = dragHandles.get(1);    
        c2.setX(getGlobalP2().getX());
        c2.setY(getGlobalP2().getY());     
    }
    
     @Override
    public ObservableList<MDrag> getDragHandles()
    {
        if(dragHandles.isEmpty())
        {
            MDrag c1 = new MDragSquare(6);       
            c1.setX(getGlobalP1().getX());
            c1.setY(getGlobalP1().getY());           
            dragHandles.add(c1);
            
            c1.setOnMouseDragged(e->{
                Point2D p = new Point2D(e.getX(), e.getY());   //in global coordinates             
                p1 = this.pointInvTransform(p);   //transform to local coordinates
                
                updateDragHandles(null);                
                engine2D.draw();
            });
            
            c1.setOnMouseMoved(e->{
                c1.setCursor(Cursor.HAND);
            });
            
            MDrag c2 = new MDragSquare(6);       
            c2.setX(getGlobalP2().getX());
            c2.setY(getGlobalP2().getY());            
            dragHandles.add(c2);
            
            c2.setOnMouseDragged(e->{
                Point2D p = new Point2D(e.getX(), e.getY());
                p2 = this.pointInvTransform(p); //transform to local coordinates
                updateDragHandles(null);                
                engine2D.draw();
            });
            
            c2.setOnMouseMoved(e->{
                c2.setCursor(Cursor.HAND);
            });
        }
        
        return dragHandles;
    }
    
    @Override
    public boolean intersect(Point2D p, MIntersection isect)
    {        
        boolean boundIntersect = MambaShape.super.intersect(p, isect);
        
        //https://en.wikipedia.org/wiki/Distance_from_a_point_to_a_line#Line_defined_by_two_points
        if(boundIntersect)
        {
            Point2D p0 = pointInvTransform(p); //to local coordinates
            double lengthLine = p1.distance(p2);
            double numerator = (p2.getX()-p1.getX())*(p1.getY()-p0.getY()) - 
                    (p1.getX()-p0.getX())*(p2.getY()-p1.getY());
            numerator = Math.abs(numerator);
            
            double distance = numerator/lengthLine;
            if(distance < 5)
                return true;            
        }
        
        return false;
    }
    
    private Point2D pointTransform(Point2D point)
    {
        return transform.transform(point);
    }
    
    private Point2D pointInvTransform(Point2D point)
    {       
        return transform.inverseTransform(point);       
    }    
    
    private Point2D getGlobalP1()
    {
        return pointTransform(p1);
    }
    
    private Point2D getGlobalP2()
    {
        return pointTransform(p2);
    }    
    
    public void setDashedLine(boolean dashedLine)
    {       
        this.dashedLine.set(dashedLine);    
    }    
    public boolean getDashedLine()
    {
        return this.dashedLine.get();
    }
    
    public BooleanProperty dashedLineProperty()
    {
        return dashedLine;
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
            return "Line";
        else
            return getName();
    }
}
