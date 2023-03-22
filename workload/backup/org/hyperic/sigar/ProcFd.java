// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class ProcFd implements Serializable
{
    private static final long serialVersionUID = 948L;
    long total;
    
    public ProcFd() {
        this.total = 0L;
    }
    
    public native void gather(final Sigar p0, final long p1) throws SigarException;
    
    static ProcFd fetch(final Sigar sigar, final long pid) throws SigarException {
        final ProcFd procFd = new ProcFd();
        procFd.gather(sigar, pid);
        return procFd;
    }
    
    public long getTotal() {
        return this.total;
    }
    
    void copyTo(final ProcFd copy) {
        copy.total = this.total;
    }
    
    public Map toMap() {
        final Map map = new HashMap();
        final String strtotal = String.valueOf(this.total);
        if (!"-1".equals(strtotal)) {
            map.put("Total", strtotal);
        }
        return map;
    }
    
    public String toString() {
        return this.toMap().toString();
    }
}
