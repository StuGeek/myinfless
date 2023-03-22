// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.win32;

public class LocaleInfo extends Win32
{
    public static final int LOCALE_SENGLANGUAGE = 4097;
    public static final int LOCALE_SENGCOUNTRY = 4098;
    public static final int LANG_ENGLISH = 9;
    private int id;
    
    private static native int getSystemDefaultLCID();
    
    private static native String getAttribute(final int p0, final int p1);
    
    public LocaleInfo() {
        this(getSystemDefaultLCID());
    }
    
    public static final int makeLangId(final int primary, final int sub) {
        return sub << 10 | primary;
    }
    
    public LocaleInfo(final Integer id) {
        this((int)id);
    }
    
    public LocaleInfo(final int id) {
        this.id = id;
    }
    
    public LocaleInfo(final int primary, final int sub) {
        this(makeLangId(primary, sub));
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    private static int getPrimaryLangId(final int id) {
        return id & 0x3FF;
    }
    
    public int getPrimaryLangId() {
        return getPrimaryLangId(this.id);
    }
    
    private static int getSubLangId(final int id) {
        return id >> 10;
    }
    
    public int getSubLangId() {
        return getSubLangId(this.id);
    }
    
    public static boolean isEnglish() {
        final int id = getSystemDefaultLCID();
        return getPrimaryLangId(id) == 9;
    }
    
    public String getPerflibLangId() {
        final String id = Integer.toHexString(this.getPrimaryLangId()).toUpperCase();
        int pad = 3 - id.length();
        final StringBuffer fid = new StringBuffer(3);
        while (pad-- > 0) {
            fid.append("0");
        }
        fid.append(id);
        return fid.toString();
    }
    
    public String getAttribute(final int attr) {
        return getAttribute(this.id, attr);
    }
    
    public String getEnglishLanguageName() {
        return this.getAttribute(4097);
    }
    
    public String getEnglishCountryName() {
        return this.getAttribute(4098);
    }
    
    public String toString() {
        return this.getId() + ":" + this.getEnglishLanguageName() + " (" + this.getEnglishCountryName() + ")";
    }
}
