/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.base.engine.shape.attributes;

import javafx.geometry.Point2D;
import mamba.base.engine.shape.MPoly;

/**
 *
 * @author user
 */
public class MPathPoint {
    MPoly.PathTo pathTo;
    Point2D point;
    Point2D control1;
    Point2D control2;
    
    public MPathPoint()
    {
        
    }
    
    public void setPoint(Point2D point)
    {
        this.point = point;
    }
    
    public void setControl1(Point2D control1)
    {
        this.control1 = control1;
    }
    
    public void setControl2(Point2D control2)
    {
        this.control2 = control2;
    }
}
