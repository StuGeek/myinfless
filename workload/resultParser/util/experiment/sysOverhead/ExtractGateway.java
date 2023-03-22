// 
// Decompiled by Procyon v0.5.36
// 

package util.experiment.sysOverhead;

import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import util.tools.FileOperation;

public class ExtractGateway
{
    public static void main(final String[] args) throws Exception {
        final FileOperation fileOperation = new FileOperation();
        final List<String> fileList = fileOperation.readStringFile("C:\\Users\\DELL\\Desktop\\\u65b0\u5efa\u6587\u4ef6\u5939 (2)\\f16_60_gateway.txt");
        final Map<String, Double> map = new HashMap<String, Double>();
        int countFor = 0;
        int countGet = 0;
        int countSet = 0;
        for (final String str : fileList) {
            final int costIndex = str.indexOf("took:");
            double cost = 0.0;
            if (costIndex != -1) {
                final String sub = str.substring(costIndex + 5, str.length() - 1);
                if (sub.contains("s")) {
                    cost = Double.valueOf(sub.substring(0, sub.length() - 1));
                }
                else {
                    cost = Double.valueOf(sub);
                }
            }
            if (str.contains("forward")) {
                ++countFor;
                map.put("forward", map.getOrDefault("forward", 0.0) + cost);
            }
            else if (str.contains("GetReplicas")) {
                ++countGet;
                map.put("GetReplicas", map.getOrDefault("GetReplicas", 0.0) + cost);
            }
            else {
                if (!str.contains("SetReplicas")) {
                    continue;
                }
                ++countSet;
                map.put("SetReplicas", map.getOrDefault("SetReplicas", 0.0) + cost);
            }
        }
        double avgFor = 0.0;
        double avgGet = 0.0;
        double avgSet = 0.0;
        if (countFor != 0) {
            avgFor = map.get("forward") / countFor;
        }
        if (countGet != 0) {
            avgGet = map.get("GetReplicas") / countGet;
        }
        if (countSet != 0) {
            avgSet = map.get("SetReplicas") / countSet;
        }
        System.out.println("forward: " + avgFor);
        System.out.println("GetReplicas: " + avgGet);
        System.out.println("SetReplicas: " + avgSet);
    }
}
