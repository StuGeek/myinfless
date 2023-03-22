// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.Humidor;
import java.util.ArrayList;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.SigarProxy;

public class TestHumidor extends SigarTestCase
{
    public TestHumidor(final String name) {
        super(name);
    }
    
    private static void getProcCpu(final SigarProxy sigar) throws Exception {
        final long[] pids = sigar.getProcList();
        for (int j = 0; j < 10; ++j) {
            for (int i = 0; i < pids.length; ++i) {
                try {
                    final double cpu = sigar.getProcCpu(pids[i]).getPercent();
                    if (SigarTestCase.getVerbose()) {
                        System.out.println(Thread.currentThread().getName() + " " + pids[i] + "=" + CpuPerc.format(cpu));
                    }
                }
                catch (SigarException ex) {}
            }
        }
    }
    
    private void runTests(final SigarProxy sigar) throws Exception {
        final ArrayList threads = new ArrayList();
        for (int i = 0; i < 3; ++i) {
            final Thread t = new HumidorThread(sigar);
            threads.add(t);
            t.start();
        }
        for (int i = 0; i < threads.size(); ++i) {
            final Thread t = threads.get(i);
            t.join();
        }
    }
    
    public void testGlobalInstance() throws Exception {
        this.runTests(Humidor.getInstance().getSigar());
    }
    
    public void testInstance() throws Exception {
        final Sigar sigar = new Sigar();
        this.runTests(new Humidor(sigar).getSigar());
        sigar.close();
    }
    
    private static class HumidorThread extends Thread
    {
        private SigarProxy sigar;
        
        private HumidorThread(final SigarProxy sigar) {
            this.sigar = sigar;
        }
        
        public void run() {
            try {
                getProcCpu(this.sigar);
            }
            catch (Exception e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
    }
}
