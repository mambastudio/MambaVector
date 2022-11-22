/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.base.engine.shape;

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
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import static javafx.scene.shape.StrokeLineCap.BUTT;
import mamba.base.MambaShape;
import mamba.base.engine.MEngine;
import static mamba.base.engine.shape.MPath.PathTo.LINE_TO;
import static mamba.base.engine.shape.MPath.PathToMove.MOVE_TO;
import mamba.base.engine.shape.attributes.MPathBezier;
import mamba.base.engine.shape.attributes.MPathPoint;
import mamba.base.engine.shape.attributes.MPathTypeGeneric;
import mamba.overlayselect.drag.MDrag;
import mamba.base.math.MBound;
import mamba.base.math.MTransform;
import mamba.base.math.MTransformGeneric;

/**
 *
 * @author user
 */
public class MPath extends MPathBezier<MPathPoint> implements MambaShape<MEngine>{

    public static enum PathTo implements MPathTypeGeneric{
        LINE_TO, QUADRATIC_CURVE_TO, BEZIER_CURVE_TO, ARC_TO;
    };
    
    public static enum PathToMove implements MPathTypeGeneric{
        MOVE_TO;
    }
    
    private MEngine engine2D;
    private GraphicsContext graphicContext;
    
    private Point2D offset;
        
    private final ObjectProperty<Color> lineColor;
    private final DoubleProperty lineWidth;
    private final BooleanProperty isClosed;
    private final ObjectProperty<Color> fillColor;
    private final BooleanProperty fillPath;
    private final ObjectProperty<StrokeLineCap> lineCap;
    private final BooleanProperty dashedLine;
    private final DoubleProperty dashSize;
    private final DoubleProperty gapSize;
    
    
    private MTransformGeneric transform;
    
    private Effect effect = null;
    
    private final StringProperty nameProperty;
    
    private final ObservableList<MambaShape<MEngine>> children = FXCollections.emptyObservableList();
    
    private final ObjectProperty<PathTo> pathToProperty;
    
    private final BooleanProperty isBezierEdit;
    
    public MPath()
    {
        //to be at positon (p1, p2)
        transform = MTransform.translate(0, 0); 
        
        add(new MPathPoint(this));
        add(new MPathPoint(this, LINE_TO, new Point2D(150, 150)));
        
        offset = new Point2D(0, 0);        
         
        nameProperty = new SimpleStringProperty();
        pathToProperty = new SimpleObjectProperty(LINE_TO);
        
        lineColor = new SimpleObjectProperty(Color.BLACK);
        lineWidth = new SimpleDoubleProperty(2);
        fillColor = new SimpleObjectProperty(Color.BLACK);
        isClosed = new SimpleBooleanProperty(false);
        fillPath = new SimpleBooleanProperty(false);
        dashedLine = new SimpleBooleanProperty(false);
        dashSize = new SimpleDoubleProperty(5);
        gapSize = new SimpleDoubleProperty(5);
        lineCap = new SimpleObjectProperty(BUTT);
        
        isBezierEdit = new SimpleBooleanProperty(false);
        isBezierEdit.addListener((o, ov, nv)->{
            getEngine2D().getSelectionModel().refreshOverlay(); //refresh overlay
        });
    }
        
    public void addLineTo(double x, double y)
    {
        Point2D point = new Point2D(x, y);        
        add(new MPathPoint(this, LINE_TO, point));
        getEngine2D().draw();
        getEngine2D().getSelectionModel().refreshOverlay(); //refresh overlay
    }
    
