/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.beans;

import java.util.List;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import mamba.beans.editors.base.MInterfacePropertyEditor;

/**
 *
 * @author user
 * @param <P>
 * @param <E>
 */
public class MBeanPropertySheet<P extends MBeanPropertyItem, E extends MInterfacePropertyEditor> extends VBox {
    private Callback<P, E> propertyEditorFactory;
    private static final int MIN_COLUMN_WIDTH = 60;
    
    public MBeanPropertySheet()
    {
       setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
    }
    
    public final void init(ObservableList<P> list)
    {
        PropertyPane propertyPane = new PropertyPane(list);
        getChildren().add(propertyPane);
    }
    
    public final void setFactory(Callback<P, E> callBack)
    {
        this.propertyEditorFactory = callBack;
    }
    
    private final class PropertyPane extends GridPane {
        public PropertyPane( List<P> properties ) 
        {
            this( properties, 0 );
        }
        
        public PropertyPane( List<P> properties, int nestingLevel ) {
            setVgap(5);
            setHgap(5);
            setPadding(new Insets(5, 15, 5, 15 + nestingLevel*10 ));
            getStyleClass().add("property-pane"); //$NON-NLS-1$
            setItems(properties);
            //setGridLinesVisible(true);
        }
        
        public void setItems( List<P> properties ) {
            getChildren().clear();

            int row = 0;
            
            for (P item : properties) {

                // filter properties
                String title = item.getName();
                
                // setup property label
                Label label = new Label(title);
                label.setMinWidth(MIN_COLUMN_WIDTH);
                
                // show description as a tooltip
                String description = item.getDescription();
                if ( description != null && !description.trim().isEmpty()) {
                    label.setTooltip(new Tooltip(description));
                }
                
                add(label, 0, row);

                // setup property editor
                Node editor = getEditor(item);
                
                if(editor != null)
                {
                    if (editor instanceof Region) {
                        ((Region)editor).setMinWidth(MIN_COLUMN_WIDTH);
                        ((Region)editor).setMaxWidth(Double.MAX_VALUE);
                    }
                    label.setLabelFor(editor);               
                    add(editor, 1, row);
                    GridPane.setHgrow(editor, Priority.ALWAYS);

                    //TODO add support for recursive properties                
                   
                }
                 row++;
            }            
        }
    }
    
    private Node getEditor(P item)
    {
        E editor = propertyEditorFactory.call(item);
        if(editor != null)
        {
            return editor.getEditor();
        }
        return null;
    }
}
