// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.io.IOException;
import java.util.StringTokenizer;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class RPC
{
    private static Map programs;
    public static final int UDP = 32;
    public static final int TCP = 16;
    
    public static native int ping(final String p0, final int p1, final long p2, final long p3);
    
    public static native String strerror(final int p0);
    
    public static int ping(final String hostname, final int protocol, final String program, final long version) {
        return ping(hostname, protocol, getProgram(program), version);
    }
    
    public static int ping(final String hostname, final long program) {
        return ping(hostname, 32, program, 2L);
    }
    
    public static int ping(final String hostname, final String program) {
        return ping(hostname, 32, program, 2L);
    }
    
    private static void parse(final String fileName) {
        RPC.programs = new HashMap();
        final File file = new File(fileName);
        if (!file.exists()) {
            return;
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.length() != 0) {
                    if (line.charAt(0) == '#') {
                        continue;
                    }
                    final StringTokenizer st = new StringTokenizer(line, " \t");
                    if (st.countTokens() < 2) {
                        continue;
                    }
                    final String name = st.nextToken().trim();
                    final String num = st.nextToken().trim();
                    RPC.programs.put(name, num);
                }
            }
        }
        catch (IOException e) {}
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException ex) {}
            }
        }
    }
    
    public static long getProgram(final String program) {
        if (RPC.programs == null) {
            parse("/etc/rpc");
        }
        final Object obj = RPC.programs.get(program);
        if (obj == null) {
            return -1L;
        }
        Long num;
        if (obj instanceof Long) {
            num = (Long)obj;
        }
        else {
            num = Long.valueOf((String)obj);
            RPC.programs.put(program, num);
        }
        return num;
    }
    
    public static void main(final String[] args) throws Exception {
        Sigar.load();
        final int retval = ping(args[0], args[1]);
        System.out.println("(" + retval + ") " + strerror(retval));
    }
    
    static {
        RPC.programs = null;
    }
}
