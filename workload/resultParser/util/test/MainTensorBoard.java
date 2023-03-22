// 
// Decompiled by Procyon v0.5.36
// 

package util.test;

import java.io.File;
import util.resource.BashService;
import org.apache.http.impl.client.CloseableHttpClient;
import repository.Repository;
import util.tools.HttpClientPool;
import util.resource.DockerService;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import util.tools.FileOperation;

public class MainTensorBoard
{
    public static void main(final String[] args) throws InterruptedException, IOException {
        final List<String> list = new FileOperation().readStringFile("C:\\Users\\DELL\\Desktop\\mobilenet-profile-results.txt");
        for (final String item : list) {
            final String[] aString = item.split(" ");
            System.out.println(String.valueOf(aString[0]) + " " + aString[1] + " " + aString[2] + " " + aString[3] + " " + aString[4]);
        }
        if (args.length != 1) {
            System.out.println("modelName");
            System.exit(0);
        }
        new MainTensorBoard().record(args[0]);
    }
    
    public void startExper(final String[] args) throws InterruptedException, NumberFormatException, IOException {
        final DockerService dService = DockerService.getInstance();
        final CloseableHttpClient client = HttpClientPool.getInstance().getConnection();
        final String modelName = "resnet";
        final int GpuQuotaStart = 0;
        final int socketId = 0;
        final int CpuQuotaStart = 2;
        final String batches = "1#2#4#8";
        final String[] batchSplits = batches.split("#");
        String[] array;
        for (int length = (array = batchSplits).length, i = 0; i < length; ++i) {
            final String batch = array[i];
            for (int GpuQuota = GpuQuotaStart; GpuQuota <= 50; GpuQuota += 10) {
                String command = dService.genRemoveCommand(modelName);
                command = dService.genRunCommand(modelName, Integer.parseInt(batch), GpuQuota, socketId, CpuQuotaStart);
                for (int CpuQuota = CpuQuotaStart; CpuQuota <= 16; CpuQuota += 2) {
                    command = dService.genUpdateCommand(modelName, socketId, CpuQuota);
                }
            }
        }
        Repository.getInstance().setURL("resnet-50", "192.168.1.106", 8080);
    }
    
    public void record(final String modelName) throws IOException {
        final FileOperation fileOperation = new FileOperation();
        final CloseableHttpClient httpclient = HttpClientPool.getInstance().getConnection();
        final String basePath = "/home/tank/lijie/automatic_profiler/profiled_data/" + modelName + "/tensorflow_stats_outputs/";
        final List<String> profileList = BashService.getInstance().execCommandList("ls /home/tank/lijie/automatic_profiler/profiled_data/" + modelName + "/tensorflow_stats_outputs");
        for (final String profileItem : profileList) {
            final String[] splits = profileItem.split("_");
            if (splits.length == 4) {
                final int cpuQuota = Integer.parseInt(splits[1].replace("cpu", ""));
                final int gpuQuota = Integer.parseInt(splits[2].replace("mps", ""));
                final int batchSize = Integer.parseInt(splits[3].replace("batch", "").replace(".csv", ""));
                final String fileName = String.valueOf(modelName) + "_cpu" + cpuQuota + "_mps" + gpuQuota + "_batch" + batchSize + ".csv";
                final File file = new File(String.valueOf(basePath) + fileName);
                float sum = 0.0f;
                if (!file.exists()) {
                    continue;
                }
                final List<String> csvList = fileOperation.readStringFile(String.valueOf(basePath) + fileName);
                sum = 0.0f;
                for (int size = csvList.size(), i = 1; i < size; ++i) {
                    final String[] items = csvList.get(i).split(",");
                    if (items.length == 4) {
                        sum += Float.parseFloat(items[3]);
                    }
                }
                final String datetimeStr = BashService.getInstance().execCommand("ls /home/tank/lijie/automatic_profiler/profiled_data/" + modelName + "/results/" + modelName + "_cpu" + cpuQuota + "_mps" + gpuQuota + "_batch" + batchSize + "/plugins/profile");
                final String Url = "http://192.168.1.129:9999/data/plugin/profile/data?run=" + modelName + "/results" + "/" + modelName + "_cpu" + cpuQuota + "_mps" + gpuQuota + "_batch" + batchSize + "/" + datetimeStr + "&tag=overview_page@&host=192.168.1.129";
                final String html = HttpClientPool.getResponseHtml(httpclient, Url);
                final int start = html.indexOf("rage");
                final int end = html.indexOf("d_d");
                final String avgTime = html.substring(start, end).split("\"")[2];
                System.out.println(String.valueOf(modelName) + "," + cpuQuota + "," + gpuQuota + "," + batchSize + "," + 0 + "," + sum / 1000.0f + "," + avgTime);
            }
        }
    }
}
