// 
// Decompiled by Procyon v0.5.36
// 

package scs.util.utilitization;

import scs.util.tools.DateFormats;
import scs.util.tools.DataFormats;

public class UtilityController extends Thread
{
    private final int SLEEP_TIME = 3000;
    DataFormats dataFormats;
    DateFormats dateFormats;
    
    public UtilityController() {
        this.dataFormats = DataFormats.getInstance();
        this.dateFormats = DateFormats.getInstance();
    }
    
    @Override
    public void run() {
    }
}
