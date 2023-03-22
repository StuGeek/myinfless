// 
// Decompiled by Procyon v0.5.36
// 

package scs.util.utilitization;

import java.io.IOException;
import java.io.FileWriter;
import scs.util.repository.Repository;
import scs.util.tools.DateFormats;
import scs.util.tools.DataFormats;

public class LatencyRecordController extends Thread
{
    private final int SLEEP_TIME = 3000;
    DataFormats dataFormats;
    DateFormats dateFormats;
    
    public LatencyRecordController() {
        this.dataFormats = DataFormats.getInstance();
        this.dateFormats = DateFormats.getInstance();
    }
    
    @Override
    public void run() {
        try {
            final FileWriter writer = new FileWriter(String.valueOf(Repository.resultFilePath) + "Latency_99th_s" + Repository.serviceId + "_m" + Repository.maxSimQPS + "_r" + Repository.simQpsPeekRate + "_i" + Repository.simQpsRemainInterval + "_d" + this.dateFormats.getNowDate1() + ".txt");
            final FileWriter writer2 = new FileWriter(String.valueOf(Repository.resultFilePath) + "Latency_avg_s" + Repository.serviceId + "_m" + Repository.maxSimQPS + "_r" + Repository.simQpsPeekRate + "_i" + Repository.simQpsRemainInterval + "_d" + this.dateFormats.getNowDate1() + ".txt");
            final FileWriter writer3 = new FileWriter(String.valueOf(Repository.resultFilePath) + "TotalServiceRate_avg_s" + Repository.serviceId + "_m" + Repository.maxSimQPS + "_r" + Repository.simQpsPeekRate + "_i" + Repository.simQpsRemainInterval + "_d" + this.dateFormats.getNowDate1() + ".txt");
            int i = 0;
            while (Repository.SYSTEM_RUN_FLAG) {
                Thread.sleep(3000L);
                if (++i == 72000) {
                    break;
                }
                writer.write(String.valueOf(Repository.loader.getRealPerSecLatency(Repository.serviceId, "99th")) + "\n");
                writer2.write(String.valueOf(Repository.loader.getRealPerSecLatency(Repository.serviceId, "avg")) + "\n");
                writer3.write(String.valueOf(Repository.loader.getTotalRequestCount(Repository.serviceId)) + "\t" + Repository.loader.getTotalQueryCount(Repository.serviceId) + "\t" + Repository.loader.getTotalAvgServiceRate(Repository.serviceId) + "\n");
                if (i % 10 != 0) {
                    continue;
                }
                writer.flush();
                writer2.flush();
                writer3.flush();
            }
            writer.close();
            writer2.close();
            writer3.close();
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
        catch (InterruptedException e2) {
            e2.printStackTrace();
        }
    }
}
