// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.win32;

import org.hyperic.sigar.SigarLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.List;
import java.util.Map;

public class Pdh extends Win32
{
    public static final int VALID_DATA = 0;
    public static final int NO_INSTANCE = -2147481647;
    public static final int NO_COUNTER = -1073738823;
    public static final int NO_OBJECT = -1073738824;
    public static final int NO_MACHINE = -2147481648;
    public static final int BAD_COUNTERNAME = -1073738816;
    public static final String PERFLIB_KEY = "SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion\\Perflib";
    public static final long PERF_TYPE_NUMBER = 0L;
    public static final long PERF_TYPE_COUNTER = 1024L;
    public static final long PERF_TYPE_TEXT = 2048L;
    public static final long PERF_TYPE_ZERO = 3072L;
    private long query;
    private String hostname;
    private static Map counters;
    private static final String DELIM = "\\";
    
    public Pdh() throws Win32Exception {
        this.query = -1L;
        this.hostname = null;
        this.query = pdhOpenQuery();
    }
    
    public Pdh(final String hostName) throws Win32Exception {
        this();
        this.hostname = hostName;
    }
    
    protected void finalize() throws Throwable {
        try {
            this.close();
        }
        finally {
            super.finalize();
        }
    }
    
    public synchronized void close() throws Win32Exception {
        if (this.query != -1L) {
            pdhCloseQuery(this.query);
            this.query = -1L;
        }
    }
    
    public static void enableTranslation() throws Win32Exception {
        if (Pdh.counters != null) {
            return;
        }
        if (LocaleInfo.isEnglish()) {
            return;
        }
        Pdh.counters = getEnglishPerflibCounterMap();
    }
    
    public static Map getEnglishPerflibCounterMap() throws Win32Exception {
        final LocaleInfo locale = new LocaleInfo(9);
        return getPerflibCounterMap(locale);
    }
    
    public static Map getPerflibCounterMap(final LocaleInfo locale) throws Win32Exception {
        final String path = "SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion\\Perflib\\" + locale.getPerflibLangId();
        final RegistryKey key = RegistryKey.LocalMachine.openSubKey(path);
        final PerflibCounterMap counters = new PerflibCounterMap();
        try {
            key.getMultiStringValue("Counter", counters);
        }
        finally {
            key.close();
        }
        return counters.map;
    }
    
    public static String getCounterName(final int index) throws Win32Exception {
        final String name = pdhLookupPerfName(index).trim();
        return name;
    }
    
    public double getSingleValue(final String path) throws Win32Exception {
        return this.getRawValue(path);
    }
    
    public double getRawValue(final String path) throws Win32Exception {
        return this.getValue(path, false);
    }
    
    public double getFormattedValue(final String path) throws Win32Exception {
        return this.getValue(path, true);
    }
    
    private static int[] getCounterIndex(final String englishName) {
        if (Pdh.counters == null) {
            return null;
        }
        return Pdh.counters.get(englishName.toLowerCase());
    }
    
    private static String getCounterName(final String englishName) throws Win32Exception {
        final int[] ix = getCounterIndex(englishName);
        if (ix == null) {
            return englishName;
        }
        final String name = getCounterName(ix[0]);
        return name;
    }
    
    public static String translate(final String path) throws Win32Exception {
        if (Pdh.counters == null) {
            return path;
        }
        final StringBuffer trans = new StringBuffer();
        final StringTokenizer tok = new StringTokenizer(path, "\\");
        final int num = tok.countTokens();
        if (num == 3) {
            final String hostname = tok.nextToken();
            trans.append("\\").append("\\").append(hostname);
        }
        String object = tok.nextToken();
        String instance = null;
        final int ix = object.indexOf(40);
        if (ix != -1) {
            instance = object.substring(ix);
            object = object.substring(0, ix);
        }
        trans.append("\\").append(getCounterName(object));
        if (instance != null) {
            trans.append(instance);
        }
        String counter = tok.nextToken();
        trans.append("\\");
        final int[] cix = getCounterIndex(counter);
        if (cix != null) {
            if (cix.length == 1) {
                counter = getCounterName(cix[0]);
            }
            else {
                for (int i = 0; i < cix.length; ++i) {
                    final String name = getCounterName(cix[i]);
                    if (validate((Object)trans + name) == 0) {
                        counter = name;
                        break;
                    }
                }
            }
        }
        trans.append(counter);
        return trans.toString();
    }
    
