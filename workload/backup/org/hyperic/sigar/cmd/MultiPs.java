// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.cmd;

import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.ProcMem;
import org.hyperic.sigar.MultiProcCpu;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.CpuPerc;

public class MultiPs extends SigarCommandBase
{
    public MultiPs(final Shell shell) {
        super(shell);
    }
    
    public MultiPs() {
    }
    
    protected boolean validateArgs(final String[] args) {
        return args.length == 1;
    }
    
    public String getSyntaxArgs() {
        return "query";
    }
    
    public String getUsageShort() {
        return "Show multi process status";
    }
    
    public boolean isPidCompleter() {
        return true;
    }
    
    public void output(final String[] args) throws SigarException {
        final String query = args[0];
        final MultiProcCpu cpu = this.proxy.getMultiProcCpu(query);
        this.println("Number of processes: " + cpu.getProcesses());
        this.println("Cpu usage: " + CpuPerc.format(cpu.getPercent()));
        this.println("Cpu time: " + Ps.getCpuTime(cpu.getTotal()));
        final ProcMem mem = this.proxy.getMultiProcMem(query);
        this.println("Size: " + Sigar.formatSize(mem.getSize()));
        this.println("Resident: " + Sigar.formatSize(mem.getResident()));
        this.println("Share: " + Sigar.formatSize(mem.getShare()));
    }
    
    public static void main(final String[] args) throws Exception {
        new MultiPs().processCommand(args);
    }
}
