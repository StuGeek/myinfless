// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.cmd;

import org.hyperic.sigar.win32.EventLogNotification;
import org.hyperic.sigar.win32.EventLogThread;
import org.hyperic.sigar.win32.Win32Exception;
import org.hyperic.sigar.win32.EventLogRecord;
import org.hyperic.sigar.win32.EventLog;

public class EventLogTail
{
    private static void tail(final String name, final Tail tail) throws Win32Exception {
        final EventLog log = new EventLog();
        log.open(name);
        int max = log.getNumberOfRecords();
        if (tail.number < max) {
            max = tail.number;
        }
        int i;
        for (int last = log.getNewestRecord() + 1, first = i = last - max; i < last; ++i) {
            final EventLogRecord record = log.read(i);
            System.out.println(record);
        }
        log.close();
    }
    
    public static void main(final String[] args) throws Exception {
        final Tail tail = new Tail();
        tail.parseArgs(args);
        if (tail.files.size() == 0) {
            tail.files.add("System");
        }
        for (int i = 0; i < tail.files.size(); ++i) {
            final String name = tail.files.get(i);
            tail(name, tail);
            if (tail.follow) {
                final TailNotification notifier = new TailNotification();
                final EventLogThread thread = EventLogThread.getInstance(name);
                thread.add(notifier);
                thread.doStart();
            }
        }
        if (tail.follow) {
            System.in.read();
        }
    }
    
    private static class TailNotification implements EventLogNotification
    {
        public void handleNotification(final EventLogRecord event) {
            System.out.println(event);
        }
        
        public boolean matches(final EventLogRecord event) {
            return true;
        }
    }
}
