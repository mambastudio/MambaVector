package mamba.base;

import javafx.collections.ObservableList;

/**
 * Used to mark an object as hierarchical data.This object can then be used as data source for an hierarchical control, like the {@link javafx.scene.control.TreeView}.
 *
 * @author Christian Schudt
 * @param <T>
 */
public interface MambaHierarchyData<T extends MambaHierarchyData> {
    /**
     * The children collection, which represents the recursive nature of the hierarchy.
     * Each child is again a {@link MambaHierarchyData}.
     *
     * @return A list of children.
     */
    ObservableList<T> getChildren();
    
    default ObservableList<T> get(T display)
    {
        return display.getChildren();
    }
    
    
}
