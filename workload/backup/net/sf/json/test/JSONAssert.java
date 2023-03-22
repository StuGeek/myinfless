// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json.test;

import net.sf.json.JSONSerializer;
import java.util.Iterator;
import net.sf.json.JSONException;
import net.sf.ezmorph.Morpher;
import net.sf.ezmorph.object.IdentityObjectMorpher;
import net.sf.json.util.JSONUtils;
import net.sf.json.JSONObject;
import net.sf.json.JSONNull;
import net.sf.json.JSONFunction;
import net.sf.json.JSONArray;
import net.sf.json.JSON;
import junit.framework.Assert;

public class JSONAssert extends Assert
{
    public static void assertEquals(final JSON expected, final JSON actual) {
        assertEquals(null, expected, actual);
    }
    
    public static void assertEquals(final JSONArray expected, final JSONArray actual) {
        assertEquals(null, expected, actual);
    }
    
    public static void assertEquals(final JSONArray expected, final String actual) {
        assertEquals(null, expected, actual);
    }
    
    public static void assertEquals(final JSONFunction expected, final String actual) {
        assertEquals(null, expected, actual);
    }
    
    public static void assertEquals(final JSONNull expected, final String actual) {
        assertEquals(null, expected, actual);
    }
    
    public static void assertEquals(final JSONObject expected, final JSONObject actual) {
        assertEquals(null, expected, actual);
    }
    
    public static void assertEquals(final JSONObject expected, final String actual) {
        assertEquals(null, expected, actual);
    }
    
    public static void assertEquals(final String message, final JSON expected, final JSON actual) {
        final String header = (message == null) ? "" : (message + ": ");
        if (expected == null) {
            Assert.fail(header + "expected was null");
        }
        if (actual == null) {
            Assert.fail(header + "actual was null");
        }
        if (expected == actual || expected.equals(actual)) {
            return;
        }
        if (expected instanceof JSONArray) {
            if (actual instanceof JSONArray) {
                assertEquals(header, (JSONArray)expected, (JSONArray)actual);
            }
            else {
                Assert.fail(header + "actual is not a JSONArray");
            }
        }
        else if (expected instanceof JSONObject) {
            if (actual instanceof JSONObject) {
                assertEquals(header, (JSONObject)expected, (JSONObject)actual);
            }
            else {
                Assert.fail(header + "actual is not a JSONObject");
            }
        }
        else if (expected instanceof JSONNull) {
            if (actual instanceof JSONNull) {
                return;
            }
            Assert.fail(header + "actual is not a JSONNull");
        }
    }
    
    public static void assertEquals(final String expected, final JSONArray actual) {
        assertEquals(null, expected, actual);
    }
    
    public static void assertEquals(final String message, final JSONArray expected, final JSONArray actual) {
        final String header = (message == null) ? "" : (message + ": ");
        if (expected == null) {
            Assert.fail(header + "expected array was null");
        }
        if (actual == null) {
            Assert.fail(header + "actual array was null");
        }
        if (expected == actual || expected.equals(actual)) {
            return;
        }
        if (actual.size() != expected.size()) {
            Assert.fail(header + "arrays sizes differed, expected.length()=" + expected.size() + " actual.length()=" + actual.size());
        }
        for (int max = expected.size(), i = 0; i < max; ++i) {
            final Object o1 = expected.get(i);
            final Object o2 = actual.get(i);
            if (JSONNull.getInstance().equals(o1)) {
                if (JSONNull.getInstance().equals(o2)) {
                    continue;
                }
                Assert.fail(header + "arrays first differed at element [" + i + "];");
            }
            else if (JSONNull.getInstance().equals(o2)) {
                Assert.fail(header + "arrays first differed at element [" + i + "];");
            }
            if (o1 instanceof JSONArray && o2 instanceof JSONArray) {
                final JSONArray e = (JSONArray)o1;
                final JSONArray a = (JSONArray)o2;
                assertEquals(header + "arrays first differed at element " + i + ";", e, a);
            }
            else if (o1 instanceof String && o2 instanceof JSONFunction) {
                assertEquals(header + "arrays first differed at element [" + i + "];", (String)o1, (JSONFunction)o2);
            }
            else if (o1 instanceof JSONFunction && o2 instanceof String) {
                assertEquals(header + "arrays first differed at element [" + i + "];", (JSONFunction)o1, (String)o2);
            }
            else if (o1 instanceof JSONObject && o2 instanceof JSONObject) {
                assertEquals(header + "arrays first differed at element [" + i + "];", (JSONObject)o1, (JSONObject)o2);
            }
            else if (o1 instanceof JSONArray && o2 instanceof JSONArray) {
                assertEquals(header + "arrays first differed at element [" + i + "];", (JSONArray)o1, (JSONArray)o2);
            }
            else if (o1 instanceof JSONFunction && o2 instanceof JSONFunction) {
                Assert.assertEquals(header + "arrays first differed at element [" + i + "];", (Object)o1, (Object)o2);
            }
            else if (o1 instanceof String) {
                Assert.assertEquals(header + "arrays first differed at element [" + i + "];", (String)o1, String.valueOf(o2));
            }
            else if (o2 instanceof String) {
                Assert.assertEquals(header + "arrays first differed at element [" + i + "];", String.valueOf(o1), (String)o2);
            }
            else {
                final Morpher m1 = JSONUtils.getMorpherRegistry().getMorpherFor(o1.getClass());
                final Morpher m2 = JSONUtils.getMorpherRegistry().getMorpherFor(o2.getClass());
                if (m1 != null && m1 != IdentityObjectMorpher.getInstance()) {
                    Assert.assertEquals(header + "arrays first differed at element [" + i + "];", o1, JSONUtils.getMorpherRegistry().morph(o1.getClass(), o2));
                }
                else if (m2 != null && m2 != IdentityObjectMorpher.getInstance()) {
                    Assert.assertEquals(header + "arrays first differed at element [" + i + "];", JSONUtils.getMorpherRegistry().morph(o1.getClass(), o1), o2);
                }
                else {
                    Assert.assertEquals(header + "arrays first differed at element [" + i + "];", o1, o2);
                }
            }
        }
    }
    
