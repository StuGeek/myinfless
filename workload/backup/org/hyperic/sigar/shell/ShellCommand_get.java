// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.shell;

import java.io.PrintStream;

public class ShellCommand_get extends ShellCommandBase
{
    private void printProperty(final String key, final String value) {
        final PrintStream out = this.getOutStream();
        out.print(key + "=");
        if (value.trim() != value) {
            out.println("'" + value + "'");
        }
        else {
            out.println(value);
        }
    }
    
    public void processCommand(final String[] args) throws ShellCommandUsageException, ShellCommandExecException {
        if (args.length < 1) {
            throw new ShellCommandUsageException(this.getSyntax());
        }
        for (int i = 0; i < args.length; ++i) {
            final String val = System.getProperty(args[i], "UNDEFINED");
            this.printProperty(args[i], val);
        }
    }
    
    public String getSyntaxArgs() {
        return "<key1> [key2] ...";
    }
    
    public String getUsageShort() {
        return "Get system properties";
    }
    
    public String getUsageHelp(final String[] args) {
        return "    " + this.getUsageShort() + ".";
    }
}
