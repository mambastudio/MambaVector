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
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
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
public class MCircle2 implements MambaShape<MEngine>{
    
    private ShapeState shapeState = ShapeState.DISPLAY;
    
    private MEngine engine2D;
    private GraphicsContext graphicContext;
    
    private Point2D offset;
    
    private final DoubleProperty radius;
    private final ObjectProperty<Color> solidColor;
    
    private Transform transform;
    
    public MCircle2()
    {
        offset = new Point2D(0, 0);
        
        radius = new SimpleDoubleProperty(45);
        solidColor = new SimpleObjectProperty(Color.YELLOW);
        
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
        graphicContext.fillOval(
                -radius.doubleValue(), -radius.doubleValue(), 
                radius.doubleValue() * 2, radius.doubleValue() * 2);
        
        graphicContext.restore();
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
            Logger.getLogger(MCircle2.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
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
    public void updateDragHandles(MDragHandle referenceHandle) {
        //TODO
    }
    
}
