// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.ezmorph.bean;

import java.lang.reflect.InvocationTargetException;
import net.sf.ezmorph.object.IdentityObjectMorpher;
import org.apache.commons.beanutils.DynaProperty;
import java.beans.PropertyDescriptor;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.PropertyUtils;
import net.sf.ezmorph.MorphException;
import org.apache.commons.logging.LogFactory;
import net.sf.ezmorph.MorpherRegistry;
import org.apache.commons.logging.Log;
import net.sf.ezmorph.ObjectMorpher;

public final class BeanMorpher implements ObjectMorpher
{
    private static final Log log;
    private final Class beanClass;
    private boolean lenient;
    private final MorpherRegistry morpherRegistry;
    static /* synthetic */ Class class$0;
    static /* synthetic */ Class class$1;
    static /* synthetic */ Class class$2;
    static /* synthetic */ Class class$3;
    static /* synthetic */ Class class$4;
    static /* synthetic */ Class class$5;
    static /* synthetic */ Class class$6;
    static /* synthetic */ Class class$7;
    static /* synthetic */ Class class$8;
    
    static {
        Class class$0;
        if ((class$0 = BeanMorpher.class$0) == null) {
            try {
                class$0 = (BeanMorpher.class$0 = Class.forName("net.sf.ezmorph.bean.BeanMorpher"));
            }
            catch (ClassNotFoundException ex) {
                throw new NoClassDefFoundError(ex.getMessage());
            }
        }
        log = LogFactory.getLog(class$0);
    }
    
    public BeanMorpher(final Class beanClass, final MorpherRegistry morpherRegistry) {
        this(beanClass, morpherRegistry, false);
    }
    
    public BeanMorpher(final Class beanClass, final MorpherRegistry morpherRegistry, final boolean lenient) {
        this.validateClass(beanClass);
        if (morpherRegistry == null) {
            throw new MorphException("morpherRegistry is null");
        }
        this.beanClass = beanClass;
        this.morpherRegistry = morpherRegistry;
        this.lenient = lenient;
    }
    
    public Object morph(final Object sourceBean) {
        if (sourceBean == null) {
            return null;
        }
        if (!this.supports(sourceBean.getClass())) {
            throw new MorphException("unsupported class: " + sourceBean.getClass().getName());
        }
        Object targetBean = null;
        try {
            targetBean = this.beanClass.newInstance();
            final PropertyDescriptor[] targetPds = PropertyUtils.getPropertyDescriptors(this.beanClass);
            for (int i = 0; i < targetPds.length; ++i) {
                final PropertyDescriptor targetPd = targetPds[i];
                final String name = targetPd.getName();
                if (targetPd.getWriteMethod() == null) {
                    BeanMorpher.log.info("Property '" + this.beanClass.getName() + "." + name + "' has no write method. SKIPPED.");
                }
                else {
                    Class sourceType = null;
                    if (sourceBean instanceof DynaBean) {
                        final DynaBean dynaBean = (DynaBean)sourceBean;
                        final DynaProperty dynaProperty = dynaBean.getDynaClass().getDynaProperty(name);
                        if (dynaProperty == null) {
                            BeanMorpher.log.warn("DynaProperty '" + name + "' does not exist. SKIPPED.");
                            continue;
                        }
                        sourceType = dynaProperty.getType();
                    }
                    else {
                        final PropertyDescriptor sourcePd = PropertyUtils.getPropertyDescriptor(sourceBean, name);
                        if (sourcePd == null) {
                            BeanMorpher.log.warn("Property '" + sourceBean.getClass().getName() + "." + name + "' does not exist. SKIPPED.");
                            continue;
                        }
                        if (sourcePd.getReadMethod() == null) {
                            BeanMorpher.log.warn("Property '" + sourceBean.getClass().getName() + "." + name + "' has no read method. SKIPPED.");
                            continue;
                        }
                        sourceType = sourcePd.getPropertyType();
                    }
                    final Class targetType = targetPd.getPropertyType();
                    final Object value = PropertyUtils.getProperty(sourceBean, name);
                    this.setProperty(targetBean, name, sourceType, targetType, value);
                }
            }
        }
        catch (MorphException me) {
            throw me;
        }
        catch (Exception e) {
            throw new MorphException(e);
        }
        return targetBean;
    }
    
    public Class morphsTo() {
        return this.beanClass;
    }
    
    public boolean supports(final Class clazz) {
        return !clazz.isArray();
    }
    
