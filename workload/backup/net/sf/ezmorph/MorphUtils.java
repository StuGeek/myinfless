// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.ezmorph;

import net.sf.ezmorph.primitive.DoubleMorpher;
import net.sf.ezmorph.primitive.FloatMorpher;
import net.sf.ezmorph.primitive.LongMorpher;
import net.sf.ezmorph.primitive.IntMorpher;
import net.sf.ezmorph.primitive.ShortMorpher;
import net.sf.ezmorph.primitive.ByteMorpher;
import net.sf.ezmorph.primitive.CharMorpher;
import net.sf.ezmorph.primitive.BooleanMorpher;
import net.sf.ezmorph.array.DoubleArrayMorpher;
import net.sf.ezmorph.array.FloatArrayMorpher;
import net.sf.ezmorph.array.LongArrayMorpher;
import net.sf.ezmorph.array.IntArrayMorpher;
import net.sf.ezmorph.array.ShortArrayMorpher;
import net.sf.ezmorph.array.ByteArrayMorpher;
import net.sf.ezmorph.array.CharArrayMorpher;
import net.sf.ezmorph.array.BooleanArrayMorpher;
import net.sf.ezmorph.object.ClassMorpher;
import java.math.BigInteger;
import net.sf.ezmorph.object.NumberMorpher;
import net.sf.ezmorph.object.StringMorpher;
import net.sf.ezmorph.object.CharacterObjectMorpher;
import net.sf.ezmorph.array.ObjectArrayMorpher;
import net.sf.ezmorph.object.BooleanObjectMorpher;
import java.math.BigDecimal;

public class MorphUtils
{
    public static final BigDecimal BIGDECIMAL_ONE;
    public static final BigDecimal BIGDECIMAL_ZERO;
    static /* synthetic */ Class class$0;
    static /* synthetic */ Class class$1;
    static /* synthetic */ Class class$2;
    static /* synthetic */ Class class$3;
    static /* synthetic */ Class class$4;
    static /* synthetic */ Class class$5;
    static /* synthetic */ Class class$6;
    static /* synthetic */ Class class$7;
    
    static {
        BIGDECIMAL_ONE = new BigDecimal("1");
        BIGDECIMAL_ZERO = new BigDecimal("0");
    }
    
    public static void registerStandardMorphers(final MorpherRegistry morpherRegistry) {
        morpherRegistry.clear();
        registerStandardPrimitiveMorphers(morpherRegistry);
        registerStandardPrimitiveArrayMorphers(morpherRegistry);
        registerStandardObjectMorphers(morpherRegistry);
        registerStandardObjectArrayMorphers(morpherRegistry);
    }
    
