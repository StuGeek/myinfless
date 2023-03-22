// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.io.InputStream;

public class SudoFileInputStream extends InputStream
{
    private Process process;
    private InputStream is;
    
    public SudoFileInputStream(final String file) throws IOException, FileNotFoundException {
        this(new File(file));
    }
    
    public SudoFileInputStream(final File file) throws IOException, FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        final String[] args = { "sudo", "cat", file.toString() };
        this.process = Runtime.getRuntime().exec(args);
        this.is = this.process.getInputStream();
    }
    
    public void close() throws IOException {
        this.process.destroy();
    }
    
    public int read() throws IOException {
        return this.is.read();
    }
    
    public int read(final byte[] b) throws IOException {
        return this.is.read(b);
    }
    
    public int read(final byte[] b, final int off, final int len) throws IOException {
        return this.is.read(b, off, len);
    }
    
    public long skip(final long n) throws IOException {
        return this.is.skip(n);
    }
    
    public int available() throws IOException {
        return this.is.available();
    }
    
    public synchronized void mark(final int readlimit) {
        this.is.mark(readlimit);
    }
    
    public synchronized void reset() throws IOException {
        this.is.reset();
    }
    
    public boolean markSupported() {
        return this.is.markSupported();
    }
}
