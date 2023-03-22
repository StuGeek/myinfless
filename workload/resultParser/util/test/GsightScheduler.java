// 
// Decompiled by Procyon v0.5.36
// 

package util.test;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;

public class GsightScheduler
{
    static final int funcSize = 26;
    static final int serverNum = 2;
    static int[][] placement;
    static int serverConsumed;
    
    static {
        GsightScheduler.placement = new int[26][2];
        GsightScheduler.serverConsumed = 0;
    }
    
    public static void main(final String[] args) {
        final GsightScheduler scheduler = new GsightScheduler();
        final ArrayList<ServerBean> serverList = new ArrayList<ServerBean>();
        serverList.add(new ServerBean(0, "", 0.1f));
        serverList.add(new ServerBean(1, "", 0.2f));
        final ArrayList<FunctionBean> funcList = new ArrayList<FunctionBean>();
        for (int i = 0; i < 26; ++i) {
            funcList.add(new FunctionBean("", (float)Math.random() * 0.5f, i, 0));
        }
        scheduler.scheduler(2, serverList, funcList);
    }
    
    private void scheduler(int serverNum, ArrayList<ServerBean> serverList, final ArrayList<FunctionBean> funcList) {
        if (funcList.size() <= 0) {
            System.out.println("all function are scheduled");
            return;
        }
        final int min = 0;
        int mid;
        int max = mid = funcList.size();
        boolean violation = false;
        Collections.sort(serverList, new Comparator<ServerBean>() {
            @Override
            public int compare(final ServerBean a, final ServerBean b) {
                return (int)(b.getAvailRate() - a.getAvailRate());
            }
        });
        Collections.sort(funcList, new Comparator<FunctionBean>() {
            @Override
            public int compare(final FunctionBean a, final FunctionBean b) {
                return (int)(b.getUsageRate() - a.getUsageRate());
            }
        });
        while (min < max) {
            for (final FunctionBean item : funcList) {
                item.setPlacement(0);
            }
            violation = this.predict(mid, serverList, funcList);
            if (!violation) {
                break;
            }
            max = mid;
            mid = (int)Math.floor((max + min) / 2.0f);
        }
        if (funcList.size() >= mid + 1) {
            serverNum -= serverList.size();
            if (serverNum <= 0) {
                System.out.println("no available server");
                return;
            }
            GsightScheduler.serverConsumed += serverList.size();
            final ServerBean serverBean = new ServerBean(GsightScheduler.serverConsumed, "", 1.0f);
            serverList = new ArrayList<ServerBean>();
            serverList.add(serverBean);
            funcList.get(mid).setPlacement(serverList.get(0).getServerId());
        }
        for (int i = 0; i < mid; ++i) {
            GsightScheduler.placement[funcList.get(i).getFunctionId()][funcList.get(i).getPlacement()] = 1;
        }
        for (int i = 0; i < mid; ++i) {
            funcList.remove(0);
        }
        this.scheduler(serverNum, serverList, funcList);
    }
    
    private boolean predict(final int mid, final ArrayList<ServerBean> serverList, final ArrayList<FunctionBean> funcList) {
        if (mid == 0) {
            return true;
        }
        String schedulerResult = "";
        boolean violation = false;
        for (int i = 0; i < mid; ++i) {
            funcList.get(i).setPlacement(serverList.get(0).getServerId());
            schedulerResult = String.valueOf(schedulerResult) + funcList.get(i).getFunctionId() + ":" + serverList.get(0).getServerId() + ",";
            final float availRate = serverList.get(0).getAvailRate() - funcList.get(i).getUsageRate();
            serverList.get(0).setAvailRate(availRate);
            Collections.sort(serverList, new Comparator<ServerBean>() {
                @Override
                public int compare(final ServerBean a, final ServerBean b) {
                    return (int)(b.getAvailRate() - a.getAvailRate());
                }
            });
        }
        schedulerResult = schedulerResult.substring(0, schedulerResult.length() - 1);
        try {
            final String result = this.executeCommand("python /usr/local/model.py " + schedulerResult);
            if (result.equals("true")) {
                violation = true;
            }
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
        final double math = Math.random();
        violation = (math > 0.5);
        return violation;
    }
    
    private String executeCommand(final String command) throws RemoteException {
        System.out.println(command);
        String result = "";
        try {
            String line = null;
            final Process process = Runtime.getRuntime().exec(command);
            final BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            final InputStreamReader isr = new InputStreamReader(process.getInputStream());
            final LineNumberReader input = new LineNumberReader(isr);
            String err;
            while ((err = br.readLine()) != null || (line = input.readLine()) != null) {
                if (err == null) {
                    result = String.valueOf(result) + line;
                    System.out.println(line);
                }
                else {
                    result = String.valueOf(result) + err;
                    System.out.println(err);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    private String display() {
        final StringBuilder result = new StringBuilder();
        System.out.print("-------------");
        for (int i = 0; i < 26; ++i) {
            System.out.println();
            for (int j = 0; j < 2; ++j) {
                System.out.print(String.valueOf(GsightScheduler.placement[i][j]) + " ");
            }
        }
        System.out.println();
        System.out.println("-------------");
        for (int i = 0; i < 26; ++i) {
            for (int j = 0; j < 2; ++j) {
                if (GsightScheduler.placement[i][j] == 1) {
                    result.append(j).append(" ");
                }
            }
        }
        return result.toString();
    }
}
