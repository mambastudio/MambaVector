/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.beans;

import java.util.Optional;
import javafx.beans.value.ObservableValue;
import mamba.beans.editors.base.MInterfacePropertyEditor;

/**
 *
 * @author user
 */
public interface MBeanPropertyItem {
    public Class<?> getType();
       
    public String getCategory();

    public String getName();

    public String getDescription();        

    public Object getValue();

    public void setValue(Object value);

    public Optional<ObservableValue<? extends Object>> getObservableValue();

    default public Optional<Class<? extends MInterfacePropertyEditor<?>>> getPropertyEditorClass() {
        return Optional.empty();
    }

    default public boolean isEditable() {
        return true;
    }
}
