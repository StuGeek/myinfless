// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarPermissionDeniedException;
import org.hyperic.sigar.SigarNotImplementedException;
import org.hyperic.sigar.jmx.SigarInvokerJMX;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.SigarProxyCache;
import org.hyperic.sigar.Sigar;

public class TestInvoker extends SigarTestCase
{
    private static final String[][] OK_QUERIES;
    private static final String[][] BROKEN_QUERIES;
    
    public TestInvoker(final String name) {
        super(name);
    }
    
    public void testCreate() throws Exception {
        final Sigar sigar = new Sigar();
        final SigarProxy proxy = SigarProxyCache.newInstance(sigar);
        this.testOK(proxy);
        sigar.close();
    }
    
    private void testOK(final SigarProxy proxy) throws Exception {
        for (int i = 0; i < TestInvoker.OK_QUERIES.length; ++i) {
            final String[] query = TestInvoker.OK_QUERIES[i];
            final SigarInvokerJMX invoker = SigarInvokerJMX.getInstance(proxy, query[0]);
            try {
                final Object o = invoker.invoke(query[1]);
                this.traceln(query[0] + ":" + query[1] + "=" + o);
                assertTrue(true);
            }
            catch (SigarNotImplementedException e2) {
                this.traceln(query[0] + " NotImplemented");
            }
            catch (SigarPermissionDeniedException e3) {
                this.traceln(query[0] + " PermissionDenied");
            }
            catch (SigarException e) {
                this.traceln(query[0] + ":" + query[1] + "=" + e);
                assertTrue(false);
            }
        }
        for (int i = 0; i < TestInvoker.BROKEN_QUERIES.length; ++i) {
            final String[] query = TestInvoker.BROKEN_QUERIES[i];
            final SigarInvokerJMX invoker = SigarInvokerJMX.getInstance(proxy, query[0]);
            try {
                invoker.invoke(query[1]);
                assertTrue(false);
            }
            catch (SigarException e) {
                this.traceln(query[0] + ":" + query[1] + "=" + e.getMessage());
                assertTrue(true);
            }
        }
    }
    
    static {
        OK_QUERIES = new String[][] { { "sigar:Type=Mem", "Free" }, { "sigar:Type=Mem", "Total" }, { "sigar:Type=Cpu", "User" }, { "sigar:Type=Cpu", "Sys" }, { "sigar:Type=CpuPerc", "User" }, { "sigar:Type=CpuPerc", "Sys" }, { "sigar:Type=Swap", "Free" }, { "sigar:Type=Swap", "Used" }, { "sigar:Type=Uptime", "Uptime" }, { "sigar:Type=LoadAverage", "0" }, { "sigar:Type=LoadAverage", "1" }, { "sigar:Type=LoadAverage", "2" }, { "sigar:Type=ProcMem,Arg=$$", "Size" }, { "sigar:Type=ProcMem,Arg=$$", "Resident" }, { "sigar:Type=ProcTime,Arg=$$", "Sys" }, { "sigar:Type=ProcTime,Arg=$$", "User" }, { "sigar:Type=ProcTime,Arg=$$", "Total" }, { "sigar:Type=MultiProcCpu,Arg=CredName.User.eq%3Ddougm", "Sys" }, { "sigar:Type=MultiProcMem,Arg=CredName.User.eq%3Ddougm", "Size" }, { "sigar:Type=ProcTime,Arg=$$", "Stime" }, { "sigar:Type=ProcTime,Arg=$$", "Utime" }, { "sigar:Type=CpuPercList,Arg=0", "Idle" }, { "sigar:Type=NetStat", "TcpOutboundTotal" }, { "sigar:Type=NetStat", "TcpListen" } };
        BROKEN_QUERIES = new String[][] { { "sigar:Type=BREAK", "Free" }, { "sigar:Type=Mem", "BREAK" }, { "sigar:Type=ProcTime,Arg=BREAK", "Sys" }, { "sigar:Type=CpuPercList,Arg=1000", "Idle" }, { "sigar:Type=CpuPercList,Arg=BREAK", "Idle" } };
    }
}
