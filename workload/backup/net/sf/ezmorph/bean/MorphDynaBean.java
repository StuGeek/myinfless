// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.ezmorph.bean;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import net.sf.ezmorph.MorphUtils;
import java.util.ArrayList;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.beanutils.DynaClass;
import java.util.List;
import java.lang.reflect.Array;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.beanutils.DynaProperty;
import net.sf.ezmorph.MorphException;
import java.util.HashMap;
import net.sf.ezmorph.MorpherRegistry;
import java.util.Map;
import java.io.Serializable;
import org.apache.commons.beanutils.DynaBean;

public final class MorphDynaBean implements DynaBean, Serializable
{
    private static final long serialVersionUID = -605547389232706344L;
    private MorphDynaClass dynaClass;
    private Map dynaValues;
    private MorpherRegistry morpherRegistry;
    static /* synthetic */ Class class$0;
    static /* synthetic */ Class class$1;
    static /* synthetic */ Class class$2;
    static /* synthetic */ Class class$3;
    static /* synthetic */ Class class$4;
    static /* synthetic */ Class class$5;
    static /* synthetic */ Class class$6;
    static /* synthetic */ Class class$7;
    static /* synthetic */ Class class$8;
    static /* synthetic */ Class class$9;
    
    public MorphDynaBean() {
        this(null);
    }
    
    public MorphDynaBean(final MorpherRegistry morpherRegistry) {
        this.dynaValues = new HashMap();
        this.setMorpherRegistry(morpherRegistry);
    }
    
