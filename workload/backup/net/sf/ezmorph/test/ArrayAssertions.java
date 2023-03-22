// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.ezmorph.test;

import java.util.List;
import junit.framework.Assert;

public class ArrayAssertions extends Assert
{
    static /* synthetic */ Class class$0;
    static /* synthetic */ Class class$1;
    static /* synthetic */ Class class$2;
    static /* synthetic */ Class class$3;
    static /* synthetic */ Class class$4;
    static /* synthetic */ Class class$5;
    static /* synthetic */ Class class$6;
    static /* synthetic */ Class class$7;
    static /* synthetic */ Class class$8;
    
    public static void assertEquals(final boolean[] expecteds, final boolean[] actuals) {
        assertEquals(null, expecteds, actuals);
    }
    
    public static void assertEquals(final boolean[] expecteds, final Object[] actuals) {
        assertEquals(null, expecteds, actuals);
    }
    
    public static void assertEquals(final byte[] expecteds, final byte[] actuals) {
        assertEquals(null, expecteds, actuals);
    }
    
    public static void assertEquals(final byte[] expecteds, final Object[] actuals) {
        assertEquals(null, expecteds, actuals);
    }
    
    public static void assertEquals(final char[] expecteds, final char[] actuals) {
        assertEquals(null, expecteds, actuals);
    }
    
    public static void assertEquals(final char[] expecteds, final Object[] actuals) {
        assertEquals(null, expecteds, actuals);
    }
    
    public static void assertEquals(final double[] expecteds, final double[] actuals) {
        assertEquals(null, expecteds, actuals);
    }
    
    public static void assertEquals(final double[] expecteds, final Object[] actuals) {
        assertEquals(null, expecteds, actuals);
    }
    
    public static void assertEquals(final float[] expecteds, final float[] actuals) {
        assertEquals(null, expecteds, actuals);
    }
    
    public static void assertEquals(final float[] expecteds, final Object[] actuals) {
        assertEquals(null, expecteds, actuals);
    }
    
    public static void assertEquals(final int[] expecteds, final int[] actuals) {
        assertEquals(null, expecteds, actuals);
    }
    
    public static void assertEquals(final int[] expecteds, final Object[] actuals) {
        assertEquals(null, expecteds, actuals);
    }
    
    public static void assertEquals(final List expecteds, final List actuals) {
        assertEquals(null, expecteds, actuals);
    }
    
    public static void assertEquals(final long[] expecteds, final long[] actuals) {
        assertEquals(null, expecteds, actuals);
    }
    
    public static void assertEquals(final long[] expecteds, final Object[] actuals) {
        assertEquals(null, expecteds, actuals);
    }
    
    public static void assertEquals(final Object expected, final Object actual) {
        assertEquals(null, expected, actual);
    }
    
    public static void assertEquals(final Object[] expecteds, final boolean[] actuals) {
        assertEquals(null, expecteds, actuals);
    }
    
    public static void assertEquals(final Object[] expecteds, final byte[] actuals) {
        assertEquals(null, expecteds, actuals);
    }
    
    public static void assertEquals(final Object[] expecteds, final char[] actuals) {
        assertEquals(null, expecteds, actuals);
    }
    
    public static void assertEquals(final Object[] expecteds, final double[] actuals) {
        assertEquals(null, expecteds, actuals);
    }
    
    public static void assertEquals(final Object[] expecteds, final float[] actuals) {
        assertEquals(null, expecteds, actuals);
    }
    
    public static void assertEquals(final Object[] expecteds, final int[] actuals) {
        assertEquals(null, expecteds, actuals);
    }
    
    public static void assertEquals(final Object[] expecteds, final long[] actuals) {
        assertEquals(null, expecteds, actuals);
    }
    
    public static void assertEquals(final Object[] expecteds, final Object[] actuals) {
        assertEquals(null, expecteds, actuals);
    }
    
    public static void assertEquals(final Object[] expecteds, final short[] actuals) {
        assertEquals(null, expecteds, actuals);
    }
    
    public static void assertEquals(final short[] expecteds, final Object[] actuals) {
        assertEquals(null, expecteds, actuals);
    }
    
    public static void assertEquals(final short[] expecteds, final short[] actuals) {
        assertEquals(null, expecteds, actuals);
    }
    
