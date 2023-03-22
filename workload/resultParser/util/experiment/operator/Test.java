// 
// Decompiled by Procyon v0.5.36
// 

package util.experiment.operator;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import pojo.TwoTuple;
import java.util.ArrayList;
import util.tools.FileOperation;
import java.util.HashMap;
import java.util.Map;

public class Test
{
    private static Map<String, Float> profileMap;
    
    static {
        Test.profileMap = new HashMap<String, Float>();
    }
    
    public static void main(final String[] args) throws IOException {
        final List<String> list = new FileOperation().readStringFile("C:\\Users\\DELL\\Desktop\\pieces\u7ed3\u679c\\\u7b97\u5b50\u523b\u753b\u7cbe\u5ea6\\profiler\\resnet-50-profile-results-cpu-gpu.txt");
        for (final String item : list) {
            final String[] splits = item.split(" ");
            final float latency = Float.parseFloat(splits[4]);
            Test.profileMap.put(String.valueOf(splits[1]) + "_" + splits[2] + "_" + splits[3], latency);
        }
        final Long start = System.nanoTime();
        final int b = 16;
        final int r = 100;
        final int target = 350;
        final ArrayList<TwoTuple<String, String>> Result = new ArrayList<TwoTuple<String, String>>();
        float allow = target / 2.0f;
        float latency2 = 0.0f;
        int L = 0;
        if (b == 1) {
            allow = (float)target;
        }
        for (final String key : Test.profileMap.keySet()) {
            if (key.startsWith(String.valueOf(b) + "_")) {
                latency2 = Test.profileMap.get(key);
                L = (int)(Math.ceil(1.0f / (target - latency2)) * b);
                if (b == 1) {
                    L = 1;
                }
                if (!(latency2 <= allow & L <= r)) {
                    continue;
                }
                Result.add(new TwoTuple<String, String>(key, Integer.toString(b)));
            }
        }
        final Long end = System.nanoTime();
        System.out.println(end - start);
    }
    
    public static void generator() {
        final String modelName = "resnet-50";
        System.out.println("docker stop " + modelName);
        System.out.println("docker rm " + modelName);
        System.out.println("docker run -itd --name=" + modelName + " -p 9501:8501 --cpuset-cpus=10-50 --memory=4g --ipc=host -v /home/tank/yanan/models/" + modelName + ":/models/" + modelName + " -e CUDA_VISIBLE_DEVICES=-1 -e MODEL_NAME=" + modelName + " tensorflow/serving:latest-gpu");
        System.out.println("sleep 5");
        System.out.println("python ../" + modelName + "_client_http.py >> /dev/null");
        System.out.println("sleep 3");
        System.out.println("python ../" + modelName + "_client_http.py >> /dev/null");
        System.out.println("docker update " + modelName + " --cpuset-cpus=10 --cpu-period=1000000 --cpu-quota=100000 >>/dev/null");
        System.out.println("echo \"#\"");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("docker update " + modelName + " --cpuset-cpus=10 --cpu-period=1000000 --cpu-quota=200000 >>/dev/null");
        System.out.println("echo \"#\"");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("docker update " + modelName + " --cpuset-cpus=10 --cpu-period=1000000 --cpu-quota=400000 >>/dev/null");
        System.out.println("echo \"#\"");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("docker update " + modelName + " --cpuset-cpus=10 --cpu-period=1000000 --cpu-quota=500000 >>/dev/null");
        System.out.println("echo \"#\"");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("docker update " + modelName + " --cpuset-cpus=10 --cpu-period=1000000 --cpu-quota=700000 >>/dev/null");
        System.out.println("echo \"#\"");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("docker update " + modelName + " --cpuset-cpus=10 --cpu-period=1000000 --cpu-quota=800000 >>/dev/null");
        System.out.println("echo \"#\"");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("docker update " + modelName + " --cpuset-cpus=10 --cpu-period=1000000 --cpu-quota=900000 >>/dev/null");
        System.out.println("echo \"#\"");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("docker update " + modelName + " --cpuset-cpus=10,50 --cpu-period=1000000 --cpu-quota=1100000 >>/dev/null");
        System.out.println("echo \"#\"");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("docker update " + modelName + " --cpuset-cpus=10,50 --cpu-period=1000000 --cpu-quota=1200000 >>/dev/null");
        System.out.println("echo \"#\"");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("docker update " + modelName + " --cpuset-cpus=10,50 --cpu-period=1000000 --cpu-quota=1400000 >>/dev/null");
        System.out.println("echo \"#\"");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("docker update " + modelName + " --cpuset-cpus=10,50 --cpu-period=1000000 --cpu-quota=1500000 >>/dev/null");
        System.out.println("echo \"#\"");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("docker update " + modelName + " --cpuset-cpus=10,50 --cpu-period=1000000 --cpu-quota=1700000 >>/dev/null");
        System.out.println("echo \"#\"");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("docker update " + modelName + " --cpuset-cpus=10,50 --cpu-period=1000000 --cpu-quota=1800000 >>/dev/null");
        System.out.println("echo \"#\"");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("docker update " + modelName + " --cpuset-cpus=10,50,11 --cpu-period=1000000 --cpu-quota=2100000 >>/dev/null");
        System.out.println("echo \"#\"");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("docker update " + modelName + " --cpuset-cpus=10,50,11 --cpu-period=1000000 --cpu-quota=2400000 >>/dev/null");
        System.out.println("echo \"#\"");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("python ../" + modelName + "_client_http.py");
        System.out.println("docker stop " + modelName + " >>/dev/null");
        System.out.println("docker rm " + modelName + " >>/dev/null");
    }
    
    public static void parse() throws IOException {
        final List<String> list = new FileOperation().readStringFile("C:\\Users\\DELL\\Desktop\\resnet-50.txt");
        final StringBuilder builder = new StringBuilder();
        for (final String item : list) {
            builder.append(item).append(" ");
        }
        final String[] splits = builder.toString().split("#");
        final int size = splits.length;
        float max = 0.0f;
        float temp = 0.0f;
        for (int i = 1; i < size; ++i) {
            final String[] aStrings = splits[i].trim().split(" ");
            max = 0.0f;
            String[] array;
            for (int length = (array = aStrings).length, j = 0; j < length; ++j) {
                final String item2 = array[j];
                temp = Float.parseFloat(item2);
                if (temp > max) {
                    max = temp;
                }
            }
            System.out.println(max);
        }
    }
}