    private double getValue(final String path, final boolean format) throws Win32Exception {
        if (this.hostname != null) {
            pdhConnectMachine(this.hostname);
        }
        final long counter = pdhAddCounter(this.query, translate(path));
        try {
            return pdhGetValue(this.query, counter, format);
        }
        finally {
            pdhRemoveCounter(counter);
        }
    }
    
    public String getDescription(final String path) throws Win32Exception {
        final long counter = pdhAddCounter(this.query, translate(path));
        try {
            return pdhGetDescription(counter);
        }
        finally {
            pdhRemoveCounter(counter);
        }
    }
    
    public long getCounterType(final String path) throws Win32Exception {
        final long counter = pdhAddCounter(this.query, translate(path));
        try {
            return pdhGetCounterType(counter);
        }
        finally {
            pdhRemoveCounter(counter);
        }
    }
    
    public static String[] getInstances(final String path) throws Win32Exception {
        final String[] instances = pdhGetInstances(getCounterName(path));
        final HashMap names = new HashMap(instances.length);
        for (int i = 0; i < instances.length; ++i) {
            InstanceIndex ix = names.get(instances[i]);
            if (ix == null) {
                ix = new InstanceIndex();
                names.put(instances[i], ix);
            }
            else {
                final InstanceIndex instanceIndex = ix;
                ++instanceIndex.index;
                instances[i] = instances[i] + "#" + ix.index;
            }
        }
        return instances;
    }
    
    public static String[] getKeys(final String path) throws Win32Exception {
        return pdhGetKeys(getCounterName(path));
    }
    
    public static String[] getObjects() throws Win32Exception {
        return pdhGetObjects();
    }
    
    public static final native int validate(final String p0);
    
    private static final native void pdhConnectMachine(final String p0) throws Win32Exception;
    
    private static final native long pdhOpenQuery() throws Win32Exception;
    
    private static final native void pdhCloseQuery(final long p0) throws Win32Exception;
    
    private static final native long pdhAddCounter(final long p0, final String p1) throws Win32Exception;
    
    private static final native void pdhRemoveCounter(final long p0) throws Win32Exception;
    
    private static final native double pdhGetValue(final long p0, final long p1, final boolean p2) throws Win32Exception;
    
    private static final native String pdhGetDescription(final long p0) throws Win32Exception;
    
    private static final native long pdhGetCounterType(final long p0) throws Win32Exception;
    
    private static final native String[] pdhGetInstances(final String p0) throws Win32Exception;
    
    private static final native String[] pdhGetKeys(final String p0) throws Win32Exception;
    
    private static final native String[] pdhGetObjects() throws Win32Exception;
    
    private static final native String pdhLookupPerfName(final int p0) throws Win32Exception;
    
    private static final native int pdhLookupPerfIndex(final String p0) throws Win32Exception;
    
