// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.shell;

public interface ShellCommandHandler
{
    void init(final String p0, final ShellBase p1) throws ShellCommandInitException;
    
    void processCommand(final String[] p0) throws ShellCommandUsageException, ShellCommandExecException;
    
    String getUsageHelp(final String[] p0);
    
    String getUsageShort();
    
    String getSyntax();
}
