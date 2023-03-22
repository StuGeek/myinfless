// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.cmd;

import org.hyperic.sigar.ProcMem;
import org.hyperic.sigar.Sigar;

public class MemWatch
{
    static final int SLEEP_TIME = 10000;
    
    public static void main(final String[] args) throws Exception {
        final Sigar sigar = new Sigar();
        if (args.length != 1) {
            throw new Exception("Usage: MemWatch pid");
        }
        final long pid = Long.parseLong(args[0]);
        long lastTime = System.currentTimeMillis();
        ProcMem last = sigar.getProcMem(pid);
        while (true) {
            final ProcMem cur = sigar.getProcMem(pid);
            final StringBuffer diff = diff(last, cur);
            if (diff.length() == 0) {
                System.out.println("no change (size=" + Sigar.formatSize(cur.getSize()) + ")");
            }
            else {
                final long curTime = System.currentTimeMillis();
                final long timeDiff = curTime - lastTime;
                lastTime = curTime;
                diff.append(" after " + timeDiff + "ms");
                System.out.println(diff);
            }
            last = cur;
            Thread.sleep(10000L);
        }
    }
    
    private static StringBuffer diff(final ProcMem last, final ProcMem cur) {
        final StringBuffer buf = new StringBuffer();
        long diff = cur.getSize() - last.getSize();
        if (diff != 0L) {
            buf.append("size=" + diff);
        }
        diff = cur.getResident() - last.getResident();
        if (diff != 0L) {
            buf.append(", resident=" + diff);
        }
        diff = cur.getShare() - last.getShare();
        if (diff != 0L) {
            buf.append(", share=" + diff);
        }
        return buf;
    }
}