    public static void main(final String[] args) {
        Pdh pdh = null;
        String objectName = null;
        String partialName = null;
        boolean showValues = false;
        boolean showInstances = false;
        boolean showKeys = false;
        if (args.length > 0) {
            for (int i = 0; i < args.length; ++i) {
                if (args[i].equals("-h") || args[i].equals("-help") || args[i].equals("--help")) {
                    System.out.println("Usage: Pdh [OPTION]");
                    System.out.println("Show information from the Windows PDH");
                    System.out.println("");
                    System.out.println("    --object=NAME    only print info on this object");
                    System.out.println("    --contains=NAME  only print info on objects that");
                    System.out.println("                     contain this substring");
                    System.out.println("-i, --instance       show instances [default=no]");
                    System.out.println("-k, --keys           show keys [default=no]");
                    System.out.println("-v, --values         include key values [default=no]");
                    System.out.println("-h, --help           display help and exit");
                    return;
                }
                if (args[i].equals("-v") || args[i].equals("--values")) {
                    showKeys = true;
                    showValues = true;
                }
                else if (args[i].equals("-i") || args[i].equals("--instances")) {
                    showInstances = true;
                }
                else if (args[i].equals("-k") || args[i].equals("--keys")) {
                    showKeys = true;
                }
                else if (args[i].startsWith("--contains=")) {
                    final int idx = args[i].indexOf("=");
                    partialName = args[i].substring(idx + 1);
                }
                else {
                    if (!args[i].startsWith("--object=")) {
                        System.out.println("Unknown option: " + args[i]);
                        System.out.println("Use --help for usage information");
                        return;
                    }
                    final int idx = args[i].indexOf("=");
                    objectName = args[i].substring(idx + 1);
                }
            }
        }
        try {
            pdh = new Pdh();
            String[] objects;
            if (partialName != null) {
                final List matching = new ArrayList();
                final String[] allObjects = getObjects();
                for (int j = 0; j < allObjects.length; ++j) {
                    if (allObjects[j].toUpperCase().indexOf(partialName.toUpperCase()) != -1) {
                        matching.add(allObjects[j]);
                    }
                }
                objects = matching.toArray(new String[0]);
            }
            else if (objectName != null) {
                objects = new String[] { objectName };
            }
            else {
                objects = getObjects();
            }
            for (int o = 0; o < objects.length; ++o) {
                System.out.println(objects[o]);
                String[] keys;
                try {
                    keys = getKeys(objects[o]);
                }
                catch (Win32Exception e) {
                    System.err.println("Unable to get keys for object=" + objects[o] + " Reason: " + e.getMessage());
                    continue;
                }
                final int pad = getLongestKey(keys);
                final String[] instances = getInstances(objects[o]);
                if (instances.length == 0) {
                    if (showKeys) {
                        for (int k = 0; k < keys.length; ++k) {
                            if (showValues) {
                                final String query = "\\" + objects[o] + "\\" + keys[k];
                                double val;
                                try {
                                    val = pdh.getRawValue(query);
                                }
                                catch (Win32Exception e2) {
                                    System.err.println("Unable to get value for  key=" + query + " Reason: " + e2.getMessage());
                                    continue;
                                }
                                final String out = pad(keys[k], pad, ' ');
                                System.out.println("  " + out + " = " + val);
                            }
                            else {
                                System.out.println("  " + keys[k]);
                            }
                        }
                    }
                }
                else if (showInstances) {
                    for (int l = 0; l < instances.length; ++l) {
                        System.out.println("  " + instances[l]);
                        if (showKeys) {
                            for (int m = 0; m < keys.length; ++m) {
                                if (showValues) {
                                    final String query2 = "\\" + objects[o] + "(" + instances[l] + ")" + "\\" + keys[m];
                                    double val2;
                                    try {
                                        val2 = pdh.getRawValue(query2);
                                    }
                                    catch (Win32Exception e3) {
                                        System.err.println("Unable to get value for key=" + query2 + " Reason: " + e3.getMessage());
                                        continue;
                                    }
                                    final String out2 = pad(keys[m], pad, ' ');
                                    System.out.println("    " + out2 + " = " + val2);
                                }
                                else {
                                    System.out.println("    " + keys[m]);
                                }
                            }
                        }
                    }
                }
            }
            pdh.close();
        }
        catch (Win32Exception e4) {
            System.err.println("Unable to dump PDH data: " + e4.getMessage());
        }
    }
    
    private static String pad(final String value, final int length, final char ch) {
        final StringBuffer padder = new StringBuffer(value);
        if (value.length() < length) {
            for (int i = 0; i < length - value.length(); ++i) {
                padder.append(ch);
            }
        }
        return padder.toString();
    }
    
    private static int getLongestKey(final String[] keys) {
        int longest = 0;
        for (int i = 0; i < keys.length; ++i) {
            final int len = keys[i].length();
            if (len > longest) {
                longest = len;
            }
        }
        return longest;
    }
    
    static {
        Pdh.counters = null;
        final String prop = "sigar.pdh.enableTranslation";
        if (SigarLoader.IS_WIN32 && !"false".equals(System.getProperty("sigar.pdh.enableTranslation"))) {
            try {
                enableTranslation();
            }
            catch (Exception e) {
                System.err.println("sigar.pdh.enableTranslation: " + e.getMessage());
            }
        }
    }
    
    private static class PerflibCounterMap extends ArrayList
    {
        private Map map;
        private String index;
        
        private PerflibCounterMap() {
            this.map = new HashMap();
            this.index = null;
        }
        
        public boolean add(final Object o) {
            if (this.index == null) {
                this.index = (String)o;
                return true;
            }
            final String name = ((String)o).trim().toLowerCase();
            int[] ix = this.map.get(name);
            if (ix == null) {
                ix = new int[] { 0 };
            }
            else {
                final int[] cur = ix;
                ix = new int[cur.length + 1];
                System.arraycopy(cur, 0, ix, 1, cur.length);
            }
            ix[0] = Integer.parseInt(this.index);
            this.map.put(name, ix);
            this.index = null;
            return true;
        }
    }
    
    private static final class InstanceIndex
    {
        long index;
        
        private InstanceIndex() {
            this.index = 0L;
        }
    }
}
