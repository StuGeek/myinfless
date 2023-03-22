// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json.processors;

import net.sf.json.JSONString;
import net.sf.json.JSONFunction;
import java.math.BigDecimal;
import java.math.BigInteger;
import net.sf.json.JSON;
import net.sf.json.JSONNull;

public final class JsonVerifier
{
    public static boolean isValidJsonValue(final Object value) {
        return JSONNull.getInstance().equals(value) || value instanceof JSON || value instanceof Boolean || value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long || value instanceof Double || value instanceof BigInteger || value instanceof BigDecimal || value instanceof JSONFunction || value instanceof JSONString || value instanceof String;
    }
}
