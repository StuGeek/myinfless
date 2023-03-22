// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json.util;

import java.util.List;
import net.sf.ezmorph.MorphUtils;
import net.sf.json.JSONString;
import java.math.BigInteger;
import java.math.BigDecimal;
import net.sf.ezmorph.bean.MorphDynaBean;
import net.sf.ezmorph.bean.MorphDynaClass;
import org.apache.commons.beanutils.DynaBean;
import net.sf.json.JSONArray;
import java.util.Collection;
import net.sf.json.JSONFunction;
import net.sf.json.JSON;
import net.sf.json.JSONNull;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONObject;
import net.sf.json.regexp.RegexpUtils;
import net.sf.json.JSONException;
import net.sf.json.JsonConfig;
import net.sf.ezmorph.MorpherRegistry;

public final class JSONUtils
{
    public static final String DOUBLE_QUOTE = "\"";
    public static final String SINGLE_QUOTE = "'";
    private static final String FUNCTION_BODY_PATTERN = "^function[ ]?\\(.*?\\)[ \n\t]*\\{(.*?)\\}$";
    private static final String FUNCTION_HEADER_PATTERN = "^function[ ]?\\(.*?\\)$";
    private static final String FUNCTION_PARAMS_PATTERN = "^function[ ]?\\((.*?)\\).*";
    private static final String FUNCTION_PATTERN = "^function[ ]?\\(.*?\\)[ \n\t]*\\{.*?\\}$";
    private static final String FUNCTION_PREFIX = "function";
    private static final MorpherRegistry morpherRegistry;
    
    public static String convertToJavaIdentifier(final String key) {
        return convertToJavaIdentifier(key, new JsonConfig());
    }
    
    public static String convertToJavaIdentifier(final String key, final JsonConfig jsonConfig) {
        try {
            return jsonConfig.getJavaIdentifierTransformer().transformToJavaIdentifier(key);
        }
        catch (JSONException jsone) {
            throw jsone;
        }
        catch (Exception e) {
            throw new JSONException(e);
        }
    }
    
    public static String doubleToString(final double d) {
        if (Double.isInfinite(d) || Double.isNaN(d)) {
            return "null";
        }
        String s = Double.toString(d);
        if (s.indexOf(46) > 0 && s.indexOf(101) < 0 && s.indexOf(69) < 0) {
            while (s.endsWith("0")) {
                s = s.substring(0, s.length() - 1);
            }
            if (s.endsWith(".")) {
                s = s.substring(0, s.length() - 1);
            }
        }
        return s;
    }
    
    public static String getFunctionBody(final String function) {
        return RegexpUtils.getMatcher("^function[ ]?\\(.*?\\)[ \n\t]*\\{(.*?)\\}$", true).getGroupIfMatches(function, 1);
    }
    
    public static String getFunctionParams(final String function) {
        return RegexpUtils.getMatcher("^function[ ]?\\((.*?)\\).*", true).getGroupIfMatches(function, 1);
    }
    
    public static Class getInnerComponentType(final Class type) {
        if (!type.isArray()) {
            return type;
        }
        return getInnerComponentType(type.getComponentType());
    }
    
    public static MorpherRegistry getMorpherRegistry() {
        return JSONUtils.morpherRegistry;
    }
    
    public static Map getProperties(final JSONObject jsonObject) {
        final Map properties = new HashMap();
        final Iterator keys = jsonObject.keys();
        while (keys.hasNext()) {
            final String key = keys.next();
            properties.put(key, getTypeClass(jsonObject.get(key)));
        }
        return properties;
    }
    
    public static Class getTypeClass(final Object obj) {
        if (isNull(obj)) {
            return Object.class;
        }
        if (isArray(obj)) {
            return List.class;
        }
        if (isFunction(obj)) {
            return JSONFunction.class;
        }
        if (isBoolean(obj)) {
            return Boolean.class;
        }
        if (isNumber(obj)) {
            final Number n = (Number)obj;
            if (isInteger(n)) {
                return Integer.class;
            }
            if (isLong(n)) {
                return Long.class;
            }
            if (isFloat(n)) {
                return Float.class;
            }
            if (isBigInteger(n)) {
                return BigInteger.class;
            }
            if (isBigDecimal(n)) {
                return BigDecimal.class;
            }
            if (isDouble(n)) {
                return Double.class;
            }
            throw new JSONException("Unsupported type");
        }
        else {
            if (isString(obj)) {
                return String.class;
            }
            if (isObject(obj)) {
                return Object.class;
            }
            throw new JSONException("Unsupported type");
        }
    }
    
