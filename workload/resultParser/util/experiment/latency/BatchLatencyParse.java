// 
// Decompiled by Procyon v0.5.36
// 

package util.experiment.latency;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import util.tools.FileOperation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BatchLatencyParse
{
    private static Map<String, Float> profileMap;
    private static List<Integer> qpsList;
    
    static {
        BatchLatencyParse.profileMap = new HashMap<String, Float>();
        BatchLatencyParse.qpsList = new ArrayList<Integer>();
    }
    
    public static void main(final String[] args) throws IOException {
        BatchLatencyParse.qpsList = new FileOperation().readIntFile("C:\\Users\\DELL\\Desktop\\pieces\u7ed3\u679c\\workload\\300-workload_3.csv");
        final List<String> list = new FileOperation().readStringFile("C:\\Users\\DELL\\Desktop\\pieces\u7ed3\u679c\\\u7b97\u5b50\u523b\u753b\u7cbe\u5ea6\\profiler\\resnet-50-profile-results-cpu-gpu.txt");
        for (final String item : list) {
            final String[] splits = item.split(" ");
            final float latency = Float.parseFloat(splits[4]);
            BatchLatencyParse.profileMap.put(String.valueOf(splits[1]) + "_" + splits[2] + "_" + splits[3], latency);
        }
        final String filePath = "C:\\Users\\DELL\\Desktop\\pieces\u7ed3\u679c\\\u8c03\u5ea6\u6548\u7387\\BATCH\\resnet-50\\3\\";
        final String fileName = "resnet-50-150ms-300QPS-workload3-6s-60min.txt";
        new BatchLatencyParse().readStringFile(filePath, fileName);
    }
    
    public void readStringFile(final String filePath, final String fileName) throws IOException {
        final FileWriter writer = new FileWriter(String.valueOf(filePath) + fileName.replace("txt", "csv"));
        final List<String> list = new FileOperation().readStringFile(String.valueOf(filePath) + fileName);
        final int size = list.size();
        int sumqps = 0;
        int totalThroughput = 0;
        int cost = 0;
        for (int i = 0; i < size; ++i) {
            if (list.get(i).equals("")) {
                sumqps += BatchLatencyParse.qpsList.get(i);
                writer.write(BatchLatencyParse.qpsList.get(i) + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + 0 + "," + "," + 0 + "," + 0 + "," + 0 + "\n");
            }
            else {
                final String[] splits = list.get(i).split(",");
                final int cpuCore = Integer.parseInt(splits[0].split(":")[1]);
                final int gpuCore = Integer.parseInt(splits[1].split(":")[1].replace("%", ""));
                final int batchSize = Integer.parseInt(splits[3].split(":")[1]);
                final float execLatency = BatchLatencyParse.profileMap.get(String.valueOf(cpuCore) + "_" + gpuCore + "_" + batchSize);
                final int throughtPerInstance = (int)(1000.0f / execLatency * batchSize);
                final int numberOfInstance = (int)Math.ceil(BatchLatencyParse.qpsList.get(i) * 1.0 / throughtPerInstance);
                final float batchUtil = BatchLatencyParse.qpsList.get(i) * 1.0f / numberOfInstance / throughtPerInstance * 100.0f;
                final float queueLatency = 1000.0f / throughtPerInstance * batchSize;
                final float timeUtil = BatchLatencyParse.qpsList.get(i) * 100.0f / (throughtPerInstance * numberOfInstance);
                writer.write(BatchLatencyParse.qpsList.get(i) + "," + throughtPerInstance * numberOfInstance + "," + cpuCore * numberOfInstance + "," + cpuCore * 64 * numberOfInstance + "," + gpuCore * numberOfInstance + "," + gpuCore * 142 * numberOfInstance + "," + (cpuCore * 64 * numberOfInstance + gpuCore * 142 * numberOfInstance) + "," + batchSize + "," + throughtPerInstance + "," + numberOfInstance + "," + timeUtil + "," + batchUtil + "\n");
                sumqps += BatchLatencyParse.qpsList.get(i);
                totalThroughput += throughtPerInstance * numberOfInstance;
                cost += cpuCore * 64 * numberOfInstance + gpuCore * 142 * numberOfInstance;
            }
            if (i % 100 == 0) {
                writer.flush();
            }
        }
        writer.flush();
        writer.close();
        System.out.println(sumqps);
        System.out.println(sumqps / (totalThroughput * 1.138));
        System.out.println(sumqps / (cost * 1.138));
    }
    
    public String handlerMaxMinCap(final String curQps, final List<String> podsList) {
        String maxCap = "";
        String minCap = "";
        final Iterator<String> iterator = podsList.iterator();
        if (iterator.hasNext()) {
            final String item = iterator.next();
            final String[] splits = item.split(",");
            maxCap = splits[2].split("=")[1];
            minCap = splits[3].split("=")[1];
        }
        return String.valueOf(curQps) + "," + maxCap + "," + minCap;
    }
    
    public String handlerResource(final String curQps, final List<String> podsList) {
        String config = "";
        int cpuCore = 0;
        int gpuSM = 0;
        for (final String item : podsList) {
            final String[] splits = item.split(" ");
            config = splits[7].replace("lstm-maxclass-2365", "lstm-2365");
            cpuCore += Integer.parseInt((String)config.subSequence(config.indexOf("-t") + 2, config.indexOf("-g")));
            gpuSM += Integer.parseInt((String)config.subSequence(config.indexOf("-s") + 2, config.indexOf("-m")));
        }
        final int resourceCost = cpuCore * 64 + gpuSM * 142;
        return String.valueOf(cpuCore) + "," + cpuCore * 64 + "," + gpuSM + "," + gpuSM * 142 + "," + resourceCost;
    }
    
    public String handlerInstance(final String curQps, final List<String> podsList) {
        String config = "";
        String lotteryStr = "";
        int cpuCap = 0;
        int gpuCap = 0;
        int qx = 0;
        int cpuLottery = 0;
        int gpuLottery = 0;
        int lottery = 0;
        for (final String item : podsList) {
            final String[] splits = item.split(" ");
            config = splits[7].replace("lstm-maxclass-2365", "lstm-2365");
            qx = Integer.parseInt((String)config.subSequence(config.indexOf("-qx") + 3, config.indexOf("-qi")));
            if (config.contains("-g-1")) {
                cpuCap += qx;
            }
            else {
                gpuCap += qx;
            }
            lotteryStr = splits[8];
            lottery = Integer.parseInt(lotteryStr.split("=")[1].replace(",", ""));
            if (config.contains("-g-1")) {
                cpuLottery += lottery;
            }
            else {
                gpuLottery += lottery;
            }
        }
        return String.valueOf(cpuCap + gpuCap) + "," + cpuCap + "," + gpuCap + "," + (cpuLottery + gpuLottery) + "," + cpuLottery + "," + gpuLottery;
    }
    
    public String handlerBatchConfig(final String curQps, final List<String> podsList) {
        String config = "";
        String cpuCore = "";
        String gpuSM = "";
        String batch = "";
        String result = "";
        for (final String item : podsList) {
            final String[] splits = item.split(" ");
            config = splits[7].replace("lstm-maxclass-2365", "lstm-2365");
            batch = (String)config.subSequence(config.indexOf("-b") + 2, config.indexOf("-qx"));
            cpuCore = (String)config.subSequence(config.indexOf("-t") + 2, config.indexOf("-g"));
            gpuSM = (String)config.subSequence(config.indexOf("-s") + 2, config.indexOf("-m"));
            result = String.valueOf(result) + batch + "," + cpuCore + "," + gpuSM + "\n";
        }
        return result;
    }
}
