// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.cmd;

import org.hyperic.sigar.SigarPermissionDeniedException;
import org.hyperic.sigar.SigarException;

public class ProcInfo extends SigarCommandBase
{
    private boolean isSingleProcess;
    
    public ProcInfo(final Shell shell) {
        super(shell);
    }
    
    public ProcInfo() {
    }
    
    protected boolean validateArgs(final String[] args) {
        return true;
    }
    
    public String getUsageShort() {
        return "Display all process info";
    }
    
    public boolean isPidCompleter() {
        return true;
    }
    
    public void output(final String[] args) throws SigarException {
        this.isSingleProcess = false;
        if (args.length != 0 && args[0].startsWith("-s")) {
            this.isSingleProcess = true;
        }
        if (this.isSingleProcess) {
            for (int i = 1; i < args.length; ++i) {
                try {
                    this.output(args[i]);
                }
                catch (SigarException e) {
                    this.println("(" + e.getMessage() + ")");
                }
                this.println("\n------------------------\n");
            }
        }
        else {
            final long[] pids = this.shell.findPids(args);
            for (int j = 0; j < pids.length; ++j) {
                try {
                    this.output(String.valueOf(pids[j]));
                }
                catch (SigarPermissionDeniedException e3) {
                    this.println(this.shell.getUserDeniedMessage(pids[j]));
                }
                catch (SigarException e2) {
                    this.println("(" + e2.getMessage() + ")");
                }
                this.println("\n------------------------\n");
            }
        }
    }
    
    public void output(final String pid) throws SigarException {
        this.println("pid=" + pid);
        try {
            this.println("state=" + this.sigar.getProcState(pid));
        }
        catch (SigarException e) {
            if (this.isSingleProcess) {
                this.println(e.getMessage());
            }
        }
        try {
            this.println("mem=" + this.sigar.getProcMem(pid));
        }
        catch (SigarException ex) {}
        try {
            this.println("cpu=" + this.sigar.getProcCpu(pid));
        }
        catch (SigarException ex2) {}
        try {
            this.println("cred=" + this.sigar.getProcCred(pid));
        }
        catch (SigarException ex3) {}
        try {
            this.println("credname=" + this.sigar.getProcCredName(pid));
        }
        catch (SigarException ex4) {}
    }
    
    public static void main(final String[] args) throws Exception {
        new ProcInfo().processCommand(args);
    }
}
