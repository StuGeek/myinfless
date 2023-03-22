// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.mail.util;

import javax.mail.Session;
import java.util.Properties;

public class PropUtil
{
    private PropUtil() {
    }
    
    public static int getIntProperty(final Properties props, final String name, final int def) {
        return getInt(props.get(name), def);
    }
    
    public static boolean getBooleanProperty(final Properties props, final String name, final boolean def) {
        return getBoolean(props.get(name), def);
    }
    
    public static int getIntSessionProperty(final Session session, final String name, final int def) {
        return getInt(session.getProperties().get(name), def);
    }
    
    public static boolean getBooleanSessionProperty(final Session session, final String name, final boolean def) {
        return getBoolean(session.getProperties().get(name), def);
    }
    
    public static boolean getBooleanSystemProperty(final String name, final boolean def) {
        try {
            return getBoolean(System.getProperties().get(name), def);
        }
        catch (SecurityException sex) {
            try {
                final String value = System.getProperty(name);
                if (value == null) {
                    return def;
                }
                if (def) {
                    return !value.equalsIgnoreCase("false");
                }
                return value.equalsIgnoreCase("true");
            }
            catch (SecurityException sex) {
                return def;
            }
        }
    }
    
    private static int getInt(final Object value, final int def) {
        if (value == null) {
            return def;
        }
        if (value instanceof String) {
            try {
                return Integer.parseInt((String)value);
            }
            catch (NumberFormatException ex) {}
        }
        if (value instanceof Integer) {
            return (int)value;
        }
        return def;
    }
    
    private static boolean getBoolean(final Object value, final boolean def) {
        if (value == null) {
            return def;
        }
        if (value instanceof String) {
            if (def) {
                return !((String)value).equalsIgnoreCase("false");
            }
            return ((String)value).equalsIgnoreCase("true");
        }
        else {
            if (value instanceof Boolean) {
                return (boolean)value;
            }
            return def;
        }
    }
}
