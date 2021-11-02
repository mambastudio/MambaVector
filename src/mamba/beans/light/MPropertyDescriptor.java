/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mamba.beans.light;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 *
 * @author user
 */
public class MPropertyDescriptor extends MFeatureDescriptor {
    private String _propertyName;
    private Method _readMethod;
    private Method _writeMethod;

    MPropertyDescriptor(String propertyName) {
        _propertyName = propertyName;
    }

    @Override
    public String getName() {
        return _propertyName;
    }
    
    @Override
    public void setName(String name) {
        this._propertyName = name;
    }

    public Class<?> getPropertyType() {
        Class<?> result = null;
        if (_readMethod != null) {
            result = _readMethod.getReturnType();
        } else if (_writeMethod != null) {
            Class<?>[] parameterTypes = _writeMethod.getParameterTypes();
            result = parameterTypes[0];
        }
        return result;
    }

    public Method getReadMethod() {
        return _readMethod;
    }

    void setReadMethod(Method readMethod) {
        this._readMethod = readMethod;
    }

    public Method getWriteMethod() {
        return _writeMethod;
    }

    void setWriteMethod(Method writeMethod) {
        this._writeMethod = writeMethod;
    }

    @Override
    public boolean equals(Object object) {
        boolean result = object instanceof MPropertyDescriptor;
        if (result) {
            MPropertyDescriptor pd = (MPropertyDescriptor) object;
            boolean gettersAreEqual = (this._readMethod == null)
                && (pd.getReadMethod() == null) || (this._readMethod != null)
                && (this._readMethod.equals(pd.getReadMethod()));
            boolean settersAreEqual = (this._writeMethod == null)
                && (pd.getWriteMethod() == null) || (this._writeMethod != null)
                && (this._writeMethod.equals(pd.getWriteMethod()));
            result = gettersAreEqual && settersAreEqual;
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_readMethod, _writeMethod);
    }
}