    public boolean contains(final String name, final String key) {
        final DynaProperty dynaProperty = this.getDynaProperty(name);
        final Class type = dynaProperty.getType();
        Class class$0;
        if ((class$0 = MorphDynaBean.class$0) == null) {
            try {
                class$0 = (MorphDynaBean.class$0 = Class.forName("java.util.Map"));
            }
            catch (ClassNotFoundException ex) {
                throw new NoClassDefFoundError(ex.getMessage());
            }
        }
        if (!class$0.isAssignableFrom(type)) {
            throw new MorphException("Non-Mapped property name: " + name + " key: " + key);
        }
        Object value = this.dynaValues.get(name);
        if (value == null) {
            value = new HashMap();
            this.dynaValues.put(name, value);
        }
        return ((Map)value).containsKey(key);
    }
    
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MorphDynaBean)) {
            return false;
        }
        final MorphDynaBean other = (MorphDynaBean)obj;
        final EqualsBuilder builder = new EqualsBuilder().append(this.dynaClass, other.dynaClass);
        final DynaProperty[] props = this.dynaClass.getDynaProperties();
        for (int i = 0; i < props.length; ++i) {
            final DynaProperty prop = props[i];
            builder.append(this.dynaValues.get(prop.getName()), this.dynaValues.get(prop.getName()));
        }
        return builder.isEquals();
    }
    
    public Object get(final String name) {
        final Object value = this.dynaValues.get(name);
        if (value != null) {
            return value;
        }
        final Class type = this.getDynaProperty(name).getType();
        if (!type.isPrimitive()) {
            return value;
        }
        return this.morpherRegistry.morph(type, value);
    }
    
    public Object get(final String name, final int index) {
        final DynaProperty dynaProperty = this.getDynaProperty(name);
        final Class type = dynaProperty.getType();
        if (!type.isArray()) {
            Class class$1;
            if ((class$1 = MorphDynaBean.class$1) == null) {
                try {
                    class$1 = (MorphDynaBean.class$1 = Class.forName("java.util.List"));
                }
                catch (ClassNotFoundException ex) {
                    throw new NoClassDefFoundError(ex.getMessage());
                }
            }
            if (!class$1.isAssignableFrom(type)) {
                throw new MorphException("Non-Indexed property name: " + name + " index: " + index);
            }
        }
        Object value = this.dynaValues.get(name);
        if (value.getClass().isArray()) {
            value = Array.get(value, index);
        }
        else if (value instanceof List) {
            value = ((List)value).get(index);
        }
        return value;
    }
    
    public Object get(final String name, final String key) {
        final DynaProperty dynaProperty = this.getDynaProperty(name);
        final Class type = dynaProperty.getType();
        Class class$0;
        if ((class$0 = MorphDynaBean.class$0) == null) {
            try {
                class$0 = (MorphDynaBean.class$0 = Class.forName("java.util.Map"));
            }
            catch (ClassNotFoundException ex) {
                throw new NoClassDefFoundError(ex.getMessage());
            }
        }
        if (!class$0.isAssignableFrom(type)) {
            throw new MorphException("Non-Mapped property name: " + name + " key: " + key);
        }
        Object value = this.dynaValues.get(name);
        if (value == null) {
            value = new HashMap();
            this.dynaValues.put(name, value);
        }
        return ((Map)value).get(key);
    }
    
    public DynaClass getDynaClass() {
        return this.dynaClass;
    }
    
    public MorpherRegistry getMorpherRegistry() {
        return this.morpherRegistry;
    }
    
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder().append(this.dynaClass);
        final DynaProperty[] props = this.dynaClass.getDynaProperties();
        for (int i = 0; i < props.length; ++i) {
            final DynaProperty prop = props[i];
            builder.append(this.dynaValues.get(prop.getName()));
        }
        return builder.toHashCode();
    }
    
    public void remove(final String name, final String key) {
        final DynaProperty dynaProperty = this.getDynaProperty(name);
        final Class type = dynaProperty.getType();
        Class class$0;
        if ((class$0 = MorphDynaBean.class$0) == null) {
            try {
                class$0 = (MorphDynaBean.class$0 = Class.forName("java.util.Map"));
            }
            catch (ClassNotFoundException ex) {
                throw new NoClassDefFoundError(ex.getMessage());
            }
        }
        if (!class$0.isAssignableFrom(type)) {
            throw new MorphException("Non-Mapped property name: " + name + " key: " + key);
        }
        Object value = this.dynaValues.get(name);
        if (value == null) {
            value = new HashMap();
            this.dynaValues.put(name, value);
        }
        ((Map)value).remove(key);
    }
    
    public void set(final String name, final int index, final Object value) {
        final DynaProperty dynaProperty = this.getDynaProperty(name);
        final Class type = dynaProperty.getType();
        if (!type.isArray()) {
            Class class$1;
            if ((class$1 = MorphDynaBean.class$1) == null) {
                try {
                    class$1 = (MorphDynaBean.class$1 = Class.forName("java.util.List"));
                }
                catch (ClassNotFoundException ex) {
                    throw new NoClassDefFoundError(ex.getMessage());
                }
            }
            if (!class$1.isAssignableFrom(type)) {
                throw new MorphException("Non-Indexed property name: " + name + " index: " + index);
            }
        }
        Object prop = this.dynaValues.get(name);
        if (prop == null) {
            Class class$2;
            if ((class$2 = MorphDynaBean.class$1) == null) {
                try {
                    class$2 = (MorphDynaBean.class$1 = Class.forName("java.util.List"));
                }
                catch (ClassNotFoundException ex2) {
                    throw new NoClassDefFoundError(ex2.getMessage());
                }
            }
            if (class$2.isAssignableFrom(type)) {
                prop = new ArrayList();
            }
            else {
                prop = Array.newInstance(type.getComponentType(), index + 1);
            }
            this.dynaValues.put(name, prop);
        }
        if (prop.getClass().isArray()) {
            if (index >= Array.getLength(prop)) {
                final Object tmp = Array.newInstance(type.getComponentType(), index + 1);
                System.arraycopy(prop, 0, tmp, 0, Array.getLength(prop));
                prop = tmp;
                this.dynaValues.put(name, tmp);
            }
            Array.set(prop, index, value);
        }
        else if (prop instanceof List) {
            final List l = (List)prop;
            if (index >= l.size()) {
                for (int i = l.size(); i <= index + 1; ++i) {
                    l.add(null);
                }
            }
            ((List)prop).set(index, value);
        }
    }
    
    public void set(final String name, Object value) {
        final DynaProperty property = this.getDynaProperty(name);
        if (value == null || !this.isDynaAssignable(property.getType(), value.getClass())) {
            value = this.morpherRegistry.morph(property.getType(), value);
        }
        this.dynaValues.put(name, value);
    }
    
    public void set(final String name, final String key, final Object value) {
        final DynaProperty dynaProperty = this.getDynaProperty(name);
        final Class type = dynaProperty.getType();
        Class class$0;
        if ((class$0 = MorphDynaBean.class$0) == null) {
            try {
                class$0 = (MorphDynaBean.class$0 = Class.forName("java.util.Map"));
            }
            catch (ClassNotFoundException ex) {
                throw new NoClassDefFoundError(ex.getMessage());
            }
        }
        if (!class$0.isAssignableFrom(type)) {
            throw new MorphException("Non-Mapped property name: " + name + " key: " + key);
        }
        Object prop = this.dynaValues.get(name);
        if (prop == null) {
            prop = new HashMap();
            this.dynaValues.put(name, prop);
        }
        ((Map)prop).put(key, value);
    }
    
    public synchronized void setDynaBeanClass(final MorphDynaClass dynaClass) {
        if (this.dynaClass == null) {
            this.dynaClass = dynaClass;
        }
    }
    
    public void setMorpherRegistry(final MorpherRegistry morpherRegistry) {
        if (morpherRegistry == null) {
            MorphUtils.registerStandardMorphers(this.morpherRegistry = new MorpherRegistry());
        }
        else {
            this.morpherRegistry = morpherRegistry;
        }
    }
    
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append(this.dynaValues).toString();
    }
    
    protected DynaProperty getDynaProperty(final String name) {
        final DynaProperty property = this.getDynaClass().getDynaProperty(name);
        if (property == null) {
            throw new MorphException("Unspecified property for " + name);
        }
        return property;
    }
    
    protected boolean isDynaAssignable(final Class dest, final Class src) {
        boolean assignable = dest.isAssignableFrom(src);
        if (assignable) {
            return true;
        }
        boolean b = false;
        Label_0061: {
            if (dest == Boolean.TYPE) {
                Class class$2;
                if ((class$2 = MorphDynaBean.class$2) == null) {
                    try {
                        class$2 = (MorphDynaBean.class$2 = Class.forName("java.lang.Boolean"));
                    }
                    catch (ClassNotFoundException ex) {
                        throw new NoClassDefFoundError(ex.getMessage());
                    }
                }
                if (src == class$2) {
                    b = true;
                    break Label_0061;
                }
            }
            b = assignable;
        }
        assignable = b;
        boolean b2 = false;
        Label_0111: {
            if (dest == Byte.TYPE) {
                Class class$3;
                if ((class$3 = MorphDynaBean.class$3) == null) {
                    try {
                        class$3 = (MorphDynaBean.class$3 = Class.forName("java.lang.Byte"));
                    }
                    catch (ClassNotFoundException ex2) {
                        throw new NoClassDefFoundError(ex2.getMessage());
                    }
                }
                if (src == class$3) {
                    b2 = true;
                    break Label_0111;
                }
            }
            b2 = assignable;
        }
        assignable = b2;
        boolean b3 = false;
        Label_0161: {
            if (dest == Character.TYPE) {
                Class class$4;
                if ((class$4 = MorphDynaBean.class$4) == null) {
                    try {
                        class$4 = (MorphDynaBean.class$4 = Class.forName("java.lang.Character"));
                    }
                    catch (ClassNotFoundException ex3) {
                        throw new NoClassDefFoundError(ex3.getMessage());
                    }
                }
                if (src == class$4) {
                    b3 = true;
                    break Label_0161;
                }
            }
            b3 = assignable;
        }
        assignable = b3;
        boolean b4 = false;
        Label_0211: {
            if (dest == Short.TYPE) {
                Class class$5;
                if ((class$5 = MorphDynaBean.class$5) == null) {
                    try {
                        class$5 = (MorphDynaBean.class$5 = Class.forName("java.lang.Short"));
                    }
                    catch (ClassNotFoundException ex4) {
                        throw new NoClassDefFoundError(ex4.getMessage());
                    }
                }
                if (src == class$5) {
                    b4 = true;
                    break Label_0211;
                }
            }
            b4 = assignable;
        }
        assignable = b4;
        boolean b5 = false;
        Label_0261: {
            if (dest == Integer.TYPE) {
                Class class$6;
                if ((class$6 = MorphDynaBean.class$6) == null) {
                    try {
                        class$6 = (MorphDynaBean.class$6 = Class.forName("java.lang.Integer"));
                    }
                    catch (ClassNotFoundException ex5) {
                        throw new NoClassDefFoundError(ex5.getMessage());
                    }
                }
                if (src == class$6) {
                    b5 = true;
                    break Label_0261;
                }
            }
            b5 = assignable;
        }
        assignable = b5;
        boolean b6 = false;
        Label_0311: {
            if (dest == Long.TYPE) {
                Class class$7;
                if ((class$7 = MorphDynaBean.class$7) == null) {
                    try {
                        class$7 = (MorphDynaBean.class$7 = Class.forName("java.lang.Long"));
                    }
                    catch (ClassNotFoundException ex6) {
                        throw new NoClassDefFoundError(ex6.getMessage());
                    }
                }
                if (src == class$7) {
                    b6 = true;
                    break Label_0311;
                }
            }
            b6 = assignable;
        }
        assignable = b6;
        boolean b7 = false;
        Label_0361: {
            if (dest == Float.TYPE) {
                Class class$8;
                if ((class$8 = MorphDynaBean.class$8) == null) {
                    try {
                        class$8 = (MorphDynaBean.class$8 = Class.forName("java.lang.Float"));
                    }
                    catch (ClassNotFoundException ex7) {
                        throw new NoClassDefFoundError(ex7.getMessage());
                    }
                }
                if (src == class$8) {
                    b7 = true;
                    break Label_0361;
                }
            }
            b7 = assignable;
        }
        assignable = b7;
        boolean b8 = false;
        Label_0411: {
            if (dest == Double.TYPE) {
                Class class$9;
                if ((class$9 = MorphDynaBean.class$9) == null) {
                    try {
                        class$9 = (MorphDynaBean.class$9 = Class.forName("java.lang.Double"));
                    }
                    catch (ClassNotFoundException ex8) {
                        throw new NoClassDefFoundError(ex8.getMessage());
                    }
                }
                if (src == class$9) {
                    b8 = true;
                    break Label_0411;
                }
            }
            b8 = assignable;
        }
        assignable = b8;
        Label_0505: {
            if (src != Double.TYPE) {
                Class class$10;
                if ((class$10 = MorphDynaBean.class$9) == null) {
                    try {
                        class$10 = (MorphDynaBean.class$9 = Class.forName("java.lang.Double"));
                    }
                    catch (ClassNotFoundException ex9) {
                        throw new NoClassDefFoundError(ex9.getMessage());
                    }
                }
                if (!class$10.isAssignableFrom(src)) {
                    break Label_0505;
                }
            }
            assignable = (this.isByte(dest) || this.isShort(dest) || this.isInteger(dest) || this.isLong(dest) || this.isFloat(dest) || assignable);
        }
        Label_0590: {
            if (src != Float.TYPE) {
                Class class$11;
                if ((class$11 = MorphDynaBean.class$8) == null) {
                    try {
                        class$11 = (MorphDynaBean.class$8 = Class.forName("java.lang.Float"));
                    }
                    catch (ClassNotFoundException ex10) {
                        throw new NoClassDefFoundError(ex10.getMessage());
                    }
                }
                if (!class$11.isAssignableFrom(src)) {
                    break Label_0590;
                }
            }
            assignable = (this.isByte(dest) || this.isShort(dest) || this.isInteger(dest) || this.isLong(dest) || assignable);
        }
        Label_0667: {
            if (src != Long.TYPE) {
                Class class$12;
                if ((class$12 = MorphDynaBean.class$7) == null) {
                    try {
                        class$12 = (MorphDynaBean.class$7 = Class.forName("java.lang.Long"));
                    }
                    catch (ClassNotFoundException ex11) {
                        throw new NoClassDefFoundError(ex11.getMessage());
                    }
                }
                if (!class$12.isAssignableFrom(src)) {
                    break Label_0667;
                }
            }
            assignable = (this.isByte(dest) || this.isShort(dest) || this.isInteger(dest) || assignable);
        }
        Label_0736: {
            if (src != Integer.TYPE) {
                Class class$13;
                if ((class$13 = MorphDynaBean.class$6) == null) {
                    try {
                        class$13 = (MorphDynaBean.class$6 = Class.forName("java.lang.Integer"));
                    }
                    catch (ClassNotFoundException ex12) {
                        throw new NoClassDefFoundError(ex12.getMessage());
                    }
                }
                if (!class$13.isAssignableFrom(src)) {
                    break Label_0736;
                }
            }
            assignable = (this.isByte(dest) || this.isShort(dest) || assignable);
        }
        if (src != Short.TYPE) {
            Class class$14;
            if ((class$14 = MorphDynaBean.class$5) == null) {
                try {
                    class$14 = (MorphDynaBean.class$5 = Class.forName("java.lang.Short"));
                }
                catch (ClassNotFoundException ex13) {
                    throw new NoClassDefFoundError(ex13.getMessage());
                }
            }
            if (!class$14.isAssignableFrom(src)) {
                return assignable;
            }
        }
        assignable = (this.isByte(dest) || assignable);
        return assignable;
    }
    
    private boolean isByte(final Class clazz) {
        Class class$3;
        if ((class$3 = MorphDynaBean.class$3) == null) {
            try {
                class$3 = (MorphDynaBean.class$3 = Class.forName("java.lang.Byte"));
            }
            catch (ClassNotFoundException ex) {
                throw new NoClassDefFoundError(ex.getMessage());
            }
        }
        return class$3.isAssignableFrom(clazz) || clazz == Byte.TYPE;
    }
    
    private boolean isFloat(final Class clazz) {
        Class class$8;
        if ((class$8 = MorphDynaBean.class$8) == null) {
            try {
                class$8 = (MorphDynaBean.class$8 = Class.forName("java.lang.Float"));
            }
            catch (ClassNotFoundException ex) {
                throw new NoClassDefFoundError(ex.getMessage());
            }
        }
        return class$8.isAssignableFrom(clazz) || clazz == Float.TYPE;
    }
    
    private boolean isInteger(final Class clazz) {
        Class class$6;
        if ((class$6 = MorphDynaBean.class$6) == null) {
            try {
                class$6 = (MorphDynaBean.class$6 = Class.forName("java.lang.Integer"));
            }
            catch (ClassNotFoundException ex) {
                throw new NoClassDefFoundError(ex.getMessage());
            }
        }
        return class$6.isAssignableFrom(clazz) || clazz == Integer.TYPE;
    }
    
    private boolean isLong(final Class clazz) {
        Class class$7;
        if ((class$7 = MorphDynaBean.class$7) == null) {
            try {
                class$7 = (MorphDynaBean.class$7 = Class.forName("java.lang.Long"));
            }
            catch (ClassNotFoundException ex) {
                throw new NoClassDefFoundError(ex.getMessage());
            }
        }
        return class$7.isAssignableFrom(clazz) || clazz == Long.TYPE;
    }
    
    private boolean isShort(final Class clazz) {
        Class class$5;
        if ((class$5 = MorphDynaBean.class$5) == null) {
            try {
                class$5 = (MorphDynaBean.class$5 = Class.forName("java.lang.Short"));
            }
            catch (ClassNotFoundException ex) {
                throw new NoClassDefFoundError(ex.getMessage());
            }
        }
        return class$5.isAssignableFrom(clazz) || clazz == Short.TYPE;
    }
}
