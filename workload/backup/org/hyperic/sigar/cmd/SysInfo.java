// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.cmd;

import org.hyperic.sigar.SigarException;
import java.util.Arrays;

public class SysInfo extends SigarCommandBase
{
    public SysInfo(final Shell shell) {
        super(shell);
    }
    
    public SysInfo() {
    }
    
    public String getUsageShort() {
        return "Display system information";
    }
    
    public void output(final String[] args) throws SigarException {
        Version.printInfo(this.out);
        this.println("");
        new Uptime(this.shell).output(args);
        this.println("");
        final CpuInfo cpuinfo = new CpuInfo(this.shell);
        cpuinfo.displayTimes = false;
        cpuinfo.output(args);
        this.println("");
        new Free(this.shell).output(args);
        this.println("");
        this.println("File Systems........." + Arrays.asList(this.sigar.getFileSystemList()));
        this.println("");
        this.println("Network Interfaces..." + Arrays.asList(this.sigar.getNetInterfaceList()));
        this.println("");
        this.println("System resource limits:");
        new Ulimit(this.shell).output(args);
    }
    
    public static void main(final String[] args) throws Exception {
        new SysInfo().processCommand(args);
    }
}
