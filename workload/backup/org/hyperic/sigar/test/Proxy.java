// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import org.hyperic.sigar.FileSystem;
import java.io.FileFilter;
import java.io.File;
import org.hyperic.sigar.SigarProxyCache;
import java.lang.reflect.InvocationTargetException;
import org.hyperic.sigar.SigarInvoker;
import java.lang.reflect.Method;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.Sigar;
import java.io.PrintStream;

public class Proxy
{
    private static final String HOME;
    private static boolean sameArg;
    private boolean pause;
    private boolean verbose;
    private boolean leakVerbose;
    private boolean fukksor;
    private boolean useReflection;
    private PrintStream out;
    private String ourPid;
    private Sigar sigar;
    private SigarProxy proxy;
    private long lastChange;
    private long startSize;
    private long currentSize;
    private PidList pids;
    private NetifList netif;
    private FsList fs;
    private DirList dirs;
    private FileList files;
    
    public Proxy(final Sigar sigar, final SigarProxy proxy) {
        this.pause = false;
        this.verbose = true;
        this.leakVerbose = false;
        this.fukksor = false;
        this.useReflection = false;
        this.out = System.out;
        this.lastChange = 0L;
        this.startSize = 0L;
        this.currentSize = 0L;
        this.sigar = sigar;
        this.proxy = proxy;
        this.pids = new PidList(sigar);
        this.netif = new NetifList(sigar);
        this.fs = new FsList(sigar);
        this.dirs = new DirList(Proxy.HOME);
        this.files = new FileList(Proxy.HOME);
    }
    
    public void setOutputStream(final PrintStream out) {
        this.out = out;
    }
    
    public void setVerbose(final boolean value) {
        this.verbose = value;
    }
    
    public void setLeakVerbose(final boolean value) {
        this.leakVerbose = value;
    }
    
    private void output() {
        this.out.println();
    }
    
    private void output(final String s) {
        final String name = Thread.currentThread().getName();
        this.out.println("[" + name + "] " + s);
    }
    
    private long getSize() throws SigarException {
        return this.sigar.getProcMem(this.ourPid).getResident();
    }
    
    private boolean memstat(final long i) throws SigarException {
        final long size = this.getSize();
        String changed = "";
        if (this.currentSize != size) {
            final long diff = size - this.currentSize;
            final long iters = i - this.lastChange;
            changed = " (change=" + diff + ", iters=" + iters + ")";
            this.output(i + ") size=" + size + changed);
            this.currentSize = size;
            this.lastChange = i;
            return true;
        }
        return false;
    }
    
    private void trace(final String msg) {
        if (this.verbose) {
            this.output(msg);
        }
    }
    
    private boolean isNonStringArg(final Method method) {
        final Class[] paramTypes = method.getParameterTypes();
        return paramTypes.length >= 1 && paramTypes[0] != String.class;
    }
    
    private String argsToString(final Object[] args) {
        if (args == null || args.length == 0) {
            return "";
        }
        final StringBuffer sb = new StringBuffer();
        sb.append('(').append(args[0].toString());
        for (int i = 1; i < args.length; ++i) {
            sb.append(',').append(args[i].toString());
        }
        sb.append(')');
        return sb.toString();
    }
    
