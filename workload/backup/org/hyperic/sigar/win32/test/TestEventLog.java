// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.win32.test;

import org.hyperic.sigar.win32.EventLogNotification;
import org.hyperic.sigar.win32.EventLogThread;
import org.hyperic.sigar.win32.EventLogRecord;
import org.hyperic.sigar.win32.Win32Exception;
import org.hyperic.sigar.win32.EventLog;
import org.hyperic.sigar.test.SigarTestCase;

public class TestEventLog extends SigarTestCase
{
    public TestEventLog(final String name) {
        super(name);
    }
    
    public void testOpenClose() throws Exception {
        final EventLog log = new EventLog();
        try {
            log.close();
            fail("Closing an unopened event log succeeded");
        }
        catch (Win32Exception ex) {}
        log.open("Application");
        log.close();
        log.open("System");
        log.close();
    }
    
    public void testGetNumberOfRecords() throws Exception {
        final EventLog log = new EventLog();
        log.open("Application");
        try {
            final int numRecords = log.getNumberOfRecords();
        }
        catch (Exception e) {
            fail("Unable to get the number of records");
        }
        log.close();
    }
    
    public void testGetOldestRecord() throws Exception {
        final EventLog log = new EventLog();
        log.open("Application");
        try {
            final int oldestRecord = log.getOldestRecord();
        }
        catch (Exception e) {
            fail("Unable to get the oldest event record");
        }
        log.close();
    }
    
    public void testGetNewestRecord() throws Exception {
        final EventLog log = new EventLog();
        log.open("Application");
        try {
            final int newestRecord = log.getNewestRecord();
        }
        catch (Exception e) {
            fail("Unable to get the newest event record");
        }
        log.close();
    }
    
    private int readAll(final String logname) throws Exception {
        int fail = 0;
        int success = 0;
        int max = 500;
        final String testMax = System.getProperty("sigar.testeventlog.max");
        if (testMax != null) {
            max = Integer.parseInt(testMax);
        }
        final EventLog log = new EventLog();
        log.open(logname);
        if (log.getNumberOfRecords() == 0) {
            log.close();
            return 0;
        }
        final int oldestRecord = log.getOldestRecord();
        final int numRecords = log.getNumberOfRecords();
        this.traceln("oldest=" + oldestRecord + ", total=" + numRecords + ", max=" + max);
        for (int i = oldestRecord; i < oldestRecord + numRecords; ++i) {
            try {
                final EventLogRecord record = log.read(i);
                if (++success > max) {
                    break;
                }
            }
            catch (Win32Exception e) {
                ++fail;
                this.traceln("Error reading record " + i + ": " + e.getMessage());
            }
        }
        log.close();
        this.traceln("success=" + success + ", fail=" + fail);
        return success;
    }
    
    public void testRead() throws Exception {
        int total = 0;
        final String[] logs = EventLog.getLogNames();
        for (int i = 0; i < logs.length; ++i) {
            final String msg = "readAll(" + logs[i] + ")";
            this.traceln(msg);
            total += this.readAll(logs[i]);
        }
        if (total == 0) {
            fail("No eventlog entries read");
        }
    }
    
    public void testEventLogThread() throws Exception {
        final EventLogThread thread = EventLogThread.getInstance("Application");
        thread.doStart();
        final SSHEventLogNotification notification = new SSHEventLogNotification();
        thread.add(notification);
        thread.doStop();
    }
    
    private class SSHEventLogNotification implements EventLogNotification
    {
        public boolean matches(final EventLogRecord record) {
            return record.getSource().equals("sshd");
        }
        
        public void handleNotification(final EventLogRecord record) {
            System.out.println(record);
        }
    }
}
