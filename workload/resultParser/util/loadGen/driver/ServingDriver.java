// 
// Decompiled by Procyon v0.5.36
// 

package util.loadGen.driver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import repository.Repository;
import util.tools.HttpClientPool;

public class ServingDriver extends AbstractJobDriver
{
    private static ServingDriver driver;
    
    static {
        ServingDriver.driver = null;
    }
    
    public ServingDriver() {
        this.initVariables();
    }
    
    public static synchronized ServingDriver getInstance() {
        if (ServingDriver.driver == null) {
            ServingDriver.driver = new ServingDriver();
        }
        return ServingDriver.driver;
    }
    
    @Override
    protected void initVariables() {
        this.httpClient = HttpClientPool.getInstance().getConnection();
        this.queryItemsStr = Repository.reqBaseURL;
        this.jsonParmStr = Repository.reqParmStr;
    }
    
    @Override
    public void executeJob(final int serviceId) {
        final ExecutorService executor = Executors.newCachedThreadPool();
        Repository.sendFlag = true;
        while (Repository.onlineDataFlag) {
            if (Repository.sendFlag && Repository.realRequestIntensity > 0) {
                final CountDownLatch begin = new CountDownLatch(1);
                final int sleepUnit = 1000 / Repository.realRequestIntensity;
                for (int i = 0; i < Repository.realRequestIntensity; ++i) {}
                Repository.sendFlag = false;
                begin.countDown();
            }
            else {
                try {
                    Thread.sleep(50L);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
            try {
                Thread.sleep(2000L);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