    private void runall(final int iter) throws SigarException, IllegalAccessException, InvocationTargetException {
        final Method[] types = SigarProxy.class.getMethods();
        for (int i = 0; i < types.length; ++i) {
            Object arg = null;
            final Method method = types[i];
            final Class[] parms = method.getParameterTypes();
            final String type = method.getName().substring(3);
            final Class attrClass = method.getReturnType();
            if (!this.isNonStringArg(method)) {
                Object[] objArgs = new Object[0];
                if (attrClass.isArray() || !attrClass.getName().startsWith("org.hyperic.sigar")) {
                    if (parms.length > 0) {
                        if (!type.startsWith("Proc")) {
                            this.trace("SKIPPING: " + type);
                            continue;
                        }
                        arg = this.pids.getName(iter);
                        switch (parms.length) {
                            case 1: {
                                objArgs = new Object[] { arg };
                                break;
                            }
                            case 2: {
                                if (type.equals("ProcEnv")) {
                                    objArgs = new Object[] { arg, "SHELL" };
                                    break;
                                }
                                if (type.equals("ProcPort")) {
                                    objArgs = new Object[] { "tcp", "80" };
                                    break;
                                }
                                break;
                            }
                        }
                    }
                    Object obj;
                    if (this.useReflection) {
                        obj = method.invoke(this.sigar, objArgs);
                    }
                    else {
                        obj = this.invoke(new SigarInvoker(this.proxy, type), objArgs, null);
                    }
                    if (iter > 0 && this.memstat(iter)) {
                        this.out.print(type);
                        if (arg != null) {
                            this.out.print(" " + arg);
                        }
                        this.output();
                    }
                    String value;
                    if (obj instanceof Object[]) {
                        value = this.argsToString((Object[])obj);
                    }
                    else {
                        value = String.valueOf(obj);
                    }
                    this.trace(type + this.argsToString(objArgs) + "=" + value);
                }
                else {
                    final Method[] attrs = attrClass.getMethods();
                    for (int j = 0; j < attrs.length; ++j) {
                        final Method getter = attrs[j];
                        if (getter.getDeclaringClass() == attrClass) {
                            String attrName = getter.getName();
                            if (attrName.startsWith("get")) {
                                attrName = attrName.substring(3);
                                objArgs = new Object[0];
                                if (parms.length > 0) {
                                    if (type.startsWith("Proc")) {
                                        arg = this.pids.getName(iter);
                                    }
                                    else if (type.startsWith("Net")) {
                                        arg = this.netif.getName(iter);
                                    }
                                    else if (type.startsWith("MultiProc")) {
                                        arg = "State.Name.eq=java";
                                    }
                                    else if (type.equals("FileSystemUsage") || type.equals("MountedFileSystemUsage")) {
                                        arg = this.fs.getName(iter);
                                    }
                                    else if (type.equals("FileInfo") || type.equals("LinkInfo")) {
                                        arg = this.files.getName(iter);
                                    }
                                    else {
                                        if (!type.equals("DirStat")) {
                                            this.trace("SKIPPING: " + type);
                                            continue;
                                        }
                                        arg = this.dirs.getName(iter);
                                    }
                                    objArgs = new Object[] { arg };
                                }
                                if (!this.isNonStringArg(method)) {
                                    Object obj2;
                                    if (this.useReflection) {
                                        final Object typeObject = method.invoke(this.sigar, objArgs);
                                        obj2 = getter.invoke(typeObject, new Object[0]);
                                    }
                                    else {
                                        obj2 = this.invoke(new SigarInvoker(this.proxy, type), objArgs, attrName);
                                    }
                                    if (iter > 0 && this.memstat(iter)) {
                                        this.out.print(type);
                                        if (arg != null) {
                                            this.out.print(" " + arg);
                                        }
                                        this.output();
                                    }
                                    this.trace(type + this.argsToString(objArgs) + "." + attrName + "=" + obj2);
                                    if (this.pause) {
                                        this.pause();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void pause() {
        this.output("hit enter to continue");
        try {
            System.in.read();
        }
        catch (Exception ex) {}
    }
    
    private Object invoke(final SigarInvoker invoker, Object[] args, final String attr) {
        final String type = invoker.getType();
        if (this.fukksor && args.length != 0 && args[0] instanceof String) {
            if (type.startsWith("Proc")) {
                args[0] = new String("666666");
            }
            else {
                args[0] = new String("bogus");
            }
        }
        if (args.length == 0) {
            args = null;
        }
        try {
            return invoker.invoke(args, attr);
        }
        catch (SigarException e) {
            final String msg = type + " failed: " + e.getMessage();
            System.err.println(msg);
            return null;
        }
    }
    
    public static void main(final String[] args) throws Exception {
        final int expire = 30000;
        final Sigar sigar = new Sigar();
        final SigarProxy proxy = SigarProxyCache.newInstance(sigar, expire);
        new Proxy(sigar, proxy).run(args);
    }
    
    public void run(final String[] args) throws SigarException {
        this.ourPid = String.valueOf(this.sigar.getPid());
        this.output("ourPid=" + this.ourPid);
        if (args.length >= 2) {
            final String type = args[0];
            final String attr = args[args.length - 1];
            if (type.equals("leaktest")) {
                final int num = Integer.parseInt(args[1]);
                this.verbose = this.leakVerbose;
                final long size = this.getSize();
                this.currentSize = size;
                this.startSize = size;
                final long startTime = System.currentTimeMillis();
                for (int i = 0; i < num; ++i) {
                    final Sigar s = new Sigar();
                    try {
                        this.runall(i);
                    }
                    catch (IllegalAccessException e) {
                        throw new SigarException(e.getMessage());
                    }
                    catch (InvocationTargetException e2) {
                        throw new SigarException(e2.getMessage());
                    }
                    finally {
                        s.close();
                    }
                }
                final long totalTime = System.currentTimeMillis() - startTime;
                this.proxy = null;
                this.output("Running garbage collector..");
                System.gc();
                this.memstat(this.lastChange + 1L);
                this.output(num + " iterations took " + totalTime + "ms");
                this.output("startSize=" + this.startSize + ", endSize=" + this.currentSize + ", diff=" + (this.currentSize - this.startSize));
            }
            else {
                final Object obj = this.invoke(new SigarInvoker(this.proxy, type), null, attr);
                this.output(obj.toString());
            }
        }
        else {
            try {
                this.runall(0);
            }
            catch (IllegalAccessException e3) {
                throw new SigarException(e3.getMessage());
            }
            catch (InvocationTargetException e4) {
                throw new SigarException(e4.getMessage());
            }
        }
    }
    
    static {
        HOME = System.getProperty("user.home");
        Proxy.sameArg = true;
    }
    
    private abstract static class ArgList
    {
        String[] values;
        int ix;
        
        private ArgList() {
            this.ix = 0;
        }
        
        public String get() {
            if (this.ix == this.values.length) {
                this.ix = 0;
            }
            return this.values[this.ix++];
        }
        
        public String getName(final int iter) {
            if (iter == 0 || Proxy.sameArg) {
                return this.values[0];
            }
            return this.get();
        }
    }
    
    private static class PidList extends ArgList
    {
        public PidList(final Sigar sigar) {
            try {
                final long[] pids = sigar.getProcList();
                (this.values = new String[pids.length + 1])[0] = String.valueOf(sigar.getPid());
                for (int i = 0; i < pids.length; ++i) {
                    this.values[i + 1] = String.valueOf(pids[i]);
                }
            }
            catch (SigarException e) {
                e.printStackTrace();
            }
        }
    }
    
    private static class NetifList extends ArgList
    {
        public NetifList(final Sigar sigar) {
            try {
                this.values = sigar.getNetInterfaceList();
            }
            catch (SigarException e) {
                e.printStackTrace();
            }
        }
    }
    
    private static class DirList extends ArgList
    {
        public DirList(final String dir) {
            final File[] dirs = new File(dir).listFiles(new FileFilter() {
                public boolean accept(final File f) {
                    return f.isDirectory();
                }
            });
            this.values = new String[dirs.length];
            for (int i = 0; i < dirs.length; ++i) {
                this.values[i] = dirs[i].getAbsolutePath();
            }
        }
    }
    
    private static class FileList extends ArgList
    {
        public FileList(final String dir) {
            final File[] files = new File(dir).listFiles(new FileFilter() {
                public boolean accept(final File f) {
                    return !f.isDirectory();
                }
            });
            this.values = new String[files.length];
            for (int i = 0; i < files.length; ++i) {
                this.values[i] = files[i].getAbsolutePath();
            }
        }
    }
    
    private static class FsList extends ArgList
    {
        public FsList(final Sigar sigar) {
            try {
                final FileSystem[] fs = sigar.getFileSystemList();
                this.values = new String[fs.length];
                for (int i = 0; i < fs.length; ++i) {
                    this.values[i] = fs[i].getDirName();
                }
            }
            catch (SigarException e) {
                e.printStackTrace();
            }
        }
    }
}
