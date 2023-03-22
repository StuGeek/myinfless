// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.cmd;

import org.hyperic.sigar.ProcFileMirror;
import org.hyperic.sigar.FileWatcherThread;
import java.io.FileFilter;
import org.hyperic.sigar.FileWatcher;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.DirStat;
import java.util.Date;
import java.io.IOException;
import java.io.File;
import org.hyperic.sigar.FileInfo;
import org.hyperic.sigar.Sigar;

public class Watch
{
    private static void printHeader(final Sigar sigar, final FileInfo info) throws SigarException {
        final String file = info.getName();
        final FileInfo link = sigar.getLinkInfo(file);
        if (link.getType() == 6) {
            try {
                System.out.println(file + " -> " + new File(file).getCanonicalPath());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(link.getTypeChar() + info.getPermissionsString() + "\t" + info.getUid() + "\t" + info.getGid() + "\t" + info.getSize() + "\t" + new Date(info.getMtime()) + "\t" + file);
        if (info.getType() == 2) {
            info.enableDirStat(true);
            final DirStat stats = sigar.getDirStat(file);
            System.out.println("   Files......." + stats.getFiles());
            System.out.println("   Subdirs....." + stats.getSubdirs());
            System.out.println("   Symlinks...." + stats.getSymlinks());
            System.out.println("   Chrdevs....." + stats.getChrdevs());
            System.out.println("   Blkdevs....." + stats.getBlkdevs());
            System.out.println("   Sockets....." + stats.getSockets());
            System.out.println("   Total......." + stats.getTotal());
            System.out.println("   Disk Usage.." + stats.getDiskUsage());
        }
    }
    
    private static void add(final Sigar sigar, final FileWatcher watcher, final String file, final boolean recurse) throws SigarException {
        final FileInfo info = watcher.add(file);
        printHeader(sigar, info);
        if (!recurse) {
            return;
        }
        if (info.getType() == 2) {
            final File[] dirs = new File(info.getName()).listFiles(new FileFilter() {
                public boolean accept(final File file) {
                    return file.isDirectory() && file.canRead();
                }
            });
            for (int i = 0; i < dirs.length; ++i) {
                add(sigar, watcher, dirs[i].getAbsolutePath(), recurse);
            }
        }
    }
    
    public static void main(final String[] args) throws SigarException {
        boolean recurse = false;
        final Sigar sigar = new Sigar();
        final FileWatcherThread watcherThread = FileWatcherThread.getInstance();
        watcherThread.setInterval(1000L);
        final FileWatcher watcher = new FileWatcher(sigar) {
            public void onChange(final FileInfo info) {
                System.out.println(info.getName() + " Changed:\n" + info.diff());
            }
            
            public void onNotFound(final FileInfo info) {
                System.out.println(info.getName() + " no longer exists");
                this.remove(info.getName());
            }
            
            public void onException(final FileInfo info, final SigarException e) {
                System.out.println("Error checking " + info.getName() + ":");
                e.printStackTrace();
            }
        };
        final ProcFileMirror mirror = new ProcFileMirror(sigar, "./proc");
        watcher.setInterval(watcherThread.getInterval());
        mirror.setInterval(watcherThread.getInterval());
        mirror.setExpire(60L);
        for (int i = 0; i < args.length; ++i) {
            String arg = args[i];
            if (arg.startsWith("/proc/")) {
                mirror.add(arg);
                arg = mirror.getProcFile(arg);
                add(sigar, watcher, arg, false);
            }
            else if (arg.equals("-r")) {
                recurse = true;
            }
            else {
                add(sigar, watcher, arg, recurse);
            }
        }
        watcherThread.add(mirror);
        watcherThread.add(watcher);
        watcherThread.doStart();
        System.out.println("Press any key to stop");
        try {
            System.in.read();
        }
        catch (IOException ex) {}
        watcherThread.doStop();
    }
}
