// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.shell;

import java.io.IOException;
import java.io.File;
import org.hyperic.sigar.util.GetlineCompleter;

public class ShellCommand_source extends ShellCommandBase implements GetlineCompleter
{
    public String complete(final String line) {
        return new FileCompleter(this.getShell()).complete(line);
    }
    
    public void processCommand(final String[] args) throws ShellCommandUsageException, ShellCommandExecException {
        if (args.length != 1) {
            throw new ShellCommandUsageException("Syntax: " + this.getCommandName() + " <rcfile>");
        }
        final File rcFile = new File(FileCompleter.expand(args[0]));
        if (!rcFile.isFile()) {
            throw new ShellCommandExecException("File '" + rcFile + "' not found");
        }
        try {
            this.getShell().readRCFile(rcFile, true);
        }
        catch (IOException exc) {
            throw new ShellCommandExecException("Error reading file '" + rcFile + ": " + exc.getMessage());
        }
    }
    
    public String getSyntaxArgs() {
        return "<rcfile>";
    }
    
    public String getUsageShort() {
        return "Read a file, executing the contents";
    }
    
    public String getUsageHelp(final String[] args) {
        return "    " + this.getUsageShort() + ".  The file must contain " + "commands\n    which are executable by the shell.";
    }
}
