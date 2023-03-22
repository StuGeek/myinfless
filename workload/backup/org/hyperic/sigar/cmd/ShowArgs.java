// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.cmd;

import org.hyperic.sigar.SigarNotImplementedException;
import org.hyperic.sigar.SigarException;

public class ShowArgs extends SigarCommandBase
{
    public ShowArgs(final Shell shell) {
        super(shell);
    }
    
    public ShowArgs() {
    }
    
    protected boolean validateArgs(final String[] args) {
        return true;
    }
    
    public String getUsageShort() {
        return "Show process command line arguments";
    }
    
    public boolean isPidCompleter() {
        return true;
    }
    
    public void output(final String[] args) throws SigarException {
        final long[] pids = this.shell.findPids(args);
        for (int i = 0; i < pids.length; ++i) {
            try {
                this.println("pid=" + pids[i]);
                this.output(pids[i]);
            }
            catch (SigarException e) {
                this.println(e.getMessage());
            }
            this.println("\n------------------------\n");
        }
    }
    
    public void output(final long pid) throws SigarException {
        final String[] argv = this.proxy.getProcArgs(pid);
        try {
            final String exe = this.proxy.getProcExe(pid).getName();
            this.println("exe=" + exe);
        }
        catch (SigarNotImplementedException e) {}
        catch (SigarException e2) {
            this.println("exe=???");
        }
        try {
            final String cwd = this.proxy.getProcExe(pid).getCwd();
            this.println("cwd=" + cwd);
        }
        catch (SigarNotImplementedException e) {}
        catch (SigarException e2) {
            this.println("cwd=???");
        }
        for (int i = 0; i < argv.length; ++i) {
            this.println("   " + i + "=>" + argv[i] + "<=");
        }
    }
    
    public static void main(final String[] args) throws Exception {
        new ShowArgs().processCommand(args);
    }
}
