// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import org.apache.log4j.Logger;

public class ProcFileMirror extends FileWatcher
{
    private String proc;
    private long expire;
    private static final Logger log;
    private static final boolean isDebug;
    
    public ProcFileMirror(final Sigar sigar, final String proc) {
        super(sigar);
        this.proc = proc;
        this.expire = 300000L;
    }
    
    public long getExpireMillis() {
        return this.expire;
    }
    
    public void setExpire(final long seconds) {
        this.setExpireMillis(seconds * 1000L);
    }
    
    public void setExpireMillis(final long millis) {
        this.expire = millis;
    }
    
    public String getProcFile(final File file) {
        return this.getProcFile(file.getPath());
    }
    
    public String getProcFile(String file) {
        final String PROC = "/proc/";
        if (file.startsWith("/proc/")) {
            file = file.substring("/proc/".length());
        }
        return this.proc + File.separator + file;
    }
    
    private void mirror(final String source) throws IOException {
        this.mirror(source, this.getProcFile(source));
    }
    
    private String mirrorToString(final File source, final File dest) {
        return "mirror(" + source + ", " + dest + ")";
    }
    
    private void mirror(final String source, final String dest) throws IOException {
        this.mirror(new File(source), new File(dest));
    }
    
    private void mirror(final File source, final File dest) throws IOException {
        FileInputStream is = null;
        FileOutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            final byte[] buffer = new byte[2048];
            while (true) {
                final int nread = is.read(buffer);
                if (nread == -1) {
                    break;
                }
                os.write(buffer, 0, nread);
            }
        }
        catch (IOException e) {
            final String msg = this.mirrorToString(source, dest) + " failed: " + e.getMessage();
            throw new IOException(msg);
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                }
                catch (IOException ex) {}
            }
            if (os != null) {
                os.close();
            }
        }
        if (ProcFileMirror.isDebug) {
            ProcFileMirror.log.debug((Object)this.mirrorToString(source, dest));
        }
    }
    
    public FileInfo add(final String name) throws SigarException {
        final File source = new File(name);
        final File dest = new File(this.getProcFile(source));
        final File dir = dest.getParentFile();
        if (!dir.exists() && !dir.mkdirs()) {
            final String msg = "mkdir(" + dir + ") failed";
            throw new SigarException(msg);
        }
        if (!source.canRead()) {
            throw new SigarException("Cannot read: " + source);
        }
        if ((dest.isFile() && !dest.canWrite()) || !dir.isDirectory() || !dir.canWrite()) {
            throw new SigarException("Cannot write: " + dest);
        }
        try {
            this.mirror(source.getPath(), dest.getPath());
        }
        catch (IOException e) {
            throw new SigarException(e.getMessage());
        }
        return super.add(source.getPath());
    }
    
    public void onChange(final FileInfo info) {
        try {
            this.mirror(info.getName());
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    
    protected boolean changed(final FileInfo info) throws SigarException, SigarFileNotFoundException {
        final File dest = new File(this.getProcFile(info.getName()));
        final long now = System.currentTimeMillis();
        return now - dest.lastModified() > this.expire;
    }
    
    static {
        log = SigarLog.getLogger(ProcFileMirror.class.getName());
        isDebug = ProcFileMirror.log.isDebugEnabled();
    }
}
