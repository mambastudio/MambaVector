/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.beans;

import java.util.function.Function;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mamba.beans.light.MBeanInfo;
import mamba.beans.light.MIntrospectionException;
import mamba.beans.light.MIntrospector;
import mamba.beans.light.MPropertyDescriptor;

/**
 *
 * @author user
 */
public class MBeanPropertyUtility {
    
    public static ObservableList<MBeanPropertyItem> getProperties(final Object bean, MConsumerVoid consume)
    {
        return getProperties(
                bean, 
                //https://stackoverflow.com/questions/3752636/java-split-string-when-an-uppercase-letter-is-found
                //https://attacomsian.com/blog/capitalize-first-letter-of-string-java#:~:text=The%20simplest%20way%20to%20capitalize,substring(0%2C%201).
                (p)->{
                    String[] r = p.split("(?=\\p{Upper})");
                    r[0] = r[0].substring(0, 1).toUpperCase() + r[0].substring(1);
                    StringBuilder string = new StringBuilder();
                    for(String str: r)
                    {
                        string.append(str).append(" ");
                    }
                    return string.toString();
                }, 
                consume);
    }
    
    public static ObservableList<MBeanPropertyItem> getProperties(final Object bean, final Function<String, String> displayNameCall, MConsumerVoid consume)
    {
       // return getProperties(bean, (p)->{return true;});
        ObservableList<MBeanPropertyItem> list = FXCollections.observableArrayList();
        try {
            MBeanInfo beanInfo = MIntrospector.getBeanInfo(bean.getClass(), Object.class);
            for (MPropertyDescriptor p : beanInfo.getPropertyDescriptors()) {     
                //custom display name if any
                String displayName = displayNameCall.apply(p.getName());
                p.setDisplayName(displayName);
                
                //init bean property
                MBeanProperty property = new MBeanProperty(bean, p, consume);
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
