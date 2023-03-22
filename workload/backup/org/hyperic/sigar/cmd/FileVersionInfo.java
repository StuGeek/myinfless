// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.cmd;

import java.util.Iterator;
import org.hyperic.sigar.win32.FileVersion;
import java.util.Map;
import org.hyperic.sigar.win32.Win32;
import org.hyperic.sigar.SigarException;
import java.io.File;

public class FileVersionInfo extends SigarCommandBase
{
    public FileVersionInfo(final Shell shell) {
        super(shell);
    }
    
    public FileVersionInfo() {
    }
    
    protected boolean validateArgs(final String[] args) {
        return args.length >= 1;
    }
    
    public String getUsageShort() {
        return "Display file version info";
    }
    
    public void output(final String[] args) throws SigarException {
        for (int i = 0; i < args.length; ++i) {
            final String exe = args[i];
            if (new File(exe).exists()) {
                this.output(exe);
            }
            else {
                final long[] pids = this.shell.findPids(exe);
                for (int j = 0; j < pids.length; ++j) {
                    try {
                        this.output(this.sigar.getProcExe(pids[j]).getName());
                    }
                    catch (SigarException e) {
                        this.println(exe + ": " + e.getMessage());
                    }
                }
            }
        }
    }
    
    private void output(final String key, final String val) {
        final int max = 20;
        int len = 20 - key.length();
        final StringBuffer sb = new StringBuffer();
        sb.append("  ").append(key);
        while (len-- > 0) {
            sb.append('.');
        }
        sb.append(val);
        this.println(sb.toString());
    }
    
    public void output(final String exe) throws SigarException {
        final FileVersion info = Win32.getFileVersion(exe);
        if (info == null) {
            return;
        }
        this.println("Version info for file '" + exe + "':");
        this.output("FileVersion", info.getFileVersion());
        this.output("ProductVersion", info.getProductVersion());
        for (final Map.Entry entry : info.getInfo().entrySet()) {
            this.output(entry.getKey(), entry.getValue());
        }
    }
    
    public static void main(final String[] args) throws Exception {
        new FileVersionInfo().processCommand(args);
    }
}
