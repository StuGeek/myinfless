// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.cmd;

import org.hyperic.sigar.SigarException;
import java.lang.reflect.Method;

public class Nfsstat extends SigarCommandBase
{
    public Nfsstat(final Shell shell) {
        super(shell);
    }
    
    public Nfsstat() {
    }
    
    protected boolean validateArgs(final String[] args) {
        return true;
    }
    
    public String getUsageShort() {
        return "Display nfs stats";
    }
    
    private String getValue(final Object obj, final String attr) {
        if (attr == "") {
            return "";
        }
        final String name = "get" + Character.toUpperCase(attr.charAt(0)) + attr.substring(1);
        try {
            final Method method = obj.getClass().getMethod(name, (Class<?>[])new Class[0]);
            return method.invoke(obj, new Object[0]).toString();
        }
        catch (Exception e) {
            return "EINVAL";
        }
    }
    
    private void printnfs(final Object nfs, final String[] names) {
        final String[] values = new String[names.length];
        for (int i = 0; i < names.length; ++i) {
            values[i] = this.getValue(nfs, names[i]);
        }
        this.printf(names);
        this.printf(values);
    }
    
    private void outputNfsV2(final String header, final Object nfs) {
        this.println(header + ":");
        this.printnfs(nfs, new String[] { "null", "getattr", "setattr", "root", "lookup", "readlink" });
        this.printnfs(nfs, new String[] { "read", "writecache", "write", "create", "remove", "rename" });
        this.printnfs(nfs, new String[] { "link", "symlink", "mkdir", "rmdir", "readdir", "fsstat" });
        this.println("");
        this.flush();
    }
    
    private void outputNfsV3(final String header, final Object nfs) {
        this.println(header + ":");
        this.printnfs(nfs, new String[] { "null", "getattr", "setattr", "lookup", "access", "readlink" });
        this.printnfs(nfs, new String[] { "read", "write", "create", "mkdir", "symlink", "mknod" });
        this.printnfs(nfs, new String[] { "remove", "rmdir", "rename", "link", "readdir", "readdirplus" });
        this.printnfs(nfs, new String[] { "fsstat", "fsinfo", "pathconf", "commit", "", "" });
        this.println("");
        this.flush();
    }
    
    public void output(final String[] args) throws SigarException {
        try {
            this.outputNfsV2("Client nfs v2", this.sigar.getNfsClientV2());
        }
        catch (SigarException ex) {}
        try {
            this.outputNfsV2("Server nfs v2", this.sigar.getNfsServerV2());
        }
        catch (SigarException ex2) {}
        try {
            this.outputNfsV3("Client nfs v3", this.sigar.getNfsClientV3());
        }
        catch (SigarException ex3) {}
        try {
            this.outputNfsV3("Server nfs v3", this.sigar.getNfsServerV3());
        }
        catch (SigarException ex4) {}
    }
}
