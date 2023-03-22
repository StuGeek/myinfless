// 
// Decompiled by Procyon v0.5.36
// 

package scs.util.experiment;

import java.rmi.RemoteException;
import java.io.IOException;
import scs.util.controller.ControlDriver;
import scs.util.loadGen.HttpRealLoadThread;
import scs.util.repository.Repository;
import scs.util.rmi.LoadInterface;

public class ExecDriver
{
    private LoadInterface loader;
    
    public ExecDriver() {
        this.loader = null;
        this.loader = Repository.loader;
    }
    
    public void webServerRealLoadMixed(final int systemRunTime, final int simQpsRemainInterval, final int serviceType, final int concurrency) throws InterruptedException, IOException {
        final Thread realLoadThread = new Thread(new HttpRealLoadThread(this.loader, simQpsRemainInterval, serviceType, concurrency));
        realLoadThread.start();
        Thread.sleep(5000L);
        ControlDriver.start(systemRunTime);
    }
    
    class StartHttpThread extends Thread
    {
        private int serviceType;
        private int concurrency;
        private int intensity;
        
        public StartHttpThread(final int serviceType, final int intensity, final int concurrency) {
            this.serviceType = serviceType;
            this.concurrency = concurrency;
            this.intensity = intensity;
        }
        
        @Override
        public void run() {
            try {
                Repository.loader.execStartHttpLoader(this.serviceType, this.intensity, this.concurrency);
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
