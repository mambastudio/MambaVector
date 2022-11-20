/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.util;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;

/**
 *
 * @author user
 */
public class MDisableStateNodes {
    private final ObservableList<Node> nodes;
    private final BooleanProperty disableProperty;
    
    public MDisableStateNodes()
    {
        nodes = FXCollections.observableArrayList();
        disableProperty = new SimpleBooleanProperty(false);
        disableProperty.addListener((o, ov, nv) ->{
            if(nv)
                for(Node node : nodes)                
                    node.setDisable(true);                
            else
                for(Node node : nodes)
                    node.setDisable(false);
        });
    }
    
    public void addNodes(Node... nodeArray)
    {
        nodes.addAll(nodeArray);
    }
    
    public BooleanProperty getDisableProperty()
    {
        return disableProperty;
    }
    
    public void disable()
    {
        disableProperty.set(true);
    }
    
    public void enable()
    {
        disableProperty.set(false);        
    }
    
    public boolean isEnabled()
    {
        return !disableProperty.get();
    }
}
