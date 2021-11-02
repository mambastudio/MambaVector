/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.beans;

import java.util.function.Predicate;
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
    public static ObservableList<MBeanPropertyItem> getProperties(final Object bean)
    {
        return getProperties(bean, (p)->{return true;});
    }
    public static ObservableList<MBeanPropertyItem> getProperties(final Object bean, Predicate<MPropertyDescriptor> test) {
        ObservableList<MBeanPropertyItem> list = FXCollections.observableArrayList();
        try {
            MBeanInfo beanInfo = MIntrospector.getBeanInfo(bean.getClass(), Object.class);
            for (MPropertyDescriptor p : beanInfo.getPropertyDescriptors()) {
                if (test.test(p)) {
                    MBeanProperty property = new MBeanProperty(bean, p);
                    if(property.isObservable())
                    {                        
                        list.add(property);
                    }
                }
            }
         } catch (MIntrospectionException e) {
            e.printStackTrace();
        }
        //Collections.reverse(list);
        return list;
    }
}
