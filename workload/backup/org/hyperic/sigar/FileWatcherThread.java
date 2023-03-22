// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.Iterator;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class FileWatcherThread implements Runnable
{
    public static final int DEFAULT_INTERVAL = 300000;
    private Thread thread;
    private static FileWatcherThread instance;
    private boolean shouldDie;
    private long interval;
    private Set watchers;
    
    public FileWatcherThread() {
        this.thread = null;
        this.shouldDie = false;
        this.interval = 300000L;
        this.watchers = Collections.synchronizedSet(new HashSet<Object>());
    }
    
    public static synchronized FileWatcherThread getInstance() {
        if (FileWatcherThread.instance == null) {
            FileWatcherThread.instance = new FileWatcherThread();
        }
        return FileWatcherThread.instance;
    }
    
    public synchronized void doStart() {
        if (this.thread != null) {
            return;
        }
        (this.thread = new Thread(this, "FileWatcherThread")).setDaemon(true);
        this.thread.start();
    }
    
    public synchronized void doStop() {
        if (this.thread == null) {
            return;
        }
        this.die();
        this.thread.interrupt();
        this.thread = null;
    }
    
    public void setInterval(final long interval) {
        this.interval = interval;
    }
    
    public long getInterval() {
        return this.interval;
    }
    
    public void add(final FileWatcher watcher) {
        this.watchers.add(watcher);
    }
    
    public void remove(final FileWatcher watcher) {
        this.watchers.remove(watcher);
    }
    
    public void run() {
        while (!this.shouldDie) {
            this.check();
            try {
                Thread.sleep(this.interval);
            }
            catch (InterruptedException e) {}
        }
    }
    
    public void die() {
        this.shouldDie = true;
    }
    
    public void check() {
        synchronized (this.watchers) {
            for (final FileWatcher watcher : this.watchers) {
                try {
                    watcher.check();
                }
                catch (Exception e) {
                    FileTail.error("Unexpected exception: " + e.getMessage(), e);
                }
            }
        }
    }
    
    static {
        FileWatcherThread.instance = null;
    }
}
