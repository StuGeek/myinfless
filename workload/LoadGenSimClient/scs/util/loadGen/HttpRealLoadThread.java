// 
// Decompiled by Procyon v0.5.36
// 

package scs.util.loadGen;

import java.rmi.RemoteException;
import scs.util.repository.Repository;
import scs.util.rmi.LoadInterface;

public class HttpRealLoadThread extends Thread
{
    LoadInterface loader;
    int intervalTime;
    int serviceType;
    int concurrency;
    
    public HttpRealLoadThread(final LoadInterface loader, final int intervalTime, final int serviceType, final int concurrency) {
        this.loader = loader;
        this.intervalTime = intervalTime * 1000;
        this.serviceType = serviceType;
        this.concurrency = concurrency;
    }
    
    @Override
    public void run() {
        try {
            final int size = GenSimQpsDriver.getInstance().simRPSList.size();
            System.out.println("--------sim load requests thread started--------- ");
            int i = 0;
            this.loader.execStopHttpLoader(this.serviceType);
            Thread.sleep(5000L);
            System.out.println("loader initially closed");
            final Thread thread = new Thread(new StartHttpThread(this.serviceType, 1, this.concurrency));
            thread.start();
            Thread.sleep(5000L);
            System.out.println("loader initially started, default QPS=1");
            while (Repository.SYSTEM_RUN_FLAG) {
                ++i;
                this.loader.setIntensity(GenSimQpsDriver.getInstance().simRPSList.get(i % size), this.serviceType);
                int intensity = this.loader.getRealRequestIntensity(this.serviceType);
                System.out.println("loader working, QPS=" + intensity);
                final Thread thread_ = new Thread(new StartHttpThread(this.serviceType, intensity, this.concurrency));
                thread_.start();
                Thread.sleep(this.intervalTime);
            }
            this.loader.execStopHttpLoader(this.serviceType);
            System.out.println("--------sim load requests thread stoped---------");
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e2) {
            e2.printStackTrace();
        }
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
                HttpRealLoadThread.this.loader.execStartHttpLoader(this.serviceType, this.intensity, this.concurrency);
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
