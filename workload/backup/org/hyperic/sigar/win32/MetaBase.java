// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.win32;

import java.util.Collection;
import java.util.Vector;

public class MetaBase extends Win32
{
    private static int IIS_MD_SERVER_BASE;
    private static int IIS_MD_HTTP_BASE;
    public static int MD_SERVER_COMMAND;
    public static int MD_CONNECTION_TIMEOUT;
    public static int MD_MAX_CONNECTIONS;
    public static int MD_SERVER_COMMENT;
    public static int MD_SERVER_STATE;
    public static int MD_SERVER_AUTOSTART;
    public static int MD_SERVER_SIZE;
    public static int MD_SERVER_LISTEN_BACKLOG;
    public static int MD_SERVER_LISTEN_TIMEOUT;
    public static int MD_DOWNLEVEL_ADMIN_INSTANCE;
    public static int MD_LEVELS_TO_SCAN;
    public static int MD_SERVER_BINDINGS;
    public static int MD_MAX_ENDPOINT_CONNECTIONS;
    public static int MD_SERVER_CONFIGURATION_INFO;
    public static int MD_IISADMIN_EXTENSIONS;
    public static int MD_LOGFILEDIRECTORY;
    public static int MD_SECURE_BINDINGS;
    private int m_handle;
    private long pIMeta;
    
    public MetaBase() {
        this.pIMeta = this.MetaBaseInit();
    }
    
    public void close() {
        this.MetaBaseClose();
        this.MetaBaseRelease();
    }
    
    public void OpenSubKey(final String subkey) {
        if (subkey.startsWith("/")) {
            this.MetaBaseOpenSubKeyAbs(subkey);
        }
        else {
            this.MetaBaseOpenSubKey(subkey);
        }
    }
    
    public int getIntValue(final int datakey) throws Win32Exception {
        int iResult = 0;
        try {
            iResult = this.MetaBaseGetIntValue(datakey);
        }
        catch (Throwable t) {
            throw new Win32Exception("Error getting int value");
        }
        return iResult;
    }
    
    public int getIntValue(final int datakey, final int defaultValue) {
        int iResult;
        try {
            iResult = this.getIntValue(datakey);
        }
        catch (Win32Exception e) {
            iResult = defaultValue;
        }
        return iResult;
    }
    
    public String getStringValue(final int datakey) throws Win32Exception {
        final String strResult = this.MetaBaseGetStringValue(datakey);
        if (strResult == null) {
            throw new Win32Exception("Error getting string value");
        }
        return strResult;
    }
    
    public String getStringValue(final int datakey, final String defaultValue) {
        String strResult;
        try {
            strResult = this.getStringValue(datakey);
        }
        catch (Win32Exception e) {
            strResult = defaultValue;
        }
        return strResult;
    }
    
    public String[] getMultiStringValue(final int datakey) throws Win32Exception {
        final String[] strResult = this.MetaBaseGetMultiStringValue(datakey);
        return strResult;
    }
    
    public String[] getSubKeyNames() {
        final Collection coll = new Vector();
        String strName;
        for (int i = 0; (strName = this.MetaBaseEnumKey(i)) != null; ++i) {
            coll.add(strName);
        }
        return coll.toArray(new String[coll.size()]);
    }
    
    private final native long MetaBaseInit();
    
    private final native void MetaBaseClose();
    
    private final native void MetaBaseRelease();
    
    private final native String MetaBaseEnumKey(final int p0);
    
    private final native void MetaBaseOpenSubKey(final String p0);
    
    private final native void MetaBaseOpenSubKeyAbs(final String p0);
    
    private final native int MetaBaseGetIntValue(final int p0);
    
    private final native String MetaBaseGetStringValue(final int p0);
    
    private final native String[] MetaBaseGetMultiStringValue(final int p0);
    
    public static void main(final String[] args) {
        final String key = "/LM/W3SVC";
        try {
            final MetaBase mb = new MetaBase();
            mb.OpenSubKey(key);
            final String logdir = mb.getStringValue(MetaBase.MD_LOGFILEDIRECTORY);
            System.out.println("Logdir: " + logdir);
            final String[] keys = mb.getSubKeyNames();
            System.out.println("Listing IIS Web Sites");
            for (int i = 0; i < keys.length; ++i) {
                int serverNum;
                try {
                    serverNum = Integer.parseInt(keys[i]);
                }
                catch (NumberFormatException e) {
                    continue;
                }
                final MetaBase vhost = new MetaBase();
                vhost.OpenSubKey(key + "/" + serverNum);
                final String[] bindings = vhost.getMultiStringValue(MetaBase.MD_SERVER_BINDINGS);
                final String hostname = vhost.getStringValue(MetaBase.MD_SERVER_COMMENT);
                System.out.println("");
                System.out.println("Host: " + hostname);
                for (int j = 0; j < bindings.length; ++j) {
                    System.out.println("Bindings: " + bindings[j]);
                }
                vhost.close();
            }
            mb.close();
        }
        catch (Win32Exception e2) {
            System.out.println("Unable to query MetaBase for IIS Web Sites");
        }
    }
    
    static {
        MetaBase.IIS_MD_SERVER_BASE = 1000;
        MetaBase.IIS_MD_HTTP_BASE = 2000;
        MetaBase.MD_SERVER_COMMAND = MetaBase.IIS_MD_SERVER_BASE + 12;
        MetaBase.MD_CONNECTION_TIMEOUT = MetaBase.IIS_MD_SERVER_BASE + 13;
        MetaBase.MD_MAX_CONNECTIONS = MetaBase.IIS_MD_SERVER_BASE + 14;
        MetaBase.MD_SERVER_COMMENT = MetaBase.IIS_MD_SERVER_BASE + 15;
        MetaBase.MD_SERVER_STATE = MetaBase.IIS_MD_SERVER_BASE + 16;
        MetaBase.MD_SERVER_AUTOSTART = MetaBase.IIS_MD_SERVER_BASE + 17;
        MetaBase.MD_SERVER_SIZE = MetaBase.IIS_MD_SERVER_BASE + 18;
        MetaBase.MD_SERVER_LISTEN_BACKLOG = MetaBase.IIS_MD_SERVER_BASE + 19;
        MetaBase.MD_SERVER_LISTEN_TIMEOUT = MetaBase.IIS_MD_SERVER_BASE + 20;
        MetaBase.MD_DOWNLEVEL_ADMIN_INSTANCE = MetaBase.IIS_MD_SERVER_BASE + 21;
        MetaBase.MD_LEVELS_TO_SCAN = MetaBase.IIS_MD_SERVER_BASE + 22;
        MetaBase.MD_SERVER_BINDINGS = MetaBase.IIS_MD_SERVER_BASE + 23;
        MetaBase.MD_MAX_ENDPOINT_CONNECTIONS = MetaBase.IIS_MD_SERVER_BASE + 24;
        MetaBase.MD_SERVER_CONFIGURATION_INFO = MetaBase.IIS_MD_SERVER_BASE + 27;
        MetaBase.MD_IISADMIN_EXTENSIONS = MetaBase.IIS_MD_SERVER_BASE + 28;
        MetaBase.MD_LOGFILEDIRECTORY = 4001;
        MetaBase.MD_SECURE_BINDINGS = MetaBase.IIS_MD_HTTP_BASE + 21;
    }
}