    public static void assertEquals(final String message, final JSONArray expected, final String actual) {
        try {
            assertEquals(message, expected, JSONArray.fromObject(actual));
        }
        catch (JSONException e) {
            final String header = (message == null) ? "" : (message + ": ");
            Assert.fail(header + "actual is not a JSONArray");
        }
    }
    
    public static void assertEquals(final String expected, final JSONFunction actual) {
        assertEquals(null, expected, actual);
    }
    
    public static void assertEquals(final String message, final JSONFunction expected, final String actual) {
        final String header = (message == null) ? "" : (message + ": ");
        if (expected == null) {
            Assert.fail(header + "expected function was null");
        }
        if (actual == null) {
            Assert.fail(header + "actual string was null");
        }
        try {
            Assert.assertEquals(header, (Object)expected, (Object)JSONFunction.parse(actual));
        }
        catch (JSONException jsone) {
            Assert.fail(header + "'" + actual + "' is not a function");
        }
    }
    
    public static void assertEquals(final String expected, final JSONNull actual) {
        assertEquals(null, expected, actual);
    }
    
    public static void assertEquals(final String message, final JSONNull expected, final String actual) {
        final String header = (message == null) ? "" : (message + ": ");
        if (actual == null) {
            Assert.fail(header + "actual string was null");
        }
        else if (expected == null) {
            Assert.assertEquals(header, "null", actual);
        }
        else {
            Assert.assertEquals(header, expected.toString(), actual);
        }
    }
    
    public static void assertEquals(final String expected, final JSONObject actual) {
        assertEquals(null, expected, actual);
    }
    
    public static void assertEquals(final String message, final JSONObject expected, final JSONObject actual) {
        final String header = (message == null) ? "" : (message + ": ");
        if (expected == null) {
            Assert.fail(header + "expected object was null");
        }
        if (actual == null) {
            Assert.fail(header + "actual object was null");
        }
        if (expected == actual) {
            return;
        }
        if (expected.isNullObject()) {
            if (actual.isNullObject()) {
                return;
            }
            Assert.fail(header + "actual is not a null JSONObject");
        }
        else if (actual.isNullObject()) {
            Assert.fail(header + "actual is a null JSONObject");
        }
        Assert.assertEquals(header + "names sizes differed, expected.names().length()=" + expected.names().size() + " actual.names().length()=" + actual.names().size(), expected.names().size(), actual.names().size());
        final Iterator keys = expected.keys();
        while (keys.hasNext()) {
            final String key = keys.next();
            final Object o1 = expected.opt(key);
            final Object o2 = actual.opt(key);
            if (JSONNull.getInstance().equals(o1)) {
                if (JSONNull.getInstance().equals(o2)) {
                    continue;
                }
                Assert.fail(header + "objects differed at key [" + key + "];");
            }
            else if (JSONNull.getInstance().equals(o2)) {
                Assert.fail(header + "objects differed at key [" + key + "];");
            }
            if (o1 instanceof String && o2 instanceof JSONFunction) {
                assertEquals(header + "objects differed at key [" + key + "];", (String)o1, (JSONFunction)o2);
            }
            else if (o1 instanceof JSONFunction && o2 instanceof String) {
                assertEquals(header + "objects differed at key [" + key + "];", (JSONFunction)o1, (String)o2);
            }
            else if (o1 instanceof JSONObject && o2 instanceof JSONObject) {
                assertEquals(header + "objects differed at key [" + key + "];", (JSONObject)o1, (JSONObject)o2);
            }
            else if (o1 instanceof JSONArray && o2 instanceof JSONArray) {
                assertEquals(header + "objects differed at key [" + key + "];", (JSONArray)o1, (JSONArray)o2);
            }
            else if (o1 instanceof JSONFunction && o2 instanceof JSONFunction) {
                Assert.assertEquals(header + "objects differed at key [" + key + "];", (Object)o1, (Object)o2);
            }
            else if (o1 instanceof String) {
                Assert.assertEquals(header + "objects differed at key [" + key + "];", (String)o1, String.valueOf(o2));
            }
            else if (o2 instanceof String) {
                Assert.assertEquals(header + "objects differed at key [" + key + "];", String.valueOf(o1), (String)o2);
            }
            else {
                final Morpher m1 = JSONUtils.getMorpherRegistry().getMorpherFor(o1.getClass());
                final Morpher m2 = JSONUtils.getMorpherRegistry().getMorpherFor(o2.getClass());
                if (m1 != null && m1 != IdentityObjectMorpher.getInstance()) {
                    Assert.assertEquals(header + "objects differed at key [" + key + "];", o1, JSONUtils.getMorpherRegistry().morph(o1.getClass(), o2));
                }
                else if (m2 != null && m2 != IdentityObjectMorpher.getInstance()) {
                    Assert.assertEquals(header + "objects differed at key [" + key + "];", JSONUtils.getMorpherRegistry().morph(o1.getClass(), o1), o2);
                }
                else {
                    Assert.assertEquals(header + "objects differed at key [" + key + "];", o1, o2);
                }
            }
        }
    }
    
