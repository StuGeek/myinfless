// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.ptql;

import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Sigar;

public interface ProcessQuery
{
    boolean match(final Sigar p0, final long p1) throws SigarException;
    
    long findProcess(final Sigar p0) throws SigarException;
    
    long[] find(final Sigar p0) throws SigarException;
}
