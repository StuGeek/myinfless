// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.Collections;
import java.util.HashMap;
import java.io.PrintStream;
import java.util.Map;
import org.hyperic.sigar.jmx.CpuTimerMBean;

public class CpuTimer implements CpuTimerMBean
{
    private static final Map timers;
    private Sigar sigar;
    private long totalTime;
    private long cpuTotal;
    private long cpuUser;
    private long cpuSys;
    private long cpuSampleFirst;
    private long cpuSampleLast;
    private long cpuSampleTime;
    private ThreadCpu cpu;
    private long startTime;
    private long stopTime;
    
    public CpuTimer() {
        this(null);
    }
    
    public CpuTimer(final Sigar sigar) {
        this.cpu = new ThreadCpu();
        this.clear();
        this.sigar = sigar;
    }
    
    public void clear() {
        this.startTime = -1L;
        this.stopTime = -1L;
        this.totalTime = 0L;
        this.cpuTotal = 0L;
        this.cpuUser = 0L;
        this.cpuSys = 0L;
        this.cpuSampleFirst = 0L;
        this.cpuSampleLast = 0L;
        this.cpuSampleTime = 0L;
    }
    
    private void stamp(final CpuTimer timer) {
        if (this.cpuSampleFirst == 0L) {
            this.cpuSampleFirst = this.toMillis(timer.cpu.total);
            this.cpuSampleTime = timer.startTime;
        }
        else {
            this.cpuSampleLast = this.toMillis(timer.cpu.total);
        }
    }
    
    public void add(final CpuTimer timer) {
        this.stamp(timer);
        this.cpuTotal += timer.cpuTotal;
        this.cpuUser += timer.cpuUser;
        this.cpuSys += timer.cpuSys;
        this.totalTime += timer.totalTime;
    }
    
    public void start() {
        this.start(this.sigar);
    }
    
    public void start(final Sigar sigar) {
        this.startTime = System.currentTimeMillis();
        try {
            this.cpu.gather(sigar, 0L);
        }
        catch (SigarException e) {
            throw new IllegalArgumentException(e.toString());
        }
        this.stamp(this);
    }
    
    public void stop() {
        this.stop(this.sigar);
    }
    
    public void stop(final Sigar sigar) {
        final ThreadCpu diff = this.getDiff(sigar);
        this.cpuTotal += diff.total;
        this.cpuUser += diff.user;
        this.cpuSys += diff.sys;
        this.stopTime = System.currentTimeMillis();
        final double timeDiff = (double)(this.stopTime - this.startTime);
        this.totalTime += (long)timeDiff;
    }
    
    public ThreadCpu getDiff() {
        return this.getDiff(this.sigar);
    }
    
    public ThreadCpu getDiff(final Sigar sigar) {
        final long startTotal = this.cpu.total;
        final long startUser = this.cpu.user;
        final long startSys = this.cpu.sys;
        final ThreadCpu diff = new ThreadCpu();
        try {
            this.cpu.gather(sigar, 0L);
        }
        catch (SigarException e) {
            throw new IllegalArgumentException(e.toString());
        }
        diff.total = this.cpu.total - startTotal;
        diff.user = this.cpu.user - startUser;
        diff.sys = this.cpu.sys - startSys;
        this.stamp(this);
        return diff;
    }
    
    public long getTotalTime() {
        return this.totalTime;
    }
    
    private long toMillis(final long ns) {
        return ns / 1000000L;
    }
    
    public long getCpuTotal() {
        return this.toMillis(this.cpuTotal);
    }
    
    public long getCpuUser() {
        return this.toMillis(this.cpuUser);
    }
    
    public long getCpuSys() {
        return this.toMillis(this.cpuSys);
    }
    
    public double getCpuUsage() {
        if (this.cpuSampleFirst == 0L || this.cpuSampleLast == 0L) {
            return 0.0;
        }
        final long timeNow = System.currentTimeMillis();
        final double diff = (double)(timeNow - this.cpuSampleTime);
        if (diff == 0.0) {
            return 0.0;
        }
        final double usage = (this.cpuSampleLast - this.cpuSampleFirst) / diff;
        this.cpuSampleFirst = 0L;
        this.cpuSampleLast = 0L;
        this.cpuSampleTime = 0L;
        return usage;
    }
    
    public long getLastSampleTime() {
        return this.stopTime;
    }
    
    public static CpuTimer getInstance(final String name) {
        CpuTimer timer = CpuTimer.timers.get(name);
        if (timer == null) {
            timer = new CpuTimer();
            CpuTimer.timers.put(name, timer);
        }
        return timer;
    }
    
    public String format(final long elap) {
        final String fraction = elap % 1000L + "";
        int pad = 3 - fraction.length();
        final StringBuffer buf = new StringBuffer().append(elap / 1000L).append('.');
        while (pad-- > 0) {
            buf.append("0");
        }
        buf.append(fraction).append(" seconds");
        return buf.toString();
    }
    
    public void list(final PrintStream out) {
        out.println("real....." + this.format(this.getTotalTime()));
        out.println("user....." + this.format(this.getCpuUser()));
        out.println("sys......" + this.format(this.getCpuSys()));
        out.println("usage...." + CpuPerc.format(this.getCpuUsage()));
    }
    
    static {
        timers = Collections.synchronizedMap(new HashMap<Object, Object>());
    }
}
