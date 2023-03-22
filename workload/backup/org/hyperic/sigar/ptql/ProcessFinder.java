// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.ptql;

import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxyCache;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.Sigar;

public class ProcessFinder
{
    private Sigar sigar;
    private ProcessQueryFactory qf;
    
    public ProcessFinder(final SigarProxy proxy) {
        this(SigarProxyCache.getSigar(proxy));
    }
    
    public ProcessFinder(final Sigar sigar) {
        this.sigar = sigar;
        this.qf = ProcessQueryFactory.getInstance();
    }
    
    public long findSingleProcess(final String query) throws SigarException {
        return this.findSingleProcess(this.qf.getQuery(query));
    }
    
    public long findSingleProcess(final ProcessQuery query) throws SigarException {
        return query.findProcess(this.sigar);
    }
    
    public static long[] find(final Sigar sigar, final String query) throws SigarException {
        return new ProcessFinder(sigar).find(query);
    }
    
    public static long[] find(final SigarProxy sigar, final String query) throws SigarException {
        return new ProcessFinder(sigar).find(query);
    }
    
    public long[] find(final String query) throws SigarException {
        return this.find(this.qf.getQuery(query));
    }
    
    public long[] find(final ProcessQuery query) throws SigarException {
        return query.find(this.sigar);
    }
}
