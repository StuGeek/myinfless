// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class Cpu implements Serializable
{
    private static final long serialVersionUID = 8076L;
    long user;
    long sys;
    long nice;
    long idle;
    long wait;
    long irq;
    long softIrq;
    long stolen;
    long total;
    
    public Cpu() {
        this.user = 0L;
        this.sys = 0L;
        this.nice = 0L;
        this.idle = 0L;
        this.wait = 0L;
        this.irq = 0L;
        this.softIrq = 0L;
        this.stolen = 0L;
        this.total = 0L;
    }
    
    public native void gather(final Sigar p0) throws SigarException;
    
    static Cpu fetch(final Sigar sigar) throws SigarException {
        final Cpu cpu = new Cpu();
        cpu.gather(sigar);
        return cpu;
    }
    
    public long getUser() {
        return this.user;
    }
    
    public long getSys() {
        return this.sys;
    }
    
    public long getNice() {
        return this.nice;
    }
    
    public long getIdle() {
        return this.idle;
    }
    
    public long getWait() {
        return this.wait;
    }
    
    public long getIrq() {
        return this.irq;
    }
    
    public long getSoftIrq() {
        return this.softIrq;
    }
    
    public long getStolen() {
        return this.stolen;
    }
    
    public long getTotal() {
        return this.total;
    }
    
    void copyTo(final Cpu copy) {
        copy.user = this.user;
        copy.sys = this.sys;
        copy.nice = this.nice;
        copy.idle = this.idle;
        copy.wait = this.wait;
        copy.irq = this.irq;
        copy.softIrq = this.softIrq;
        copy.stolen = this.stolen;
        copy.total = this.total;
    }
    
    public Map toMap() {
        final Map map = new HashMap();
        final String struser = String.valueOf(this.user);
        if (!"-1".equals(struser)) {
            map.put("User", struser);
        }
        final String strsys = String.valueOf(this.sys);
        if (!"-1".equals(strsys)) {
            map.put("Sys", strsys);
        }
        final String strnice = String.valueOf(this.nice);
        if (!"-1".equals(strnice)) {
            map.put("Nice", strnice);
        }
        final String stridle = String.valueOf(this.idle);
        if (!"-1".equals(stridle)) {
            map.put("Idle", stridle);
        }
        final String strwait = String.valueOf(this.wait);
        if (!"-1".equals(strwait)) {
            map.put("Wait", strwait);
        }
        final String strirq = String.valueOf(this.irq);
        if (!"-1".equals(strirq)) {
            map.put("Irq", strirq);
        }
        final String strsoftIrq = String.valueOf(this.softIrq);
        if (!"-1".equals(strsoftIrq)) {
            map.put("SoftIrq", strsoftIrq);
        }
        final String strstolen = String.valueOf(this.stolen);
        if (!"-1".equals(strstolen)) {
            map.put("Stolen", strstolen);
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
