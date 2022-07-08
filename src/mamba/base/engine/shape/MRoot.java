/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.base.engine.shape;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Transform;
import mamba.base.MambaShape;
import static mamba.base.MambaShape.ShapeState.DISPLAY;
import mamba.base.engine.MEngine;
import mamba.overlayselect.MDragHandle;
import mamba.util.MIntersection;

/**
 *
 * @author jmburu
 */
public class MRoot implements MambaShape<MEngine>{
    private Transform transform;
    private final ShapeState shapeState = ShapeState.DISPLAY;    
    
    private MEngine engine2D;
    private GraphicsContext graphicContext;
    
    private final StringProperty nameProperty;    
    private final ObservableList<MambaShape<MEngine>> children;
    
    public MRoot()
    {
        this.transform = Transform.translate(0, 0); //identity
        this.nameProperty = new SimpleStringProperty("Root");
        this.children = FXCollections.observableArrayList();
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Point2D getTranslate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setOffset(Point2D offset) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Point2D getOffset() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ShapeType getType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        return graphicContext;
    }

    @Override
    public void draw() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BoundingBox getBounds() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean contains(Point2D p) {
        return true;
    }

    @Override
    public ShapeState getState() {
        return DISPLAY;
    }

    @Override
    public void setState(ShapeState shapeState) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateDragHandles(MDragHandle referenceHandle) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public ObservableList getChildren() {
        return children;
    }
    
    @Override
    public boolean intersect(Point2D p, MIntersection isect)
    {
        for(MambaShape shape : children)
            if(shape.contains(p)) //return first hit
            {
                isect.shape = shape;
                return true;
            }
        return false;
    }
}