    public static int hashCode(final Object value) {
        if (value == null) {
            return JSONNull.getInstance().hashCode();
        }
        if (value instanceof JSON || value instanceof String || value instanceof JSONFunction) {
            return value.hashCode();
        }
        return String.valueOf(value).hashCode();
    }
    
    public static boolean isArray(final Class clazz) {
        return clazz != null && (clazz.isArray() || Collection.class.isAssignableFrom(clazz) || JSONArray.class.isAssignableFrom(clazz));
    }
    
    public static boolean isArray(final Object obj) {
        return (obj != null && obj.getClass().isArray()) || obj instanceof Collection || obj instanceof JSONArray;
    }
    
    public static boolean isBoolean(final Class clazz) {
        return clazz != null && (Boolean.TYPE.isAssignableFrom(clazz) || Boolean.class.isAssignableFrom(clazz));
    }
    
    public static boolean isBoolean(final Object obj) {
        return obj instanceof Boolean || (obj != null && obj.getClass() == Boolean.TYPE);
    }
    
    public static boolean isDouble(final Class clazz) {
        return clazz != null && (Double.TYPE.isAssignableFrom(clazz) || Double.class.isAssignableFrom(clazz));
    }
    
    public static boolean isFunction(final Object obj) {
        if (obj instanceof String) {
            final String str = (String)obj;
            return str.startsWith("function") && RegexpUtils.getMatcher("^function[ ]?\\(.*?\\)[ \n\t]*\\{.*?\\}$", true).matches(str);
        }
        return obj instanceof JSONFunction;
    }
    
    public static boolean isFunctionHeader(final Object obj) {
        if (obj instanceof String) {
            final String str = (String)obj;
            return str.startsWith("function") && RegexpUtils.getMatcher("^function[ ]?\\(.*?\\)$", true).matches(str);
        }
        return false;
    }
    
