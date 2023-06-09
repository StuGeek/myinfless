// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.beanutils.converters;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConversionException;
import java.util.Collection;
import java.lang.reflect.Array;
import org.apache.commons.logging.Log;
import org.apache.commons.beanutils.Converter;

public abstract class AbstractConverter implements Converter
{
    private static final String DEFAULT_CONFIG_MSG = "(N.B. Converters can be configured to use default values to avoid throwing exceptions)";
    private static final String PACKAGE = "org.apache.commons.beanutils.converters.";
    private transient Log log;
    private boolean useDefault;
    private Object defaultValue;
    
    public AbstractConverter() {
        this.useDefault = false;
        this.defaultValue = null;
    }
    
    public AbstractConverter(final Object defaultValue) {
        this.useDefault = false;
        this.defaultValue = null;
        this.setDefaultValue(defaultValue);
    }
    
    public boolean isUseDefault() {
        return this.useDefault;
    }
    
    public Object convert(final Class type, Object value) {
        Class sourceType = (value == null) ? null : value.getClass();
        final Class targetType = this.primitive((type == null) ? this.getDefaultType() : type);
        if (this.log().isDebugEnabled()) {
            this.log().debug("Converting" + ((value == null) ? "" : (" '" + this.toString(sourceType) + "'")) + " value '" + value + "' to type '" + this.toString(targetType) + "'");
        }
        value = this.convertArray(value);
        if (value == null) {
            return this.handleMissing(targetType);
        }
        sourceType = value.getClass();
        try {
            if (targetType.equals(String.class)) {
                return this.convertToString(value);
            }
            if (targetType.equals(sourceType)) {
                if (this.log().isDebugEnabled()) {
                    this.log().debug("    No conversion required, value is already a " + this.toString(targetType));
                }
                return value;
            }
            final Object result = this.convertToType(targetType, value);
            if (this.log().isDebugEnabled()) {
                this.log().debug("    Converted to " + this.toString(targetType) + " value '" + result + "'");
            }
            return result;
        }
        catch (Throwable t) {
            return this.handleError(targetType, value, t);
        }
    }
    
    protected String convertToString(final Object value) throws Throwable {
        return value.toString();
    }
    
    protected abstract Object convertToType(final Class p0, final Object p1) throws Throwable;
    
    protected Object convertArray(final Object value) {
        if (value == null) {
            return null;
        }
        if (value.getClass().isArray()) {
            if (Array.getLength(value) > 0) {
                return Array.get(value, 0);
            }
            return null;
        }
        else {
            if (!(value instanceof Collection)) {
                return value;
            }
            final Collection collection = (Collection)value;
            if (collection.size() > 0) {
                return collection.iterator().next();
            }
            return null;
        }
    }
    
    protected Object handleError(final Class type, final Object value, final Throwable cause) {
        if (this.log().isDebugEnabled()) {
            if (cause instanceof ConversionException) {
                this.log().debug("    Conversion threw ConversionException: " + cause.getMessage());
            }
            else {
                this.log().debug("    Conversion threw " + cause);
            }
        }
        if (this.useDefault) {
            return this.handleMissing(type);
        }
        ConversionException cex = null;
        if (cause instanceof ConversionException) {
            cex = (ConversionException)cause;
            if (this.log().isDebugEnabled()) {
                this.log().debug("    Re-throwing ConversionException: " + cex.getMessage());
                this.log().debug("    (N.B. Converters can be configured to use default values to avoid throwing exceptions)");
            }
        }
        else {
            final String msg = "Error converting from '" + this.toString(value.getClass()) + "' to '" + this.toString(type) + "' " + cause.getMessage();
            cex = new ConversionException(msg, cause);
            if (this.log().isDebugEnabled()) {
                this.log().debug("    Throwing ConversionException: " + msg);
                this.log().debug("    (N.B. Converters can be configured to use default values to avoid throwing exceptions)");
            }
            BeanUtils.initCause(cex, cause);
        }
        throw cex;
    }
    
    protected Object handleMissing(final Class type) {
        if (this.useDefault || type.equals(String.class)) {
            Object value = this.getDefault(type);
            if (this.useDefault && value != null && !type.equals(value.getClass())) {
                try {
                    value = this.convertToType(type, this.defaultValue);
                }
                catch (Throwable t) {
                    this.log().error("    Default conversion to " + this.toString(type) + "failed: " + t);
                }
            }
            if (this.log().isDebugEnabled()) {
                this.log().debug("    Using default " + ((value == null) ? "" : (this.toString(value.getClass()) + " ")) + "value '" + this.defaultValue + "'");
            }
            return value;
        }
        final ConversionException cex = new ConversionException("No value specified for '" + this.toString(type) + "'");
        if (this.log().isDebugEnabled()) {
            this.log().debug("    Throwing ConversionException: " + cex.getMessage());
            this.log().debug("    (N.B. Converters can be configured to use default values to avoid throwing exceptions)");
        }
        throw cex;
    }
    
    protected void setDefaultValue(final Object defaultValue) {
        this.useDefault = false;
        if (this.log().isDebugEnabled()) {
            this.log().debug("Setting default value: " + defaultValue);
        }
        if (defaultValue == null) {
            this.defaultValue = null;
        }
        else {
            this.defaultValue = this.convert(this.getDefaultType(), defaultValue);
        }
        this.useDefault = true;
    }
    
    protected abstract Class getDefaultType();
    
    protected Object getDefault(final Class type) {
        if (type.equals(String.class)) {
            return null;
        }
        return this.defaultValue;
    }
    
    public String toString() {
        return this.toString(this.getClass()) + "[UseDefault=" + this.useDefault + "]";
    }
    
    Log log() {
        if (this.log == null) {
            this.log = LogFactory.getLog(this.getClass());
        }
        return this.log;
    }
    
    Class primitive(final Class type) {
        if (type == null || !type.isPrimitive()) {
            return type;
        }
        if (type == Integer.TYPE) {
            return Integer.class;
        }
        if (type == Double.TYPE) {
            return Double.class;
        }
        if (type == Long.TYPE) {
            return Long.class;
        }
        if (type == Boolean.TYPE) {
            return Boolean.class;
        }
        if (type == Float.TYPE) {
            return Float.class;
        }
        if (type == Short.TYPE) {
            return Short.class;
        }
        if (type == Byte.TYPE) {
            return Byte.class;
        }
        if (type == Character.TYPE) {
            return Character.class;
        }
        return type;
    }
    
    String toString(final Class type) {
        String typeName = null;
        if (type == null) {
            typeName = "null";
        }
        else if (type.isArray()) {
            Class elementType;
            int count;
            for (elementType = type.getComponentType(), count = 1; elementType.isArray(); elementType = elementType.getComponentType(), ++count) {}
            typeName = elementType.getName();
            for (int i = 0; i < count; ++i) {
                typeName += "[]";
            }
        }
        else {
            typeName = type.getName();
        }
        if (typeName.startsWith("java.lang.") || typeName.startsWith("java.util.") || typeName.startsWith("java.math.")) {
            typeName = typeName.substring("java.lang.".length());
        }
        else if (typeName.startsWith("org.apache.commons.beanutils.converters.")) {
            typeName = typeName.substring("org.apache.commons.beanutils.converters.".length());
        }
        return typeName;
    }
}
