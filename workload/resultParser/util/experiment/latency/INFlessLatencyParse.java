// 
// Decompiled by Procyon v0.5.36
// 

package util.experiment.latency;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.File;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import util.tools.FileOperation;
import java.util.HashMap;
import java.util.Map;

public class INFlessLatencyParse
{
    private static Map<String, Float> profileMap;
    private static Map<String, String> repeatMap;
    
    static {
        INFlessLatencyParse.profileMap = new HashMap<String, Float>();
        INFlessLatencyParse.repeatMap = new HashMap<String, String>();
    }
    
    public static void main(final String[] args) throws IOException {
        final List<String> list = new FileOperation().readStringFile("C:\\Users\\DELL\\Desktop\\pieces\u7ed3\u679c\\\u7b97\u5b50\u523b\u753b\u7cbe\u5ea6\\profiler\\resnet-50-profile-results-cpu-gpu.txt");
        for (final String item : list) {
            final String[] splits = item.split(" ");
            final float latency = Float.parseFloat(splits[4]);
            INFlessLatencyParse.profileMap.put(String.valueOf(splits[1]) + "_" + splits[2] + "_" + splits[3], latency);
        }
        final String filePath = "C:\\Users\\DELL\\Desktop\\pieces\u7ed3\u679c\\\u8c03\u5ea6\u6548\u7387\\INFless\\resnet-50\\";
        final String fileName = "resnet-50-350ms-300QPS-workload3-6s-60min.txt";
        new INFlessLatencyParse().readStringFile(filePath, fileName);
        System.out.println(INFlessLatencyParse.repeatMap.size());
    }
    
    public void readStringFile(final String filePath, final String fileName) throws IOException {
        final FileWriter writer1 = new FileWriter(String.valueOf(filePath) + "latency\\" + fileName.replace(".txt", "_latency.csv"));
        final FileWriter writer2 = new FileWriter(String.valueOf(filePath) + "util\\" + fileName.replace(".txt", "_util.csv"));
        int i = 0;
        String curQps = "";
        String lastQps = "";
        final List<String> podsList = new ArrayList<String>();
        final File file = new File(String.valueOf(filePath) + fileName);
        BufferedReader reader = null;
        try {
            final FileInputStream fileInputStream = new FileInputStream(file);
            final InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String line = "";
            while ((line = reader.readLine()) != null) {
                if (line.contains("ps=")) {
                    curQps = line.split("ps=")[1].trim();
                    if (podsList.size() > 0) {
                        writer1.write(this.handlerLatency(podsList));
                        writer2.write(this.handlerInstanceTimeUtilization(podsList));
                        if (++i % 100 == 0) {
                            writer1.flush();
                            writer2.flush();
                        }
                        podsList.clear();
                    }
                    lastQps = curQps;
                }
                if (line.contains("type i")) {
                    podsList.add(line);
                }
            }
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }
        catch (IOException e3) {
            e3.printStackTrace();
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
        }
        if (reader != null) {
            try {
                reader.close();
            }
            catch (IOException e4) {
                e4.printStackTrace();
            }
        }
        if (podsList.size() > 0) {
            writer1.write(this.handlerLatency(podsList));
            writer2.write(this.handlerInstanceTimeUtilization(podsList));
        }
        writer1.flush();
        writer1.close();
        writer2.flush();
        writer2.close();
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
    
    public String handlerResource(final List<String> podsList) {
        String config = "";
        int cpuCore = 0;
        int gpuSM = 0;
        for (final String item : podsList) {
            final String[] splits = item.split(" ");
            config = splits[7].replace("lstm-maxclass-2365", "lstm-2365");
            config = splits[7].replace("ssd", "ssd-0");
            config = splits[7].replace("mobilenet", "mobilenet-0");
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
            config = splits[7].replace("ssd", "ssd-0");
            config = splits[7].replace("mobilenet", "mobilenet-0");
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
    
    public String handlerLatency(final List<String> podsList) {
        String config = "";
        String cpuCore = "";
        String gpuSM = "";
        String batch = "";
        String result = "";
        String lottery = "";
        String maxCap = "";
        for (final String item : podsList) {
            final String[] splits = item.split(" ");
            config = splits[7].replace("lstm-maxclass-2365", "lstm-2365");
            config = splits[7].replace("ssd", "ssd-0");
            config = splits[7].replace("mobilenet", "mobilenet-0");
            batch = (String)config.subSequence(config.indexOf("-b") + 2, config.indexOf("-qx"));
            cpuCore = (String)config.subSequence(config.indexOf("-t") + 2, config.indexOf("-g"));
            gpuSM = (String)config.subSequence(config.indexOf("-s") + 2, config.indexOf("-m"));
            lottery = splits[8].split("=")[1].replace(",", "");
            final float execLatency = INFlessLatencyParse.profileMap.get(String.valueOf(cpuCore) + "_" + gpuSM + "_" + batch);
            final float queueLatency = 1000 * Integer.parseInt(batch) / Float.parseFloat(lottery);
            maxCap = (String)config.subSequence(config.indexOf("-qx") + 3, config.indexOf("-qi"));
            if (!lottery.equals("0")) {
                if (execLatency + queueLatency > 350.0f) {
                    continue;
                }
                if (INFlessLatencyParse.repeatMap.containsKey(String.valueOf(execLatency) + "," + queueLatency)) {
                    continue;
                }
                INFlessLatencyParse.repeatMap.put(String.valueOf(execLatency) + "," + queueLatency, "");
                result = String.valueOf(result) + batch + "," + cpuCore + "," + gpuSM + "," + maxCap + "," + execLatency + "," + queueLatency + "\n";
            }
        }
        return result;
    }
    
    public String handlerInstanceTimeUtilization(final List<String> podsList) {
        String config = "";
        String cpuCore = "";
        String gpuSM = "";
        String batch = "";
        String result = "";
        String lottery = "";
        String maxCap = "";
        for (final String item : podsList) {
            final String[] splits = item.split(" ");
            config = splits[7].replace("lstm-maxclass-2365", "lstm-2365");
            config = splits[7].replace("ssd", "ssd-0");
            config = splits[7].replace("mobilenet", "mobilenet-0");
            batch = (String)config.subSequence(config.indexOf("-b") + 2, config.indexOf("-qx"));
            cpuCore = (String)config.subSequence(config.indexOf("-t") + 2, config.indexOf("-g"));
            gpuSM = (String)config.subSequence(config.indexOf("-s") + 2, config.indexOf("-m"));
            lottery = splits[8].split("=")[1].replace(",", "");
            final float execLatency = INFlessLatencyParse.profileMap.get(String.valueOf(cpuCore) + "_" + gpuSM + "_" + batch);
            final float queueLatency = 1000 * Integer.parseInt(batch) / Float.parseFloat(lottery);
            maxCap = (String)config.subSequence(config.indexOf("-qx") + 3, config.indexOf("-qi"));
            if (!lottery.equals("0")) {
                if (execLatency + queueLatency > 8000.0f) {
                    continue;
                }
                final float timeUtil = (execLatency + queueLatency) / (queueLatency + queueLatency) * 100.0f;
                final float batchUtil = Float.parseFloat(lottery) / Float.parseFloat(maxCap) * 100.0f;
                result = String.valueOf(result) + batch + "," + cpuCore + "," + gpuSM + "," + maxCap + "," + execLatency + "," + queueLatency + "," + timeUtil + "," + batchUtil + "\n";
            }
        }
        return result;
    }
}
