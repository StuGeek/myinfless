// 
// Decompiled by Procyon v0.5.36
// 

package util.loadGen.threads;

public class RecordExecThread extends Thread
{
    private int executeInterval;
    private int serviceId;
    Long start;
    
    public RecordExecThread(final int executeInterval, final int serviceId) {
        this.start = 0L;
        this.executeInterval = executeInterval;
        this.serviceId = serviceId;
    }
    
    @Override
    public void run() {
    }
}
