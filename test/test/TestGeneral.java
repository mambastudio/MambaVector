/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import javafx.geometry.Point2D;
import mamba.base.engine.shape.MPathCubic;
import mamba.base.engine.shape.attributes.bezier.MCubicBezier;

/**
 *
 * @author user
 */
public class TestGeneral {
    public static void main(String... args)
    {
        MPathCubic path = new MPathCubic();
        MCubicBezier b1 = new MCubicBezier(Point2D.ZERO);
        MCubicBezier b2 = new MCubicBezier(Point2D.ZERO);
        MCubicBezier b3 = new MCubicBezier(Point2D.ZERO);
        path.addAll(b1, b2, b3);
        System.out.println(path.hasNext(b1));
        System.out.println(path);
    }
    
}
