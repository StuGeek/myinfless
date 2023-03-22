// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.win32;

public class EventLog extends Win32
{
    int eventLogHandle;
    public static final String SYSTEM = "System";
    public static final String APPLICATION = "Application";
    public static final String SECURITY = "Security";
    public static final int EVENTLOG_SUCCESS = 0;
    public static final int EVENTLOG_ERROR_TYPE = 1;
    public static final int EVENTLOG_WARNING_TYPE = 2;
    public static final int EVENTLOG_INFORMATION_TYPE = 4;
    public static final int EVENTLOG_AUDIT_SUCCESS = 8;
    public static final int EVENTLOG_AUDIT_FAILURE = 16;
    public static final int EVENTLOG_WAIT_INFINITE = -1;
    private String name;
    
    public EventLog() {
        this.eventLogHandle = 0;
    }
    
    public void open(final String name) throws Win32Exception {
        this.openlog(this.name = name);
    }
    
    public native void openlog(final String p0) throws Win32Exception;
    
    public native void close() throws Win32Exception;
    
    public native int getNumberOfRecords() throws Win32Exception;
    
    public native int getOldestRecord() throws Win32Exception;
    
    public int getNewestRecord() throws Win32Exception {
        return this.getOldestRecord() + this.getNumberOfRecords() - 1;
    }
    
    public EventLogRecord read(final int recordOffset) throws Win32Exception {
        final EventLogRecord record = this.readlog(this.name, recordOffset);
        record.setLogName(this.name);
        return record;
    }
    
    private native EventLogRecord readlog(final String p0, final int p1) throws Win32Exception;
    
    public native void waitForChange(final int p0) throws Win32Exception;
    
    public static String[] getLogNames() {
        final String EVENTLOG_KEY = "SYSTEM\\CurrentControlSet\\Services\\Eventlog";
        RegistryKey key = null;
        String[] names;
        try {
            key = RegistryKey.LocalMachine.openSubKey("SYSTEM\\CurrentControlSet\\Services\\Eventlog");
            names = key.getSubKeyNames();
        }
        catch (Win32Exception e) {
            names = new String[] { "System", "Application", "Security" };
        }
        finally {
            if (key != null) {
                key.close();
            }
        }
        return names;
    }
}
