// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.cmd;

import java.util.List;
import org.hyperic.sigar.SigarNotImplementedException;
import org.hyperic.sigar.SigarException;

public class ProcModuleInfo extends SigarCommandBase
{
    public ProcModuleInfo(final Shell shell) {
        super(shell);
    }
    
    public ProcModuleInfo() {
    }
    
    protected boolean validateArgs(final String[] args) {
        return true;
    }
    
    public String getUsageShort() {
        return "Display process module info";
    }
    
    public boolean isPidCompleter() {
        return true;
    }
    
    public void output(final String[] args) throws SigarException {
        final long[] pids = this.shell.findPids(args);
        for (int i = 0; i < pids.length; ++i) {
            try {
                this.output(pids[i]);
            }
            catch (SigarException e) {
                this.println("(" + e.getMessage() + ")");
            }
            this.println("\n------------------------\n");
        }
    }
    
    public void output(final long pid) throws SigarException {
        this.println("pid=" + pid);
        try {
            final List modules = this.sigar.getProcModules(pid);
            for (int i = 0; i < modules.size(); ++i) {
                this.println(i + "=" + modules.get(i));
            }
        }
        catch (SigarNotImplementedException e) {
            throw e;
        }
        catch (SigarException e2) {
            this.println("[" + e2.getMessage() + "]");
        }
    }
    
    public static void main(final String[] args) throws Exception {
        new ProcModuleInfo().processCommand(args);
    }
}
