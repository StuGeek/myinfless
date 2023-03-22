// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.io.Serializable;

public class CpuPerc implements Serializable
{
    private static final long serialVersionUID = 1393671L;
    private double user;
    private double sys;
    private double nice;
    private double idle;
    private double wait;
    private double irq;
    private double softIrq;
    private double stolen;
    private double combined;
    
    CpuPerc() {
    }
    
    native void gather(final Sigar p0, final Cpu p1, final Cpu p2);
    
    static CpuPerc fetch(final Sigar sigar, final Cpu oldCpu, final Cpu curCpu) {
        final CpuPerc perc = new CpuPerc();
        perc.gather(sigar, oldCpu, curCpu);
        return perc;
    }
    
    public static CpuPerc calculate(final Cpu oldCpu, final Cpu curCpu) {
        final Sigar sigar = new Sigar();
        try {
            return fetch(sigar, oldCpu, curCpu);
        }
        finally {
            sigar.close();
        }
    }
    
    public double getUser() {
        return this.user;
    }
    
    public double getSys() {
        return this.sys;
    }
    
    public double getNice() {
        return this.nice;
    }
    
    public double getIdle() {
        return this.idle;
    }
    
    public double getWait() {
        return this.wait;
    }
    
    public double getIrq() {
        return this.irq;
    }
    
    public double getSoftIrq() {
        return this.softIrq;
    }
    
    public double getStolen() {
        return this.stolen;
    }
    
    public double getCombined() {
        return this.combined;
    }
    
    public static String format(final double val) {
        final String p = String.valueOf(val * 100.0);
        final int ix = p.indexOf(".") + 1;
        final String percent = p.substring(0, ix) + p.substring(ix, ix + 1);
        return percent + "%";
    }
    
    public String toString() {
        return "CPU states: " + format(this.user) + " user, " + format(this.sys) + " system, " + format(this.nice) + " nice, " + format(this.wait) + " wait, " + format(this.idle) + " idle";
    }
}
