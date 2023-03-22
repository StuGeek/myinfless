// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.shell;

import java.util.Iterator;
import java.util.HashMap;

public class ShellCommand_set extends ShellCommandBase
{
    private HashMap keyDescriptions;
    
    public ShellCommand_set() {
        this.keyDescriptions = new HashMap();
        (this.keyDescriptions = new HashMap()).put("page.size", "The maximum size of a shell page");
    }
    
    public void processCommand(final String[] args) throws ShellCommandUsageException, ShellCommandExecException {
        if (args.length < 1 || args.length > 2) {
            throw new ShellCommandUsageException(this.getSyntax());
        }
        if (args.length == 1) {
            System.getProperties().remove(args[0]);
        }
        else {
            if (args[0].equalsIgnoreCase("page.size")) {
                int newSize;
                try {
                    newSize = Integer.parseInt(args[1]);
                    if (newSize == 0 || newSize < -1) {
                        throw new NumberFormatException();
                    }
                }
                catch (NumberFormatException exc) {
                    throw new ShellCommandUsageException(args[0] + " must be " + "an integer > 0 or " + "-1");
                }
                this.getShell().setPageSize(newSize);
            }
            System.setProperty(args[0], args[1]);
        }
    }
    
    public void addSetKey(final String key, final String description) {
        this.keyDescriptions.put(key, description);
    }
    
    public String getSyntaxArgs() {
        return "<key> [value]";
    }
    
    public String getUsageShort() {
        return "Set system properties";
    }
    
    public String getUsageHelp(final String[] args) {
        String res = "    " + this.getUsageShort() + ".  If no value is provided, " + "the key will be\n    deleted.";
        if (this.keyDescriptions.size() != 0) {
            res += "\n\n    Common keys include:";
        }
        for (final String key : this.keyDescriptions.keySet()) {
            final String value = this.keyDescriptions.get(key);
            res = res + "\n      " + key + ": " + value;
        }
        return res;
    }
}
