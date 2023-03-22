// 
// Decompiled by Procyon v0.5.36
// 

package scs.util.repository;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import scs.util.rmi.LoadInterface;

public class Repository
{
    private static Repository repository;
    public static LoadInterface loader;
    public static boolean SYSTEM_RUN_FLAG;
    public static boolean recordLatency;
    public static boolean recordUtility;
    public static String loaderRmiUrl;
    public static String resultFilePath;
    public static int maxSimQPS;
    public static float simQpsPeekRate;
    public static String realQpsFilePath;
    public static int simQpsRemainInterval;
    public static int systemRunTime;
    public static int serviceId;
    public static int concurrency;
    
    static {
        Repository.repository = null;
        Repository.loader = null;
        Repository.SYSTEM_RUN_FLAG = true;
        Repository.recordLatency = false;
        Repository.recordUtility = false;
        Repository.loaderRmiUrl = "http://192.168.1.129:8080";
        Repository.resultFilePath = "/home/tank/simLoad/result/";
        Repository.maxSimQPS = 0;
        Repository.simQpsPeekRate = 0.0f;
        Repository.realQpsFilePath = "";
        Repository.simQpsRemainInterval = 0;
        Repository.systemRunTime = 0;
        Repository.serviceId = 0;
        Repository.concurrency = 0;
    }
    
    private Repository() {
    }
    
    public static synchronized Repository getInstance() {
        if (Repository.repository == null) {
            Repository.repository = new Repository();
        }
        return Repository.repository;
    }
    
    public static void setupRmiConnection() {
        try {
            Repository.loader = (LoadInterface)Naming.lookup(Repository.loaderRmiUrl);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (RemoteException e2) {
            e2.printStackTrace();
        }
        catch (NotBoundException e3) {
            e3.printStackTrace();
        }
        if (Repository.loader != null) {
            System.out.println(String.valueOf(Repository.loaderRmiUrl) + "\u5efa\u7acb\u8fde\u63a5 success");
        }
        else {
            System.out.println(String.valueOf(Repository.loaderRmiUrl) + "\u5efa\u7acb\u8fde\u63a5 fail");
        }
    }
}
