// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.ezmorph.object;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import net.sf.ezmorph.MorphException;
import org.apache.commons.lang.builder.HashCodeBuilder;
import java.lang.reflect.Method;
import net.sf.ezmorph.Morpher;

public final class ObjectListMorpher extends AbstractObjectMorpher
{
    private Object defaultValue;
    private Morpher morpher;
    private Method morphMethod;
    static /* synthetic */ Class class$0;
    static /* synthetic */ Class class$1;
    
    public ObjectListMorpher(final Morpher morpher) {
        this.setMorpher(morpher);
    }
    
    public ObjectListMorpher(final Morpher morpher, final Object defaultValue) {
        super(true);
        this.defaultValue = defaultValue;
        this.setMorpher(morpher);
    }
    
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ObjectListMorpher)) {
            return false;
        }
        final ObjectListMorpher other = (ObjectListMorpher)obj;
        return this.morpher.equals(other.morpher);
    }
    
    public int hashCode() {
        return new HashCodeBuilder().append(this.morpher).toHashCode();
    }
    
    public Object morph(final Object value) {
        if (value == null) {
            return null;
        }
        if (!this.supports(value.getClass())) {
            throw new MorphException(value.getClass() + " is not supported");
        }
        final List list = new ArrayList();
        for (final Object object : (List)value) {
            if (object == null) {
                if (this.isUseDefault()) {
                    list.add(this.defaultValue);
                }
                else {
                    list.add(object);
                }
            }
            else {
                if (!this.morpher.supports(object.getClass())) {
                    throw new MorphException(object.getClass() + " is not supported");
                }
                try {
                    list.add(this.morphMethod.invoke(this.morpher, object));
                }
                catch (MorphException me) {
                    throw me;
                }
                catch (Exception e) {
                    throw new MorphException(e);
                }
            }
        }
        return list;
    }
    
    public Class morphsTo() {
        Class class$0;
        if ((class$0 = ObjectListMorpher.class$0) == null) {
            try {
                class$0 = (ObjectListMorpher.class$0 = Class.forName("java.util.List"));
            }
            catch (ClassNotFoundException ex) {
                throw new NoClassDefFoundError(ex.getMessage());
            }
        }
        return class$0;
    }
    
    public boolean supports(final Class clazz) {
        if (clazz != null) {
            Class class$0;
            if ((class$0 = ObjectListMorpher.class$0) == null) {
                try {
                    class$0 = (ObjectListMorpher.class$0 = Class.forName("java.util.List"));
                }
                catch (ClassNotFoundException ex) {
                    throw new NoClassDefFoundError(ex.getMessage());
                }
            }
            if (class$0.isAssignableFrom(clazz)) {
                return true;
            }
        }
        return false;
    }
    
    private void setMorpher(final Morpher morpher) {
        if (morpher == null) {
            throw new IllegalArgumentException("morpher can not be null");
        }
        this.morpher = morpher;
        try {
            final Class<? extends Morpher> class1 = morpher.getClass();
            final String name = "morph";
            final Class[] parameterTypes = { null };
            final int n = 0;
            Class class$1;
            if ((class$1 = ObjectListMorpher.class$1) == null) {
                try {
                    class$1 = (ObjectListMorpher.class$1 = Class.forName("java.lang.Object"));
                }
                catch (ClassNotFoundException ex) {
                    throw new NoClassDefFoundError(ex.getMessage());
                }
            }
            parameterTypes[n] = class$1;
            this.morphMethod = class1.getDeclaredMethod(name, (Class[])parameterTypes);
        }
        catch (NoSuchMethodException nsme) {
            throw new IllegalArgumentException(nsme.getMessage());
        }
    }
}