    public static void registerStandardObjectArrayMorphers(final MorpherRegistry morpherRegistry) {
        morpherRegistry.registerMorpher(new ObjectArrayMorpher(new BooleanObjectMorpher(Boolean.FALSE)));
        morpherRegistry.registerMorpher(new ObjectArrayMorpher(new CharacterObjectMorpher(new Character('\0'))));
        morpherRegistry.registerMorpher(new ObjectArrayMorpher(StringMorpher.getInstance()));
        Class class$0;
        if ((class$0 = MorphUtils.class$0) == null) {
            try {
                class$0 = (MorphUtils.class$0 = Class.forName("java.lang.Byte"));
            }
            catch (ClassNotFoundException ex) {
                throw new NoClassDefFoundError(ex.getMessage());
            }
        }
        morpherRegistry.registerMorpher(new ObjectArrayMorpher(new NumberMorpher(class$0, new Byte((byte)0))));
        Class class$2;
        if ((class$2 = MorphUtils.class$1) == null) {
            try {
                class$2 = (MorphUtils.class$1 = Class.forName("java.lang.Short"));
            }
            catch (ClassNotFoundException ex2) {
                throw new NoClassDefFoundError(ex2.getMessage());
            }
        }
        morpherRegistry.registerMorpher(new ObjectArrayMorpher(new NumberMorpher(class$2, new Short((short)0))));
        Class class$3;
        if ((class$3 = MorphUtils.class$2) == null) {
            try {
                class$3 = (MorphUtils.class$2 = Class.forName("java.lang.Integer"));
            }
            catch (ClassNotFoundException ex3) {
                throw new NoClassDefFoundError(ex3.getMessage());
            }
        }
        morpherRegistry.registerMorpher(new ObjectArrayMorpher(new NumberMorpher(class$3, new Integer(0))));
        Class class$4;
        if ((class$4 = MorphUtils.class$3) == null) {
            try {
                class$4 = (MorphUtils.class$3 = Class.forName("java.lang.Long"));
            }
            catch (ClassNotFoundException ex4) {
                throw new NoClassDefFoundError(ex4.getMessage());
            }
        }
        morpherRegistry.registerMorpher(new ObjectArrayMorpher(new NumberMorpher(class$4, new Long(0L))));
        Class class$5;
        if ((class$5 = MorphUtils.class$4) == null) {
            try {
                class$5 = (MorphUtils.class$4 = Class.forName("java.lang.Float"));
            }
            catch (ClassNotFoundException ex5) {
                throw new NoClassDefFoundError(ex5.getMessage());
            }
        }
        morpherRegistry.registerMorpher(new ObjectArrayMorpher(new NumberMorpher(class$5, new Float(0.0f))));
        Class class$6;
        if ((class$6 = MorphUtils.class$5) == null) {
            try {
                class$6 = (MorphUtils.class$5 = Class.forName("java.lang.Double"));
            }
            catch (ClassNotFoundException ex6) {
                throw new NoClassDefFoundError(ex6.getMessage());
            }
        }
        morpherRegistry.registerMorpher(new ObjectArrayMorpher(new NumberMorpher(class$6, new Double(0.0))));
        Class class$7;
        if ((class$7 = MorphUtils.class$6) == null) {
            try {
                class$7 = (MorphUtils.class$6 = Class.forName("java.math.BigInteger"));
            }
            catch (ClassNotFoundException ex7) {
                throw new NoClassDefFoundError(ex7.getMessage());
            }
        }
        morpherRegistry.registerMorpher(new ObjectArrayMorpher(new NumberMorpher(class$7, BigInteger.ZERO)));
        Class class$8;
        if ((class$8 = MorphUtils.class$7) == null) {
            try {
                class$8 = (MorphUtils.class$7 = Class.forName("java.math.BigDecimal"));
            }
            catch (ClassNotFoundException ex8) {
                throw new NoClassDefFoundError(ex8.getMessage());
            }
        }
        morpherRegistry.registerMorpher(new ObjectArrayMorpher(new NumberMorpher(class$8, MorphUtils.BIGDECIMAL_ZERO)));
        morpherRegistry.registerMorpher(new ObjectArrayMorpher(ClassMorpher.getInstance()));
    }
    
