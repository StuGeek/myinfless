// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class FileSystemUsage implements Serializable
{
    private static final long serialVersionUID = 18905L;
    long total;
    long free;
    long used;
    long avail;
    long files;
    long freeFiles;
    long diskReads;
    long diskWrites;
    long diskReadBytes;
    long diskWriteBytes;
    double diskQueue;
    double diskServiceTime;
    double usePercent;
    
    public FileSystemUsage() {
        this.total = 0L;
        this.free = 0L;
        this.used = 0L;
        this.avail = 0L;
        this.files = 0L;
        this.freeFiles = 0L;
        this.diskReads = 0L;
        this.diskWrites = 0L;
        this.diskReadBytes = 0L;
        this.diskWriteBytes = 0L;
        this.diskQueue = 0.0;
        this.diskServiceTime = 0.0;
        this.usePercent = 0.0;
    }
    
    public native void gather(final Sigar p0, final String p1) throws SigarException;
    
    static FileSystemUsage fetch(final Sigar sigar, final String name) throws SigarException {
        final FileSystemUsage fileSystemUsage = new FileSystemUsage();
        fileSystemUsage.gather(sigar, name);
        return fileSystemUsage;
    }
    
    public long getTotal() {
        return this.total;
    }
    
    public long getFree() {
        return this.free;
    }
    
    public long getUsed() {
        return this.used;
    }
    
    public long getAvail() {
        return this.avail;
    }
    
    public long getFiles() {
        return this.files;
    }
    
    public long getFreeFiles() {
        return this.freeFiles;
    }
    
    public long getDiskReads() {
        return this.diskReads;
    }
    
    public long getDiskWrites() {
        return this.diskWrites;
    }
    
    public long getDiskReadBytes() {
        return this.diskReadBytes;
    }
    
    public long getDiskWriteBytes() {
        return this.diskWriteBytes;
    }
    
    public double getDiskQueue() {
        return this.diskQueue;
    }
    
    public double getDiskServiceTime() {
        return this.diskServiceTime;
    }
    
    public double getUsePercent() {
        return this.usePercent;
    }
    
    void copyTo(final FileSystemUsage copy) {
        copy.total = this.total;
        copy.free = this.free;
        copy.used = this.used;
        copy.avail = this.avail;
        copy.files = this.files;
        copy.freeFiles = this.freeFiles;
        copy.diskReads = this.diskReads;
        copy.diskWrites = this.diskWrites;
        copy.diskReadBytes = this.diskReadBytes;
        copy.diskWriteBytes = this.diskWriteBytes;
        copy.diskQueue = this.diskQueue;
        copy.diskServiceTime = this.diskServiceTime;
        copy.usePercent = this.usePercent;
    }
    
    public Map toMap() {
        final Map map = new HashMap();
        final String strtotal = String.valueOf(this.total);
        if (!"-1".equals(strtotal)) {
            map.put("Total", strtotal);
        }
        final String strfree = String.valueOf(this.free);
        if (!"-1".equals(strfree)) {
            map.put("Free", strfree);
        }
        final String strused = String.valueOf(this.used);
        if (!"-1".equals(strused)) {
            map.put("Used", strused);
        }
        final String stravail = String.valueOf(this.avail);
        if (!"-1".equals(stravail)) {
            map.put("Avail", stravail);
        }
        final String strfiles = String.valueOf(this.files);
        if (!"-1".equals(strfiles)) {
            map.put("Files", strfiles);
        }
        final String strfreeFiles = String.valueOf(this.freeFiles);
        if (!"-1".equals(strfreeFiles)) {
            map.put("FreeFiles", strfreeFiles);
        }
        final String strdiskReads = String.valueOf(this.diskReads);
        if (!"-1".equals(strdiskReads)) {
            map.put("DiskReads", strdiskReads);
        }
        final String strdiskWrites = String.valueOf(this.diskWrites);
        if (!"-1".equals(strdiskWrites)) {
            map.put("DiskWrites", strdiskWrites);
        }
        final String strdiskReadBytes = String.valueOf(this.diskReadBytes);
        if (!"-1".equals(strdiskReadBytes)) {
            map.put("DiskReadBytes", strdiskReadBytes);
        }
        final String strdiskWriteBytes = String.valueOf(this.diskWriteBytes);
        if (!"-1".equals(strdiskWriteBytes)) {
            map.put("DiskWriteBytes", strdiskWriteBytes);
        }
        final String strdiskQueue = String.valueOf(this.diskQueue);
        if (!"-1".equals(strdiskQueue)) {
            map.put("DiskQueue", strdiskQueue);
        }
        final String strdiskServiceTime = String.valueOf(this.diskServiceTime);
        if (!"-1".equals(strdiskServiceTime)) {
            map.put("DiskServiceTime", strdiskServiceTime);
        }
        final String strusePercent = String.valueOf(this.usePercent);
        if (!"-1".equals(strusePercent)) {
            map.put("UsePercent", strusePercent);
        }
        return map;
    }
    
    public String toString() {
        return this.toMap().toString();
    }
}