    public static boolean isJavaIdentifier(final String str) {
        if (str.length() == 0 || !Character.isJavaIdentifierStart(str.charAt(0))) {
            return false;
        }
        for (int i = 1; i < str.length(); ++i) {
            if (!Character.isJavaIdentifierPart(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isNull(final Object obj) {
        if (obj instanceof JSONObject) {
            return ((JSONObject)obj).isNullObject();
        }
        return JSONNull.getInstance().equals(obj);
    }
    
    public static boolean isNumber(final Class clazz) {
        return clazz != null && (Byte.TYPE.isAssignableFrom(clazz) || Short.TYPE.isAssignableFrom(clazz) || Integer.TYPE.isAssignableFrom(clazz) || Long.TYPE.isAssignableFrom(clazz) || Float.TYPE.isAssignableFrom(clazz) || Double.TYPE.isAssignableFrom(clazz) || Number.class.isAssignableFrom(clazz));
    }
    
    public static boolean isNumber(final Object obj) {
        return (obj != null && obj.getClass() == Byte.TYPE) || (obj != null && obj.getClass() == Short.TYPE) || (obj != null && obj.getClass() == Integer.TYPE) || (obj != null && obj.getClass() == Long.TYPE) || (obj != null && obj.getClass() == Float.TYPE) || (obj != null && obj.getClass() == Double.TYPE) || obj instanceof Number;
    }
    
    public static boolean isObject(final Object obj) {
        return (!isNumber(obj) && !isString(obj) && !isBoolean(obj) && !isArray(obj) && !isFunction(obj)) || isNull(obj);
    }
    
    public static boolean isString(final Class clazz) {
        return clazz != null && (String.class.isAssignableFrom(clazz) || Character.TYPE.isAssignableFrom(clazz) || Character.class.isAssignableFrom(clazz));
    }
    
    public static boolean isString(final Object obj) {
        return obj instanceof String || obj instanceof Character || (obj != null && (obj.getClass() == Character.TYPE || String.class.isAssignableFrom(obj.getClass())));
    }
    
    public static boolean mayBeJSON(final String string) {
        return string != null && ("null".equals(string) || (string.startsWith("[") && string.endsWith("]")) || (string.startsWith("{") && string.endsWith("}")));
    }
    
    public static DynaBean newDynaBean(final JSONObject jsonObject) {
        return newDynaBean(jsonObject, new JsonConfig());
    }
    
    public static DynaBean newDynaBean(final JSONObject jsonObject, final JsonConfig jsonConfig) {
        final Map props = getProperties(jsonObject);
        for (final Map.Entry entry : props.entrySet()) {
            final String key = entry.getKey();
            if (!isJavaIdentifier(key)) {
                final String parsedKey = convertToJavaIdentifier(key, jsonConfig);
                if (parsedKey.compareTo(key) == 0) {
                    continue;
                }
                props.put(parsedKey, props.remove(key));
            }
        }
        final MorphDynaClass dynaClass = new MorphDynaClass(props);
        MorphDynaBean dynaBean = null;
        try {
            dynaBean = (MorphDynaBean)dynaClass.newInstance();
            dynaBean.setDynaBeanClass(dynaClass);
        }
        catch (Exception e) {
            throw new JSONException(e);
        }
        return dynaBean;
    }
    
    public static String numberToString(final Number n) {
        if (n == null) {
            throw new JSONException("Null pointer");
        }
        testValidity(n);
        String s = n.toString();
        if (s.indexOf(46) > 0 && s.indexOf(101) < 0 && s.indexOf(69) < 0) {
            while (s.endsWith("0")) {
                s = s.substring(0, s.length() - 1);
            }
            if (s.endsWith(".")) {
                s = s.substring(0, s.length() - 1);
            }
        }
        return s;
    }
    
    public static String quote(final String string) {
        if (isFunction(string)) {
            return string;
        }
        if (string == null || string.length() == 0) {
            return "\"\"";
        }
        char c = '\0';
        final int len = string.length();
        final StringBuffer sb = new StringBuffer(len * 2);
        final char[] chars = string.toCharArray();
        final char[] buffer = new char[1030];
        int bufferIndex = 0;
        sb.append('\"');
        for (int i = 0; i < len; ++i) {
            if (bufferIndex > 1024) {
                sb.append(buffer, 0, bufferIndex);
                bufferIndex = 0;
            }
            final char b = c;
            c = chars[i];
            switch (c) {
                case '\"':
                case '\\': {
                    buffer[bufferIndex++] = '\\';
                    buffer[bufferIndex++] = c;
                    break;
                }
                case '/': {
                    if (b == '<') {
                        buffer[bufferIndex++] = '\\';
                    }
                    buffer[bufferIndex++] = c;
                    break;
                }
                default: {
                    if (c < ' ') {
                        switch (c) {
                            case '\b': {
                                buffer[bufferIndex++] = '\\';
                                buffer[bufferIndex++] = 'b';
                                break;
                            }
                            case '\t': {
                                buffer[bufferIndex++] = '\\';
                                buffer[bufferIndex++] = 't';
                                break;
                            }
                            case '\n': {
                                buffer[bufferIndex++] = '\\';
                                buffer[bufferIndex++] = 'n';
                                break;
                            }
                            case '\f': {
                                buffer[bufferIndex++] = '\\';
                                buffer[bufferIndex++] = 'f';
                                break;
                            }
                            case '\r': {
                                buffer[bufferIndex++] = '\\';
                                buffer[bufferIndex++] = 'r';
                                break;
                            }
                            default: {
                                final String t = "000" + Integer.toHexString(c);
                                final int tLength = t.length();
                                buffer[bufferIndex++] = '\\';
                                buffer[bufferIndex++] = 'u';
                                buffer[bufferIndex++] = t.charAt(tLength - 4);
                                buffer[bufferIndex++] = t.charAt(tLength - 3);
                                buffer[bufferIndex++] = t.charAt(tLength - 2);
                                buffer[bufferIndex++] = t.charAt(tLength - 1);
                                break;
                            }
                        }
                        break;
                    }
                    buffer[bufferIndex++] = c;
                    break;
                }
            }
        }
        sb.append(buffer, 0, bufferIndex);
        sb.append('\"');
        return sb.toString();
    }
    
    public static String stripQuotes(final String input) {
        if (input.length() < 2) {
            return input;
        }
        if (input.startsWith("'") && input.endsWith("'")) {
            return input.substring(1, input.length() - 1);
        }
        if (input.startsWith("\"") && input.endsWith("\"")) {
            return input.substring(1, input.length() - 1);
        }
        return input;
    }
    
    public static boolean hasQuotes(final String input) {
        return input != null && input.length() >= 2 && ((input.startsWith("'") && input.endsWith("'")) || (input.startsWith("\"") && input.endsWith("\"")));
    }
    
    public static boolean isJsonKeyword(final String input, final JsonConfig jsonConfig) {
        return input != null && ("null".equals(input) || "true".equals(input) || "false".equals(input) || (jsonConfig.isJavascriptCompliant() && "undefined".equals(input)));
    }
    
    public static void testValidity(final Object o) {
        if (o != null) {
            if (o instanceof Double) {
                if (((Double)o).isInfinite() || ((Double)o).isNaN()) {
                    throw new JSONException("JSON does not allow non-finite numbers");
                }
            }
            else if (o instanceof Float) {
                if (((Float)o).isInfinite() || ((Float)o).isNaN()) {
                    throw new JSONException("JSON does not allow non-finite numbers.");
                }
            }
            else if (o instanceof BigDecimal || o instanceof BigInteger) {
                return;
            }
        }
    }
    
    public static Number transformNumber(final Number input) {
        if (input instanceof Float) {
            return new Double(input.toString());
        }
        if (input instanceof Short) {
            return new Integer(input.intValue());
        }
        if (input instanceof Byte) {
            return new Integer(input.intValue());
        }
        if (input instanceof Long) {
            final Long max = new Long(2147483647L);
            if (input.longValue() <= max && input.longValue() >= -2147483648L) {
                return new Integer(input.intValue());
            }
        }
        return input;
    }
    
    public static String valueToString(final Object value) {
        if (value == null || isNull(value)) {
            return "null";
        }
        if (value instanceof JSONFunction) {
            return ((JSONFunction)value).toString();
        }
        if (value instanceof JSONString) {
            Object o;
            try {
                o = ((JSONString)value).toJSONString();
            }
            catch (Exception e) {
                throw new JSONException(e);
            }
            if (o instanceof String) {
                return (String)o;
            }
            throw new JSONException("Bad value from toJSONString: " + o);
        }
        else {
            if (value instanceof Number) {
                return numberToString((Number)value);
            }
            if (value instanceof Boolean || value instanceof JSONObject || value instanceof JSONArray) {
                return value.toString();
            }
            return quote(value.toString());
        }
    }
    
    public static String valueToString(final Object value, final int indentFactor, final int indent) {
        if (value == null || isNull(value)) {
            return "null";
        }
        if (value instanceof JSONFunction) {
            return ((JSONFunction)value).toString();
        }
        if (value instanceof JSONString) {
            return ((JSONString)value).toJSONString();
        }
        if (value instanceof Number) {
            return numberToString((Number)value);
        }
        if (value instanceof Boolean) {
            return value.toString();
        }
        if (value instanceof JSONObject) {
            return ((JSONObject)value).toString(indentFactor, indent);
        }
        if (value instanceof JSONArray) {
            return ((JSONArray)value).toString(indentFactor, indent);
        }
        return quote(value.toString());
    }
    
    private static boolean isBigDecimal(final Number n) {
        if (n instanceof BigDecimal) {
            return true;
        }
        try {
            new BigDecimal(String.valueOf(n));
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
    
    private static boolean isBigInteger(final Number n) {
        if (n instanceof BigInteger) {
            return true;
        }
        try {
            new BigInteger(String.valueOf(n));
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
    
    private static boolean isDouble(final Number n) {
        if (n instanceof Double) {
            return true;
        }
        try {
            final double d = Double.parseDouble(String.valueOf(n));
            return !Double.isInfinite(d);
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
    
    private static boolean isFloat(final Number n) {
        if (n instanceof Float) {
            return true;
        }
        try {
            final float f = Float.parseFloat(String.valueOf(n));
            return !Float.isInfinite(f);
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
    
    private static boolean isInteger(final Number n) {
        if (n instanceof Integer) {
            return true;
        }
        try {
            Integer.parseInt(String.valueOf(n));
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
    
    private static boolean isLong(final Number n) {
        if (n instanceof Long) {
            return true;
        }
        try {
            Long.parseLong(String.valueOf(n));
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
    
    private JSONUtils() {
    }
    
    static {
        MorphUtils.registerStandardMorphers(morpherRegistry = new MorpherRegistry());
    }
}
