// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.win32;

import java.util.Collection;
import java.util.Vector;
import java.util.List;

public class RegistryKey extends Win32
{
    private static final int HKEY_CLASSES_ROOT = Integer.MIN_VALUE;
    private static final int HKEY_CURRENT_USER = -2147483647;
    private static final int HKEY_LOCAL_MACHINE = -2147483646;
    private static final int HKEY_USERS = -2147483645;
    private static final int HKEY_PERFORMANCE_DATA = -2147483644;
    private static final int HKEY_CURRENT_CONFIG = -2147483643;
    private static final int HKEY_DYN_DATA = -2147483642;
    public static final RegistryKey ClassesRoot;
    public static final RegistryKey CurrentUser;
    public static final RegistryKey LocalMachine;
    private long m_hkey;
    private String subkey;
    
    private RegistryKey() {
    }
    
    private RegistryKey(final long hkey) {
        this.m_hkey = hkey;
    }
    
    public synchronized void close() {
        if (this.m_hkey != 0L) {
            RegCloseKey(this.m_hkey);
            this.m_hkey = 0L;
        }
    }
    
    public RegistryKey createSubKey(final String subkey) {
        return new RegistryKey(RegCreateKey(this.m_hkey, subkey));
    }
    
    public String getSubKeyName() {
        return this.subkey;
    }
    
    public RegistryKey createSubKey(final String subkey, final String value) throws Win32Exception {
        RegistryKey keyResult = null;
        final long hkey = RegCreateKey(this.m_hkey, subkey);
        if (hkey != 0L) {
            keyResult = new RegistryKey(hkey);
            if (keyResult != null) {
                keyResult.setStringValue(null, value);
            }
            return keyResult;
        }
        throw new Win32Exception("Error creating subkey");
    }
    
    public RegistryKey createSubKey(final String subkey, final int value) throws Win32Exception {
        RegistryKey keyResult = null;
        final long hkey = RegCreateKey(this.m_hkey, subkey);
        if (hkey != 0L) {
            keyResult = new RegistryKey(hkey);
            if (keyResult != null) {
                keyResult.setIntValue(null, value);
            }
            return keyResult;
        }
        throw new Win32Exception("Error creating subkey");
    }
    
    public void deleteSubKey(final String subkey) {
        RegDeleteKey(this.m_hkey, subkey);
    }
    
    public void deleteSubKeyTree(final String subkey) {
    }
    
    public void deleteValue(final String name) {
        RegDeleteValue(this.m_hkey, name);
    }
    
    public void flush() {
        RegFlushKey(this.m_hkey);
    }
    
    public int getIntValue(final String name) throws Win32Exception {
        int iResult = 0;
        try {
            iResult = RegQueryIntValue(this.m_hkey, name);
        }
        catch (Throwable t) {
            throw new Win32Exception("Error getting int value");
        }
        return iResult;
    }
    
    public int getIntValue(final String name, final int defaultValue) {
        int iResult;
        try {
            iResult = this.getIntValue(name);
        }
        catch (Win32Exception e) {
            iResult = defaultValue;
        }
        return iResult;
    }
    
    public String getStringValue(final String name) throws Win32Exception {
        final String strResult = RegQueryStringValue(this.m_hkey, name);
        if (strResult == null) {
            throw new Win32Exception("Error getting string value");
        }
        return strResult;
    }
    
    public void getMultiStringValue(final String name, final List values) throws Win32Exception {
        RegQueryMultiStringValue(this.m_hkey, name, values);
    }
    
    public String getStringValue(final String name, final String defaultValue) {
        String strResult;
        try {
            strResult = this.getStringValue(name);
        }
        catch (Win32Exception e) {
            strResult = defaultValue;
        }
        return strResult;
    }
    
    public String[] getSubKeyNames() {
        final Collection coll = new Vector();
        String strName;
        for (int i = 0; (strName = RegEnumKey(this.m_hkey, i)) != null; ++i) {
            coll.add(strName);
        }
        return coll.toArray(new String[coll.size()]);
    }
    
    public String[] getValueNames() {
        final Collection coll = new Vector();
        String strName;
        for (int i = 0; (strName = RegEnumValueName(this.m_hkey, i)) != null; ++i) {
            coll.add(strName);
        }
        return coll.toArray(new String[coll.size()]);
    }
    
    public RegistryKey openSubKey(final String subkey) throws Win32Exception {
        final long hkey = RegOpenKey(this.m_hkey, subkey);
        if (hkey == 0L) {
            throw new Win32Exception("Error opening subkey");
        }
        final RegistryKey key = new RegistryKey(hkey);
        key.subkey = subkey;
        return key;
    }
    
    public void setIntValue(final String name, final int value) throws Win32Exception {
        final int iResult = RegSetIntValue(this.m_hkey, name, value);
        if (iResult != 0) {
            throw new Win32Exception("Error setting int value");
        }
    }
    
    public void setStringValue(final String name, final String value) throws Win32Exception {
        final int iResult = RegSetStringValue(this.m_hkey, name, value);
        if (iResult != 0) {
            throw new Win32Exception("Error setting string value");
        }
    }
    
    protected void finalize() {
        this.close();
    }
    
    private static native int RegCloseKey(final long p0);
    
    private static native long RegCreateKey(final long p0, final String p1);
    
    private static native int RegDeleteKey(final long p0, final String p1);
    
    private static native int RegDeleteValue(final long p0, final String p1);
    
    private static native String RegEnumKey(final long p0, final int p1);
    
    private static native String RegEnumValueName(final long p0, final int p1);
    
    private static native int RegFlushKey(final long p0);
    
    private static native int RegLoadKey(final long p0, final String p1, final String p2);
    
    private static native long RegOpenKey(final long p0, final String p1);
    
    private static native byte[] RegQueryBufferValue(final long p0, final String p1);
    
    private static native int RegQueryIntValue(final long p0, final String p1);
    
    private static native String RegQueryStringValue(final long p0, final String p1);
    
    private static native void RegQueryMultiStringValue(final long p0, final String p1, final List p2);
    
    private static native int RegSetIntValue(final long p0, final String p1, final int p2);
    
    private static native int RegSetStringValue(final long p0, final String p1, final String p2);
    
    static {
        ClassesRoot = new RegistryKey(-2147483648L);
        CurrentUser = new RegistryKey(-2147483647L);
        LocalMachine = new RegistryKey(-2147483646L);
    }
}
