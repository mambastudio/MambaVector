/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.beans;

import java.util.function.Function;
import java.util.function.Predicate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mamba.base.MambaEngine2D;
import mamba.beans.light.MBeanInfo;
import mamba.beans.light.MIntrospectionException;
import mamba.beans.light.MIntrospector;
import mamba.beans.light.MPropertyDescriptor;

/**
 *
 * @author user
 */
public class MBeanPropertyUtility {
    
    public static ObservableList<MBeanPropertyItem> getProperties(final Object bean, MambaEngine2D engine)
    {
        return getProperties(bean, (p)->{return p;}, engine);
    }
    
    public static ObservableList<MBeanPropertyItem> getProperties(final Object bean, final Function<String, String> displayNameCall, MambaEngine2D engine)
    {
       // return getProperties(bean, (p)->{return true;});
        ObservableList<MBeanPropertyItem> list = FXCollections.observableArrayList();
        try {
            MBeanInfo beanInfo = MIntrospector.getBeanInfo(bean.getClass(), Object.class);
            for (MPropertyDescriptor p : beanInfo.getPropertyDescriptors()) {     
                //custom display name if any
                String displayName = displayNameCall.apply(p.getDisplayName());
                p.setDisplayName(displayName);
                
                //init bean property
                MBeanProperty property = new MBeanProperty(bean, p, engine);
                if(property.isObservable() && property.isEditable())
                {                            
                    list.add(property);
                }                
            }
         } catch (MIntrospectionException e) {
            System.err.println(e);
        }
        //Collections.reverse(list);
        return list;
    }    
}
