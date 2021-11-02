/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.beans.editors.base;

import javafx.scene.Node;

/**
 *
 * @author user
 * @param <T>
 */
public interface MInterfacePropertyEditor<T> {
    /**
     * Returns the editor responsible for editing this property.
     * @return 
     */
    public Node getEditor();

    /**
     * Returns the current value in the editor - this may not be the value of
     * the property itself!
     * @return 
     */
    public T getPropertyValue();

    /**
     * Sets the value to display in the editor - this may not be the value of 
     * the property itself - and the property value will not change!
     * @param value
     */
    public void setPropertyValue(T value);
    
    public void initEditorValue();
}
