// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.Iterator;
import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class FileWatcher
{
    private Sigar sigar;
    private long interval;
    private long lastTime;
    private Set files;
    
    public abstract void onChange(final FileInfo p0);
    
    public void onNotFound(final FileInfo info) {
    }
    
    public void onException(final FileInfo info, final SigarException e) {
    }
    
    public FileWatcher(final Sigar sigar) {
        this.interval = 0L;
        this.lastTime = 0L;
        this.files = Collections.synchronizedSet(new HashSet<Object>());
        this.sigar = sigar;
    }
    
    public void setInterval(final long interval) {
        this.interval = interval;
    }
    
    public long getInterval() {
        return this.interval;
    }
    
    public FileInfo add(final File file) throws SigarException {
        return this.add(file.getAbsolutePath());
    }
    
    public FileInfo add(final String file) throws SigarException {
        final FileInfo info = this.sigar.getFileInfo(file);
        this.files.add(info);
        return info;
    }
    
    public void add(final File[] files) throws SigarException {
        for (int i = 0; i < files.length; ++i) {
            this.add(files[i]);
        }
    }
    
    public void add(final String[] files) throws SigarException {
        for (int i = 0; i < files.length; ++i) {
            this.add(files[i]);
        }
    }
    
    public void remove(final File file) {
        this.remove(file.getAbsolutePath());
    }
    
    public void remove(final String file) {
        final FileInfo info = new FileInfo();
        info.name = file;
        this.files.remove(info);
    }
    
    public void clear() {
        this.files.clear();
    }
    
    public Set getFiles() {
        return this.files;
    }
    
    protected boolean changed(final FileInfo info) throws SigarException, SigarFileNotFoundException {
        return info.changed();
    }
    
    public void check() {
        if (this.interval != 0L) {
            final long timeNow = System.currentTimeMillis();
            final long timeDiff = timeNow - this.lastTime;
            if (timeDiff < this.interval) {
                return;
            }
            this.lastTime = timeNow;
        }
        synchronized (this.files) {
            for (final FileInfo info : this.files) {
                try {
                    if (!this.changed(info)) {
                        continue;
                    }
                    this.onChange(info);
                }
                catch (SigarFileNotFoundException e2) {
                    this.onNotFound(info);
                }
                catch (SigarException e) {
                    this.onException(info, e);
                }
            }
        }
    }
}
