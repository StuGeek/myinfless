// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class ProcStat implements Serializable
{
    private static final long serialVersionUID = 7546L;
    long total;
    long idle;
    long running;
    long sleeping;
    long stopped;
    long zombie;
    long threads;
    
    public ProcStat() {
        this.total = 0L;
        this.idle = 0L;
        this.running = 0L;
        this.sleeping = 0L;
        this.stopped = 0L;
        this.zombie = 0L;
        this.threads = 0L;
    }
    
    public native void gather(final Sigar p0) throws SigarException;
    
    static ProcStat fetch(final Sigar sigar) throws SigarException {
        final ProcStat procStat = new ProcStat();
        procStat.gather(sigar);
        return procStat;
    }
    
    public long getTotal() {
        return this.total;
    }
    
    public long getIdle() {
        return this.idle;
    }
    
    public long getRunning() {
        return this.running;
    }
    
    public long getSleeping() {
        return this.sleeping;
    }
    
    public long getStopped() {
        return this.stopped;
    }
    
    public long getZombie() {
        return this.zombie;
    }
    
    public long getThreads() {
        return this.threads;
    }
    
    void copyTo(final ProcStat copy) {
        copy.total = this.total;
        copy.idle = this.idle;
        copy.running = this.running;
        copy.sleeping = this.sleeping;
        copy.stopped = this.stopped;
        copy.zombie = this.zombie;
        copy.threads = this.threads;
    }
    
    public Map toMap() {
        final Map map = new HashMap();
        final String strtotal = String.valueOf(this.total);
        if (!"-1".equals(strtotal)) {
            map.put("Total", strtotal);
        }
        final String stridle = String.valueOf(this.idle);
        if (!"-1".equals(stridle)) {
            map.put("Idle", stridle);
        }
        final String strrunning = String.valueOf(this.running);
        if (!"-1".equals(strrunning)) {
            map.put("Running", strrunning);
        }
        final String strsleeping = String.valueOf(this.sleeping);
        if (!"-1".equals(strsleeping)) {
            map.put("Sleeping", strsleeping);
        }
        final String strstopped = String.valueOf(this.stopped);
        if (!"-1".equals(strstopped)) {
            map.put("Stopped", strstopped);
        }
        final String strzombie = String.valueOf(this.zombie);
        if (!"-1".equals(strzombie)) {
            map.put("Zombie", strzombie);
        }
        final String strthreads = String.valueOf(this.threads);
        if (!"-1".equals(strthreads)) {
            map.put("Threads", strthreads);
        }
        return map;
    }
    
    public String toString() {
        return this.toMap().toString();
    }
}
