/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import javafx.geometry.Point2D;
import mamba.base.engine.shape.MPathCubic;
import mamba.base.engine.shape.attributes.MCubicPoint;

/**
 *
 * @author user
 */
public class TestGeneral {
    public static void main(String... args)
    {
        MPathCubic path = new MPathCubic();
        MCubicPoint b1 = new MCubicPoint(Point2D.ZERO);
        MCubicPoint b2 = new MCubicPoint(Point2D.ZERO);
        MCubicPoint b3 = new MCubicPoint(Point2D.ZERO);
        path.addAll(b1, b2);
        System.out.println(path.hasNext(b1));
        System.out.println(path);
    }
    
}
