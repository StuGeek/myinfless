// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.cmd;

import org.hyperic.sigar.ProcExe;
import org.hyperic.sigar.ProcFd;
import org.hyperic.sigar.SigarNotImplementedException;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarPermissionDeniedException;

public class ProcFileInfo extends SigarCommandBase
{
    public ProcFileInfo(final Shell shell) {
        super(shell);
    }
    
    public ProcFileInfo() {
    }
    
    protected boolean validateArgs(final String[] args) {
        return true;
    }
    
    public String getUsageShort() {
        return "Display process file info";
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
            catch (SigarPermissionDeniedException e2) {
                this.println(this.shell.getUserDeniedMessage(pids[i]));
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
            final ProcFd fd = this.sigar.getProcFd(pid);
            this.println("open file descriptors=" + fd.getTotal());
        }
        catch (SigarNotImplementedException ex) {}
        final ProcExe exe = this.sigar.getProcExe(pid);
        String name = exe.getName();
        if (name.length() == 0) {
            name = "unknown";
        }
        this.println("name=" + name);
        this.println("cwd=" + exe.getCwd());
    }
    
    public static void main(final String[] args) throws Exception {
        new ProcFileInfo().processCommand(args);
    }
}
