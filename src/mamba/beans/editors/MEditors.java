/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.beans.editors;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.effect.Blend;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.MotionBlur;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
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
    
    public static final MInterfacePropertyEditor<?> createEnumEditor(MBeanPropertyItem propertyItem) {
        Enum enumValue = (Enum) propertyItem.getValue();
        ObservableList<Enum> enumValueList = FXCollections.observableArrayList(enumValue.getClass().getEnumConstants());        
        
        return new MAbstractPropertyEditor<Enum, ComboBox>(
                propertyItem, 
                new ComboBox(enumValueList)) 
                {                           
                    @Override
                    protected ObservableValue<Enum> getEditorObservableValue() {
                        return getEditor().valueProperty();
                    }
                    
                    @Override
                    public void initEditorValue() {                        
                        getEditor().setValue(this.getPropertyValue());                        
                    }
                };
    }
    
    public static final MInterfacePropertyEditor<?> createEffectEditor(MBeanPropertyItem propertyItem) {
        ObservableList<Effect> effectValueList = FXCollections.observableArrayList();        
        effectValueList.add(new DropShadow());
        effectValueList.add(new BoxBlur());
        effectValueList.add(new Reflection());
        effectValueList.add(new Blend());
        effectValueList.add(new Bloom());
        effectValueList.add(new MotionBlur());
        
        
        
        ComboBox<Effect> comboBox = new ComboBox(effectValueList);
        comboBox.setMaxWidth(Double.MAX_VALUE);
        comboBox.setConverter(new StringConverter<Effect>(){
            @Override
            public String toString(Effect t) {
                if(t != null)
                    return t.getClass().getSimpleName();
                else
                    return "Null";
            }

            @Override
            public Effect fromString(String string) {
                return null;
            }
            
        });
        
        Button remove = new Button("R");
        remove.setOnAction(e->comboBox.setValue(null));
        
        HBox hbox = new HBox(comboBox, remove);
        hbox.setSpacing(5);
        HBox.setHgrow(comboBox, Priority.ALWAYS);
                
        return new MAbstractPropertyEditor<Effect, HBox>(
                propertyItem, 
                hbox) 
                {                           
                    @Override
                    protected ObservableValue<Effect> getEditorObservableValue() {
                        return comboBox.valueProperty();
                    }
                    
                    @Override
                    public void initEditorValue() {                        
                        comboBox.setValue(this.getPropertyValue());                        
                    }
                };
    }
}
