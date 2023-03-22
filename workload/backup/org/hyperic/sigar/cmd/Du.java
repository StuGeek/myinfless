// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.cmd;

import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.DirUsage;

public class Du extends SigarCommandBase
{
    public Du(final Shell shell) {
        super(shell);
    }
    
    public Du() {
    }
    
    public String getUsageShort() {
        return "Display usage for a directory recursively";
    }
    
    protected boolean validateArgs(final String[] args) {
        return args.length == 1;
    }
    
    public void output(final String[] args) throws SigarException {
        final String dir = args[0];
        final DirUsage du = this.sigar.getDirUsage(dir);
        this.println(du.getDiskUsage() + "\t" + dir);
    }
    
    public static void main(final String[] args) throws Exception {
        new Du().processCommand(args);
    }
}
