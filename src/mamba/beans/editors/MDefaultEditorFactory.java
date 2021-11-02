/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.beans.editors;

import mamba.beans.editors.base.MInterfacePropertyEditor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Callback;
import mamba.beans.MBeanPropertyItem;

/**
 *
 * @author user
 */
public class MDefaultEditorFactory implements Callback<MBeanPropertyItem, MInterfacePropertyEditor<?>> {
    
    @Override
    public MInterfacePropertyEditor<?> call(MBeanPropertyItem item) {
        Class<?> type = item.getType();
        
         
        if (isNumber(type))
            return MEditors.createNumericEditor(item);
        
        else if(type == Color.class)
            return MEditors.createColorEditor(item);
      
        
        return null; 
    }
    
    private static final Class<?>[] numericTypes = new Class[]{
        byte.class, Byte.class,
        short.class, Short.class,
        int.class, Integer.class,
        long.class, Long.class,
        float.class, Float.class,
        double.class, Double.class,
        BigInteger.class, BigDecimal.class
    };    
    
    // there should be better ways to do this
    private static boolean isNumber(Class<?> type)  {
        if ( type == null ) return false;
        for (Class<?> cls : numericTypes) {
            if ( type == cls ) return true;
        }
        return false;
    }
}