    private void setProperty(final Object targetBean, final String name, final Class sourceType, final Class targetType, Object value) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (targetType.isAssignableFrom(sourceType)) {
            if (value == null && targetType.isPrimitive()) {
                value = this.morpherRegistry.morph(targetType, value);
            }
            PropertyUtils.setProperty(targetBean, name, value);
        }
        else {
            Class class$1;
            if ((class$1 = BeanMorpher.class$1) == null) {
                try {
                    class$1 = (BeanMorpher.class$1 = Class.forName("java.lang.Object"));
                }
                catch (ClassNotFoundException ex) {
                    throw new NoClassDefFoundError(ex.getMessage());
                }
            }
            if (targetType.equals(class$1)) {
                PropertyUtils.setProperty(targetBean, name, value);
            }
            else if (value == null) {
                if (targetType.isPrimitive()) {
                    PropertyUtils.setProperty(targetBean, name, this.morpherRegistry.morph(targetType, value));
                }
            }
            else if (IdentityObjectMorpher.getInstance() == this.morpherRegistry.getMorpherFor(targetType)) {
                if (!this.lenient) {
                    throw new MorphException("Can't find a morpher for target class " + targetType.getName() + " (" + name + ")");
                }
                BeanMorpher.log.info("Can't find a morpher for target class " + targetType.getName() + " (" + name + ") SKIPPED");
            }
            else {
                PropertyUtils.setProperty(targetBean, name, this.morpherRegistry.morph(targetType, value));
            }
        }
    }
    
    private void validateClass(final Class clazz) {
        if (clazz == null) {
            throw new MorphException("target class is null");
        }
        if (clazz.isPrimitive()) {
            throw new MorphException("target class is a primitive");
        }
        if (clazz.isArray()) {
            throw new MorphException("target class is an array");
        }
        if (clazz.isInterface()) {
            throw new MorphException("target class is an interface");
        }
        Class class$2;
        if ((class$2 = BeanMorpher.class$2) == null) {
            try {
                class$2 = (BeanMorpher.class$2 = Class.forName("org.apache.commons.beanutils.DynaBean"));
            }
            catch (ClassNotFoundException ex) {
                throw new NoClassDefFoundError(ex.getMessage());
            }
        }
        if (class$2.isAssignableFrom(clazz)) {
            throw new MorphException("target class is a DynaBean");
        }
        Class class$3;
        if ((class$3 = BeanMorpher.class$3) == null) {
            try {
                class$3 = (BeanMorpher.class$3 = Class.forName("java.lang.Number"));
            }
            catch (ClassNotFoundException ex2) {
                throw new NoClassDefFoundError(ex2.getMessage());
            }
        }
        if (!class$3.isAssignableFrom(clazz)) {
            Class class$4;
            if ((class$4 = BeanMorpher.class$4) == null) {
                try {
                    class$4 = (BeanMorpher.class$4 = Class.forName("java.lang.Boolean"));
                }
                catch (ClassNotFoundException ex3) {
                    throw new NoClassDefFoundError(ex3.getMessage());
                }
            }
            if (!class$4.isAssignableFrom(clazz)) {
                Class class$5;
                if ((class$5 = BeanMorpher.class$5) == null) {
                    try {
                        class$5 = (BeanMorpher.class$5 = Class.forName("java.lang.Character"));
                    }
                    catch (ClassNotFoundException ex4) {
                        throw new NoClassDefFoundError(ex4.getMessage());
                    }
                }
                if (!class$5.isAssignableFrom(clazz)) {
                    Class class$6;
                    if ((class$6 = BeanMorpher.class$6) == null) {
                        try {
                            class$6 = (BeanMorpher.class$6 = Class.forName("java.lang.String"));
                        }
                        catch (ClassNotFoundException ex5) {
                            throw new NoClassDefFoundError(ex5.getMessage());
                        }
                    }
                    if (class$6.isAssignableFrom(clazz)) {
                        throw new MorphException("target class is a String");
                    }
                    Class class$7;
                    if ((class$7 = BeanMorpher.class$7) == null) {
                        try {
                            class$7 = (BeanMorpher.class$7 = Class.forName("java.util.Collection"));
                        }
                        catch (ClassNotFoundException ex6) {
                            throw new NoClassDefFoundError(ex6.getMessage());
                        }
                    }
                    if (class$7.isAssignableFrom(clazz)) {
                        throw new MorphException("target class is a Collection");
                    }
                    Class class$8;
                    if ((class$8 = BeanMorpher.class$8) == null) {
                        try {
                            class$8 = (BeanMorpher.class$8 = Class.forName("java.util.Map"));
                        }
                        catch (ClassNotFoundException ex7) {
                            throw new NoClassDefFoundError(ex7.getMessage());
                        }
                    }
                    if (class$8.isAssignableFrom(clazz)) {
                        throw new MorphException("target class is a Map");
                    }
                    return;
                }
            }
        }
        throw new MorphException("target class is a wrapper");
    }
}