    public void addMoveTo(double x, double y)
    {
        Point2D point = new Point2D(x, y);        
        add(new MPathPoint(this, MOVE_TO, point));
        getEngine2D().draw();
        getEngine2D().getSelectionModel().refreshOverlay(); //refresh overlay
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
        return ShapeType.SHAPE_POLY;
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
        
        graphicContext.setLineWidth(lineWidth.get());
        // Set the Color
        graphicContext.setStroke(lineColor.get());  
        
        //draw shape, this is just local coordinates         
        graphicContext.setLineCap(lineCap.get());   
        
        if(dashedLine.get())
            graphicContext.setLineDashes(dashSize.get(), gapSize.get());
        
        graphicContext.setFill(fillColor.get());
        
        graphicContext.beginPath(); 
                        
        for(MPathPoint point : getList())
        {
            boolean isFirst = isFirst(point);
            if(isFirst)
                graphicContext.moveTo(
                        point.getPoint().getX(), 
                        point.getPoint().getY());            
            else if(point.isInBezierRange())
            {
                MPathPoint previousPoint = getPrevious(point);
                graphicContext.bezierCurveTo(
                        previousPoint.getMirrorControl().getX(), 
                        previousPoint.getMirrorControl().getY(), 
                        point.getControl().getX(), 
                        point.getControl().getY(), 
                        point.getPoint().getX(), 
                        point.getPoint().getY());
            }
            else
                graphicContext.lineTo(
                        point.getPoint().getX(), 
                        point.getPoint().getY());       
        }   
        if(getIsClosed())
            graphicContext.closePath();
        
        graphicContext.setEffect(effect);     
        if(getFillPath())
            graphicContext.fill();
        graphicContext.setEffect(null);
        
        graphicContext.stroke();
        
        
        graphicContext.restore(); //reset transforms and any other configurations
    }

    @Override
    public BoundingBox getBounds() {
        MBound bound = new MBound();
        for(MPathPoint point : getList())
        {
            bound.include(point.getPoint());
        }
        return (BoundingBox) transform.transform(bound.getBoundingBox());
    }

    @Override
    public boolean contains(Point2D p) {
       
        //transform p to local coordinates
        Point2D invP = transform.inverseTransform(p);
        MBound bound = new MBound();
        for(MPathPoint point : getList())
        {
            bound.include(point.getPoint());
        }
        return bound.contains(invP);        
    }

    @Override
    public void updateDragHandles(MDrag referenceHandle) {
        for(MPathPoint point: getList())
        {
            point.updateDragHandles();
        }        
    }
    
     @Override
    public ObservableList<MDrag> getDragHandles()
    {
        ObservableList<MDrag> handles = FXCollections.observableArrayList();
        //if(points.isEmpty())
        {
            for(MPathPoint point: getList())
            {
                handles.addAll(point.getDragHandles());
            }
        }
        
        return handles;
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
    
    public PathTo getPathTo()
    {
        return pathToProperty.get();
    }
    
    public void setPathTo(PathTo pathTo)
    {
        this.pathToProperty.set(pathTo);
    }
    
    public ObjectProperty<PathTo> pathToProperty()
    {
        return this.pathToProperty;
    }
    
    public void setLineColor(Color lineColor)
    {       
        this.lineColor.set(lineColor);    
    }    
    public Color getLineColor()
    {
        return this.lineColor.get();
    }
    
    public void setLineWidth(double lineWidth)
    {       
        this.lineWidth.set(lineWidth);    
    }    
    public double getLineWidth()
    {
        return this.lineWidth.get();
    }
    
    public DoubleProperty lineWidthProperty()
    {
        return lineWidth;
    }
    
    public ObjectProperty<Color> lineColorProperty()
    {
        return lineColor;
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
    public void setIsClosed(boolean closePath)
    {       
        this.isClosed.set(closePath);    
    }    
    @Override
    public boolean getIsClosed()
    {
        return this.isClosed.get();
    }
    
    @Override
    public BooleanProperty isClosedProperty()
    {
        return isClosed;
    }
    
    public void setFillPath(boolean fillPath)
    {       
        this.fillPath.set(fillPath);    
    }    
    public boolean getFillPath()
    {
        return this.fillPath.get();
    }
    
    public BooleanProperty fillPathProperty()
    {
        return fillPath;
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
    
    public BooleanProperty getIsBezierEdit()
    {
        return isBezierEdit;
    }
        
    public boolean isBezierEdit()
    {
        return isBezierEdit.get();
    }
    
    @Override
    public boolean isComplete()
    {
        return size() > 1;
    }
    
    @Override
    public boolean isPath()
    {
        return true;
    }

    @Override
    public ObservableList<MambaShape<MEngine>> getChildren() {
        return children;
    }
   
    @Override
    public String toString()
    {
        return "Path";
    }    
}
