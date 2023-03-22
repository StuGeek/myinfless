// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class ProcTime implements Serializable
{
    private static final long serialVersionUID = 4030L;
    long startTime;
    long user;
    long sys;
    long total;
    
    public ProcTime() {
        this.startTime = 0L;
        this.user = 0L;
        this.sys = 0L;
        this.total = 0L;
    }
    
    public native void gather(final Sigar p0, final long p1) throws SigarException;
    
    static ProcTime fetch(final Sigar sigar, final long pid) throws SigarException {
        final ProcTime procTime = new ProcTime();
        procTime.gather(sigar, pid);
        return procTime;
    }
    
    public long getStartTime() {
        return this.startTime;
    }
    
    public long getUser() {
        return this.user;
    }
    
    public long getSys() {
        return this.sys;
    }
    
    public long getTotal() {
        return this.total;
    }
    
    void copyTo(final ProcTime copy) {
        copy.startTime = this.startTime;
        copy.user = this.user;
        copy.sys = this.sys;
        copy.total = this.total;
    }
    
    public Map toMap() {
        final Map map = new HashMap();
        final String strstartTime = String.valueOf(this.startTime);
        if (!"-1".equals(strstartTime)) {
            map.put("StartTime", strstartTime);
        }
        final String struser = String.valueOf(this.user);
        if (!"-1".equals(struser)) {
            map.put("User", struser);
        }
        final String strsys = String.valueOf(this.sys);
        if (!"-1".equals(strsys)) {
            map.put("Sys", strsys);
        }
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
