// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.cmd;

import org.hyperic.sigar.util.GetlineCompleter;
import org.hyperic.sigar.SigarPermissionDeniedException;
import java.io.IOException;
import org.hyperic.sigar.util.Getline;
import java.io.File;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.ptql.ProcessFinder;
import org.hyperic.sigar.shell.ShellCommandExecException;
import org.hyperic.sigar.shell.ShellCommandUsageException;
import java.lang.reflect.Constructor;
import org.hyperic.sigar.shell.ShellCommandInitException;
import org.hyperic.sigar.SigarLoader;
import org.hyperic.sigar.shell.ShellCommandHandler;
import org.hyperic.sigar.SigarProxyCache;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.shell.ShellBase;

public class Shell extends ShellBase
{
    public static final String RCFILE_NAME = ".sigar_shellrc";
    private static final String CLEAR_SCREEN = "\u001b[2J";
    private Sigar sigar;
    private SigarProxy proxy;
    private long[] foundPids;
    private boolean isInteractive;
    
    public Shell() {
        this.sigar = new Sigar();
        this.proxy = SigarProxyCache.newInstance(this.sigar);
        this.foundPids = new long[0];
        this.isInteractive = false;
    }
    
    public static void clearScreen() {
        System.out.print("\u001b[2J");
    }
    
    public SigarProxy getSigarProxy() {
        return this.proxy;
    }
    
    public Sigar getSigar() {
        return this.sigar;
    }
    
    public boolean isInteractive() {
        return this.isInteractive;
    }
    
    public void setInteractive(final boolean value) {
        this.isInteractive = value;
    }
    
    public void registerCommands() throws ShellCommandInitException {
        this.registerCommandHandler("df", new Df(this));
        this.registerCommandHandler("du", new Du(this));
        this.registerCommandHandler("ls", new Ls(this));
        this.registerCommandHandler("iostat", new Iostat(this));
        this.registerCommandHandler("free", new Free(this));
        this.registerCommandHandler("pargs", new ShowArgs(this));
        this.registerCommandHandler("penv", new ShowEnv(this));
        this.registerCommandHandler("pfile", new ProcFileInfo(this));
        this.registerCommandHandler("pmodules", new ProcModuleInfo(this));
        this.registerCommandHandler("pinfo", new ProcInfo(this));
        this.registerCommandHandler("cpuinfo", new CpuInfo(this));
        this.registerCommandHandler("ifconfig", new Ifconfig(this));
        this.registerCommandHandler("uptime", new Uptime(this));
        this.registerCommandHandler("ps", new Ps(this));
        this.registerCommandHandler("pidof", new Pidof(this));
        this.registerCommandHandler("kill", new Kill(this));
        this.registerCommandHandler("netstat", new Netstat(this));
        this.registerCommandHandler("netinfo", new NetInfo(this));
        this.registerCommandHandler("nfsstat", new Nfsstat(this));
        this.registerCommandHandler("route", new Route(this));
        this.registerCommandHandler("version", new Version(this));
        this.registerCommandHandler("mps", new MultiPs(this));
        this.registerCommandHandler("sysinfo", new SysInfo(this));
        this.registerCommandHandler("time", new Time(this));
        this.registerCommandHandler("ulimit", new Ulimit(this));
        this.registerCommandHandler("who", new Who(this));
        if (SigarLoader.IS_WIN32) {
            this.registerCommandHandler("service", new Win32Service(this));
            this.registerCommandHandler("fversion", new FileVersionInfo(this));
        }
        try {
            this.registerCommandHandler("test", "org.hyperic.sigar.test.SigarTestRunner");
        }
        catch (NoClassDefFoundError e) {}
        catch (Exception ex) {}
    }
    
    private void registerCommandHandler(final String name, final String className) throws Exception {
        final Class cls = Class.forName(className);
        final Constructor con = cls.getConstructor(this.getClass());
        this.registerCommandHandler(name, con.newInstance(this));
    }
    
    public void processCommand(final ShellCommandHandler handler, final String[] args) throws ShellCommandUsageException, ShellCommandExecException {
        try {
            super.processCommand(handler, args);
            if (handler instanceof SigarCommandBase) {
                ((SigarCommandBase)handler).flush();
            }
        }
        finally {
            SigarProxyCache.clear(this.proxy);
        }
    }
    
    public static long[] getPids(final SigarProxy sigar, final String[] args) throws SigarException {
        long[] pids = null;
        switch (args.length) {
            case 0: {
                pids = new long[] { sigar.getPid() };
                break;
            }
            case 1: {
                if (args[0].indexOf("=") > 0) {
                    pids = ProcessFinder.find(sigar, args[0]);
                    break;
                }
                if (args[0].equals("$$")) {
                    pids = new long[] { sigar.getPid() };
                    break;
                }
                pids = new long[] { Long.parseLong(args[0]) };
                break;
            }
            default: {
                pids = new long[args.length];
                for (int i = 0; i < args.length; ++i) {
                    pids[i] = Long.parseLong(args[i]);
                }
                break;
            }
        }
        return pids;
    }
    
    public long[] findPids(final String[] args) throws SigarException {
        if (args.length == 1 && args[0].equals("-")) {
            return this.foundPids;
        }
        return this.foundPids = getPids(this.proxy, args);
    }
    
    public long[] findPids(final String query) throws SigarException {
        return this.findPids(new String[] { query });
    }
    
    public void readCommandFile(final String dir) {
        try {
            final File rc = new File(dir, ".sigar_shellrc");
            this.readRCFile(rc, false);
            if (this.isInteractive && Getline.isTTY()) {
                this.out.println("Loaded rc file: " + rc);
            }
        }
        catch (IOException ex) {}
    }
    
    public String getUserDeniedMessage(final long pid) {
        return SigarPermissionDeniedException.getUserDeniedMessage(this.proxy, pid);
    }
    
    public void shutdown() {
        this.sigar.close();
        try {
            Class.forName("org.hyperic.sigar.test.SigarTestCase").getMethod("closeSigar", (Class<?>[])new Class[0]).invoke(null, new Object[0]);
        }
        catch (ClassNotFoundException e2) {}
        catch (Exception e) {
            e.printStackTrace();
        }
        catch (NoClassDefFoundError noClassDefFoundError) {}
        super.shutdown();
    }
    
    public static void main(final String[] args) {
        final Shell shell = new Shell();
        try {
            if (args.length == 0) {
                shell.isInteractive = true;
            }
            shell.init("sigar", System.out, System.err);
            shell.registerCommands();
            shell.readCommandFile(System.getProperty("user.home"));
            shell.readCommandFile(".");
            shell.readCommandFile(SigarLoader.getLocation());
            if (shell.isInteractive) {
                shell.initHistory();
                Getline.setCompleter(shell);
                shell.run();
            }
            else {
                shell.handleCommand(null, args);
            }
        }
        catch (Exception e) {
            System.err.println("Unexpected exception: " + e);
        }
        finally {
            shell.shutdown();
        }
    }
}
