// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.shell;

public class ShellCommand_sleep extends ShellCommandBase
{
    public void processCommand(final String[] args) throws ShellCommandUsageException, ShellCommandExecException {
        if (args.length != 1) {
            throw new ShellCommandUsageException(this.getSyntax());
        }
        try {
            Thread.sleep(Integer.parseInt(args[0]) * 1000);
        }
        catch (NumberFormatException exc) {
            throw new ShellCommandExecException("Invalid time '" + args[0] + "' -- must be an integer");
        }
        catch (InterruptedException exc2) {
            throw new ShellCommandExecException("Sleep interrupted");
        }
    }
    
    public String getSyntaxArgs() {
        return "<numSeconds>";
    }
    
    public String getUsageShort() {
        return "Delay execution for the a number of seconds ";
    }
    
    public String getUsageHelp(final String[] args) {
        return "    " + this.getUsageShort() + ".";
    }
}
