/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.base.engine.shape.groups;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import mamba.base.MambaShape;
import mamba.base.engine.MEngine;
import mamba.overlayselect.drag.MDrag;
import mamba.base.math.MBound;
import mamba.base.math.MTransformGeneric;

/**
 *
 * @author jmburu
 */
public class MGroup implements MambaShape<MEngine>{
    
    private MEngine engine2D;
    private GraphicsContext graphicContext;
    
    private final StringProperty nameProperty;    
    private final ObservableList<MambaShape<MEngine>> children;
    
    public MGroup()
    {
        nameProperty = new SimpleStringProperty();
        children = FXCollections.observableArrayList();
    }

    @Override
    public MTransformGeneric getTransform() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setTransform(MTransformGeneric transform) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public MEngine getEngine2D() {
        return engine2D;
    }

    @Override
    public void setEngine(MEngine engine2D) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setGraphicContext(GraphicsContext context) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public GraphicsContext getGraphicsContext() {
        return graphicContext;
    }

    @Override
    public void draw() {
        for(MambaShape shape : children)
        {
            shape.draw();
        }
    }

    @Override
    public BoundingBox getBounds() {
        MBound bound = new MBound();
        for(MambaShape shape : children)
        {
            bound.include(shape.getBounds());
        }
        return bound.getBoundingBox();
    }

    @Override
    public boolean contains(Point2D p) {
        
        for(MambaShape shape : children)
        {
            if(shape.contains(p))
                return true;
        }
        return false;
    }

    @Override
    public void updateDragHandles(MDrag referenceHandle) {
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
    public ShapeType getType() {
        return ShapeType.CONTAINER;
    }
    
    @Override
    public ObservableList<MambaShape<MEngine>> getChildren() {
        return children;
    }
    
}
