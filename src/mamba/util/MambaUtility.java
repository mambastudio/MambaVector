/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.util;

import javafx.geometry.Bounds;
import javafx.scene.control.TreeItem;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import mamba.base.MambaEngine2D;
import mamba.base.MambaShape;

/**
 *
 * @author user
 */
public class MambaUtility {
    
    private static long lastClickTime = 0;
   
    public static<E extends MambaEngine2D, T extends MambaShape<E>> TreeItem<T> searchTreeItem(TreeItem<T> parent , T value) 
    {
        if (parent != null && parent.getValue().equals(value))
            return  parent;
        
        for (TreeItem<T> child : parent.getChildren()){
            TreeItem<T> s = searchTreeItem(child, value);
                if(s != null)
                    return s;

        }
        return null;
    }
    
    public static<E extends MambaEngine2D> MambaShape<E> searchMambaShape(MambaShape<E> parent , MambaShape<E> value) 
    {
        if (parent != null && parent.equals(value))
            return  parent;
        
        for (MambaShape<E> child : parent.getChildren()){
            MambaShape<E> s = searchMambaShape(child, value);
            if(s != null)
                return s;
        }
        return null;
    }
    
    public static boolean isDoubleClick(long intervalRangeMsec)
    {
        long currentClickTime = System.currentTimeMillis();
        long diff = 0;       
        if(lastClickTime!=0 && currentClickTime!=0)
            diff = currentClickTime - lastClickTime;        
        lastClickTime = currentClickTime;
        
        return diff < intervalRangeMsec && intervalRangeMsec > 0;
    }    
    
    //https://stackoverflow.com/questions/32237048/javafx-fontmetrics
    public static Bounds reportSize(String s, Font myFont) {
        Text text = new Text(s);
        text.setFont(myFont);
        Bounds tb = text.getBoundsInLocal();
        Rectangle stencil = new Rectangle(
                tb.getMinX(), tb.getMinY(), tb.getWidth(), tb.getHeight()
        );

        Shape intersection = Shape.intersect(text, stencil);

        Bounds ib = intersection.getBoundsInLocal();
        return ib;
    }
}
