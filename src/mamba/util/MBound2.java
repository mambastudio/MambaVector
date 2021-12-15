/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.util;

import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;

/**
 *
 * @author user
 */
public class MBound2 {
    private Point2D min;
    private Point2D max;
    
    public MBound2()
    {
        min = new Point2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        max = new Point2D(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
    }
    
    public final void include(Point2D p) {
        double x = p.getX();
        double y = p.getY();
        double minX = min.getX();
        double minY = min.getY();
        double maxX = max.getX();
        double maxY = max.getY();
        
        if (x < minX)
            minX = x;
        if (x > maxX)
            maxX = x;
        if (y < minY)
            minY = y;
        if (y > maxY)
            maxY = y;           
        
        this.min = new Point2D(minX, minY);
        this.max = new Point2D(maxX, maxY);
    }
    
    public final void include(Point2D... pArray)
    {
        for (Point2D p : pArray) {
            include(p);
        }
    }
    
    public final void include(BoundingBox boundingBox)
    {
        Point2D p0 = new Point2D(boundingBox.getMinX(), boundingBox.getMinY());
        Point2D p1 = new Point2D(boundingBox.getMinX(), boundingBox.getMinY());
        Point2D p2 = new Point2D(boundingBox.getMaxX(), boundingBox.getMaxY());
        Point2D p3 = new Point2D(boundingBox.getMinX(), boundingBox.getMaxY());
        
        include(p0, p1, p2, p3);
        
    }
    
    public final void include(MBound2 bound)
    {
        include(bound.getMin(), bound.getMax());
    }
    
    public void setBoundingBox(BoundingBox boundingBox)
    {
        this.min = new Point2D(boundingBox.getMinX(), boundingBox.getMinY());
        this.max = new Point2D(boundingBox.getMaxX(), boundingBox.getMaxY());
    }
    
    public BoundingBox getBoundingBox()
    {
        return new BoundingBox(getMin().getX(), getMin().getY(), getWidth(), getHeight());
    }
    
    public final Point2D getPoint(int i)
    {
        if(i == 0)
            return copyMin();
        if(i == 1)
            return new Point2D(max.getX(), min.getY());
        if(i == 2)
            return copyMax();
        if(i == 3)
            return new Point2D(min.getX(), max.getY());
        return null;
    }
    
    public Point2D getMin()
    {
        return min;
    }
    
    public Point2D getMax()
    {
        return max;
    }
    
    public double getWidth()
    {
        return getExtents().getX();
    }
    
    public double getHeight()
    {
        return getExtents().getY();
    }
    
    public Point2D getExtents()
    {
        return max.subtract(min);
    }
    
    public double getMaxExtent()
    {
        Point2D extents = getExtents();
        return Math.max(extents.getX(), extents.getY());
    }
    
    public double getMaxExtentRadius()
    {
        return getMaxExtent()/2;
    }
    
    public final boolean isEmpty()
    {
        return (max.getX() < min.getX()) || (max.getY() < min.getY());
    }
    
    public boolean contains(Point2D p)
    {
        return ((p != null) && 
                (p.getX() >= min.getX()) && 
                (p.getX() <= max.getX()) && 
                (p.getY() >= min.getY()) && 
                (p.getY() <= max.getY()));
    }
    
    public Point2D copyMin()
    {
        return new Point2D(min.getX(), min.getY());
    }
    
    public Point2D copyMax()
    {
        return new Point2D(max.getX(), max.getY());
    }
    
    @Override
    public final String toString() {
        return String.format("(%.2f, %.2f) to (%.2f, %.2f)", min.getX(), min.getY(), max.getX(), max.getY());
    }
}
