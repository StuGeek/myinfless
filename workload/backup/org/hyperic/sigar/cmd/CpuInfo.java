// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.cmd;

import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarLoader;
import org.hyperic.sigar.CpuPerc;

public class CpuInfo extends SigarCommandBase
{
    public boolean displayTimes;
    
    public CpuInfo(final Shell shell) {
        super(shell);
        this.displayTimes = true;
    }
    
    public CpuInfo() {
        this.displayTimes = true;
    }
    
    public String getUsageShort() {
        return "Display cpu information";
    }
    
    private void output(final CpuPerc cpu) {
        this.println("User Time....." + CpuPerc.format(cpu.getUser()));
        this.println("Sys Time......" + CpuPerc.format(cpu.getSys()));
        this.println("Idle Time....." + CpuPerc.format(cpu.getIdle()));
        this.println("Wait Time....." + CpuPerc.format(cpu.getWait()));
        this.println("Nice Time....." + CpuPerc.format(cpu.getNice()));
        this.println("Combined......" + CpuPerc.format(cpu.getCombined()));
        this.println("Irq Time......" + CpuPerc.format(cpu.getIrq()));
        if (SigarLoader.IS_LINUX) {
            this.println("SoftIrq Time.." + CpuPerc.format(cpu.getSoftIrq()));
            this.println("Stolen Time...." + CpuPerc.format(cpu.getStolen()));
        }
        this.println("");
    }
    
    public void output(final String[] args) throws SigarException {
        final org.hyperic.sigar.CpuInfo[] infos = this.sigar.getCpuInfoList();
        final CpuPerc[] cpus = this.sigar.getCpuPercList();
        final org.hyperic.sigar.CpuInfo info = infos[0];
        final long cacheSize = info.getCacheSize();
        this.println("Vendor........." + info.getVendor());
        this.println("Model.........." + info.getModel());
        this.println("Mhz............" + info.getMhz());
        this.println("Total CPUs....." + info.getTotalCores());
        if (info.getTotalCores() != info.getTotalSockets() || info.getCoresPerSocket() > info.getTotalCores()) {
            this.println("Physical CPUs.." + info.getTotalSockets());
            this.println("Cores per CPU.." + info.getCoresPerSocket());
        }
        if (cacheSize != -1L) {
            this.println("Cache size...." + cacheSize);
        }
        this.println("");
        if (!this.displayTimes) {
            return;
        }
        for (int i = 0; i < cpus.length; ++i) {
            this.println("CPU " + i + ".........");
            this.output(cpus[i]);
        }
        this.println("Totals........");
        this.output(this.sigar.getCpuPerc());
    }
    
    public static void main(final String[] args) throws Exception {
        new CpuInfo().processCommand(args);
    }
}
