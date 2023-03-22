// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.shell;

import java.util.Iterator;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.ArrayList;
import org.hyperic.sigar.util.PrintfFormat;

public class ShellCommand_help extends ShellCommandBase
{
    public void processCommand(final String[] args) throws ShellCommandUsageException, ShellCommandExecException {
        final PrintStream out = this.getOutStream();
        if (args.length == 0) {
            final PrintfFormat fmt = new PrintfFormat("\t%-14s - %s");
            final Object[] fArgs = new Object[2];
            final ArrayList cmdNamesList = new ArrayList();
            final Iterator i = this.itsShell.getCommandNameIterator();
            while (i.hasNext()) {
                cmdNamesList.add(i.next());
            }
            final String[] cmdNames = cmdNamesList.toArray(new String[0]);
            Arrays.sort(cmdNames);
            out.println("Available commands:");
            for (int j = 0; j < cmdNames.length; ++j) {
                final ShellCommandHandler handler = this.itsShell.getHandler(cmdNames[j]);
                fArgs[0] = cmdNames[j];
                fArgs[1] = handler.getUsageShort();
                out.println(fmt.sprintf(fArgs));
            }
            return;
        }
        ShellCommandHandler lastHandler;
        ShellCommandHandler handler = lastHandler = this.itsShell.getHandler(args[0]);
        int useArgs = 1;
        for (int k = 1; k < args.length && !args[k].startsWith("-"); ++k) {
            if (null == handler) {
                handler = lastHandler;
                useArgs = k + 1;
                break;
            }
            if (handler instanceof MultiwordShellCommand) {
                final MultiwordShellCommand mwsc = (MultiwordShellCommand)handler;
                lastHandler = handler;
                handler = mwsc.getSubHandler(args[k]);
                useArgs = k + 1;
            }
        }
        if (handler == null) {
            out.print("Command '");
            for (int k = 0; k < args.length && !args[k].startsWith("-"); ++k) {
                out.print(args[k]);
                if (k < args.length - 1) {
                    out.print(' ');
                }
            }
            out.println("' not found.");
        }
        else {
            final String[] otherArgs = new String[args.length - useArgs];
            System.arraycopy(args, useArgs, otherArgs, 0, otherArgs.length);
            out.println(handler.getSyntax());
            out.println(handler.getUsageHelp(otherArgs));
        }
    }
    
    public String getSyntaxArgs() {
        return "<command name> [command arguments]";
    }
    
    public String getUsageShort() {
        return "Gives help on shell commands";
    }
    
    public String getUsageHelp(final String[] args) {
        return "    Displays help about the given command name.  If the \n    command has arguments they may be entered for more specific\n    help";
    }
}
