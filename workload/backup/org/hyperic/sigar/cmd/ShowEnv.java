// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.cmd;

import java.util.Iterator;
import java.util.Map;
import org.hyperic.sigar.SigarException;

public class ShowEnv extends SigarCommandBase
{
    public ShowEnv(final Shell shell) {
        super(shell);
    }
    
    public ShowEnv() {
    }
    
    protected boolean validateArgs(final String[] args) {
        return true;
    }
    
    public String getUsageShort() {
        return "Show process environment";
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
        final Map env = this.proxy.getProcEnv(pid);
        for (final Map.Entry ent : env.entrySet()) {
            this.println(ent.getKey() + "=" + ent.getValue());
        }
    }
    
    public static void main(final String[] args) throws Exception {
        new ShowEnv().processCommand(args);
    }
}
