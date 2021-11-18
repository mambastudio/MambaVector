/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.beans.editors;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import mamba.beans.MBeanPropertyItem;
import mamba.beans.editors.base.MAbstractPropertyEditor;
import mamba.beans.editors.base.MInterfacePropertyEditor;

/**
 *
 * @author user
 */
public class MEditors {
    private MEditors()
    {
        
    }
    
    public static final MInterfacePropertyEditor<?> createNumericEditor(MBeanPropertyItem propertyItem) {
        
        return new MAbstractPropertyEditor<Number, MNumericField>(
                propertyItem, 
                new MNumericField( (Class<? extends Number>) propertyItem.getType())) 
                {                           
                    @Override
                    protected ObservableValue<Number> getEditorObservableValue() {
                        return getEditor().valueProperty();
                    }
                    
                    @Override
                    public void initEditorValue() {                        
                        getEditor().setText(this.getPropertyValue().toString());                        
                    }
                };
    }
    
    public static final MInterfacePropertyEditor<?> createColorEditor(MBeanPropertyItem propertyItem) {
        
        return new MAbstractPropertyEditor<Color, ColorPicker>(
                propertyItem, 
                new ColorPicker()) 
                {                           
                    @Override
                    protected ObservableValue<Color> getEditorObservableValue() {
                        return getEditor().valueProperty();
                    }
                    
                    @Override
                    public void initEditorValue() {                        
                        getEditor().setValue(this.getPropertyValue());                        
                    }
                };
    }
}
