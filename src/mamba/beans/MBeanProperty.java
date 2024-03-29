/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.beans;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import mamba.beans.light.MPropertyDescriptor;

/**
 *
 * @author user
 */
public final class MBeanProperty implements MBeanPropertyItem{
    public static final String CATEGORY_LABEL_KEY = "propertysheet.item.category.label";
    
    private final Object bean;
    private final MPropertyDescriptor beanPropertyDescriptor; //contains read and write method
    private boolean editable = true;
    private Optional<ObservableValue<? extends Object>> observableValue = Optional.empty();
    private final MConsumerVoid consume;
    
    public MBeanProperty(final Object bean, final MPropertyDescriptor propertyDescriptor, MConsumerVoid consume)
    {
        this.bean = bean;
        this.beanPropertyDescriptor = propertyDescriptor;
        this.consume = consume;
        if (this.beanPropertyDescriptor.getWriteMethod() == null) {
            this.setEditable(false);            
        }

        this.findObservableValue();
    }
    
    @Override
    public void setValue(final Object value) {
        final Method writeMethod = this.beanPropertyDescriptor.getWriteMethod();
        if ( writeMethod != null ) {
            try {
                writeMethod.invoke(this.bean, value);
                if(consume != null)
                    consume.call();
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {                
                Logger.getLogger(MBeanProperty.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
    
    @Override
    public String getName() {
        return this.beanPropertyDescriptor.getDisplayName();
    }

    @Override
    public String getDescription() {
        return this.beanPropertyDescriptor.getShortDescription();
    }

    @Override
    public Class<?> getType() {
        return this.beanPropertyDescriptor.getPropertyType();
    }

    @Override
    public Object getValue() {
        try {
            Object obj = this.beanPropertyDescriptor.getReadMethod().invoke(this.bean);
            
            
            return obj;
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NullPointerException e) {
            System.err.println(e);
             
            System.out.println(this.bean);
            System.out.println(beanPropertyDescriptor.getReadMethod()+ "<- readmethod; getname-> " +getName());
            return null;
        }
    }
    
    public MPropertyDescriptor getPropertyDescriptor() {
        return this.beanPropertyDescriptor;
    }
    
    public Object getBean() {
        return this.bean;
    }
    
    @Override
    public boolean isEditable() {
        return this.editable;
    }
    
    public boolean isReadAndWriteAndObservable()
    {
        return (!Objects.isNull(beanPropertyDescriptor.getReadMethod()) &&
                !Objects.isNull(beanPropertyDescriptor.getWriteMethod()) && isObservable());
    }
    
    public boolean isObservable()
    {
        return observableValue.isPresent();
    }
    
    public void setEditable(final boolean editable) {
        this.editable = editable;
    }
    
    @Override
    public Optional<ObservableValue<? extends Object>> getObservableValue() {
        return this.observableValue;
    }
    
    private void findObservableValue() {
        try {
            final String propName = this.beanPropertyDescriptor.getName() + "Property";
            final Method m = this.getBean().getClass().getMethod(propName);
            final Object val = m.invoke(this.getBean());
            if ((val != null) && (val instanceof ObservableValue)) {
                this.observableValue = Optional.of((ObservableValue<?>) val);
            }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            //Logger.getLogger(BeanProperty.class.getName()).log(Level.SEVERE, null, ex);
            // ignore it...
        }
    }

    @Override
    public String getCategory() {
        String category = (String) this.beanPropertyDescriptor.getValue(MBeanProperty.CATEGORY_LABEL_KEY);

        /* ControlsFX
        // fall back to default behavior if there is no category provided.
        if (category == null) {
            category = Localization.localize(Localization.asKey(this.beanPropertyDescriptor.isExpert()
                    ? "bean.property.category.expert" : "bean.property.category.basic")); //$NON-NLS-1$ //$NON-NLS-2$
        }
        */
        return category;
    }
}
