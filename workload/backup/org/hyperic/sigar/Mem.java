// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class Mem implements Serializable
{
    private static final long serialVersionUID = 10181L;
    long total;
    long ram;
    long used;
    long free;
    long actualUsed;
    long actualFree;
    double usedPercent;
    double freePercent;
    
    public Mem() {
        this.total = 0L;
        this.ram = 0L;
        this.used = 0L;
        this.free = 0L;
        this.actualUsed = 0L;
        this.actualFree = 0L;
        this.usedPercent = 0.0;
        this.freePercent = 0.0;
    }
    
    public native void gather(final Sigar p0) throws SigarException;
    
    static Mem fetch(final Sigar sigar) throws SigarException {
        final Mem mem = new Mem();
        mem.gather(sigar);
        return mem;
    }
    
    public long getTotal() {
        return this.total;
    }
    
    public long getRam() {
        return this.ram;
    }
    
    public long getUsed() {
        return this.used;
    }
    
    public long getFree() {
        return this.free;
    }
    
    public long getActualUsed() {
        return this.actualUsed;
    }
    
    public long getActualFree() {
        return this.actualFree;
    }
    
    public double getUsedPercent() {
        return this.usedPercent;
    }
    
    public double getFreePercent() {
        return this.freePercent;
    }
    
    void copyTo(final Mem copy) {
        copy.total = this.total;
        copy.ram = this.ram;
        copy.used = this.used;
        copy.free = this.free;
        copy.actualUsed = this.actualUsed;
        copy.actualFree = this.actualFree;
        copy.usedPercent = this.usedPercent;
        copy.freePercent = this.freePercent;
    }
    
    public String toString() {
        return "Mem: " + this.total / 1024L + "K av, " + this.used / 1024L + "K used, " + this.free / 1024L + "K free";
    }
    
    public Map toMap() {
        final Map map = new HashMap();
        final String strtotal = String.valueOf(this.total);
        if (!"-1".equals(strtotal)) {
            map.put("Total", strtotal);
        }
        final String strram = String.valueOf(this.ram);
        if (!"-1".equals(strram)) {
            map.put("Ram", strram);
        }
        final String strused = String.valueOf(this.used);
        if (!"-1".equals(strused)) {
            map.put("Used", strused);
        }
        final String strfree = String.valueOf(this.free);
        if (!"-1".equals(strfree)) {
            map.put("Free", strfree);
        }
        final String stractualUsed = String.valueOf(this.actualUsed);
        if (!"-1".equals(stractualUsed)) {
            map.put("ActualUsed", stractualUsed);
        }
        final String stractualFree = String.valueOf(this.actualFree);
        if (!"-1".equals(stractualFree)) {
            map.put("ActualFree", stractualFree);
        }
        final String strusedPercent = String.valueOf(this.usedPercent);
        if (!"-1".equals(strusedPercent)) {
            map.put("UsedPercent", strusedPercent);
        }
        final String strfreePercent = String.valueOf(this.freePercent);
        if (!"-1".equals(strfreePercent)) {
            map.put("FreePercent", strfreePercent);
        }
        return map;
    }
}
