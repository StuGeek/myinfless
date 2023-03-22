// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class ProcMem implements Serializable
{
    private static final long serialVersionUID = 7985L;
    long size;
    long resident;
    long share;
    long minorFaults;
    long majorFaults;
    long pageFaults;
    
    public ProcMem() {
        this.size = 0L;
        this.resident = 0L;
        this.share = 0L;
        this.minorFaults = 0L;
        this.majorFaults = 0L;
        this.pageFaults = 0L;
    }
    
    public native void gather(final Sigar p0, final long p1) throws SigarException;
    
    static ProcMem fetch(final Sigar sigar, final long pid) throws SigarException {
        final ProcMem procMem = new ProcMem();
        procMem.gather(sigar, pid);
        return procMem;
    }
    
    public long getSize() {
        return this.size;
    }
    
    public long getResident() {
        return this.resident;
    }
    
    public long getShare() {
        return this.share;
    }
    
    public long getMinorFaults() {
        return this.minorFaults;
    }
    
    public long getMajorFaults() {
        return this.majorFaults;
    }
    
    public long getPageFaults() {
        return this.pageFaults;
    }
    
    void copyTo(final ProcMem copy) {
        copy.size = this.size;
        copy.resident = this.resident;
        copy.share = this.share;
        copy.minorFaults = this.minorFaults;
        copy.majorFaults = this.majorFaults;
        copy.pageFaults = this.pageFaults;
    }
    
    public long getRss() {
        return this.getResident();
    }
    
    public long getVsize() {
        return this.getSize();
    }
    
    public Map toMap() {
        final Map map = new HashMap();
        final String strsize = String.valueOf(this.size);
        if (!"-1".equals(strsize)) {
            map.put("Size", strsize);
        }
        final String strresident = String.valueOf(this.resident);
        if (!"-1".equals(strresident)) {
            map.put("Resident", strresident);
        }
        final String strshare = String.valueOf(this.share);
        if (!"-1".equals(strshare)) {
            map.put("Share", strshare);
        }
        final String strminorFaults = String.valueOf(this.minorFaults);
        if (!"-1".equals(strminorFaults)) {
            map.put("MinorFaults", strminorFaults);
        }
        final String strmajorFaults = String.valueOf(this.majorFaults);
        if (!"-1".equals(strmajorFaults)) {
            map.put("MajorFaults", strmajorFaults);
        }
        final String strpageFaults = String.valueOf(this.pageFaults);
        if (!"-1".equals(strpageFaults)) {
            map.put("PageFaults", strpageFaults);
        }
        return map;
    }
    
    public String toString() {
        return this.toMap().toString();
    }
}
