// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.ezmorph.object;

import net.sf.ezmorph.primitive.ShortMorpher;
import net.sf.ezmorph.primitive.LongMorpher;
import net.sf.ezmorph.primitive.IntMorpher;
import net.sf.ezmorph.primitive.FloatMorpher;
import net.sf.ezmorph.primitive.DoubleMorpher;
import net.sf.ezmorph.primitive.ByteMorpher;
import java.math.BigInteger;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import net.sf.ezmorph.MorphException;

public final class NumberMorpher extends AbstractObjectMorpher
{
    private Number defaultValue;
    private Class type;
    static /* synthetic */ Class class$0;
    static /* synthetic */ Class class$1;
    static /* synthetic */ Class class$2;
    static /* synthetic */ Class class$3;
    static /* synthetic */ Class class$4;
    static /* synthetic */ Class class$5;
    static /* synthetic */ Class class$6;
    static /* synthetic */ Class class$7;
    
    public NumberMorpher(final Class type) {
        super(false);
        if (type == null) {
            throw new MorphException("Must specify a type");
        }
        if (type != Byte.TYPE && type != Short.TYPE && type != Integer.TYPE && type != Long.TYPE && type != Float.TYPE && type != Double.TYPE) {
            Class class$0;
            if ((class$0 = NumberMorpher.class$0) == null) {
                try {
                    class$0 = (NumberMorpher.class$0 = Class.forName("java.lang.Byte"));
                }
                catch (ClassNotFoundException ex) {
                    throw new NoClassDefFoundError(ex.getMessage());
                }
            }
            if (!class$0.isAssignableFrom(type)) {
                Class class$2;
                if ((class$2 = NumberMorpher.class$1) == null) {
                    try {
                        class$2 = (NumberMorpher.class$1 = Class.forName("java.lang.Short"));
                    }
                    catch (ClassNotFoundException ex2) {
                        throw new NoClassDefFoundError(ex2.getMessage());
                    }
                }
                if (!class$2.isAssignableFrom(type)) {
                    Class class$3;
                    if ((class$3 = NumberMorpher.class$2) == null) {
                        try {
                            class$3 = (NumberMorpher.class$2 = Class.forName("java.lang.Integer"));
                        }
                        catch (ClassNotFoundException ex3) {
                            throw new NoClassDefFoundError(ex3.getMessage());
                        }
                    }
                    if (!class$3.isAssignableFrom(type)) {
                        Class class$4;
                        if ((class$4 = NumberMorpher.class$3) == null) {
                            try {
                                class$4 = (NumberMorpher.class$3 = Class.forName("java.lang.Long"));
                            }
                            catch (ClassNotFoundException ex4) {
                                throw new NoClassDefFoundError(ex4.getMessage());
                            }
                        }
                        if (!class$4.isAssignableFrom(type)) {
                            Class class$5;
                            if ((class$5 = NumberMorpher.class$4) == null) {
                                try {
                                    class$5 = (NumberMorpher.class$4 = Class.forName("java.lang.Float"));
                                }
                                catch (ClassNotFoundException ex5) {
                                    throw new NoClassDefFoundError(ex5.getMessage());
                                }
                            }
                            if (!class$5.isAssignableFrom(type)) {
                                Class class$6;
                                if ((class$6 = NumberMorpher.class$5) == null) {
                                    try {
                                        class$6 = (NumberMorpher.class$5 = Class.forName("java.lang.Double"));
                                    }
                                    catch (ClassNotFoundException ex6) {
                                        throw new NoClassDefFoundError(ex6.getMessage());
                                    }
                                }
                                if (!class$6.isAssignableFrom(type)) {
                                    Class class$7;
                                    if ((class$7 = NumberMorpher.class$6) == null) {
                                        try {
                                            class$7 = (NumberMorpher.class$6 = Class.forName("java.math.BigInteger"));
                                        }
                                        catch (ClassNotFoundException ex7) {
                                            throw new NoClassDefFoundError(ex7.getMessage());
                                        }
                                    }
                                    if (!class$7.isAssignableFrom(type)) {
                                        Class class$8;
                                        if ((class$8 = NumberMorpher.class$7) == null) {
                                            try {
                                                class$8 = (NumberMorpher.class$7 = Class.forName("java.math.BigDecimal"));
                                            }
                                            catch (ClassNotFoundException ex8) {
                                                throw new NoClassDefFoundError(ex8.getMessage());
                                            }
                                        }
                                        if (!class$8.isAssignableFrom(type)) {
                                            throw new MorphException("Must specify a Number subclass");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        this.type = type;
    }
    
    public NumberMorpher(final Class type, final Number defaultValue) {
        super(true);
        if (type == null) {
            throw new MorphException("Must specify a type");
        }
        if (type != Byte.TYPE && type != Short.TYPE && type != Integer.TYPE && type != Long.TYPE && type != Float.TYPE && type != Double.TYPE) {
            Class class$0;
            if ((class$0 = NumberMorpher.class$0) == null) {
                try {
                    class$0 = (NumberMorpher.class$0 = Class.forName("java.lang.Byte"));
                }
                catch (ClassNotFoundException ex) {
                    throw new NoClassDefFoundError(ex.getMessage());
                }
            }
            if (!class$0.isAssignableFrom(type)) {
                Class class$2;
                if ((class$2 = NumberMorpher.class$1) == null) {
                    try {
                        class$2 = (NumberMorpher.class$1 = Class.forName("java.lang.Short"));
                    }
                    catch (ClassNotFoundException ex2) {
                        throw new NoClassDefFoundError(ex2.getMessage());
                    }
                }
                if (!class$2.isAssignableFrom(type)) {
                    Class class$3;
                    if ((class$3 = NumberMorpher.class$2) == null) {
                        try {
                            class$3 = (NumberMorpher.class$2 = Class.forName("java.lang.Integer"));
                        }
                        catch (ClassNotFoundException ex3) {
                            throw new NoClassDefFoundError(ex3.getMessage());
                        }
                    }
                    if (!class$3.isAssignableFrom(type)) {
                        Class class$4;
                        if ((class$4 = NumberMorpher.class$3) == null) {
                            try {
                                class$4 = (NumberMorpher.class$3 = Class.forName("java.lang.Long"));
                            }
                            catch (ClassNotFoundException ex4) {
                                throw new NoClassDefFoundError(ex4.getMessage());
                            }
                        }
                        if (!class$4.isAssignableFrom(type)) {
                            Class class$5;
                            if ((class$5 = NumberMorpher.class$4) == null) {
                                try {
                                    class$5 = (NumberMorpher.class$4 = Class.forName("java.lang.Float"));
                                }
                                catch (ClassNotFoundException ex5) {
                                    throw new NoClassDefFoundError(ex5.getMessage());
                                }
                            }
                            if (!class$5.isAssignableFrom(type)) {
                                Class class$6;
                                if ((class$6 = NumberMorpher.class$5) == null) {
                                    try {
                                        class$6 = (NumberMorpher.class$5 = Class.forName("java.lang.Double"));
                                    }
                                    catch (ClassNotFoundException ex6) {
                                        throw new NoClassDefFoundError(ex6.getMessage());
                                    }
                                }
                                if (!class$6.isAssignableFrom(type)) {
                                    Class class$7;
                                    if ((class$7 = NumberMorpher.class$6) == null) {
                                        try {
                                            class$7 = (NumberMorpher.class$6 = Class.forName("java.math.BigInteger"));
                                        }
                                        catch (ClassNotFoundException ex7) {
                                            throw new NoClassDefFoundError(ex7.getMessage());
                                        }
                                    }
                                    if (!class$7.isAssignableFrom(type)) {
                                        Class class$8;
                                        if ((class$8 = NumberMorpher.class$7) == null) {
                                            try {
                                                class$8 = (NumberMorpher.class$7 = Class.forName("java.math.BigDecimal"));
                                            }
                                            catch (ClassNotFoundException ex8) {
                                                throw new NoClassDefFoundError(ex8.getMessage());
                                            }
                                        }
                                        if (!class$8.isAssignableFrom(type)) {
                                            throw new MorphException("Must specify a Number subclass");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (defaultValue != null && !type.isInstance(defaultValue)) {
            throw new MorphException("Default value must be of type " + type);
        }
        this.type = type;
        this.setDefaultValue(defaultValue);
    }
    
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof NumberMorpher)) {
            return false;
        }
        final NumberMorpher other = (NumberMorpher)obj;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.type, other.type);
        if (this.isUseDefault() && other.isUseDefault()) {
            builder.append(this.getDefaultValue(), other.getDefaultValue());
            return builder.isEquals();
        }
        return !this.isUseDefault() && !other.isUseDefault() && builder.isEquals();
    }
    
    public Number getDefaultValue() {
        return this.defaultValue;
    }
    
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(this.type);
        if (this.isUseDefault()) {
            builder.append(this.getDefaultValue());
        }
        return builder.toHashCode();
    }
    
    public Object morph(final Object value) {
        if (value != null && this.type.isAssignableFrom(value.getClass())) {
            return value;
        }
        final String str = String.valueOf(value).trim();
        if (!this.type.isPrimitive() && (value == null || str.length() == 0 || "null".equalsIgnoreCase(str))) {
            return null;
        }
        if (this.isDecimalNumber(this.type)) {
            Class class$4;
            if ((class$4 = NumberMorpher.class$4) == null) {
                try {
                    class$4 = (NumberMorpher.class$4 = Class.forName("java.lang.Float"));
                }
                catch (ClassNotFoundException ex) {
                    throw new NoClassDefFoundError(ex.getMessage());
                }
            }
            if (class$4.isAssignableFrom(this.type) || Float.TYPE == this.type) {
                return this.morphToFloat(str);
            }
            Class class$5;
            if ((class$5 = NumberMorpher.class$5) == null) {
                try {
                    class$5 = (NumberMorpher.class$5 = Class.forName("java.lang.Double"));
                }
                catch (ClassNotFoundException ex2) {
                    throw new NoClassDefFoundError(ex2.getMessage());
                }
            }
            if (class$5.isAssignableFrom(this.type) || Double.TYPE == this.type) {
                return this.morphToDouble(str);
            }
            return this.morphToBigDecimal(str);
        }
        else {
            Class class$6;
            if ((class$6 = NumberMorpher.class$0) == null) {
                try {
                    class$6 = (NumberMorpher.class$0 = Class.forName("java.lang.Byte"));
                }
                catch (ClassNotFoundException ex3) {
                    throw new NoClassDefFoundError(ex3.getMessage());
                }
            }
            if (class$6.isAssignableFrom(this.type) || Byte.TYPE == this.type) {
                return this.morphToByte(str);
            }
            Class class$7;
            if ((class$7 = NumberMorpher.class$1) == null) {
                try {
                    class$7 = (NumberMorpher.class$1 = Class.forName("java.lang.Short"));
                }
                catch (ClassNotFoundException ex4) {
                    throw new NoClassDefFoundError(ex4.getMessage());
                }
            }
            if (class$7.isAssignableFrom(this.type) || Short.TYPE == this.type) {
                return this.morphToShort(str);
            }
            Class class$8;
            if ((class$8 = NumberMorpher.class$2) == null) {
                try {
                    class$8 = (NumberMorpher.class$2 = Class.forName("java.lang.Integer"));
                }
                catch (ClassNotFoundException ex5) {
                    throw new NoClassDefFoundError(ex5.getMessage());
                }
            }
            if (class$8.isAssignableFrom(this.type) || Integer.TYPE == this.type) {
                return this.morphToInteger(str);
            }
            Class class$9;
            if ((class$9 = NumberMorpher.class$3) == null) {
                try {
                    class$9 = (NumberMorpher.class$3 = Class.forName("java.lang.Long"));
                }
                catch (ClassNotFoundException ex6) {
                    throw new NoClassDefFoundError(ex6.getMessage());
                }
            }
            if (class$9.isAssignableFrom(this.type) || Long.TYPE == this.type) {
                return this.morphToLong(str);
            }
            return this.morphToBigInteger(str);
        }
    }
    
    public Class morphsTo() {
        return this.type;
    }
    
    public void setDefaultValue(final Number defaultValue) {
        if (defaultValue != null && !this.type.isInstance(defaultValue)) {
            throw new MorphException("Default value must be of type " + this.type);
        }
        this.defaultValue = defaultValue;
    }
    
    private boolean isDecimalNumber(final Class type) {
        Class class$5;
        if ((class$5 = NumberMorpher.class$5) == null) {
            try {
                class$5 = (NumberMorpher.class$5 = Class.forName("java.lang.Double"));
            }
            catch (ClassNotFoundException ex) {
                throw new NoClassDefFoundError(ex.getMessage());
            }
        }
        if (!class$5.isAssignableFrom(type)) {
            Class class$6;
            if ((class$6 = NumberMorpher.class$4) == null) {
                try {
                    class$6 = (NumberMorpher.class$4 = Class.forName("java.lang.Float"));
                }
                catch (ClassNotFoundException ex2) {
                    throw new NoClassDefFoundError(ex2.getMessage());
                }
            }
            if (!class$6.isAssignableFrom(type) && Double.TYPE != type && Float.TYPE != type) {
                Class class$7;
                if ((class$7 = NumberMorpher.class$7) == null) {
                    try {
                        class$7 = (NumberMorpher.class$7 = Class.forName("java.math.BigDecimal"));
                    }
                    catch (ClassNotFoundException ex3) {
                        throw new NoClassDefFoundError(ex3.getMessage());
                    }
                }
                if (!class$7.isAssignableFrom(type)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private Object morphToBigDecimal(final String str) {
        Object result = null;
        if (this.isUseDefault()) {
            result = new BigDecimalMorpher((BigDecimal)this.defaultValue).morph(str);
        }
        else {
            result = new BigDecimal(str);
        }
        return result;
    }
    
    private Object morphToBigInteger(final String str) {
        Object result = null;
        if (this.isUseDefault()) {
            result = new BigIntegerMorpher((BigInteger)this.defaultValue).morph(str);
        }
        else {
            result = new BigInteger(str);
        }
        return result;
    }
    
    private Object morphToByte(final String str) {
        Object result = null;
        if (this.isUseDefault()) {
            if (this.defaultValue == null) {
                return null;
            }
            result = new Byte(new ByteMorpher(this.defaultValue.byteValue()).morph(str));
        }
        else {
            result = new Byte(new ByteMorpher().morph(str));
        }
        return result;
    }
    
    private Object morphToDouble(final String str) {
        Object result = null;
        if (this.isUseDefault()) {
            if (this.defaultValue == null) {
                return null;
            }
            result = new Double(new DoubleMorpher(this.defaultValue.doubleValue()).morph(str));
        }
        else {
            result = new Double(new DoubleMorpher().morph(str));
        }
        return result;
    }
    
    private Object morphToFloat(final String str) {
        Object result = null;
        if (this.isUseDefault()) {
            if (this.defaultValue == null) {
                return null;
            }
            result = new Float(new FloatMorpher(this.defaultValue.floatValue()).morph(str));
        }
        else {
            result = new Float(new FloatMorpher().morph(str));
        }
        return result;
    }
    
    private Object morphToInteger(final String str) {
        Object result = null;
        if (this.isUseDefault()) {
            if (this.defaultValue == null) {
                return null;
            }
            result = new Integer(new IntMorpher(this.defaultValue.intValue()).morph(str));
        }
        else {
            result = new Integer(new IntMorpher().morph(str));
        }
        return result;
    }
    
    private Object morphToLong(final String str) {
        Object result = null;
        if (this.isUseDefault()) {
            if (this.defaultValue == null) {
                return null;
            }
            result = new Long(new LongMorpher(this.defaultValue.longValue()).morph(str));
        }
        else {
            result = new Long(new LongMorpher().morph(str));
        }
        return result;
    }
    
    private Object morphToShort(final String str) {
        Object result = null;
        if (this.isUseDefault()) {
            if (this.defaultValue == null) {
                return null;
            }
            result = new Short(new ShortMorpher(this.defaultValue.shortValue()).morph(str));
        }
        else {
            result = new Short(new ShortMorpher().morph(str));
        }
        return result;
    }
}
