// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.cmd;

import org.hyperic.sigar.FileWatcher;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.Reader;
import org.hyperic.sigar.FileInfo;
import org.hyperic.sigar.FileTail;
import org.hyperic.sigar.FileWatcherThread;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import java.util.ArrayList;
import java.util.List;

public class Tail
{
    public boolean follow;
    public int number;
    public List files;
    
    public Tail() {
        this.number = 10;
        this.files = new ArrayList();
    }
    
    public void parseArgs(final String[] args) throws SigarException {
        for (int i = 0; i < args.length; ++i) {
            String arg = args[i];
            if (arg.charAt(0) != '-') {
                this.files.add(arg);
            }
            else {
                arg = arg.substring(1);
                if (arg.equals("f")) {
                    this.follow = true;
                }
                else {
                    if (!Character.isDigit(arg.charAt(0))) {
                        throw new SigarException("Unknown argument: " + args[i]);
                    }
                    this.number = Integer.parseInt(arg);
                }
            }
        }
    }
    
    public static void main(final String[] args) throws SigarException {
        final Sigar sigar = new Sigar();
        final FileWatcherThread watcherThread = FileWatcherThread.getInstance();
        watcherThread.doStart();
        watcherThread.setInterval(1000L);
        final FileTail watcher = new FileTail(sigar) {
            public void tail(final FileInfo info, final Reader reader) {
                final BufferedReader buffer = new BufferedReader(reader);
                if (this.getFiles().size() > 1) {
                    System.out.println("==> " + info.getName() + " <==");
                }
                try {
                    String line;
                    while ((line = buffer.readLine()) != null) {
                        System.out.println(line);
                    }
                }
                catch (IOException e) {
                    System.out.println(e);
                }
            }
        };
        for (int i = 0; i < args.length; ++i) {
            watcher.add(args[i]);
        }
        watcherThread.add(watcher);
        try {
            System.in.read();
        }
        catch (IOException ex) {}
        watcherThread.doStop();
    }
}
