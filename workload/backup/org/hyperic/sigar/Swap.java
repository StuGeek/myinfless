// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class Swap implements Serializable
{
    private static final long serialVersionUID = 4974L;
    long total;
    long used;
    long free;
    long pageIn;
    long pageOut;
    
    public Swap() {
        this.total = 0L;
        this.used = 0L;
        this.free = 0L;
        this.pageIn = 0L;
        this.pageOut = 0L;
    }
    
    public native void gather(final Sigar p0) throws SigarException;
    
    static Swap fetch(final Sigar sigar) throws SigarException {
        final Swap swap = new Swap();
        swap.gather(sigar);
        return swap;
    }
    
    public long getTotal() {
        return this.total;
    }
    
    public long getUsed() {
        return this.used;
    }
    
    public long getFree() {
        return this.free;
    }
    
    public long getPageIn() {
        return this.pageIn;
    }
    
    public long getPageOut() {
        return this.pageOut;
    }
    
    void copyTo(final Swap copy) {
        copy.total = this.total;
        copy.used = this.used;
        copy.free = this.free;
        copy.pageIn = this.pageIn;
        copy.pageOut = this.pageOut;
    }
    
    public String toString() {
        return "Swap: " + this.total / 1024L + "K av, " + this.used / 1024L + "K used, " + this.free / 1024L + "K free";
    }
    
    public Map toMap() {
        final Map map = new HashMap();
        final String strtotal = String.valueOf(this.total);
        if (!"-1".equals(strtotal)) {
            map.put("Total", strtotal);
        }
        final String strused = String.valueOf(this.used);
        if (!"-1".equals(strused)) {
            map.put("Used", strused);
        }
        final String strfree = String.valueOf(this.free);
        if (!"-1".equals(strfree)) {
            map.put("Free", strfree);
        }
        final String strpageIn = String.valueOf(this.pageIn);
        if (!"-1".equals(strpageIn)) {
            map.put("PageIn", strpageIn);
        }
        final String strpageOut = String.valueOf(this.pageOut);
        if (!"-1".equals(strpageOut)) {
            map.put("PageOut", strpageOut);
        }
        return map;
    }
}
