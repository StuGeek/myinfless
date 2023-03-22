// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.HashMap;
import org.hyperic.sigar.ptql.ProcessFinder;
import java.util.Map;

public class MultiProcCpu extends ProcCpu
{
    private long pid;
    private int nproc;
    private static Map ptable;
    
    public MultiProcCpu() {
        this.nproc = 0;
    }
    
    static synchronized MultiProcCpu get(final Sigar sigar, final String query) throws SigarException {
        MultiProcCpu cpu = MultiProcCpu.ptable.get(query);
        if (cpu == null) {
            cpu = new MultiProcCpu();
            cpu.pid = query.hashCode();
            MultiProcCpu.ptable.put(query, cpu);
        }
        final long timeNow = System.currentTimeMillis();
        final double diff = (double)(timeNow - cpu.lastTime);
        if (diff == 0.0) {
            return cpu;
        }
        cpu.lastTime = timeNow;
        final long otime = cpu.total;
        cpu.total = 0L;
        cpu.user = 0L;
        cpu.sys = 0L;
        cpu.nproc = 0;
        final long[] pids = ProcessFinder.find(sigar, query);
        cpu.nproc = pids.length;
        for (int i = 0; i < pids.length; ++i) {
            ProcTime time;
            try {
                time = sigar.getProcTime(pids[i]);
            }
            catch (SigarException e) {
                continue;
            }
            final MultiProcCpu multiProcCpu = cpu;
            multiProcCpu.total += time.total;
            final MultiProcCpu multiProcCpu2 = cpu;
            multiProcCpu2.user += time.user;
            final MultiProcCpu multiProcCpu3 = cpu;
            multiProcCpu3.sys += time.sys;
        }
        if (otime == 0L) {
            return cpu;
        }
        cpu.percent = (cpu.total - otime) / diff;
        if (cpu.percent < 0.0) {
            cpu.percent = 0.0 - cpu.percent;
        }
        if (cpu.percent >= 1.0) {
            cpu.percent = 0.99;
        }
        return cpu;
    }
    
    public double getPercent() {
        return this.percent;
    }
    
    public int getProcesses() {
        return this.nproc;
    }
    
    public int hashCode() {
        return (int)this.pid;
    }
    
    public boolean equals(final Object cpu) {
        return cpu instanceof MultiProcCpu && ((MultiProcCpu)cpu).pid == this.pid;
    }
    
    static {
        MultiProcCpu.ptable = new HashMap();
    }
}
