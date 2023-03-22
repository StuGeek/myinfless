// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.cmd;

import java.util.Arrays;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.win32.Win32Exception;
import org.hyperic.sigar.win32.Service;
import java.util.Collection;
import java.util.List;

public class Win32Service extends SigarCommandBase
{
    private static final List COMMANDS;
    
    public Win32Service() {
    }
    
    public Win32Service(final Shell shell) {
        super(shell);
    }
    
    public String getSyntaxArgs() {
        return "[name] [action]";
    }
    
    public String getUsageShort() {
        return "Windows service commands";
    }
    
    protected boolean validateArgs(final String[] args) {
        return args.length == 1 || args.length == 2;
    }
    
    public Collection getCompletions() {
        try {
            return Service.getServiceNames();
        }
        catch (Win32Exception e) {
            return null;
        }
    }
    
    public void output(final String[] args) throws SigarException {
        Service service = null;
        final String name = args[0];
        String cmd = null;
        if (args.length == 2) {
            cmd = args[1];
        }
        try {
            service = new Service(name);
            if (cmd == null || cmd.equals("state")) {
                service.list(this.out);
            }
            else if (cmd.equals("start")) {
                service.start();
            }
            else if (cmd.equals("stop")) {
                service.stop();
            }
            else if (cmd.equals("pause")) {
                service.pause();
            }
            else if (cmd.equals("resume")) {
                service.resume();
            }
            else if (cmd.equals("delete")) {
                service.delete();
            }
            else if (cmd.equals("restart")) {
                service.stop(0L);
                service.start();
            }
            else {
                this.println("Unsupported service command: " + args[1]);
                this.println("Valid commands: " + Win32Service.COMMANDS);
            }
        }
        finally {
            if (service != null) {
                service.close();
            }
        }
    }
    
    static {
        COMMANDS = Arrays.asList("state", "start", "stop", "pause", "resume", "restart");
    }
}
