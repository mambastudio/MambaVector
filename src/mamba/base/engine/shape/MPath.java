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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import static javafx.scene.shape.StrokeLineCap.BUTT;
import static mamba.base.engine.shape.MPath.PathTo.LINE_TO;
import static mamba.base.engine.shape.MPath.PathToMove.MOVE_TO;
import mamba.base.engine.shape.attributes.MPathSpline;
import mamba.base.engine.shape.attributes.MPathPoint;
import mamba.base.engine.shape.attributes.MPathTypeGeneric;
import mamba.overlayselect.drag.MDrag;
import mamba.base.math.MBound;
import mamba.util.MIntersection;

/**
 *
 * @author user
 */
public class MPath extends MPathSpline<MPathPoint>{
    
    public static enum PathTo implements MPathTypeGeneric{
        LINE_TO, QUADRATIC_CURVE_TO, BEZIER_CURVE_TO, ARC_TO;
    };
    
    public static enum PathToMove implements MPathTypeGeneric{
        MOVE_TO;
    }
    
    private final ObjectProperty<Color> lineColor;
    private final DoubleProperty lineWidth;
    private final BooleanProperty isClosed;
    private final ObjectProperty<Color> fillColor;
    private final BooleanProperty fillPath;
    private final ObjectProperty<StrokeLineCap> lineCap;
    private final BooleanProperty dashedLine;
    private final DoubleProperty dashSize;
    private final DoubleProperty gapSize;
    
    
    private final ObjectProperty<PathTo> pathToProperty;
    
    private final BooleanProperty isBezierEdit;
    
    public MPath()
    {        
        add(new MPathPoint(this));
        add(new MPathPoint(this, LINE_TO, new Point2D(150, 150)));
              
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
            getEngine2D().getSelectionModel().refreshDragHandlesAndDraw(); //refresh overlay
        });
    }
        
    public void addLineTo(double x, double y)
    {
        Point2D point = new Point2D(x, y);        
        add(new MPathPoint(this, LINE_TO, point));
        getEngine2D().getSelectionModel().refreshDragHandlesAndDraw(); //refresh overlay        
    }
    
    public void addMoveTo(double x, double y)
    {
        Point2D point = new Point2D(x, y);        
        add(new MPathPoint(this, MOVE_TO, point));        
        getEngine2D().getSelectionModel().refreshDragHandlesAndDraw(); //refresh overlay
    }
    
    public void removePoint(MPathPoint point)
    {
        remove(point);
        getEngine2D().getSelectionModel().refreshDragHandlesAndDraw(); //refresh overlay        
    }
    
   
    @Override
    public void draw() {
        getGraphicsContext().save();
        //apply transform first
        this.shapeToGlobalTransform().transformGraphicsContext(getGraphicsContext());
        
        getGraphicsContext().setLineWidth(lineWidth.get());
        // Set the Color
        getGraphicsContext().setStroke(lineColor.get());  
        
        //draw shape, this is just local coordinates         
        getGraphicsContext().setLineCap(lineCap.get());   
        
        if(dashedLine.get())
            getGraphicsContext().setLineDashes(dashSize.get(), gapSize.get());
        
        getGraphicsContext().setFill(fillColor.get());
        
        getGraphicsContext().beginPath(); 
                        
        for(MPathPoint point : getList())
        {
            boolean isFirst = isFirst(point);
            if(isFirst)
                getGraphicsContext().moveTo(
                        point.getShapePoint().getX(), 
                        point.getShapePoint().getY());            
            else
            {
                MPathPoint previousPoint = getPrevious(point);
                getGraphicsContext().bezierCurveTo(
                        previousPoint.getMirrorShapeControl().getX(), 
                        previousPoint.getMirrorShapeControl().getY(), 
                        point.getShapeControl().getX(), 
                        point.getShapeControl().getY(), 
                        point.getShapePoint().getX(), 
                        point.getShapePoint().getY());
            }           
        }   
        if(getIsClosed())
            getGraphicsContext().closePath();
        
        getGraphicsContext().setEffect(getEffect());     
        if(getFillPath())
            getGraphicsContext().fill();
        getGraphicsContext().setEffect(null);        
        getGraphicsContext().stroke();
        
        
        getGraphicsContext().restore(); //reset transforms and any other configurations
    }

    @Override
    public BoundingBox getShapeBound() {
        MBound shapeBound = new MBound();
        for(MPathPoint point : getList())
        {
            shapeBound.include(point.getShapePoint());
        }
        return shapeBound.getBoundingBox();
    }

    @Override
    public boolean intersect(Point2D parentPoint, MIntersection isect) {
       
        //transform p to local coordinates
        Point2D invP = this.localToShapeTransform(parentPoint);
        Bounds bound = getShapeBound();
        if(bound.contains(invP))
        {
            isect.shape = this;
            return true;
        }
        else 
            return false;
    }
    
    @Override
    public boolean intersect(Bounds parentBound, MIntersection isect) {
        //transform p to local coordinates
        Bounds invBound = this.localToShapeTransform(parentBound);
        Bounds bound = getShapeBound();        
        if(bound.contains(invBound))
        {
            isect.shape = this;
            return true;
        }
        else 
            return false;
    }

    @Override
    public void updateDragHandles() {
        for(MPathPoint point: getList())
        {
            point.updateDragHandles();
        }        
    }
    
    @Override
    public ObservableList<MDrag> initDragHandles()
    {
        ObservableList<MDrag> handles = FXCollections.observableArrayList();
        for(MPathPoint point: getList())
        {
            handles.addAll(point.initDragHandles());
        }
        return handles;
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
    public String toString()
    {
        return "Path";
    }    
    
    

    @Override
    public boolean containsGlobalPoint(Point2D globalPoint) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
