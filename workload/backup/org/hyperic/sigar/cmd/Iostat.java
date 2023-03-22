// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.cmd;

import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.DiskUsage;
import java.util.List;
import org.hyperic.sigar.Sigar;
import java.util.ArrayList;
import org.hyperic.sigar.FileSystemMap;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.shell.ShellBase;
import org.hyperic.sigar.shell.FileCompleter;
import org.hyperic.sigar.util.GetlineCompleter;

public class Iostat extends SigarCommandBase
{
    private static final String OUTPUT_FORMAT = "%-15s %-15s %10s %10s %7s %7s %5s %5s";
    private static final String[] HEADER;
    private GetlineCompleter completer;
    
    public Iostat(final Shell shell) {
        super(shell);
        this.setOutputFormat("%-15s %-15s %10s %10s %7s %7s %5s %5s");
        this.completer = new FileCompleter(shell);
    }
    
    public Iostat() {
        this.setOutputFormat("%-15s %-15s %10s %10s %7s %7s %5s %5s");
    }
    
    public GetlineCompleter getCompleter() {
        return this.completer;
    }
    
    protected boolean validateArgs(final String[] args) {
        return args.length <= 1;
    }
    
    public String getSyntaxArgs() {
        return "[filesystem]";
    }
    
    public String getUsageShort() {
        return "Report filesystem disk i/o";
    }
    
    public void printHeader() {
        this.printf(Iostat.HEADER);
    }
    
    private String svctm(final double val) {
        return this.sprintf("%3.2f", new Object[] { new Double(val) });
    }
    
    public void output(final String[] args) throws SigarException {
        if (args.length == 1) {
            final String arg = args[0];
            if (arg.indexOf(47) != -1 || arg.indexOf(92) != -1) {
                this.outputFileSystem(arg);
            }
            else {
                this.outputDisk(arg);
            }
        }
        else {
            final FileSystem[] fslist = this.proxy.getFileSystemList();
            this.printHeader();
            for (int i = 0; i < fslist.length; ++i) {
                if (fslist[i].getType() == 2) {
                    this.output(fslist[i]);
                }
            }
        }
    }
    
    public void outputFileSystem(final String arg) throws SigarException {
        final FileSystemMap mounts = this.proxy.getFileSystemMap();
        final String name = FileCompleter.expand(arg);
        final FileSystem fs = mounts.getMountPoint(name);
        if (fs != null) {
            this.printHeader();
            this.output(fs);
            return;
        }
        throw new SigarException(arg + " No such file or directory");
    }
    
    public void outputDisk(final String name) throws SigarException {
        final DiskUsage disk = this.sigar.getDiskUsage(name);
        final ArrayList items = new ArrayList();
        this.printHeader();
        items.add(name);
        items.add("-");
        items.add(String.valueOf(disk.getReads()));
        items.add(String.valueOf(disk.getWrites()));
        if (disk.getReadBytes() == -1L) {
            items.add("-");
            items.add("-");
        }
        else {
            items.add(Sigar.formatSize(disk.getReadBytes()));
            items.add(Sigar.formatSize(disk.getWriteBytes()));
        }
        if (disk.getQueue() == -1.0) {
            items.add("-");
        }
        else {
            items.add(this.svctm(disk.getQueue()));
        }
        if (disk.getServiceTime() == -1.0) {
            items.add("-");
        }
        else {
            items.add(this.svctm(disk.getServiceTime()));
        }
        this.printf(items);
    }
    
    public void output(final FileSystem fs) throws SigarException {
        final FileSystemUsage usage = this.sigar.getFileSystemUsage(fs.getDirName());
        final ArrayList items = new ArrayList();
        items.add(fs.getDevName());
        items.add(fs.getDirName());
        items.add(String.valueOf(usage.getDiskReads()));
        items.add(String.valueOf(usage.getDiskWrites()));
        if (usage.getDiskReadBytes() == -1L) {
            items.add("-");
            items.add("-");
        }
        else {
            items.add(Sigar.formatSize(usage.getDiskReadBytes()));
            items.add(Sigar.formatSize(usage.getDiskWriteBytes()));
        }
        if (usage.getDiskQueue() == -1.0) {
            items.add("-");
        }
        else {
            items.add(this.svctm(usage.getDiskQueue()));
        }
        if (usage.getDiskServiceTime() == -1.0) {
            items.add("-");
        }
        else {
            items.add(this.svctm(usage.getDiskServiceTime()));
        }
        this.printf(items);
    }
    
    public static void main(final String[] args) throws Exception {
        new Iostat().processCommand(args);
    }
    
    static {
        HEADER = new String[] { "Filesystem", "Mounted on", "Reads", "Writes", "R-bytes", "W-bytes", "Queue", "Svctm" };
    }
}
