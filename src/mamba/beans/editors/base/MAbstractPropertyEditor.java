/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.beans.editors.base;

import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import mamba.beans.MBeanPropertyItem;

/**
 *
 * @author user
 * @param <T>
 * @param <C>
 */
public abstract class MAbstractPropertyEditor<T, C extends Node> implements MInterfacePropertyEditor<T> {
    private final MBeanPropertyItem property;
    private final C control;    
       
    public MAbstractPropertyEditor(MBeanPropertyItem property, C control) 
    {        
        this.control = control;
        this.property = property;
        this.initEditorValue();
        
        getEditorObservableValue().addListener(
                (o, ov, nv)->{                           
                    setPropertyValue(nv);
                });        
    }
    
    protected abstract ObservableValue<T> getEditorObservableValue();
   
    public final MBeanPropertyItem getMBeanProperty() {
        return property;
    }
     
    @Override 
    public C getEditor() {
        return control;
    }
    
    @Override
    public void setPropertyValue(T value) {
        getMBeanProperty().setValue(value);

    } 
    
    @Override     
    public T getPropertyValue() {
        return (T) getMBeanProperty().getValue();
    }    
}
