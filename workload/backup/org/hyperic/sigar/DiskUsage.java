// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class DiskUsage implements Serializable
{
    private static final long serialVersionUID = 8090L;
    long reads;
    long writes;
    long readBytes;
    long writeBytes;
    double queue;
    double serviceTime;
    
    public DiskUsage() {
        this.reads = 0L;
        this.writes = 0L;
        this.readBytes = 0L;
        this.writeBytes = 0L;
        this.queue = 0.0;
        this.serviceTime = 0.0;
    }
    
    public native void gather(final Sigar p0, final String p1) throws SigarException;
    
    static DiskUsage fetch(final Sigar sigar, final String name) throws SigarException {
        final DiskUsage diskUsage = new DiskUsage();
        diskUsage.gather(sigar, name);
        return diskUsage;
    }
    
    public long getReads() {
        return this.reads;
    }
    
    public long getWrites() {
        return this.writes;
    }
    
    public long getReadBytes() {
        return this.readBytes;
    }
    
    public long getWriteBytes() {
        return this.writeBytes;
    }
    
    public double getQueue() {
        return this.queue;
    }
    
    public double getServiceTime() {
        return this.serviceTime;
    }
    
    void copyTo(final DiskUsage copy) {
        copy.reads = this.reads;
        copy.writes = this.writes;
        copy.readBytes = this.readBytes;
        copy.writeBytes = this.writeBytes;
        copy.queue = this.queue;
        copy.serviceTime = this.serviceTime;
    }
    
    public Map toMap() {
        final Map map = new HashMap();
        final String strreads = String.valueOf(this.reads);
        if (!"-1".equals(strreads)) {
            map.put("Reads", strreads);
        }
        final String strwrites = String.valueOf(this.writes);
        if (!"-1".equals(strwrites)) {
            map.put("Writes", strwrites);
        }
        final String strreadBytes = String.valueOf(this.readBytes);
        if (!"-1".equals(strreadBytes)) {
            map.put("ReadBytes", strreadBytes);
        }
        final String strwriteBytes = String.valueOf(this.writeBytes);
        if (!"-1".equals(strwriteBytes)) {
            map.put("WriteBytes", strwriteBytes);
        }
        final String strqueue = String.valueOf(this.queue);
        if (!"-1".equals(strqueue)) {
            map.put("Queue", strqueue);
        }
        final String strserviceTime = String.valueOf(this.serviceTime);
        if (!"-1".equals(strserviceTime)) {
            map.put("ServiceTime", strserviceTime);
        }
        return map;
    }
    
    public String toString() {
        return this.toMap().toString();
    }
}
