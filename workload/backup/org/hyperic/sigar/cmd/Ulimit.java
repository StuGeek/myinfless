// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.cmd;

import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.ResourceLimit;
import org.hyperic.sigar.jmx.SigarInvokerJMX;

public class Ulimit extends SigarCommandBase
{
    private SigarInvokerJMX invoker;
    private String mode;
    
    public Ulimit(final Shell shell) {
        super(shell);
    }
    
    public Ulimit() {
    }
    
    public String getUsageShort() {
        return "Display system resource limits";
    }
    
    protected boolean validateArgs(final String[] args) {
        return true;
    }
    
    private static String format(final long val) {
        if (val == ResourceLimit.INFINITY()) {
            return "unlimited";
        }
        return String.valueOf(val);
    }
    
    private String getValue(final String attr) throws SigarException {
        final Long val = (Long)this.invoker.invoke(attr + this.mode);
        return format(val);
    }
    
    public void output(final String[] args) throws SigarException {
        this.mode = "Cur";
        this.invoker = SigarInvokerJMX.getInstance(this.proxy, "Type=ResourceLimit");
        for (int i = 0; i < args.length; ++i) {
            final String arg = args[i];
            if (arg.equals("-H")) {
                this.mode = "Max";
            }
            else {
                if (!arg.equals("-S")) {
                    throw new SigarException("Unknown argument: " + arg);
                }
                this.mode = "Cur";
            }
        }
        this.println("core file size......." + this.getValue("Core"));
        this.println("data seg size........" + this.getValue("Data"));
        this.println("file size............" + this.getValue("FileSize"));
        this.println("pipe size............" + this.getValue("PipeSize"));
        this.println("max memory size......" + this.getValue("Memory"));
        this.println("open files..........." + this.getValue("OpenFiles"));
        this.println("stack size..........." + this.getValue("Stack"));
        this.println("cpu time............." + this.getValue("Cpu"));
        this.println("max user processes..." + this.getValue("Processes"));
        this.println("virtual memory......." + this.getValue("VirtualMemory"));
    }
    
    public static void main(final String[] args) throws Exception {
        new Ulimit().processCommand(args);
    }
}
