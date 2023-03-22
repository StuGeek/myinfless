// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.beanutils;

public class ConvertUtils
{
    public static boolean getDefaultBoolean() {
        return ConvertUtilsBean.getInstance().getDefaultBoolean();
    }
    
    public static void setDefaultBoolean(final boolean newDefaultBoolean) {
        ConvertUtilsBean.getInstance().setDefaultBoolean(newDefaultBoolean);
    }
    
    public static byte getDefaultByte() {
        return ConvertUtilsBean.getInstance().getDefaultByte();
    }
    
    public static void setDefaultByte(final byte newDefaultByte) {
        ConvertUtilsBean.getInstance().setDefaultByte(newDefaultByte);
    }
    
    public static char getDefaultCharacter() {
        return ConvertUtilsBean.getInstance().getDefaultCharacter();
    }
    
    public static void setDefaultCharacter(final char newDefaultCharacter) {
        ConvertUtilsBean.getInstance().setDefaultCharacter(newDefaultCharacter);
    }
    
    public static double getDefaultDouble() {
        return ConvertUtilsBean.getInstance().getDefaultDouble();
    }
    
    public static void setDefaultDouble(final double newDefaultDouble) {
        ConvertUtilsBean.getInstance().setDefaultDouble(newDefaultDouble);
    }
    
    public static float getDefaultFloat() {
        return ConvertUtilsBean.getInstance().getDefaultFloat();
    }
    
    public static void setDefaultFloat(final float newDefaultFloat) {
        ConvertUtilsBean.getInstance().setDefaultFloat(newDefaultFloat);
    }
    
    public static int getDefaultInteger() {
        return ConvertUtilsBean.getInstance().getDefaultInteger();
    }
    
    public static void setDefaultInteger(final int newDefaultInteger) {
        ConvertUtilsBean.getInstance().setDefaultInteger(newDefaultInteger);
    }
    
    public static long getDefaultLong() {
        return ConvertUtilsBean.getInstance().getDefaultLong();
    }
    
    public static void setDefaultLong(final long newDefaultLong) {
        ConvertUtilsBean.getInstance().setDefaultLong(newDefaultLong);
    }
    
    public static short getDefaultShort() {
        return ConvertUtilsBean.getInstance().getDefaultShort();
    }
    
    public static void setDefaultShort(final short newDefaultShort) {
        ConvertUtilsBean.getInstance().setDefaultShort(newDefaultShort);
    }
    
    public static String convert(final Object value) {
        return ConvertUtilsBean.getInstance().convert(value);
    }
    
    public static Object convert(final String value, final Class clazz) {
        return ConvertUtilsBean.getInstance().convert(value, clazz);
    }
    
    public static Object convert(final String[] values, final Class clazz) {
        return ConvertUtilsBean.getInstance().convert(values, clazz);
    }
    
    public static Object convert(final Object value, final Class targetType) {
        return ConvertUtilsBean.getInstance().convert(value, targetType);
    }
    
    public static void deregister() {
        ConvertUtilsBean.getInstance().deregister();
    }
    
    public static void deregister(final Class clazz) {
        ConvertUtilsBean.getInstance().deregister(clazz);
    }
    
    public static Converter lookup(final Class clazz) {
        return ConvertUtilsBean.getInstance().lookup(clazz);
    }
    
    public static Converter lookup(final Class sourceType, final Class targetType) {
        return ConvertUtilsBean.getInstance().lookup(sourceType, targetType);
    }
    
    public static void register(final Converter converter, final Class clazz) {
        ConvertUtilsBean.getInstance().register(converter, clazz);
    }
}
