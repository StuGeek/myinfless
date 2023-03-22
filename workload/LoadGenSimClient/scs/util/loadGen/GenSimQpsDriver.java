// 
// Decompiled by Procyon v0.5.36
// 

package scs.util.loadGen;

import scs.util.tools.FileOperation;
import java.io.IOException;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class GenSimQpsDriver
{
    private static GenSimQpsDriver driver;
    public List<Integer> simRPSList;
    
    static {
        GenSimQpsDriver.driver = null;
    }
    
    private GenSimQpsDriver() {
        this.simRPSList = null;
        this.simRPSList = new ArrayList<Integer>();
    }
    
    public static synchronized GenSimQpsDriver getInstance() {
        if (GenSimQpsDriver.driver == null) {
            GenSimQpsDriver.driver = new GenSimQpsDriver();
        }
        return GenSimQpsDriver.driver;
    }
    
    public void genSimRPSList(final int maxSimQPS, final String realQpsFilePath, final Float simQpsPeekRate) throws IOException {
        final List<Double> realRPSList = this.getRealRPSList(realQpsFilePath);
        double maxRealRPS = 0.0;
        for (final double item : realRPSList) {
            maxRealRPS = ((item > maxRealRPS) ? item : maxRealRPS);
        }
        for (int size = realRPSList.size(), i = 0; i < size; ++i) {
            this.simRPSList.add((int)(realRPSList.get(i) / maxRealRPS * maxSimQPS * simQpsPeekRate));
        }
        if (this.simRPSList.size() > 0) {
            System.out.println("simQPS generating finished, len=" + this.simRPSList.size() + " realQpsFilePath=" + realQpsFilePath + " maxSimQPS=" + maxSimQPS + " simQpsPeekRate=" + simQpsPeekRate);
        }
    }
    
    private List<Double> getRealRPSList(final String filePath) throws IOException {
        final FileOperation fo = new FileOperation();
        return fo.readDoubleFile(filePath);
    }
}
