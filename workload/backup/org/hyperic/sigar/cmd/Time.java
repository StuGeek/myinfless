// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.cmd;

import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.CpuTimer;

public class Time extends SigarCommandBase
{
    public Time(final Shell shell) {
        super(shell);
    }
    
    public Time() {
    }
    
    protected boolean validateArgs(final String[] args) {
        return args.length >= 1;
    }
    
    public String getSyntaxArgs() {
        return "[command] [...]";
    }
    
    public String getUsageShort() {
        return "Time command";
    }
    
    public void output(String[] args) throws SigarException {
        final boolean isInteractive = this.shell.isInteractive();
        this.shell.setInteractive(false);
        final CpuTimer cpu = new CpuTimer(this.sigar);
        int num;
        if (Character.isDigit(args[0].charAt(0))) {
            num = Integer.parseInt(args[0]);
            final String[] xargs = new String[args.length - 1];
            System.arraycopy(args, 1, xargs, 0, xargs.length);
            args = xargs;
        }
        else {
            num = 1;
        }
        cpu.start();
        try {
            for (int i = 0; i < num; ++i) {
                this.shell.handleCommand("time " + args[0], args);
            }
        }
        finally {
            this.shell.setInteractive(isInteractive);
        }
        cpu.stop();
        cpu.list(this.out);
    }
    
    public static void main(final String[] args) throws Exception {
        new Time().processCommand(args);
    }
}