    public static void registerStandardObjectMorphers(final MorpherRegistry morpherRegistry) {
        morpherRegistry.registerMorpher(new BooleanObjectMorpher(Boolean.FALSE));
        morpherRegistry.registerMorpher(new CharacterObjectMorpher(new Character('\0')));
        morpherRegistry.registerMorpher(StringMorpher.getInstance());
        Class class$0;
        if ((class$0 = MorphUtils.class$0) == null) {
            try {
                class$0 = (MorphUtils.class$0 = Class.forName("java.lang.Byte"));
            }
            catch (ClassNotFoundException ex) {
                throw new NoClassDefFoundError(ex.getMessage());
            }
        }
        morpherRegistry.registerMorpher(new NumberMorpher(class$0, new Byte((byte)0)));
        Class class$2;
        if ((class$2 = MorphUtils.class$1) == null) {
            try {
                class$2 = (MorphUtils.class$1 = Class.forName("java.lang.Short"));
            }
            catch (ClassNotFoundException ex2) {
                throw new NoClassDefFoundError(ex2.getMessage());
            }
        }
        morpherRegistry.registerMorpher(new NumberMorpher(class$2, new Short((short)0)));
        Class class$3;
        if ((class$3 = MorphUtils.class$2) == null) {
            try {
                class$3 = (MorphUtils.class$2 = Class.forName("java.lang.Integer"));
            }
            catch (ClassNotFoundException ex3) {
                throw new NoClassDefFoundError(ex3.getMessage());
            }
        }
        morpherRegistry.registerMorpher(new NumberMorpher(class$3, new Integer(0)));
        Class class$4;
        if ((class$4 = MorphUtils.class$3) == null) {
            try {
                class$4 = (MorphUtils.class$3 = Class.forName("java.lang.Long"));
            }
            catch (ClassNotFoundException ex4) {
                throw new NoClassDefFoundError(ex4.getMessage());
            }
        }
        morpherRegistry.registerMorpher(new NumberMorpher(class$4, new Long(0L)));
        Class class$5;
        if ((class$5 = MorphUtils.class$4) == null) {
            try {
                class$5 = (MorphUtils.class$4 = Class.forName("java.lang.Float"));
            }
            catch (ClassNotFoundException ex5) {
                throw new NoClassDefFoundError(ex5.getMessage());
            }
        }
        morpherRegistry.registerMorpher(new NumberMorpher(class$5, new Float(0.0f)));
        Class class$6;
        if ((class$6 = MorphUtils.class$5) == null) {
            try {
                class$6 = (MorphUtils.class$5 = Class.forName("java.lang.Double"));
            }
            catch (ClassNotFoundException ex6) {
                throw new NoClassDefFoundError(ex6.getMessage());
            }
        }
        morpherRegistry.registerMorpher(new NumberMorpher(class$6, new Double(0.0)));
        Class class$7;
        if ((class$7 = MorphUtils.class$6) == null) {
            try {
                class$7 = (MorphUtils.class$6 = Class.forName("java.math.BigInteger"));
            }
            catch (ClassNotFoundException ex7) {
                throw new NoClassDefFoundError(ex7.getMessage());
            }
        }
        morpherRegistry.registerMorpher(new NumberMorpher(class$7, BigInteger.ZERO));
        Class class$8;
        if ((class$8 = MorphUtils.class$7) == null) {
            try {
                class$8 = (MorphUtils.class$7 = Class.forName("java.math.BigDecimal"));
            }
            catch (ClassNotFoundException ex8) {
                throw new NoClassDefFoundError(ex8.getMessage());
            }
        }
        morpherRegistry.registerMorpher(new NumberMorpher(class$8, MorphUtils.BIGDECIMAL_ZERO));
        morpherRegistry.registerMorpher(ClassMorpher.getInstance());
    }
    
    public static void registerStandardPrimitiveArrayMorphers(final MorpherRegistry morpherRegistry) {
        morpherRegistry.registerMorpher(new BooleanArrayMorpher(false));
        morpherRegistry.registerMorpher(new CharArrayMorpher('\0'));
        morpherRegistry.registerMorpher(new ByteArrayMorpher((byte)0));
        morpherRegistry.registerMorpher(new ShortArrayMorpher((short)0));
        morpherRegistry.registerMorpher(new IntArrayMorpher(0));
        morpherRegistry.registerMorpher(new LongArrayMorpher(0L));
        morpherRegistry.registerMorpher(new FloatArrayMorpher(0.0f));
        morpherRegistry.registerMorpher(new DoubleArrayMorpher(0.0));
    }
    
    public static void registerStandardPrimitiveMorphers(final MorpherRegistry morpherRegistry) {
        morpherRegistry.registerMorpher(new BooleanMorpher(false));
        morpherRegistry.registerMorpher(new CharMorpher('\0'));
        morpherRegistry.registerMorpher(new ByteMorpher((byte)0));
        morpherRegistry.registerMorpher(new ShortMorpher((short)0));
        morpherRegistry.registerMorpher(new IntMorpher(0));
        morpherRegistry.registerMorpher(new LongMorpher(0L));
        morpherRegistry.registerMorpher(new FloatMorpher(0.0f));
        morpherRegistry.registerMorpher(new DoubleMorpher(0.0));
    }
    
    private MorphUtils() {
    }
}
