// 
// Decompiled by Procyon v0.5.36
// 

package util.resource;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class BashService
{
    private static BashService service;
    
    static {
        BashService.service = null;
    }
    
    private BashService() {
    }
    
    public static synchronized BashService getInstance() {
        if (BashService.service == null) {
            BashService.service = new BashService();
        }
        return BashService.service;
    }
    
    public String execCommand(final String command) {
        String result = "";
        final String[] cmd = { "/bin/sh", "-c", command };
        String err = null;
        String line = null;
        try {
            final Process process = Runtime.getRuntime().exec(cmd);
            final BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            final InputStreamReader isr = new InputStreamReader(process.getInputStream());
            final LineNumberReader input = new LineNumberReader(isr);
            while ((err = br.readLine()) != null || (line = input.readLine()) != null) {
                if (line == null) {
                    result = err;
                }
                else {
                    result = line;
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public List<String> execCommandList(final String command) {
        final List<String> result = new ArrayList<String>();
        final String[] cmd = { "/bin/sh", "-c", command };
        String err = null;
        String line = null;
        try {
            final Process process = Runtime.getRuntime().exec(cmd);
            final BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            final InputStreamReader isr = new InputStreamReader(process.getInputStream());
            final LineNumberReader input = new LineNumberReader(isr);
            while ((err = br.readLine()) != null || (line = input.readLine()) != null) {
                if (line != null) {
                    result.add(line);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
