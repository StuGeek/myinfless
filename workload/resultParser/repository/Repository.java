// 
// Decompiled by Procyon v0.5.36
// 

package repository;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

public class Repository
{
    private static Repository repository;
    public static String resNetBaseURL;
    public static String resNetParmStr;
    public static String resNet50BaseURL;
    public static String resNet50ParmStr;
    public static String mnistBaseURL;
    public static String mnistParmStr;
    public static String halfBaseURL;
    public static String halfParmStr;
    public static String catdogBaseURL;
    public static String catdogParmStr;
    public static String lstm2365BaseURL;
    public static String lstm2365ParmStr;
    public static String textcnn69BaseURL;
    public static String textcnn69ParmStr;
    public static String ssdBaseURL;
    public static String ssdParmStr;
    public static String yamnetBaseURL;
    public static String yamnetParmStr;
    public static String mobilenetBaseURL;
    public static String mobilenetParmStr;
    public static String reqBaseURL;
    public static String reqParmStr;
    public static final int coresPerSocket = 10;
    public static boolean onlineDataFlag;
    public static boolean sendFlag;
    public static final int concurrency = 0;
    public static int realRequestIntensity;
    
    static {
        Repository.repository = null;
        Repository.resNetBaseURL = "";
        Repository.resNetParmStr = "";
        Repository.resNet50BaseURL = "";
        Repository.resNet50ParmStr = "";
        Repository.mnistBaseURL = "";
        Repository.mnistParmStr = "";
        Repository.halfBaseURL = "";
        Repository.halfParmStr = "";
        Repository.catdogBaseURL = "";
        Repository.catdogParmStr = "";
        Repository.lstm2365BaseURL = "";
        Repository.lstm2365ParmStr = "";
        Repository.textcnn69BaseURL = "";
        Repository.textcnn69ParmStr = "";
        Repository.ssdBaseURL = "";
        Repository.ssdParmStr = "";
        Repository.yamnetBaseURL = "";
        Repository.yamnetParmStr = "";
        Repository.mobilenetBaseURL = "";
        Repository.mobilenetParmStr = "";
        Repository.reqBaseURL = "";
        Repository.reqParmStr = "";
        Repository.onlineDataFlag = false;
        Repository.sendFlag = false;
        Repository.realRequestIntensity = 0;
        init();
    }
    
    private Repository() {
    }
    
    public static synchronized Repository getInstance() {
        if (Repository.repository == null) {
            Repository.repository = new Repository();
        }
        return Repository.repository;
    }
    
    public static void init() {
        readProperties();
    }
    
    private static void readProperties() {
        final Properties prop = new Properties();
        final InputStream is = Repository.class.getResourceAsStream("/sys.properties");
        try {
            prop.load(is);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Repository.resNetBaseURL = prop.getProperty("resNetBaseURL").trim();
        Repository.resNetParmStr = prop.getProperty("resNetParmStr").trim();
        Repository.resNet50BaseURL = prop.getProperty("resNet50BaseURL").trim();
        Repository.resNet50ParmStr = prop.getProperty("resNet50ParmStr").trim();
        Repository.mnistBaseURL = prop.getProperty("mnistBaseURL").trim();
        Repository.mnistParmStr = prop.getProperty("mnistParmStr").trim();
        Repository.halfBaseURL = prop.getProperty("halfBaseURL").trim();
        Repository.halfParmStr = prop.getProperty("halfParmStr").trim();
        Repository.catdogBaseURL = prop.getProperty("catdogBaseURL").trim();
        Repository.catdogParmStr = prop.getProperty("catdogParmStr").trim();
        Repository.lstm2365BaseURL = prop.getProperty("lstm2365BaseURL").trim();
        Repository.lstm2365ParmStr = prop.getProperty("lstm2365ParmStr").trim();
        Repository.textcnn69BaseURL = prop.getProperty("textcnn69BaseURL").trim();
        Repository.textcnn69ParmStr = prop.getProperty("textcnn69ParmStr").trim();
        Repository.ssdBaseURL = prop.getProperty("ssdBaseURL").trim();
        Repository.ssdParmStr = prop.getProperty("ssdParmStr").trim();
        Repository.yamnetBaseURL = prop.getProperty("yamnetBaseURL").trim();
        Repository.yamnetParmStr = prop.getProperty("yamnetParmStr").trim();
        Repository.mobilenetBaseURL = prop.getProperty("mobilenetBaseURL").trim();
        Repository.mobilenetParmStr = prop.getProperty("mobilenetParmStr").trim();
    }
    
    public void setURL(final String modelName, final String Ip, final int port) {
        if (modelName.equals("resnet")) {
            Repository.reqBaseURL = Repository.resNetBaseURL;
            Repository.reqParmStr = Repository.resNetParmStr;
        }
        else if (modelName.equals("resnet-50")) {
            Repository.reqBaseURL = Repository.resNet50BaseURL;
            Repository.reqParmStr = Repository.resNet50ParmStr;
        }
        else if (modelName.equals("minst")) {
            Repository.reqBaseURL = Repository.mnistBaseURL;
            Repository.reqParmStr = Repository.mnistParmStr;
        }
        else if (modelName.equals("half")) {
            Repository.reqBaseURL = Repository.halfBaseURL;
            Repository.reqParmStr = Repository.halfParmStr;
        }
        else if (modelName.equals("catdog")) {
            Repository.reqBaseURL = Repository.catdogBaseURL;
            Repository.reqParmStr = Repository.catdogParmStr;
        }
        else if (modelName.equals("lstm-maxclass-2365")) {
            Repository.reqBaseURL = Repository.lstm2365BaseURL;
            Repository.reqParmStr = Repository.lstm2365ParmStr;
        }
        else if (modelName.equals("textcnn-69")) {
            Repository.reqBaseURL = Repository.textcnn69BaseURL;
            Repository.reqParmStr = Repository.textcnn69ParmStr;
        }
        else if (modelName.equals("mobilenet")) {
            Repository.reqBaseURL = Repository.mobilenetBaseURL;
            Repository.reqParmStr = Repository.mobilenetParmStr;
        }
        else if (modelName.equals("ssd")) {
            Repository.reqBaseURL = Repository.ssdBaseURL;
            Repository.reqParmStr = Repository.ssdParmStr;
        }
        else if (modelName.equals("yamnet")) {
            Repository.reqBaseURL = Repository.yamnetBaseURL;
            Repository.reqParmStr = Repository.yamnetParmStr;
        }
        Repository.reqBaseURL = Repository.reqBaseURL.replace("Ip", Ip);
        Repository.reqBaseURL = Repository.reqBaseURL.replace("port", Integer.toString(port));
        System.out.println(Repository.reqBaseURL);
    }
}
