/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.beans.light;

import java.util.HashMap;

/**
 *
 * @author user
 */
public abstract class MFeatureDescriptor {
    private boolean expert;
    private boolean hidden;
    private String shortDescription;    
    private String displayName;
    private HashMap<String, Object> table;

    
    public abstract String getName();
    public abstract void setName(String name);
    public String getDisplayName()
    {
        if (displayName == null) {
            return getName();
        }
        return displayName;
    }
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }
    
    public boolean isExpert() 
    {
        return expert;
    }
    
    public void setExpert(boolean expert) 
    {
        this.expert = expert;
    }
    
    public boolean isHidden() 
    {
        return hidden;
    }
    
    public void setHidden(boolean hidden)
    {
        this.hidden = hidden;
    }
    
    public String getShortDescription()    
    {
        if (shortDescription == null) 
        {
            return getDisplayName();
        }
        return shortDescription;
    }
    
    public void setShortDescription(String text)
    {
        shortDescription = text;
    }
    
    public void setValue(String attributeName, Object value) 
    {
        getTable().put(attributeName, value);
    }

    public Object getValue(String attributeName) 
    {
        return (this.table != null)
                ? this.table.get(attributeName)
                : null;
    }
    
    private HashMap<String, Object> getTable() {
        if (this.table == null) {
            this.table = new HashMap<>();
        }
        return this.table;
    }

}
