// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import org.hyperic.sigar.ptql.ProcessFinder;

public class MultiProcMem extends ProcMem
{
    static ProcMem get(final Sigar sigar, final String query) throws SigarException {
        final ProcMem mem = new ProcMem();
        mem.share = -1L;
        final long[] pids = ProcessFinder.find(sigar, query);
        for (int i = 0; i < pids.length; ++i) {
            ProcMem pmem;
            try {
                pmem = sigar.getProcMem(pids[i]);
            }
            catch (SigarException e) {
                continue;
            }
            final ProcMem procMem = mem;
            procMem.size += pmem.size;
            final ProcMem procMem2 = mem;
            procMem2.resident += pmem.resident;
            if (pmem.share != -1L) {
                final ProcMem procMem3 = mem;
                procMem3.share += pmem.share;
            }
        }
        return mem;
    }
}
