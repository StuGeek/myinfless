// 
// Decompiled by Procyon v0.5.36
// 

package scs.util.experiment;

import scs.util.repository.Repository;

public class TimerThread extends Thread
{
    private final int SLEEP_TIME = 10000;
    private int EXECUTE_TIME;
    
    public TimerThread(final int EXECUTE_TIME) {
        this.EXECUTE_TIME = 600000;
        this.EXECUTE_TIME = EXECUTE_TIME * 1000;
    }
    
    @Override
    public void run() {
        Repository.SYSTEM_RUN_FLAG = true;
        final long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start <= this.EXECUTE_TIME) {
            try {
                Thread.sleep(10000L);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Repository.SYSTEM_RUN_FLAG = false;
    }
}
