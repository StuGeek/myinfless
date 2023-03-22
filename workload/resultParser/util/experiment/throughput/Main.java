// 
// Decompiled by Procyon v0.5.36
// 

package util.experiment.throughput;

import java.io.IOException;

public class Main
{
    public static void main(final String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("please input the prefix path of results file");
            System.exit(0);
        }
        final String prefixPath = args[0];
        System.out.println("prefixPath:" + prefixPath);
        new BatchResultParse().batchResult(prefixPath);
        System.out.println("---------------------------");
        new INFlessResultParse().INFlessResult(prefixPath);
    }
}
