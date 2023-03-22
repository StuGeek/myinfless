// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.shell;

public class ShellCommand_quit extends ShellCommandBase
{
    public void processCommand(final String[] args) throws ShellCommandUsageException, ShellCommandExecException {
        throw new NormalQuitCommandException();
    }
    
    public String getUsageShort() {
        return "Terminate the shell";
    }
    
    public String getUsageHelp(final String[] args) {
        return "    Terminate the shell.";
    }
}
