// 
// Decompiled by Procyon v0.5.36
// 

package util.experiment.throughput;

import java.util.Iterator;
import java.util.List;
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

public class INFlessResultParseTrafficScenario
{
    public static void main(final String[] args) throws IOException {
        final String filePath = "C:\\Users\\DELL\\Desktop\\pieces\u7ed3\u679c\\\u8c03\u5ea6\u6548\u7387\\traffic\\";
        final String fileName = "log_600msSLO_8000-16000-24000.txt";
        new INFlessResultParseTrafficScenario().readStringFile("mobilenet", filePath, fileName);
        new INFlessResultParseTrafficScenario().readStringFile("ssd", filePath, fileName);
        new INFlessResultParseTrafficScenario().readStringFile("resnet-50", filePath, fileName);
    }
    
    public void readStringFile(final String modelName, final String filePath, final String fileName) throws IOException {
        final FileWriter writer = new FileWriter(String.valueOf(filePath) + modelName + "\\" + fileName.replace("txt", "csv"));
        final FileWriter writer2 = new FileWriter(String.valueOf(filePath) + modelName + "\\" + fileName.replace(".txt", "_batchConfig.csv"));
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
            final String flag = String.valueOf(modelName) + " funcRealRps=";
            final String flag2 = String.valueOf(modelName) + " type i";
            while ((line = reader.readLine()) != null) {
                if (line.contains(flag)) {
                    curQps = line.split("ps=")[1].trim();
                    if (podsList.size() > 0) {
                        writer2.write(this.handlerBatchConfig(lastQps, podsList));
                        writer.write(String.valueOf(this.handlerMaxMinCap(lastQps, podsList)) + "," + this.handlerResource(curQps, podsList) + "," + this.handlerInstance(curQps, podsList) + "\n");
                        if (++i % 100 == 0) {
                            writer.flush();
                            writer2.flush();
                        }
                        podsList.clear();
                    }
                    lastQps = curQps;
                }
                if (line.contains(flag2)) {
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
            writer.write(String.valueOf(this.handlerMaxMinCap(lastQps, podsList)) + "," + this.handlerResource(curQps, podsList) + "," + this.handlerInstance(curQps, podsList) + "\n");
            writer2.write(this.handlerBatchConfig(lastQps, podsList));
        }
        writer.flush();
        writer.close();
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
    
    public String handlerResource(final String curQps, final List<String> podsList) {
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
        final String result = String.valueOf(cpuCap + gpuCap) + "," + cpuCap + "," + gpuCap + "," + (cpuLottery + gpuLottery) + "," + cpuLottery + "," + gpuLottery + "," + podsList.size();
        return result;
    }
    
    public String handlerBatchConfig(final String curQps, final List<String> podsList) {
        String config = "";
        String cpuCore = "";
        String gpuSM = "";
        String batch = "";
        String result = "";
        String maxCap = "";
        for (final String item : podsList) {
            final String[] splits = item.split(" ");
            config = splits[7].replace("lstm-maxclass-2365", "lstm-2365");
            config = splits[7].replace("ssd", "ssd-0");
            config = splits[7].replace("mobilenet", "mobilenet-0");
            batch = (String)config.subSequence(config.indexOf("-b") + 2, config.indexOf("-qx"));
            cpuCore = (String)config.subSequence(config.indexOf("-t") + 2, config.indexOf("-g"));
            gpuSM = (String)config.subSequence(config.indexOf("-s") + 2, config.indexOf("-m"));
            maxCap = (String)config.subSequence(config.indexOf("-qx") + 3, config.indexOf("-qi"));
            result = String.valueOf(result) + batch + "," + cpuCore + "," + gpuSM + "," + maxCap + "\n";
        }
        return result;
    }
}