    public static void assertEquals(final String message, final boolean[] expecteds, final boolean[] actuals) {
        if (expecteds == actuals) {
            return;
        }
        final String header = (message == null) ? "" : (String.valueOf(message) + ": ");
        if (expecteds == null) {
            Assert.fail(String.valueOf(header) + "expected array was null");
        }
        if (actuals == null) {
            Assert.fail(String.valueOf(header) + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            Assert.fail(String.valueOf(header) + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; ++i) {
            Assert.assertEquals(String.valueOf(header) + "arrays first differed at element [" + i + "];", expecteds[i], actuals[i]);
        }
    }
    
    public static void assertEquals(final String message, final boolean[] expecteds, final Object[] actuals) {
        final String header = (message == null) ? "" : (String.valueOf(message) + ": ");
        if (expecteds == null) {
            Assert.fail(String.valueOf(header) + "expected array was null");
        }
        if (actuals == null) {
            Assert.fail(String.valueOf(header) + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            Assert.fail(String.valueOf(header) + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; ++i) {
            assertEquals(String.valueOf(header) + "arrays first differed at element [" + i + "];", new Boolean(expecteds[i]), actuals[i]);
        }
    }
    
    public static void assertEquals(final String message, final byte[] expecteds, final byte[] actuals) {
        if (expecteds == actuals) {
            return;
        }
        final String header = (message == null) ? "" : (String.valueOf(message) + ": ");
        if (expecteds == null) {
            Assert.fail(String.valueOf(header) + "expected array was null");
        }
        if (actuals == null) {
            Assert.fail(String.valueOf(header) + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            Assert.fail(String.valueOf(header) + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; ++i) {
            Assert.assertEquals(String.valueOf(header) + "arrays first differed at element [" + i + "];", expecteds[i], actuals[i]);
        }
    }
    
    public static void assertEquals(final String message, final byte[] expecteds, final Object[] actuals) {
        final String header = (message == null) ? "" : (String.valueOf(message) + ": ");
        if (expecteds == null) {
            Assert.fail(String.valueOf(header) + "expected array was null");
        }
        if (actuals == null) {
            Assert.fail(String.valueOf(header) + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            Assert.fail(String.valueOf(header) + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; ++i) {
            assertEquals(String.valueOf(header) + "arrays first differed at element [" + i + "];", new Byte(expecteds[i]), actuals[i]);
        }
    }
    
    public static void assertEquals(final String message, final char[] expecteds, final char[] actuals) {
        if (expecteds == actuals) {
            return;
        }
        final String header = (message == null) ? "" : (String.valueOf(message) + ": ");
        if (expecteds == null) {
            Assert.fail(String.valueOf(header) + "expected array was null");
        }
        if (actuals == null) {
            Assert.fail(String.valueOf(header) + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            Assert.fail(String.valueOf(header) + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; ++i) {
            Assert.assertEquals(String.valueOf(header) + "arrays first differed at element [" + i + "];", expecteds[i], actuals[i]);
        }
    }
    
    public static void assertEquals(final String message, final char[] expecteds, final Object[] actuals) {
        final String header = (message == null) ? "" : (String.valueOf(message) + ": ");
        if (expecteds == null) {
            Assert.fail(String.valueOf(header) + "expected array was null");
        }
        if (actuals == null) {
            Assert.fail(String.valueOf(header) + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            Assert.fail(String.valueOf(header) + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; ++i) {
            assertEquals(String.valueOf(header) + "arrays first differed at element [" + i + "];", new Character(expecteds[i]), actuals[i]);
        }
    }
    
    public static void assertEquals(final String message, final double[] expecteds, final double[] actuals) {
        if (expecteds == actuals) {
            return;
        }
        final String header = (message == null) ? "" : (String.valueOf(message) + ": ");
        if (expecteds == null) {
            Assert.fail(String.valueOf(header) + "expected array was null");
        }
        if (actuals == null) {
            Assert.fail(String.valueOf(header) + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            Assert.fail(String.valueOf(header) + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; ++i) {
            Assert.assertEquals(String.valueOf(header) + "arrays first differed at element [" + i + "];", expecteds[i], actuals[i], 0.0);
        }
    }
    
    public static void assertEquals(final String message, final double[] expecteds, final Object[] actuals) {
        final String header = (message == null) ? "" : (String.valueOf(message) + ": ");
        if (expecteds == null) {
            Assert.fail(String.valueOf(header) + "expected array was null");
        }
        if (actuals == null) {
            Assert.fail(String.valueOf(header) + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            Assert.fail(String.valueOf(header) + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; ++i) {
            assertEquals(String.valueOf(header) + "arrays first differed at element [" + i + "];", new Double(expecteds[i]), actuals[i]);
        }
    }
    
    public static void assertEquals(final String message, final float[] expecteds, final float[] actuals) {
        if (expecteds == actuals) {
            return;
        }
        final String header = (message == null) ? "" : (String.valueOf(message) + ": ");
        if (expecteds == null) {
            Assert.fail(String.valueOf(header) + "expected array was null");
        }
        if (actuals == null) {
            Assert.fail(String.valueOf(header) + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            Assert.fail(String.valueOf(header) + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; ++i) {
            Assert.assertEquals(String.valueOf(header) + "arrays first differed at element [" + i + "];", expecteds[i], actuals[i], 0.0f);
        }
    }
    
    public static void assertEquals(final String message, final float[] expecteds, final Object[] actuals) {
        final String header = (message == null) ? "" : (String.valueOf(message) + ": ");
        if (expecteds == null) {
            Assert.fail(String.valueOf(header) + "expected array was null");
        }
        if (actuals == null) {
            Assert.fail(String.valueOf(header) + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            Assert.fail(String.valueOf(header) + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; ++i) {
            assertEquals(String.valueOf(header) + "arrays first differed at element [" + i + "];", new Float(expecteds[i]), actuals[i]);
        }
    }
    
    public static void assertEquals(final String message, final int[] expecteds, final int[] actuals) {
        if (expecteds == actuals) {
            return;
        }
        final String header = (message == null) ? "" : (String.valueOf(message) + ": ");
        if (expecteds == null) {
            Assert.fail(String.valueOf(header) + "expected array was null");
        }
        if (actuals == null) {
            Assert.fail(String.valueOf(header) + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            Assert.fail(String.valueOf(header) + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; ++i) {
            Assert.assertEquals(String.valueOf(header) + "arrays first differed at element [" + i + "];", expecteds[i], actuals[i]);
        }
    }
    
    public static void assertEquals(final String message, final int[] expecteds, final Object[] actuals) {
        final String header = (message == null) ? "" : (String.valueOf(message) + ": ");
        if (expecteds == null) {
            Assert.fail(String.valueOf(header) + "expected array was null");
        }
        if (actuals == null) {
            Assert.fail(String.valueOf(header) + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            Assert.fail(String.valueOf(header) + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; ++i) {
            assertEquals(String.valueOf(header) + "arrays first differed at element [" + i + "];", new Integer(expecteds[i]), actuals[i]);
        }
    }
    
    public static void assertEquals(final String message, final List expecteds, final List actuals) {
        final String header = (message == null) ? "" : (String.valueOf(message) + ": ");
        if (expecteds == null) {
            Assert.fail(String.valueOf(header) + "expected list was null");
        }
        if (actuals == null) {
            Assert.fail(String.valueOf(header) + "actual list was null");
        }
        if (expecteds == actuals || expecteds.equals(actuals)) {
            return;
        }
        if (actuals.size() != expecteds.size()) {
            Assert.fail(String.valueOf(header) + "list sizes differed, expected.size()=" + expecteds.size() + " actual.size()=" + actuals.size());
        }
        for (int max = expecteds.size(), i = 0; i < max; ++i) {
            final Object o1 = expecteds.get(i);
            final Object o2 = actuals.get(i);
            if (o1 == null) {
                if (o2 == null) {
                    return;
                }
                Assert.fail(String.valueOf(header) + "lists first differed at element [" + i + "];");
            }
            else if (o2 == null) {
                Assert.fail(String.valueOf(header) + "lists first differed at element [" + i + "];");
            }
            if (o1.getClass().isArray() && o2.getClass().isArray()) {
                final Object[] expected = (Object[])o1;
                final Object[] actual = (Object[])o2;
                assertEquals(String.valueOf(header) + "lists first differed at element " + i + ";", expected, actual);
            }
            else {
                Class class$0;
                if ((class$0 = ArrayAssertions.class$0) == null) {
                    try {
                        class$0 = (ArrayAssertions.class$0 = Class.forName("java.util.List"));
                    }
                    catch (ClassNotFoundException ex) {
                        throw new NoClassDefFoundError(ex.getMessage());
                    }
                }
                if (class$0.isAssignableFrom(o1.getClass())) {
                    Class class$2;
                    if ((class$2 = ArrayAssertions.class$0) == null) {
                        try {
                            class$2 = (ArrayAssertions.class$0 = Class.forName("java.util.List"));
                        }
                        catch (ClassNotFoundException ex2) {
                            throw new NoClassDefFoundError(ex2.getMessage());
                        }
                    }
                    if (class$2.isAssignableFrom(o2.getClass())) {
                        assertEquals(String.valueOf(header) + "lists first differed at element [" + i + "];", (List)o1, (List)o2);
                        continue;
                    }
                }
                assertEquals(String.valueOf(header) + "lists first differed at element [" + i + "];", o1, o2);
            }
        }
    }
    
    public static void assertEquals(final String message, final long[] expecteds, final long[] actuals) {
        if (expecteds == actuals) {
            return;
        }
        final String header = (message == null) ? "" : (String.valueOf(message) + ": ");
        if (expecteds == null) {
            Assert.fail(String.valueOf(header) + "expected array was null");
        }
        if (actuals == null) {
            Assert.fail(String.valueOf(header) + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            Assert.fail(String.valueOf(header) + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; ++i) {
            Assert.assertEquals(String.valueOf(header) + "arrays first differed at element [" + i + "];", expecteds[i], actuals[i]);
        }
    }
    
    public static void assertEquals(final String message, final long[] expecteds, final Object[] actuals) {
        final String header = (message == null) ? "" : (String.valueOf(message) + ": ");
        if (expecteds == null) {
            Assert.fail(String.valueOf(header) + "expected array was null");
        }
        if (actuals == null) {
            Assert.fail(String.valueOf(header) + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            Assert.fail(String.valueOf(header) + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; ++i) {
            assertEquals(String.valueOf(header) + "arrays first differed at element [" + i + "];", new Long(expecteds[i]), actuals[i]);
        }
    }
    
    public static void assertEquals(final String message, final Object expected, final Object actual) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected != null && expected.equals(actual)) {
            return;
        }
        final Class expectedClass = expected.getClass();
        final Class actualClass = actual.getClass();
        if (expectedClass.isArray() && actualClass.isArray()) {
            final Class expectedInnerType = expectedClass.getComponentType();
            final Class actualInnerType = actualClass.getComponentType();
            if (expectedInnerType.isPrimitive()) {
                assertExpectedPrimitiveArrays(message, expected, actual, expectedInnerType, actualInnerType);
            }
            else if (actualInnerType.isPrimitive()) {
                assertActualPrimitiveArrays(message, expected, actual, expectedInnerType, actualInnerType);
            }
            else {
                assertEquals(message, (Object[])expected, (Object[])actual);
            }
        }
        else {
            Assert.failNotEquals(message, expected, actual);
        }
    }
    
    public static void assertEquals(final String message, final Object[] expecteds, final boolean[] actuals) {
        final String header = (message == null) ? "" : (String.valueOf(message) + ": ");
        if (expecteds == null) {
            Assert.fail(String.valueOf(header) + "expected array was null");
        }
        if (actuals == null) {
            Assert.fail(String.valueOf(header) + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            Assert.fail(String.valueOf(header) + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; ++i) {
            assertEquals(String.valueOf(header) + "arrays first differed at element [" + i + "];", expecteds[i], new Boolean(actuals[i]));
        }
    }
    
    public static void assertEquals(final String message, final Object[] expecteds, final byte[] actuals) {
        final String header = (message == null) ? "" : (String.valueOf(message) + ": ");
        if (expecteds == null) {
            Assert.fail(String.valueOf(header) + "expected array was null");
        }
        if (actuals == null) {
            Assert.fail(String.valueOf(header) + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            Assert.fail(String.valueOf(header) + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; ++i) {
            assertEquals(String.valueOf(header) + "arrays first differed at element [" + i + "];", expecteds[i], new Byte(actuals[i]));
        }
    }
    
    public static void assertEquals(final String message, final Object[] expecteds, final char[] actuals) {
        final String header = (message == null) ? "" : (String.valueOf(message) + ": ");
        if (expecteds == null) {
            Assert.fail(String.valueOf(header) + "expected array was null");
        }
        if (actuals == null) {
            Assert.fail(String.valueOf(header) + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            Assert.fail(String.valueOf(header) + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; ++i) {
            assertEquals(String.valueOf(header) + "arrays first differed at element [" + i + "];", expecteds[i], new Character(actuals[i]));
        }
    }
    
    public static void assertEquals(final String message, final Object[] expecteds, final double[] actuals) {
        final String header = (message == null) ? "" : (String.valueOf(message) + ": ");
        if (expecteds == null) {
            Assert.fail(String.valueOf(header) + "expected array was null");
        }
        if (actuals == null) {
            Assert.fail(String.valueOf(header) + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            Assert.fail(String.valueOf(header) + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; ++i) {
            assertEquals(String.valueOf(header) + "arrays first differed at element [" + i + "];", expecteds[i], new Double(actuals[i]));
        }
    }
    
    public static void assertEquals(final String message, final Object[] expecteds, final float[] actuals) {
        final String header = (message == null) ? "" : (String.valueOf(message) + ": ");
        if (expecteds == null) {
            Assert.fail(String.valueOf(header) + "expected array was null");
        }
        if (actuals == null) {
            Assert.fail(String.valueOf(header) + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            Assert.fail(String.valueOf(header) + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; ++i) {
            assertEquals(String.valueOf(header) + "arrays first differed at element [" + i + "];", expecteds[i], new Float(actuals[i]));
        }
    }
    
    public static void assertEquals(final String message, final Object[] expecteds, final int[] actuals) {
        final String header = (message == null) ? "" : (String.valueOf(message) + ": ");
        if (expecteds == null) {
            Assert.fail(String.valueOf(header) + "expected array was null");
        }
        if (actuals == null) {
            Assert.fail(String.valueOf(header) + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            Assert.fail(String.valueOf(header) + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; ++i) {
            assertEquals(String.valueOf(header) + "arrays first differed at element [" + i + "];", expecteds[i], new Integer(actuals[i]));
        }
    }
    
    public static void assertEquals(final String message, final Object[] expecteds, final long[] actuals) {
        final String header = (message == null) ? "" : (String.valueOf(message) + ": ");
        if (expecteds == null) {
            Assert.fail(String.valueOf(header) + "expected array was null");
        }
        if (actuals == null) {
            Assert.fail(String.valueOf(header) + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            Assert.fail(String.valueOf(header) + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; ++i) {
            assertEquals(String.valueOf(header) + "arrays first differed at element [" + i + "];", expecteds[i], new Long(actuals[i]));
        }
    }
    
    public static void assertEquals(final String message, final Object[] expecteds, final Object[] actuals) {
        if (expecteds == actuals) {
            return;
        }
        final String header = (message == null) ? "" : (String.valueOf(message) + ": ");
        if (expecteds == null) {
            Assert.fail(String.valueOf(header) + "expected array was null");
        }
        if (actuals == null) {
            Assert.fail(String.valueOf(header) + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            Assert.fail(String.valueOf(header) + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; ++i) {
            final Object o1 = expecteds[i];
            final Object o2 = actuals[i];
            if (o1 == null) {
                if (o2 == null) {
                    return;
                }
                Assert.fail(String.valueOf(header) + "arrays first differed at element [" + i + "];");
            }
            else if (o2 == null) {
                Assert.fail(String.valueOf(header) + "arrays first differed at element [" + i + "];");
            }
            if (o1.getClass().isArray() && o2.getClass().isArray()) {
                final Class type1 = o1.getClass().getComponentType();
                final Class type2 = o2.getClass().getComponentType();
                if (type1.isPrimitive()) {
                    if (type1 == Boolean.TYPE) {
                        if (type2 == Boolean.TYPE) {
                            assertEquals(String.valueOf(header) + "arrays first differed at element " + i + ";", (boolean[])o1, (boolean[])o2);
                        }
                        else {
                            assertEquals(String.valueOf(header) + "arrays first differed at element " + i + ";", (boolean[])o1, (Object[])o2);
                        }
                    }
                    else if (type1 == Byte.TYPE) {
                        if (type2 == Byte.TYPE) {
                            assertEquals(String.valueOf(header) + "arrays first differed at element " + i + ";", (byte[])o1, (byte[])o2);
                        }
                        else {
                            assertEquals(String.valueOf(header) + "arrays first differed at element " + i + ";", (byte[])o1, (Object[])o2);
                        }
                    }
                    else if (type1 == Short.TYPE) {
                        if (type2 == Short.TYPE) {
                            assertEquals(String.valueOf(header) + "arrays first differed at element " + i + ";", (short[])o1, (short[])o2);
                        }
                        else {
                            assertEquals(String.valueOf(header) + "arrays first differed at element " + i + ";", (short[])o1, (Object[])o2);
                        }
                    }
                    else if (type1 == Integer.TYPE) {
                        if (type2 == Integer.TYPE) {
                            assertEquals(String.valueOf(header) + "arrays first differed at element " + i + ";", (int[])o1, (int[])o2);
                        }
                        else {
                            assertEquals(String.valueOf(header) + "arrays first differed at element " + i + ";", (int[])o1, (Object[])o2);
                        }
                    }
                    else if (type1 == Long.TYPE) {
                        if (type2 == Long.TYPE) {
                            assertEquals(String.valueOf(header) + "arrays first differed at element " + i + ";", (long[])o1, (long[])o2);
                        }
                        else {
                            assertEquals(String.valueOf(header) + "arrays first differed at element " + i + ";", (long[])o1, (Object[])o2);
                        }
                    }
                    else if (type1 == Float.TYPE) {
                        if (type2 == Float.TYPE) {
                            assertEquals(String.valueOf(header) + "arrays first differed at element " + i + ";", (float[])o1, (float[])o2);
                        }
                        else {
                            assertEquals(String.valueOf(header) + "arrays first differed at element " + i + ";", (float[])o1, (Object[])o2);
                        }
                    }
                    else if (type1 == Double.TYPE) {
                        if (type2 == Double.TYPE) {
                            assertEquals(String.valueOf(header) + "arrays first differed at element " + i + ";", (double[])o1, (double[])o2);
                        }
                        else {
                            assertEquals(String.valueOf(header) + "arrays first differed at element " + i + ";", (double[])o1, (Object[])o2);
                        }
                    }
                    else if (type1 == Character.TYPE) {
                        if (type2 == Character.TYPE) {
                            assertEquals(String.valueOf(header) + "arrays first differed at element " + i + ";", (char[])o1, (char[])o2);
                        }
                        else {
                            assertEquals(String.valueOf(header) + "arrays first differed at element " + i + ";", (char[])o1, (Object[])o2);
                        }
                    }
                }
                else if (type2.isPrimitive()) {
                    if (type2 == Boolean.TYPE) {
                        assertEquals(String.valueOf(header) + "arrays first differed at element " + i + ";", (Object[])o1, (boolean[])o2);
                    }
                    else if (type2 == Byte.TYPE) {
                        assertEquals(String.valueOf(header) + "arrays first differed at element " + i + ";", (Object[])o1, (byte[])o2);
                    }
                    else if (type2 == Short.TYPE) {
                        assertEquals(String.valueOf(header) + "arrays first differed at element " + i + ";", (Object[])o1, (short[])o2);
                    }
                    else if (type2 == Integer.TYPE) {
                        assertEquals(String.valueOf(header) + "arrays first differed at element " + i + ";", (Object[])o1, (int[])o2);
                    }
                    else if (type2 == Long.TYPE) {
                        assertEquals(String.valueOf(header) + "arrays first differed at element " + i + ";", (Object[])o1, (long[])o2);
                    }
                    else if (type2 == Float.TYPE) {
                        assertEquals(String.valueOf(header) + "arrays first differed at element " + i + ";", (Object[])o1, (float[])o2);
                    }
                    else if (type2 == Double.TYPE) {
                        assertEquals(String.valueOf(header) + "arrays first differed at element " + i + ";", (Object[])o1, (double[])o2);
                    }
                    else if (type2 == Character.TYPE) {
                        assertEquals(String.valueOf(header) + "arrays first differed at element " + i + ";", (Object[])o1, (char[])o2);
                    }
                }
                else {
                    final Object[] expected = (Object[])o1;
                    final Object[] actual = (Object[])o2;
                    assertEquals(String.valueOf(header) + "arrays first differed at element " + i + ";", expected, actual);
                }
            }
            else {
                assertEquals(String.valueOf(header) + "arrays first differed at element [" + i + "];", o1, o2);
            }
        }
    }
    
    public static void assertEquals(final String message, final Object[] expecteds, final short[] actuals) {
        final String header = (message == null) ? "" : (String.valueOf(message) + ": ");
        if (expecteds == null) {
            Assert.fail(String.valueOf(header) + "expected array was null");
        }
        if (actuals == null) {
            Assert.fail(String.valueOf(header) + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            Assert.fail(String.valueOf(header) + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; ++i) {
            assertEquals(String.valueOf(header) + "arrays first differed at element [" + i + "];", expecteds[i], new Short(actuals[i]));
        }
    }
    
    public static void assertEquals(final String message, final short[] expecteds, final Object[] actuals) {
        final String header = (message == null) ? "" : (String.valueOf(message) + ": ");
        if (expecteds == null) {
            Assert.fail(String.valueOf(header) + "expected array was null");
        }
        if (actuals == null) {
            Assert.fail(String.valueOf(header) + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            Assert.fail(String.valueOf(header) + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; ++i) {
            assertEquals(String.valueOf(header) + "arrays first differed at element [" + i + "];", new Short(expecteds[i]), actuals[i]);
        }
    }
    
    public static void assertEquals(final String message, final short[] expecteds, final short[] actuals) {
        if (expecteds == actuals) {
            return;
        }
        final String header = (message == null) ? "" : (String.valueOf(message) + ": ");
        if (expecteds == null) {
            Assert.fail(String.valueOf(header) + "expected array was null");
        }
        if (actuals == null) {
            Assert.fail(String.valueOf(header) + "actual array was null");
        }
        if (actuals.length != expecteds.length) {
            Assert.fail(String.valueOf(header) + "array lengths differed, expected.length=" + expecteds.length + " actual.length=" + actuals.length);
        }
        for (int i = 0; i < expecteds.length; ++i) {
            Assert.assertEquals(String.valueOf(header) + "arrays first differed at element [" + i + "];", expecteds[i], actuals[i]);
        }
    }
    
    private static void assertActualPrimitiveArrays(final String message, final Object expected, final Object actual, final Class expectedInnerType, final Class actualInnerType) {
        if (Boolean.TYPE.isAssignableFrom(actualInnerType)) {
            Class class$1;
            if ((class$1 = ArrayAssertions.class$1) == null) {
                try {
                    class$1 = (ArrayAssertions.class$1 = Class.forName("java.lang.Boolean"));
                }
                catch (ClassNotFoundException ex) {
                    throw new NoClassDefFoundError(ex.getMessage());
                }
            }
            if (class$1.isAssignableFrom(expectedInnerType)) {
                assertEquals(message, (Object[])expected, (boolean[])actual);
            }
            else {
                assertEquals(message, (Object[])expected, (boolean[])actual);
            }
        }
        else if (Byte.TYPE.isAssignableFrom(actualInnerType)) {
            Class class$2;
            if ((class$2 = ArrayAssertions.class$2) == null) {
                try {
                    class$2 = (ArrayAssertions.class$2 = Class.forName("java.lang.Byte"));
                }
                catch (ClassNotFoundException ex2) {
                    throw new NoClassDefFoundError(ex2.getMessage());
                }
            }
            if (class$2.isAssignableFrom(expectedInnerType)) {
                assertEquals(message, (Object[])expected, (byte[])actual);
            }
            else {
                assertEquals(message, (Object[])expected, (byte[])actual);
            }
        }
        else if (Short.TYPE.isAssignableFrom(actualInnerType)) {
            Class class$3;
            if ((class$3 = ArrayAssertions.class$3) == null) {
                try {
                    class$3 = (ArrayAssertions.class$3 = Class.forName("java.lang.Short"));
                }
                catch (ClassNotFoundException ex3) {
                    throw new NoClassDefFoundError(ex3.getMessage());
                }
            }
            if (class$3.isAssignableFrom(expectedInnerType)) {
                assertEquals(message, (Object[])expected, (short[])actual);
            }
            else {
                assertEquals(message, (Object[])expected, (short[])actual);
            }
        }
        else if (Integer.TYPE.isAssignableFrom(actualInnerType)) {
            Class class$4;
            if ((class$4 = ArrayAssertions.class$4) == null) {
                try {
                    class$4 = (ArrayAssertions.class$4 = Class.forName("java.lang.Integer"));
                }
                catch (ClassNotFoundException ex4) {
                    throw new NoClassDefFoundError(ex4.getMessage());
                }
            }
            if (class$4.isAssignableFrom(expectedInnerType)) {
                assertEquals(message, (Object[])expected, (int[])actual);
            }
            else {
                assertEquals(message, (Object[])expected, (int[])actual);
            }
        }
        else if (Long.TYPE.isAssignableFrom(actualInnerType)) {
            Class class$5;
            if ((class$5 = ArrayAssertions.class$5) == null) {
                try {
                    class$5 = (ArrayAssertions.class$5 = Class.forName("java.lang.Long"));
                }
                catch (ClassNotFoundException ex5) {
                    throw new NoClassDefFoundError(ex5.getMessage());
                }
            }
            if (class$5.isAssignableFrom(expectedInnerType)) {
                assertEquals(message, (Object[])expected, (long[])actual);
            }
            else {
                assertEquals(message, (Object[])expected, (long[])actual);
            }
        }
        else if (Float.TYPE.isAssignableFrom(actualInnerType)) {
            Class class$6;
            if ((class$6 = ArrayAssertions.class$6) == null) {
                try {
                    class$6 = (ArrayAssertions.class$6 = Class.forName("java.lang.Float"));
                }
                catch (ClassNotFoundException ex6) {
                    throw new NoClassDefFoundError(ex6.getMessage());
                }
            }
            if (class$6.isAssignableFrom(expectedInnerType)) {
                assertEquals(message, (Object[])expected, (float[])actual);
            }
            else {
                assertEquals(message, (Object[])expected, (float[])actual);
            }
        }
        else if (Double.TYPE.isAssignableFrom(actualInnerType)) {
            Class class$7;
            if ((class$7 = ArrayAssertions.class$7) == null) {
                try {
                    class$7 = (ArrayAssertions.class$7 = Class.forName("java.lang.Double"));
                }
                catch (ClassNotFoundException ex7) {
                    throw new NoClassDefFoundError(ex7.getMessage());
                }
            }
            if (class$7.isAssignableFrom(expectedInnerType)) {
                assertEquals(message, (Object[])expected, (double[])actual);
            }
            else {
                assertEquals(message, (Object[])expected, (double[])actual);
            }
        }
        else if (Character.TYPE.isAssignableFrom(actualInnerType)) {
            Class class$8;
            if ((class$8 = ArrayAssertions.class$8) == null) {
                try {
                    class$8 = (ArrayAssertions.class$8 = Class.forName("java.lang.Character"));
                }
                catch (ClassNotFoundException ex8) {
                    throw new NoClassDefFoundError(ex8.getMessage());
                }
            }
            if (class$8.isAssignableFrom(expectedInnerType)) {
                assertEquals(message, (Object[])expected, (char[])actual);
            }
            else {
                assertEquals(message, (Object[])expected, (char[])actual);
            }
        }
    }
    
    private static void assertExpectedPrimitiveArrays(final String message, final Object expected, final Object actual, final Class expectedInnerType, final Class actualInnerType) {
        if (Boolean.TYPE.isAssignableFrom(expectedInnerType)) {
            if (Boolean.TYPE.isAssignableFrom(actualInnerType)) {
                assertEquals(message, (boolean[])expected, (boolean[])actual);
            }
            else {
                Class class$1;
                if ((class$1 = ArrayAssertions.class$1) == null) {
                    try {
                        class$1 = (ArrayAssertions.class$1 = Class.forName("java.lang.Boolean"));
                    }
                    catch (ClassNotFoundException ex) {
                        throw new NoClassDefFoundError(ex.getMessage());
                    }
                }
                if (class$1.isAssignableFrom(actualInnerType)) {
                    assertEquals(message, (boolean[])expected, (Object[])actual);
                }
                else if (!actualInnerType.isPrimitive()) {
                    assertEquals(message, (boolean[])expected, (Object[])actual);
                }
                else {
                    Assert.failNotEquals(message, expected, actual);
                }
            }
        }
        else if (Byte.TYPE.isAssignableFrom(expectedInnerType)) {
            if (Byte.TYPE.isAssignableFrom(actualInnerType)) {
                assertEquals(message, (byte[])expected, (byte[])actual);
            }
            else {
                Class class$2;
                if ((class$2 = ArrayAssertions.class$2) == null) {
                    try {
                        class$2 = (ArrayAssertions.class$2 = Class.forName("java.lang.Byte"));
                    }
                    catch (ClassNotFoundException ex2) {
                        throw new NoClassDefFoundError(ex2.getMessage());
                    }
                }
                if (class$2.isAssignableFrom(actualInnerType)) {
                    assertEquals(message, (byte[])expected, (Object[])actual);
                }
                else if (!actualInnerType.isPrimitive()) {
                    assertEquals(message, (byte[])expected, (Object[])actual);
                }
                else {
                    Assert.failNotEquals(message, expected, actual);
                }
            }
        }
        else if (Short.TYPE.isAssignableFrom(expectedInnerType)) {
            if (Short.TYPE.isAssignableFrom(actualInnerType)) {
                assertEquals(message, (short[])expected, (short[])actual);
            }
            else {
                Class class$3;
                if ((class$3 = ArrayAssertions.class$3) == null) {
                    try {
                        class$3 = (ArrayAssertions.class$3 = Class.forName("java.lang.Short"));
                    }
                    catch (ClassNotFoundException ex3) {
                        throw new NoClassDefFoundError(ex3.getMessage());
                    }
                }
                if (class$3.isAssignableFrom(actualInnerType)) {
                    assertEquals(message, (short[])expected, (Object[])actual);
                }
                else if (!actualInnerType.isPrimitive()) {
                    assertEquals(message, (short[])expected, (Object[])actual);
                }
                else {
                    Assert.failNotEquals(message, expected, actual);
                }
            }
        }
        else if (Integer.TYPE.isAssignableFrom(expectedInnerType)) {
            if (Integer.TYPE.isAssignableFrom(actualInnerType)) {
                assertEquals(message, (int[])expected, (int[])actual);
            }
            else {
                Class class$4;
                if ((class$4 = ArrayAssertions.class$4) == null) {
                    try {
                        class$4 = (ArrayAssertions.class$4 = Class.forName("java.lang.Integer"));
                    }
                    catch (ClassNotFoundException ex4) {
                        throw new NoClassDefFoundError(ex4.getMessage());
                    }
                }
                if (class$4.isAssignableFrom(actualInnerType)) {
                    assertEquals(message, (int[])expected, (Object[])actual);
                }
                else if (!actualInnerType.isPrimitive()) {
                    assertEquals(message, (int[])expected, (Object[])actual);
                }
                else {
                    Assert.failNotEquals(message, expected, actual);
                }
            }
        }
        else if (Long.TYPE.isAssignableFrom(expectedInnerType)) {
            if (Long.TYPE.isAssignableFrom(actualInnerType)) {
                assertEquals(message, (long[])expected, (long[])actual);
            }
            else {
                Class class$5;
                if ((class$5 = ArrayAssertions.class$5) == null) {
                    try {
                        class$5 = (ArrayAssertions.class$5 = Class.forName("java.lang.Long"));
                    }
                    catch (ClassNotFoundException ex5) {
                        throw new NoClassDefFoundError(ex5.getMessage());
                    }
                }
                if (class$5.isAssignableFrom(actualInnerType)) {
                    assertEquals(message, (long[])expected, (Object[])actual);
                }
                else if (!actualInnerType.isPrimitive()) {
                    assertEquals(message, (long[])expected, (Object[])actual);
                }
                else {
                    Assert.failNotEquals(message, expected, actual);
                }
            }
        }
        else if (Float.TYPE.isAssignableFrom(expectedInnerType)) {
            if (Float.TYPE.isAssignableFrom(actualInnerType)) {
                assertEquals(message, (float[])expected, (float[])actual);
            }
            else {
                Class class$6;
                if ((class$6 = ArrayAssertions.class$6) == null) {
                    try {
                        class$6 = (ArrayAssertions.class$6 = Class.forName("java.lang.Float"));
                    }
                    catch (ClassNotFoundException ex6) {
                        throw new NoClassDefFoundError(ex6.getMessage());
                    }
                }
                if (class$6.isAssignableFrom(actualInnerType)) {
                    assertEquals(message, (float[])expected, (Object[])actual);
                }
                else if (!actualInnerType.isPrimitive()) {
                    assertEquals(message, (float[])expected, (Object[])actual);
                }
                else {
                    Assert.failNotEquals(message, expected, actual);
                }
            }
        }
        else if (Double.TYPE.isAssignableFrom(expectedInnerType)) {
            if (Double.TYPE.isAssignableFrom(actualInnerType)) {
                assertEquals(message, (double[])expected, (double[])actual);
            }
            else {
                Class class$7;
                if ((class$7 = ArrayAssertions.class$7) == null) {
                    try {
                        class$7 = (ArrayAssertions.class$7 = Class.forName("java.lang.Double"));
                    }
                    catch (ClassNotFoundException ex7) {
                        throw new NoClassDefFoundError(ex7.getMessage());
                    }
                }
                if (class$7.isAssignableFrom(actualInnerType)) {
                    assertEquals(message, (double[])expected, (Object[])actual);
                }
                else if (!actualInnerType.isPrimitive()) {
                    assertEquals(message, (double[])expected, (Object[])actual);
                }
                else {
                    Assert.failNotEquals(message, expected, actual);
                }
            }
        }
        else if (Character.TYPE.isAssignableFrom(expectedInnerType)) {
            if (Character.TYPE.isAssignableFrom(actualInnerType)) {
                assertEquals(message, (char[])expected, (char[])actual);
            }
            else {
                Class class$8;
                if ((class$8 = ArrayAssertions.class$8) == null) {
                    try {
                        class$8 = (ArrayAssertions.class$8 = Class.forName("java.lang.Character"));
                    }
                    catch (ClassNotFoundException ex8) {
                        throw new NoClassDefFoundError(ex8.getMessage());
                    }
                }
                if (class$8.isAssignableFrom(actualInnerType)) {
                    assertEquals(message, (char[])expected, (Object[])actual);
                }
                else if (!actualInnerType.isPrimitive()) {
                    assertEquals(message, (char[])expected, (Object[])actual);
                }
                else {
                    Assert.failNotEquals(message, expected, actual);
                }
            }
        }
    }
    
    private ArrayAssertions() {
    }
}
