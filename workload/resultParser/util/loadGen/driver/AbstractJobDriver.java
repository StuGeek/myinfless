// 
// Decompiled by Procyon v0.5.36
// 

package util.loadGen.driver;

import java.util.ArrayList;
import org.apache.http.impl.client.CloseableHttpClient;
import java.util.Random;
import java.util.List;

public abstract class AbstractJobDriver
{
    protected List<String> queryItemsList;
    protected int queryItemListSize;
    public String queryItemsStr;
    protected String jsonParmStr;
    protected Random random;
    protected CloseableHttpClient httpClient;
    
    public AbstractJobDriver() {
        this.queryItemsList = new ArrayList<String>();
        this.queryItemsStr = "";
        this.jsonParmStr = "";
        this.random = new Random();
    }
    
    protected abstract void initVariables();
    
    public abstract void executeJob(final int p0);
}
