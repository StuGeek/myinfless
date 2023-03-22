// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.util;

import java.io.File;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.BufferedReader;

public class Getline
{
    private static final boolean isatty;
    private static boolean useNative;
    private BufferedReader in;
    private String prompt;
    
    public Getline() {
        this.in = null;
        this.prompt = "> ";
    }
    
    public Getline(final String prompt) {
        this.in = null;
        this.prompt = "> ";
        this.prompt = prompt;
    }
    
    private static native boolean isatty();
    
    public static native void setCompleter(final GetlineCompleter p0);
    
    public native void redraw();
    
    public native void reset();
    
    private native void histadd(final String p0);
    
    private native void histinit(final String p0);
    
    private native String getline(final String p0) throws IOException, EOFException;
    
    public static boolean isTTY() {
        return Getline.isatty;
    }
    
    public String getLine() throws IOException, EOFException {
        return this.getLine(this.prompt, true);
    }
    
    public String getLine(final String prompt) throws IOException, EOFException {
        return this.getLine(prompt, true);
    }
    
    public String getLine(final String prompt, final boolean addToHistory) throws IOException, EOFException {
        if (Getline.useNative) {
            final String line = this.getline(prompt);
            if (addToHistory) {
                this.addToHistory(line);
            }
            return line;
        }
        if (this.in == null) {
            this.in = new BufferedReader(new InputStreamReader(System.in));
        }
        System.out.print(prompt);
        return this.in.readLine();
    }
    
    public void initHistoryFile(final File file) throws IOException {
        if (Getline.useNative) {
            this.histinit(file.getCanonicalPath());
        }
    }
    
    public void addToHistory(final String line) {
        if (line == null || line.length() == 0) {
            return;
        }
        if (Getline.useNative) {
            this.histadd(line);
        }
    }
    
    static {
        isatty = isatty();
        Getline.useNative = (!"false".equals(System.getProperty("sigar.getline.native")) && Getline.isatty);
    }
}
