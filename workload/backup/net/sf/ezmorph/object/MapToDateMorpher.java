// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.ezmorph.object;

import java.util.Calendar;
import java.util.Map;
import net.sf.ezmorph.MorphException;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import java.util.Date;

public class MapToDateMorpher extends AbstractObjectMorpher
{
    private Date defaultValue;
    static /* synthetic */ Class class$0;
    static /* synthetic */ Class class$1;
    
    public MapToDateMorpher() {
    }
    
    public MapToDateMorpher(final Date defaultValue) {
        super(true);
        this.defaultValue = defaultValue;
    }
    
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MapToDateMorpher)) {
            return false;
        }
        final MapToDateMorpher other = (MapToDateMorpher)obj;
        final EqualsBuilder builder = new EqualsBuilder();
        if (this.isUseDefault() && other.isUseDefault()) {
            builder.append(this.getDefaultValue(), other.getDefaultValue());
            return builder.isEquals();
        }
        return !this.isUseDefault() && !other.isUseDefault() && builder.isEquals();
    }
    
    public Date getDefaultValue() {
        return (Date)this.defaultValue.clone();
    }
    
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        if (this.isUseDefault()) {
            builder.append(this.getDefaultValue());
        }
        return builder.toHashCode();
    }
    
    public Object morph(final Object value) {
        if (value == null) {
            return null;
        }
        Class class$0;
        if ((class$0 = MapToDateMorpher.class$0) == null) {
            try {
                class$0 = (MapToDateMorpher.class$0 = Class.forName("java.util.Date"));
            }
            catch (ClassNotFoundException ex) {
                throw new NoClassDefFoundError(ex.getMessage());
            }
        }
        if (class$0.isAssignableFrom(value.getClass())) {
            return value;
        }
        if (!this.supports(value.getClass())) {
            throw new MorphException(value.getClass() + " is not supported");
        }
        final Map map = (Map)value;
        if (!map.isEmpty()) {
            final Calendar c = Calendar.getInstance();
            c.set(1, this.getValue(map, "year"));
            c.set(2, this.getValue(map, "month"));
            c.set(5, this.getValue(map, "day"));
            c.set(11, this.getValue(map, "hour"));
            c.set(12, this.getValue(map, "minutes"));
            c.set(13, this.getValue(map, "seconds"));
            c.set(14, this.getValue(map, "milliseconds"));
            return c.getTime();
        }
        if (this.isUseDefault()) {
            return this.defaultValue;
        }
        throw new MorphException("Unable to parse the date " + value);
    }
    
    public Class morphsTo() {
        Class class$0;
        if ((class$0 = MapToDateMorpher.class$0) == null) {
            try {
                class$0 = (MapToDateMorpher.class$0 = Class.forName("java.util.Date"));
            }
            catch (ClassNotFoundException ex) {
                throw new NoClassDefFoundError(ex.getMessage());
            }
        }
        return class$0;
    }
    
    public void setDefaultValue(final Date defaultValue) {
        this.defaultValue = (Date)defaultValue.clone();
    }
    
    public boolean supports(final Class clazz) {
        if (clazz != null) {
            Class class$1;
            if ((class$1 = MapToDateMorpher.class$1) == null) {
                try {
                    class$1 = (MapToDateMorpher.class$1 = Class.forName("java.util.Map"));
                }
                catch (ClassNotFoundException ex) {
                    throw new NoClassDefFoundError(ex.getMessage());
                }
            }
            if (class$1.isAssignableFrom(clazz)) {
                return true;
            }
        }
        return false;
    }
    
    private int getValue(final Map map, final String key) {
        final Object value = map.get(key);
        if (value == null || !(value instanceof Number)) {
            return 0;
        }
        final Number n = (Number)value;
        return n.intValue();
    }
}
