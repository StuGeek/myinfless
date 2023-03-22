// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import org.hyperic.sigar.SigarProxyCache;
import org.hyperic.sigar.SigarException;
import java.util.ArrayList;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.Sigar;
import junit.framework.TestCase;

public class TestThreads extends TestCase
{
    private static Sigar gSigar;
    private static SigarProxy gProxy;
    private static Object lock;
    private static boolean verbose;
    
    public TestThreads(final String name) {
        super(name);
    }
    
    public void testCreate() throws Exception {
        final ArrayList threads = new ArrayList();
        for (int i = 0; i < 4; ++i) {
            final ProxyThread pt = new ProxyThread();
            pt.useGlobal = true;
            threads.add(pt);
            pt.start();
        }
        for (int n = 0; n < threads.size(); ++n) {
            final ProxyThread pt = threads.get(n);
            pt.join();
            if (pt.ex != null) {
                pt.ex.printStackTrace();
                fail(pt.ex.getMessage());
            }
        }
    }
    
    static {
        TestThreads.gSigar = null;
        TestThreads.gProxy = null;
        TestThreads.lock = new Object();
        TestThreads.verbose = true;
    }
    
    class ProxyThread extends Thread
    {
        SigarException ex;
        boolean useGlobal;
        
        ProxyThread() {
            this.useGlobal = false;
        }
        
        public void run() {
            try {
                Sigar sigar;
                SigarProxy proxy;
                synchronized (TestThreads.lock) {
                    if (this.useGlobal) {
                        if (TestThreads.gSigar == null) {
                            TestThreads.gSigar = new Sigar();
                            TestThreads.gProxy = SigarProxyCache.newInstance(TestThreads.gSigar, 30000);
                        }
                        sigar = TestThreads.gSigar;
                        proxy = TestThreads.gProxy;
                    }
                    else {
                        sigar = new Sigar();
                        proxy = SigarProxyCache.newInstance(sigar, 30000);
                    }
                }
                final String[] args = { "leaktest", "50" };
                final Proxy cmdProxy = new Proxy(sigar, proxy);
                final PrintStream ps = new PrintStream(new ByteArrayOutputStream());
                if (TestThreads.verbose) {
                    cmdProxy.setVerbose(true);
                    cmdProxy.setLeakVerbose(true);
                    cmdProxy.run(args);
                }
                else {
                    cmdProxy.setOutputStream(ps);
                }
            }
            catch (SigarException e) {
                this.ex = e;
            }
        }
    }
}
