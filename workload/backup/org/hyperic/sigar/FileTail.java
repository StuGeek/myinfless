// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.Reader;
import org.apache.log4j.Logger;

public abstract class FileTail extends FileWatcher
{
    public static final String PROP_USE_SUDO = "sigar.tail.sudo";
    private boolean useSudo;
    private static final Logger log;
    private static final boolean isDebug;
    
    public abstract void tail(final FileInfo p0, final Reader p1);
    
    public FileTail(final Sigar sigar) {
        super(sigar);
        this.useSudo = "true".equals(System.getProperty("sigar.tail.sudo"));
    }
    
    public void useSudo(final boolean useSudo) {
        this.useSudo = useSudo;
    }
    
    static void error(final String name, final Throwable exc) {
        final String msg = name + ": " + exc.getMessage();
        FileTail.log.error((Object)msg, exc);
    }
    
    public void onChange(final FileInfo info) {
        InputStream in = null;
        Reader reader = null;
        final String name = info.getName();
        try {
            if (this.useSudo) {
                in = new SudoFileInputStream(name);
            }
            else {
                in = new FileInputStream(name);
            }
            final long offset = this.getOffset(info);
            if (offset > 0L) {
                in.skip(offset);
            }
            reader = new InputStreamReader(in);
            this.tail(info, reader);
        }
        catch (IOException e) {
            error(name, e);
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException ex) {}
            }
            if (in != null) {
                try {
                    in.close();
                }
                catch (IOException ex2) {}
            }
        }
    }
    
    public FileInfo add(final String file) throws SigarException {
        final FileInfo info = super.add(file);
        if (FileTail.isDebug) {
            FileTail.log.debug((Object)("add: " + file + "=" + info));
        }
        return info;
    }
    
    protected boolean changed(final FileInfo info) throws SigarException, SigarFileNotFoundException {
        return info.modified() || info.getPreviousInfo().size != info.size;
    }
    
    private long getOffset(final FileInfo current) {
        final FileInfo previous = current.getPreviousInfo();
        if (previous == null) {
            if (FileTail.isDebug) {
                FileTail.log.debug((Object)(current.getName() + ": first stat"));
            }
            return current.size;
        }
        if (current.inode != previous.inode) {
            if (FileTail.isDebug) {
                FileTail.log.debug((Object)(current.getName() + ": file inode changed"));
            }
            return -1L;
        }
        if (current.size < previous.size) {
            if (FileTail.isDebug) {
                FileTail.log.debug((Object)(current.getName() + ": file truncated"));
            }
            return -1L;
        }
        if (FileTail.isDebug) {
            final long diff = current.size - previous.size;
            FileTail.log.debug((Object)(current.getName() + ": " + diff + " new bytes"));
        }
        return previous.size;
    }
    
    static {
        log = SigarLog.getLogger(FileTail.class.getName());
        isDebug = FileTail.log.isDebugEnabled();
    }
}
