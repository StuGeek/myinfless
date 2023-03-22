// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

public class CurrentProcessSummary extends ProcStat
{
    public static CurrentProcessSummary get(final SigarProxy sigar) throws SigarException {
        final CurrentProcessSummary stat = new CurrentProcessSummary();
        stat.gather(SigarProxyCache.getSigar(sigar));
        return stat;
    }
}
