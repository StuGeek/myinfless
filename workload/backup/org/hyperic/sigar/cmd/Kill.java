// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.cmd;

import org.hyperic.sigar.SigarException;

public class Kill extends SigarCommandBase
{
    public Kill(final Shell shell) {
        super(shell);
    }
    
    public Kill() {
    }
    
    protected boolean validateArgs(final String[] args) {
        return args.length == 1 || args.length == 2;
    }
    
    public String getSyntaxArgs() {
        return "[signal] <query|pid>";
    }
    
    public String getUsageShort() {
        return "Send signal to a process";
    }
    
    public boolean isPidCompleter() {
        return true;
    }
    
    public void output(final String[] args) throws SigarException {
        String signal = "SIGTERM";
        String query;
        if (args.length == 2) {
            signal = args[0];
            query = args[1];
        }
        else {
            query = args[0];
        }
        final long[] pids = this.shell.findPids(new String[] { query });
        for (int i = 0; i < pids.length; ++i) {
            this.println("kill " + signal + " " + pids[i]);
            this.sigar.kill(pids[i], signal);
        }
    }
    
    public static void main(final String[] args) throws Exception {
        new Kill().processCommand(args);
    }
}
