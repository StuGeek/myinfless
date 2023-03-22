// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.ezmorph.object;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import net.sf.ezmorph.MorphException;
import java.util.Locale;
import java.util.Date;

public final class DateMorpher extends AbstractObjectMorpher
{
    private Date defaultValue;
    private String[] formats;
    private boolean lenient;
    private Locale locale;
    static /* synthetic */ Class class$0;
    static /* synthetic */ Class class$1;
    
    public DateMorpher(final String[] formats) {
        this(formats, Locale.getDefault(), false);
    }
    
    public DateMorpher(final String[] formats, final boolean lenient) {
        this(formats, Locale.getDefault(), lenient);
    }
    
    public DateMorpher(final String[] formats, final Date defaultValue) {
        this(formats, defaultValue, Locale.getDefault(), false);
    }
    
    public DateMorpher(final String[] formats, final Date defaultValue, final Locale locale, final boolean lenient) {
        super(true);
        if (formats == null || formats.length == 0) {
            throw new MorphException("invalid array of formats");
        }
        this.formats = formats;
        if (locale == null) {
            this.locale = Locale.getDefault();
        }
        else {
            this.locale = locale;
        }
        this.lenient = lenient;
        this.setDefaultValue(defaultValue);
    }
    
    public DateMorpher(final String[] formats, final Locale locale) {
        this(formats, locale, false);
    }
    
    public DateMorpher(final String[] formats, final Locale locale, final boolean lenient) {
        if (formats == null || formats.length == 0) {
            throw new MorphException("invalid array of formats");
        }
        this.formats = formats;
        if (locale == null) {
            this.locale = Locale.getDefault();
        }
        else {
            this.locale = locale;
        }
        this.lenient = lenient;
    }
    
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof DateMorpher)) {
            return false;
        }
        final DateMorpher other = (DateMorpher)obj;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.formats, other.formats);
        builder.append(this.locale, other.locale);
        builder.append(this.lenient, other.lenient);
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
        builder.append(this.formats);
        builder.append(this.locale);
        builder.append(this.lenient);
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
        if ((class$0 = DateMorpher.class$0) == null) {
            try {
                class$0 = (DateMorpher.class$0 = Class.forName("java.util.Date"));
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
        final String strValue = (String)value;
        SimpleDateFormat dateParser = null;
        int i = 0;
        while (i < this.formats.length) {
            if (dateParser == null) {
                dateParser = new SimpleDateFormat(this.formats[i], this.locale);
            }
            else {
                dateParser.applyPattern(this.formats[i]);
            }
            dateParser.setLenient(this.lenient);
            try {
                return dateParser.parse(strValue.toLowerCase());
            }
            catch (ParseException ex2) {
                ++i;
            }
        }
        if (this.isUseDefault()) {
            return this.defaultValue;
        }
        throw new MorphException("Unable to parse the date " + value);
    }
    
    public Class morphsTo() {
        Class class$0;
        if ((class$0 = DateMorpher.class$0) == null) {
            try {
                class$0 = (DateMorpher.class$0 = Class.forName("java.util.Date"));
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
        Class class$1;
        if ((class$1 = DateMorpher.class$1) == null) {
            try {
                class$1 = (DateMorpher.class$1 = Class.forName("java.lang.String"));
            }
            catch (ClassNotFoundException ex) {
                throw new NoClassDefFoundError(ex.getMessage());
            }
        }
        return class$1.isAssignableFrom(clazz);
    }
}
