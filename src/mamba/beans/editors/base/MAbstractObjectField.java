/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.beans.editors.base;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 *
 * @author user
 * @param <T>
 */
public abstract class MAbstractObjectField<T> extends HBox {
    private final TextField textField = new TextField();

    private ObjectProperty<T> objectProperty = new SimpleObjectProperty<>();

    public MAbstractObjectField() {
        super(1);
        textField.setEditable(false);
        textField.setFocusTraversable(false);

        /*
        StackPane button = new StackPane(new ImageView(image));
        button.setCursor(Cursor.DEFAULT);

        button.setOnMouseReleased(e -> {
            if ( MouseButton.PRIMARY == e.getButton() ) {
                final T result = edit(objectProperty.get());
                if (result != null) {
                    objectProperty.set(result);
                }
            }
        });

        //textField.setRight(button);
        
*/
        getChildren().add(textField);
        HBox.setHgrow(textField, Priority.ALWAYS);
        
        objectProperty.addListener((o, oldValue, newValue) -> textProperty().set(objectToString(newValue)));
    }

    protected StringProperty textProperty() {
        return textField.textProperty();
    }

    public ObjectProperty<T> getObjectProperty() {
        return objectProperty;
    }

    protected String objectToString(T object) {
        return object == null ? "" : object.toString(); //$NON-NLS-1$
    }

    protected abstract Class<T> getType();

    protected abstract T edit(T object);
}
