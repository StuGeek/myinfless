// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.shell;

import java.util.Iterator;
import java.util.HashMap;

public class ShellCommand_alias extends ShellCommandBase
{
    private static HashMap aliases;
    
    public static String[] getAlias(final String alias) {
        return ShellCommand_alias.aliases.get(alias);
    }
    
    public static Iterator getAliases() {
        return ShellCommand_alias.aliases.keySet().iterator();
    }
    
    public void processCommand(final String[] args) throws ShellCommandUsageException, ShellCommandExecException {
        if (args.length < 2) {
            throw new ShellCommandUsageException(this.getSyntax());
        }
        final int aliasArgsLen = args.length - 1;
        final String[] aliasArgs = new String[aliasArgsLen];
        System.arraycopy(args, 1, aliasArgs, 0, aliasArgsLen);
        ShellCommand_alias.aliases.put(args[0], aliasArgs);
    }
    
    public String getSyntaxArgs() {
        return "<alias> <command>";
    }
    
    public String getUsageShort() {
        return "Create alias command";
    }
    
    public String getUsageHelp(final String[] args) {
        if (ShellCommand_alias.aliases.size() == 0) {
            return "No aliases defined";
        }
        final StringBuffer sb = new StringBuffer();
        sb.append("Defined aliases:\n");
        for (final String key : ShellCommand_alias.aliases.keySet()) {
            final String[] cmd = getAlias(key);
            sb.append(key).append(" => ");
            for (int i = 0; i < cmd.length; ++i) {
                sb.append(cmd[i]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    static {
        ShellCommand_alias.aliases = new HashMap();
    }
}
