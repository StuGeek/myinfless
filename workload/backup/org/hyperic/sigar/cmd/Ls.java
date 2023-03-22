// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.cmd;

import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.FileInfo;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Ls extends SigarCommandBase
{
    public Ls(final Shell shell) {
        super(shell);
    }
    
    public Ls() {
    }
    
    public String getUsageShort() {
        return "simple FileInfo test at the moment (like ls -l)";
    }
    
    protected boolean validateArgs(final String[] args) {
        return args.length == 1;
    }
    
    private String getDate(final long mtime) {
        final String fmt = "MMM dd  yyyy";
        return new SimpleDateFormat("MMM dd  yyyy").format(new Date(mtime));
    }
    
    public void output(final String[] args) throws SigarException {
        String file = args[0];
        final FileInfo link = this.sigar.getLinkInfo(file);
        final FileInfo info = this.sigar.getFileInfo(file);
        if (link.getType() == 6) {
            try {
                file = file + " -> " + new File(file).getCanonicalPath();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.println(link.getTypeChar() + info.getPermissionsString() + "\t" + info.getUid() + "\t" + info.getGid() + "\t" + info.getSize() + "\t" + this.getDate(info.getMtime()) + "\t" + file);
    }
    
    public static void main(final String[] args) throws Exception {
        new Ls().processCommand(args);
    }
}
