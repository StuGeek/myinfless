// 
// Decompiled by Procyon v0.5.36
// 

package util.loadGen.threads;

import util.tools.HttpClientPool;
import java.util.concurrent.CountDownLatch;
import org.apache.http.impl.client.CloseableHttpClient;

public class LoadExecThread extends Thread
{
    private CloseableHttpClient httpclient;
    private String url;
    private CountDownLatch begin;
    private int serviceId;
    private String jsonObjectStr;
    private int sendDelay;
    private String requestType;
    
    public LoadExecThread(final CloseableHttpClient httpclient, final String url, final CountDownLatch begin, final int serviceId, final String jsonObjectStr, final int sendDelay, final String requestType) {
        this.httpclient = httpclient;
        this.url = url;
        this.begin = begin;
        this.serviceId = serviceId;
        this.jsonObjectStr = jsonObjectStr;
        this.sendDelay = sendDelay;
        this.requestType = requestType;
    }
    
    @Override
    public void run() {
        try {
            this.begin.await();
            HttpClientPool.postResponseTime(this.httpclient, this.url, this.jsonObjectStr);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
