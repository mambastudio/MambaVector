/*
 * The MIT License
 *
 * Copyright 2022 user.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package mamba.base.engine.shape;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import static javafx.scene.shape.StrokeLineCap.BUTT;
import mamba.base.engine.shape.attributes.MArcData;
import mamba.base.engine.shape.attributes.bezier.MCubicBezier;
import mamba.base.engine.shape.attributes.MSpline;
import mamba.overlayselect.drag.MDrag;
import mamba.util.MSplineUtility;
import mamba.util.MIntersection;

/**
 *
 * @author user
 */
public class MPath2 extends MSpline<MCubicBezier>{
    
    private final ObjectProperty<Color> lineColor;
    private final DoubleProperty lineWidth;
    private final BooleanProperty isClosed;
    private final ObjectProperty<Color> fillColor;
    private final BooleanProperty fillPath;
    private final ObjectProperty<StrokeLineCap> lineCap;
    private final BooleanProperty dashedLine;
    private final DoubleProperty dashSize;
    private final DoubleProperty gapSize;
    
    public MPath2()
    {
        lineColor = new SimpleObjectProperty(Color.BLACK);
        lineWidth = new SimpleDoubleProperty(2);
        fillColor = new SimpleObjectProperty(Color.BLACK);
        isClosed = new SimpleBooleanProperty(false);
        fillPath = new SimpleBooleanProperty(false);
        dashedLine = new SimpleBooleanProperty(false);
        dashSize = new SimpleDoubleProperty(5);
        gapSize = new SimpleDoubleProperty(5);
        lineCap = new SimpleObjectProperty(BUTT);
    }
    
    public void addCurve(MCubicBezier bezier)
    {
        this.add(bezier);
    }
    
    public void addArc(Point2D previousPoint, MArcData arcData)
    {
        this.addAll(MSplineUtility.convertArcToCubic(previousPoint, arcData));
    }

    @Override
    public void draw() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean intersect(Point2D parentPoint, MIntersection isect) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean intersect(Bounds parentBound, MIntersection isect) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Bounds getShapeBound() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isComplete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ObservableList<MDrag> initDragHandles() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateDragHandles() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean containsGlobalPoint(Point2D globalPoint) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
