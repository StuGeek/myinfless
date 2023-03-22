// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class ProcCpu implements Serializable
{
    private static final long serialVersionUID = 6748L;
    double percent;
    long lastTime;
    long startTime;
    long user;
    long sys;
    long total;
    
    public ProcCpu() {
        this.percent = 0.0;
        this.lastTime = 0L;
        this.startTime = 0L;
        this.user = 0L;
        this.sys = 0L;
        this.total = 0L;
    }
    
    public native void gather(final Sigar p0, final long p1) throws SigarException;
    
    static ProcCpu fetch(final Sigar sigar, final long pid) throws SigarException {
        final ProcCpu procCpu = new ProcCpu();
        procCpu.gather(sigar, pid);
        return procCpu;
    }
    
    public double getPercent() {
        return this.percent;
    }
    
    public long getLastTime() {
        return this.lastTime;
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
    
    void copyTo(final ProcCpu copy) {
        copy.percent = this.percent;
        copy.lastTime = this.lastTime;
        copy.startTime = this.startTime;
        copy.user = this.user;
        copy.sys = this.sys;
        copy.total = this.total;
    }
    
    public Map toMap() {
        final Map map = new HashMap();
        final String strpercent = String.valueOf(this.percent);
        if (!"-1".equals(strpercent)) {
            map.put("Percent", strpercent);
        }
        final String strlastTime = String.valueOf(this.lastTime);
        if (!"-1".equals(strlastTime)) {
            map.put("LastTime", strlastTime);
        }
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