    public static void assertEquals(final String message, final JSONObject expected, final String actual) {
        try {
            assertEquals(message, expected, JSONObject.fromObject(actual));
        }
        catch (JSONException e) {
            final String header = (message == null) ? "" : (message + ": ");
            Assert.fail(header + "actual is not a JSONObject");
        }
    }
    
    public static void assertEquals(final String message, final String expected, final JSONArray actual) {
        try {
            assertEquals(message, JSONArray.fromObject(expected), actual);
        }
        catch (JSONException e) {
            final String header = (message == null) ? "" : (message + ": ");
            Assert.fail(header + "expected is not a JSONArray");
        }
    }
    
    public static void assertEquals(final String message, final String expected, final JSONFunction actual) {
        final String header = (message == null) ? "" : (message + ": ");
        if (expected == null) {
            Assert.fail(header + "expected string was null");
        }
        if (actual == null) {
            Assert.fail(header + "actual function was null");
        }
        try {
            Assert.assertEquals(header, (Object)JSONFunction.parse(expected), (Object)actual);
        }
        catch (JSONException jsone) {
            Assert.fail(header + "'" + expected + "' is not a function");
        }
    }
    
    public static void assertEquals(final String message, final String expected, final JSONNull actual) {
        final String header = (message == null) ? "" : (message + ": ");
        if (expected == null) {
            Assert.fail(header + "expected was null");
        }
        else if (actual == null) {
            Assert.assertEquals(header, expected, "null");
        }
        else {
            Assert.assertEquals(header, expected, actual.toString());
        }
    }
    
    public static void assertEquals(final String message, final String expected, final JSONObject actual) {
        try {
            assertEquals(message, JSONObject.fromObject(expected), actual);
        }
        catch (JSONException e) {
            final String header = (message == null) ? "" : (message + ": ");
            Assert.fail(header + "expected is not a JSONObject");
        }
    }
    
    public static void assertJsonEquals(final String expected, final String actual) {
        assertJsonEquals(null, expected, actual);
    }
    
    public static void assertJsonEquals(final String message, final String expected, final String actual) {
        final String header = (message == null) ? "" : (message + ": ");
        if (expected == null) {
            Assert.fail(header + "expected was null");
        }
        if (actual == null) {
            Assert.fail(header + "actual was null");
        }
        JSON json1 = null;
        JSON json2 = null;
        try {
            json1 = JSONSerializer.toJSON(expected);
        }
        catch (JSONException jsone) {
            Assert.fail(header + "expected is not a valid JSON string");
        }
        try {
            json2 = JSONSerializer.toJSON(actual);
        }
        catch (JSONException jsone) {
            Assert.fail(header + "actual is not a valid JSON string");
        }
        assertEquals(header, json1, json2);
    }
    
    public static void assertNotNull(final JSON json) {
        assertNotNull(null, json);
    }
    
    public static void assertNotNull(final String message, final JSON json) {
        final String header = (message == null) ? "" : (message + ": ");
        if (json instanceof JSONObject) {
            Assert.assertFalse(header + "Object is null", ((JSONObject)json).isNullObject());
        }
        else if (JSONNull.getInstance().equals(json)) {
            Assert.fail(header + "Object is null");
        }
    }
    
    public static void assertNull(final JSON json) {
        assertNull(null, json);
    }
    
    public static void assertNull(final String message, final JSON json) {
        final String header = (message == null) ? "" : (message + ": ");
        if (json instanceof JSONObject) {
            Assert.assertTrue(header + "Object is not null", ((JSONObject)json).isNullObject());
        }
        else if (!JSONNull.getInstance().equals(json)) {
            Assert.fail(header + "Object is not null");
        }
    }
}
