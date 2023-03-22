// 
// Decompiled by Procyon v0.5.36
// 

package util.experiment.sysOverhead;

import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import util.tools.FileOperation;

public class ExtractScale
{
    public static void main(final String[] args) throws Exception {
        final FileOperation fileOperation = new FileOperation();
        final List<String> fileList = fileOperation.readStringFile("C:\\Users\\DELL\\Desktop\\\u65b0\u5efa\u6587\u4ef6\u5939 (2)\\f16_60_faas.txt");
        final Map<String, Double> map = new HashMap<String, Double>();
        int countScale = 0;
        int countBind = 0;
        for (final String str : fileList) {
            final int costIndex = str.indexOf("took");
            double cost = 0.0;
            if (costIndex != -1) {
                final String sub = str.substring(costIndex + 6, str.length() - 1);
                if (sub.contains("s")) {
                    cost = Double.valueOf(sub.substring(0, sub.length() - 1));
                }
                else {
                    cost = Double.valueOf(sub);
                }
            }
            if (str.contains("scale")) {
                ++countScale;
                map.put("scale", map.getOrDefault("scale", 0.0) + cost);
            }
            else {
                if (!str.contains("bind")) {
                    continue;
                }
                ++countBind;
                map.put("bind", map.getOrDefault("bind", 0.0) + cost);
            }
        }
        double avgScale = 0.0;
        double avgBind = 0.0;
        if (countScale != 0) {
            avgScale = map.get("scale") / countScale;
        }
        if (countBind != 0) {
            avgBind = map.get("bind") / countBind;
        }
        System.out.println("scale: " + avgScale);
        System.out.println("bind: " + avgBind);
    }
}
