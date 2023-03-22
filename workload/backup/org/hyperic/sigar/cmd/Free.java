// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.cmd;

import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;
import org.hyperic.sigar.Mem;

public class Free extends SigarCommandBase
{
    public Free(final Shell shell) {
        super(shell);
    }
    
    public Free() {
    }
    
    public String getUsageShort() {
        return "Display information about free and used memory";
    }
    
    private static Long format(final long value) {
        return new Long(value / 1024L);
    }
    
    public void output(final String[] args) throws SigarException {
        final Mem mem = this.sigar.getMem();
        final Swap swap = this.sigar.getSwap();
        final Object[] header = { "total", "used", "free" };
        final Object[] memRow = { format(mem.getTotal()), format(mem.getUsed()), format(mem.getFree()) };
        final Object[] actualRow = { format(mem.getActualUsed()), format(mem.getActualFree()) };
        final Object[] swapRow = { format(swap.getTotal()), format(swap.getUsed()), format(swap.getFree()) };
        this.printf("%18s %10s %10s", header);
        this.printf("Mem:    %10ld %10ld %10ld", memRow);
        if (mem.getUsed() != mem.getActualUsed() || mem.getFree() != mem.getActualFree()) {
            this.printf("-/+ buffers/cache: %10ld %10d", actualRow);
        }
        this.printf("Swap:   %10ld %10ld %10ld", swapRow);
        this.printf("RAM:    %10ls", new Object[] { mem.getRam() + "MB" });
    }
    
    public static void main(final String[] args) throws Exception {
        new Free().processCommand(args);
    }
}
