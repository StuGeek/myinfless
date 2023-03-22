// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.cmd;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.FileSystemUsage;
import java.util.List;
import org.hyperic.sigar.NfsFileSystem;
import org.hyperic.sigar.FileSystemMap;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.SigarException;
import java.util.ArrayList;
import org.hyperic.sigar.shell.ShellBase;
import org.hyperic.sigar.shell.FileCompleter;
import org.hyperic.sigar.util.GetlineCompleter;

public class Df extends SigarCommandBase
{
    private static final String OUTPUT_FORMAT = "%-15s %4s %4s %5s %4s %-15s %s";
    private static final String[] HEADER;
    private static final String[] IHEADER;
    private GetlineCompleter completer;
    private boolean opt_i;
    
    public Df(final Shell shell) {
        super(shell);
        this.setOutputFormat("%-15s %4s %4s %5s %4s %-15s %s");
        this.completer = new FileCompleter(shell);
    }
    
    public Df() {
        this.setOutputFormat("%-15s %4s %4s %5s %4s %-15s %s");
    }
    
    public GetlineCompleter getCompleter() {
        return this.completer;
    }
    
    protected boolean validateArgs(final String[] args) {
        return true;
    }
    
    public String getSyntaxArgs() {
        return "[filesystem]";
    }
    
    public String getUsageShort() {
        return "Report filesystem disk space usage";
    }
    
    public void printHeader() {
        this.printf(this.opt_i ? Df.IHEADER : Df.HEADER);
    }
    
    public void output(final String[] args) throws SigarException {
        this.opt_i = false;
        final ArrayList sys = new ArrayList();
        if (args.length > 0) {
            final FileSystemMap mounts = this.proxy.getFileSystemMap();
            for (int i = 0; i < args.length; ++i) {
                final String arg = args[i];
                if (arg.equals("-i")) {
                    this.opt_i = true;
                }
                else {
                    final String name = FileCompleter.expand(arg);
                    final FileSystem fs = mounts.getMountPoint(name);
                    if (fs == null) {
                        throw new SigarException(arg + " No such file or directory");
                    }
                    sys.add(fs);
                }
            }
        }
        if (sys.size() == 0) {
            final FileSystem[] fslist = this.proxy.getFileSystemList();
            for (int i = 0; i < fslist.length; ++i) {
                sys.add(fslist[i]);
            }
        }
        this.printHeader();
        for (int j = 0; j < sys.size(); ++j) {
            this.output(sys.get(j));
        }
    }
    
    public void output(final FileSystem fs) throws SigarException {
        long used;
        long avail;
        long total;
        long pct;
        try {
            if (fs instanceof NfsFileSystem) {
                final NfsFileSystem nfs = (NfsFileSystem)fs;
                if (!nfs.ping()) {
                    this.println(nfs.getUnreachableMessage());
                    return;
                }
            }
            final FileSystemUsage usage = this.sigar.getFileSystemUsage(fs.getDirName());
            if (this.opt_i) {
                used = usage.getFiles() - usage.getFreeFiles();
                avail = usage.getFreeFiles();
                total = usage.getFiles();
                if (total == 0L) {
                    pct = 0L;
                }
                else {
                    final long u100 = used * 100L;
                    pct = u100 / total + ((u100 % total != 0L) ? 1 : 0);
                }
            }
            else {
                used = usage.getTotal() - usage.getFree();
                avail = usage.getAvail();
                total = usage.getTotal();
                pct = (long)(usage.getUsePercent() * 100.0);
            }
        }
        catch (SigarException e) {
            avail = (used = (total = (pct = 0L)));
        }
        String usePct;
        if (pct == 0L) {
            usePct = "-";
        }
        else {
            usePct = pct + "%";
        }
        final ArrayList items = new ArrayList();
        items.add(fs.getDevName());
        items.add(this.formatSize(total));
        items.add(this.formatSize(used));
        items.add(this.formatSize(avail));
        items.add(usePct);
        items.add(fs.getDirName());
        items.add(fs.getSysTypeName() + "/" + fs.getTypeName());
        this.printf(items);
    }
    
    private String formatSize(final long size) {
        return this.opt_i ? String.valueOf(size) : Sigar.formatSize(size * 1024L);
    }
    
    public static void main(final String[] args) throws Exception {
        new Df().processCommand(args);
    }
    
    static {
        HEADER = new String[] { "Filesystem", "Size", "Used", "Avail", "Use%", "Mounted on", "Type" };
        IHEADER = new String[] { "Filesystem", "Inodes", "IUsed", "IFree", "IUse%", "Mounted on", "Type" };
    }
}
