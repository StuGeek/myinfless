// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.shell;

import java.io.PrintStream;

public class ShellCommandBase implements ShellCommandHandler
{
    protected String itsCommandName;
    protected ShellBase itsShell;
    private PrintStream out;
    
    public ShellCommandBase() {
        this.itsCommandName = null;
        this.itsShell = null;
        this.out = null;
    }
    
    public String getCommandName() {
        return this.itsCommandName;
    }
    
    public ShellBase getShell() {
        return this.itsShell;
    }
    
    public PrintStream getOutStream() {
        return this.getShell().getOutStream();
    }
    
    public PrintStream getErrStream() {
        return this.getShell().getErrStream();
    }
    
    public void init(final String commandName, final ShellBase shell) throws ShellCommandInitException {
        this.itsCommandName = commandName;
        this.itsShell = shell;
    }
    
    public void processCommand(final String[] args) throws ShellCommandUsageException, ShellCommandExecException {
        this.out.println("ShellCommandBase: not implemented: " + this.itsCommandName);
    }
    
    public String getSyntax() {
        return "Syntax: " + this.getCommandName() + " " + this.getSyntaxArgs();
    }
    
    public String getSyntaxArgs() {
        return "";
    }
    
    public String getUsageShort() {
        return "";
    }
    
    public String getUsageHelp(final String[] args) {
        return "Help not available for command " + this.itsCommandName;
    }
}
