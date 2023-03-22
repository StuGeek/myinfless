// 
// Decompiled by Procyon v0.5.36
// 

package scs.util.controller;

import scs.util.utilitization.UtilityController;
import scs.util.utilitization.LatencyRecordController;
import scs.util.repository.Repository;
import scs.util.experiment.TimerThread;

public class ControlDriver
{
    private static ControlDriver controlDriver;
    
    static {
        ControlDriver.controlDriver = null;
    }
    
    private ControlDriver() {
    }
    
    public static synchronized ControlDriver getInstance() {
        if (ControlDriver.controlDriver == null) {
            ControlDriver.controlDriver = new ControlDriver();
        }
        return ControlDriver.controlDriver;
    }
    
    public static void start(final int systemRunTime) {
        final Thread timeThread = new Thread(new TimerThread(systemRunTime));
        timeThread.start();
        if (Repository.recordLatency) {
            final Thread latencyRecordController = new Thread(new LatencyRecordController());
            latencyRecordController.start();
        }
        if (Repository.recordUtility) {
            final Thread utilityController = new Thread(new UtilityController());
            utilityController.start();
        }
    }
}
