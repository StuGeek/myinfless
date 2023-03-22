// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.cmd;

import org.hyperic.sigar.SigarException;

public class Pidof extends SigarCommandBase
{
    public Pidof(final Shell shell) {
        super(shell);
    }
    
    public Pidof() {
    }
    
    protected boolean validateArgs(final String[] args) {
        return args.length > 0;
    }
    
    public String getSyntaxArgs() {
        return "query";
    }
    
    public String getUsageShort() {
        return "Find the process ID of a running program";
    }
    
    public void output(final String[] args) throws SigarException {
        final long[] pids = this.shell.findPids(args);
        for (int i = 0; i < pids.length; ++i) {
            this.out.print(pids[i]);
            this.out.print(' ');
        }
        this.out.println();
    }
}
