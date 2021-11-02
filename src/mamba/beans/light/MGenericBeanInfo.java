/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.beans.light;

/**
 *
 * @author user
 */
public class MGenericBeanInfo implements MBeanInfo{
    protected final MBeanDescriptor _bean;
    protected final MPropertyDescriptor[] _properties;

    MGenericBeanInfo(MBeanDescriptor bean, MPropertyDescriptor[] properties) {
        _bean = bean;
        _properties = properties;
    }

    @Override
    public MBeanDescriptor getBeanDescriptor() {
        return _bean;
    }

    @Override
    public MPropertyDescriptor[] getPropertyDescriptors() {
        return _properties;
    }
}
