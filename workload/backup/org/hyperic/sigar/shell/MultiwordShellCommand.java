// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.shell;

import org.hyperic.sigar.util.PrintfFormat;
import java.util.Iterator;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

public class MultiwordShellCommand extends ShellCommandBase
{
    private Map itsSubHandlerMap;
    
    public MultiwordShellCommand() {
        this.itsSubHandlerMap = new HashMap();
    }
    
    public ShellCommandHandler getSubHandler(final String subName) {
        return this.itsSubHandlerMap.get(subName);
    }
    
    public Set getHandlerNames() {
        return this.itsSubHandlerMap.keySet();
    }
    
    public void registerSubHandler(final String subName, final ShellCommandHandler handler) throws ShellCommandInitException {
        if (!this.itsSubHandlerMap.containsValue(handler)) {
            handler.init(this.getCommandName() + " " + subName, this.getShell());
        }
        this.itsSubHandlerMap.put(subName, handler);
    }
    
    public void processCommand(final String[] args) throws ShellCommandUsageException, ShellCommandExecException {
        final String cmdName = this.getCommandName();
        if (args.length < 1) {
            throw new ShellCommandUsageException(cmdName + " command " + "requires an argument.");
        }
        final ShellCommandHandler handler = this.itsSubHandlerMap.get(args[0].toLowerCase());
        if (handler == null) {
            throw new ShellCommandUsageException("don't know how to " + cmdName + " " + args[0]);
        }
        final String[] subArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subArgs, 0, subArgs.length);
        handler.processCommand(subArgs);
    }
    
    public String getSyntaxArgs() {
        final StringBuffer res = new StringBuffer();
        res.append("<");
        final Iterator i = this.getHandlerNames().iterator();
        while (i.hasNext()) {
            res.append(i.next());
            if (i.hasNext()) {
                res.append(" | ");
            }
        }
        res.append(">");
        return res.toString();
    }
    
    public String getUsageHelp(final String[] args) {
        if (args.length == 0) {
            final StringBuffer res = new StringBuffer();
            final Object[] fArgs = new Object[2];
            res.append("    " + this.getUsageShort());
            res.append(".\n    For further help on each subcommand, ");
            res.append("type 'help ");
            res.append(this.getCommandName() + " <subcommand>'\n\n");
            int maxLen = 0;
            for (final String cmdName : this.getHandlerNames()) {
                if (cmdName.length() > maxLen) {
                    maxLen = cmdName.length();
                }
            }
            final String fmtStr = "      %-" + (maxLen + 1) + "s %s";
            final PrintfFormat fmt = new PrintfFormat(fmtStr);
            final Iterator i = this.getHandlerNames().iterator();
            while (i.hasNext()) {
                final String cmdName = i.next();
                final ShellCommandHandler sub = this.getSubHandler(cmdName);
                fArgs[0] = cmdName + ":";
                fArgs[1] = sub.getUsageShort();
                res.append(fmt.sprintf(fArgs));
                if (i.hasNext()) {
                    res.append("\n");
                }
            }
            return res.toString();
        }
        final ShellCommandHandler handler;
        if ((handler = this.getSubHandler(args[0].toLowerCase())) == null) {
            return null;
        }
        final String[] subArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subArgs, 0, subArgs.length);
        return handler.getUsageHelp(subArgs);
    }
}
