// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.ezmorph.bean;

import java.util.Arrays;
import org.apache.commons.lang.builder.ToStringBuilder;
import java.util.Iterator;
import net.sf.ezmorph.MorphUtils;
import net.sf.ezmorph.MorpherRegistry;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import net.sf.ezmorph.MorphException;
import java.util.HashMap;
import org.apache.commons.beanutils.DynaProperty;
import java.util.Map;
import java.util.Comparator;
import java.io.Serializable;
import org.apache.commons.beanutils.DynaClass;

public final class MorphDynaClass implements DynaClass, Serializable
{
    private static final Comparator dynaPropertyComparator;
    private static final long serialVersionUID = -613214016860871560L;
    private Map attributes;
    private Class beanClass;
    private DynaProperty[] dynaProperties;
    private String name;
    private Map properties;
    private Class type;
    static /* synthetic */ Class class$0;
    
    static {
        dynaPropertyComparator = new Comparator() {
            public int compare(final Object a, final Object b) {
                if (a instanceof DynaProperty && b instanceof DynaProperty) {
                    final DynaProperty p1 = (DynaProperty)a;
                    final DynaProperty p2 = (DynaProperty)b;
                    return p1.getName().compareTo(p2.getName());
                }
                return -1;
            }
        };
    }
    
    public MorphDynaClass(final Map attributes) {
        this(null, null, attributes);
    }
    
    public MorphDynaClass(final Map attributes, final boolean exceptionOnEmptyAttributes) {
        this(null, null, attributes, exceptionOnEmptyAttributes);
    }
    
    public MorphDynaClass(final String name, final Class type, final Map attributes) {
        this(name, type, attributes, false);
    }
    
    public MorphDynaClass(String name, Class type, Map attributes, final boolean exceptionOnEmptyAttributes) {
        this.properties = new HashMap();
        if (name == null) {
            name = "MorphDynaClass";
        }
        if (type == null) {
            Class class$0;
            if ((class$0 = MorphDynaClass.class$0) == null) {
                try {
                    class$0 = (MorphDynaClass.class$0 = Class.forName("net.sf.ezmorph.bean.MorphDynaBean"));
                }
                catch (ClassNotFoundException ex) {
                    throw new NoClassDefFoundError(ex.getMessage());
                }
            }
            type = (Class<?>)class$0;
        }
        Class class$2;
        if ((class$2 = MorphDynaClass.class$0) == null) {
            try {
                class$2 = (MorphDynaClass.class$0 = Class.forName("net.sf.ezmorph.bean.MorphDynaBean"));
            }
            catch (ClassNotFoundException ex2) {
                throw new NoClassDefFoundError(ex2.getMessage());
            }
        }
        if (!class$2.isAssignableFrom(type)) {
            throw new MorphException("MorphDynaBean is not assignable from " + type.getName());
        }
        if (attributes == null || attributes.isEmpty()) {
            if (exceptionOnEmptyAttributes) {
                throw new MorphException("Attributes map is null or empty.");
            }
            attributes = new HashMap();
        }
        this.name = name;
        this.type = type;
        this.attributes = attributes;
        this.process();
    }
    
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MorphDynaClass)) {
            return false;
        }
        final MorphDynaClass other = (MorphDynaClass)obj;
        final EqualsBuilder builder = new EqualsBuilder().append(this.name, other.name).append(this.type, other.type);
        if (this.dynaProperties.length != other.dynaProperties.length) {
            return false;
        }
        for (int i = 0; i < this.dynaProperties.length; ++i) {
            final DynaProperty a = this.dynaProperties[i];
            final DynaProperty b = other.dynaProperties[i];
            builder.append(a.getName(), b.getName());
            builder.append(a.getType(), b.getType());
        }
        return builder.isEquals();
    }
    
    public DynaProperty[] getDynaProperties() {
        return this.dynaProperties;
    }
    
    public DynaProperty getDynaProperty(final String propertyName) {
        if (propertyName == null) {
            throw new MorphException("Unnespecified bean property name");
        }
        return this.properties.get(propertyName);
    }
    
    public String getName() {
        return this.name;
    }
    
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder().append(this.name).append(this.type);
        for (int i = 0; i < this.dynaProperties.length; ++i) {
            builder.append(this.dynaProperties[i].getName());
            builder.append(this.dynaProperties[i].getType());
        }
        return builder.toHashCode();
    }
    
    public DynaBean newInstance() throws IllegalAccessException, InstantiationException {
        return this.newInstance(null);
    }
    
    public DynaBean newInstance(MorpherRegistry morpherRegistry) throws IllegalAccessException, InstantiationException {
        if (morpherRegistry == null) {
            morpherRegistry = new MorpherRegistry();
            MorphUtils.registerStandardMorphers(morpherRegistry);
        }
        final MorphDynaBean dynaBean = this.getBeanClass().newInstance();
        dynaBean.setDynaBeanClass(this);
        dynaBean.setMorpherRegistry(morpherRegistry);
        for (final String key : this.attributes.keySet()) {
            dynaBean.set(key, null);
        }
        return dynaBean;
    }
    
    public String toString() {
        return new ToStringBuilder(this).append("name", this.name).append("type", this.type).append("attributes", this.attributes).toString();
    }
    
    protected Class getBeanClass() {
        if (this.beanClass == null) {
            this.process();
        }
        return this.beanClass;
    }
    
    private void process() {
        this.beanClass = this.type;
        try {
            final Iterator entries = this.attributes.entrySet().iterator();
            this.dynaProperties = new DynaProperty[this.attributes.size()];
            int i = 0;
            while (entries.hasNext()) {
                final Map.Entry entry = entries.next();
                final String pname = entry.getKey();
                final Object pclass = entry.getValue();
                DynaProperty dynaProperty = null;
                if (pclass instanceof String) {
                    final Class klass = Class.forName((String)pclass);
                    if (klass.isArray() && klass.getComponentType().isArray()) {
                        throw new MorphException("Multidimensional arrays are not supported");
                    }
                    dynaProperty = new DynaProperty(pname, klass);
                }
                else {
                    if (!(pclass instanceof Class)) {
                        throw new MorphException("Type must be String or Class");
                    }
                    final Class klass = (Class)pclass;
                    if (klass.isArray() && klass.getComponentType().isArray()) {
                        throw new MorphException("Multidimensional arrays are not supported");
                    }
                    dynaProperty = new DynaProperty(pname, klass);
                }
                this.properties.put(dynaProperty.getName(), dynaProperty);
                this.dynaProperties[i++] = dynaProperty;
            }
        }
        catch (ClassNotFoundException cnfe) {
            throw new MorphException(cnfe);
        }
        Arrays.sort(this.dynaProperties, 0, this.dynaProperties.length, MorphDynaClass.dynaPropertyComparator);
    }
}
