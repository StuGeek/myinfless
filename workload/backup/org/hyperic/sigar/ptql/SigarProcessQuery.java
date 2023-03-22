// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.ptql;

import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Sigar;

public class SigarProcessQuery implements ProcessQuery
{
    int sigarWrapper;
    long longSigarWrapper;
    
    public SigarProcessQuery() {
        this.sigarWrapper = 0;
        this.longSigarWrapper = 0L;
    }
    
    native void create(final String p0) throws MalformedQueryException;
    
    native void destroy();
    
    protected void finalize() {
        this.destroy();
    }
    
    public native boolean match(final Sigar p0, final long p1) throws SigarException;
    
    public native long findProcess(final Sigar p0) throws SigarException;
    
    public native long[] find(final Sigar p0) throws SigarException;
    
    static boolean re(final String haystack, final String needle) {
        return haystack != null && needle != null && StringPattern.matches(haystack, needle);
    }
}
