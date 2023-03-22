// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.win32;

import java.io.IOException;
import java.util.Iterator;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import org.apache.log4j.Logger;

public class EventLogThread implements Runnable
{
    public static final int DEFAULT_INTERVAL = 60000;
    private static Logger logger;
    private Thread thread;
    private static HashMap logs;
    private boolean shouldDie;
    private Set notifiers;
    private String logName;
    private long interval;
    
    public EventLogThread() {
        this.thread = null;
        this.shouldDie = false;
        this.notifiers = Collections.synchronizedSet(new HashSet<Object>());
        this.logName = "Application";
        this.interval = 60000L;
    }
    
    public static EventLogThread getInstance() {
        return getInstance("Application");
    }
    
    public static EventLogThread getInstance(final String name) {
        EventLogThread instance;
        synchronized (EventLogThread.logs) {
            instance = EventLogThread.logs.get(name);
            if (instance == null) {
                instance = new EventLogThread();
                instance.setLogName(name);
                EventLogThread.logs.put(name, instance);
            }
        }
        return instance;
    }
    
    public static void closeInstances() {
        synchronized (EventLogThread.logs) {
            for (final EventLogThread eventLogThread : EventLogThread.logs.values()) {
                eventLogThread.doStop();
            }
            EventLogThread.logs.clear();
        }
    }
    
    public void setInterval(final long interval) {
        this.interval = interval;
    }
    
    public void setLogName(final String logName) {
        this.logName = logName;
    }
    
    public synchronized void doStart() {
        if (this.thread != null) {
            return;
        }
        (this.thread = new Thread(this, "EventLogThread")).setDaemon(true);
        this.thread.start();
        EventLogThread.logger.debug((Object)(this.thread.getName() + " started"));
    }
    
    public synchronized void doStop() {
        if (this.thread == null) {
            return;
        }
        this.die();
        this.thread.interrupt();
        EventLogThread.logger.debug((Object)(this.thread.getName() + " stopped"));
        this.thread = null;
    }
    
    public void add(final EventLogNotification notifier) {
        this.notifiers.add(notifier);
    }
    
    public void remove(final EventLogNotification notifier) {
        this.notifiers.remove(notifier);
    }
    
    private void handleEvents(final EventLog log, final int curEvent, final int lastEvent) {
        for (int i = curEvent + 1; i <= lastEvent; ++i) {
            EventLogRecord record;
            try {
                record = log.read(i);
            }
            catch (Win32Exception e) {
                EventLogThread.logger.error((Object)("Unable to read event id " + i + ": " + e));
                continue;
            }
            synchronized (this.notifiers) {
                for (final EventLogNotification notification : this.notifiers) {
                    if (notification.matches(record)) {
                        notification.handleNotification(record);
                    }
                }
            }
        }
    }
    
    public void run() {
        final EventLog log = new EventLog();
        try {
            log.open(this.logName);
            int curEvent = log.getNewestRecord();
            while (!this.shouldDie) {
                final int lastEvent = log.getNewestRecord();
                if (lastEvent > curEvent) {
                    this.handleEvents(log, curEvent, lastEvent);
                }
                curEvent = lastEvent;
                try {
                    Thread.sleep(this.interval);
                }
                catch (InterruptedException ex) {}
            }
        }
        catch (Win32Exception e) {
            EventLogThread.logger.error((Object)"Unable to monitor event log: ", (Throwable)e);
        }
        finally {
            try {
                log.close();
            }
            catch (Win32Exception ex2) {}
        }
    }
    
    public void die() {
        this.shouldDie = true;
    }
    
    public static void main(String[] args) {
        if (args.length == 0) {
            args = new String[] { "System", "Application", "Security" };
        }
        final EventLogNotification watcher = new EventLogNotification() {
            public boolean matches(final EventLogRecord record) {
                return true;
            }
            
            public void handleNotification(final EventLogRecord record) {
                System.out.println(record);
            }
        };
        for (int i = 0; i < args.length; ++i) {
            final String name = args[i];
            final EventLogThread eventLogThread = getInstance(name);
            eventLogThread.doStart();
            eventLogThread.setInterval(1000L);
            eventLogThread.add(watcher);
        }
        System.out.println("Press any key to stop");
        try {
            System.in.read();
        }
        catch (IOException ex) {}
        closeInstances();
    }
    
    static {
        EventLogThread.logger = Logger.getLogger(EventLogThread.class.getName());
        EventLogThread.logs = new HashMap();
    }
}
