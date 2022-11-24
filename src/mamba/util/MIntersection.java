/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.util;

import javafx.geometry.Point2D;
import mamba.base.MambaShape;

/**
 *
 * @author jmburu
 */
public class MIntersection {
    public Point2D offset;
    public MambaShape shape;
    
    public MIntersection()
    {
        shape = null;
        offset = Point2D.ZERO;
    }
}
