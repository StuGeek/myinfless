// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.test;

import java.io.FileInputStream;
import java.io.File;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.ThreadCpu;
import org.hyperic.sigar.Sigar;

public class RunThreadCpu
{
    static Sigar sigar;
    static ThreadCpu cpu;
    static int iter;
    
    private static long toMillis(final long nano) {
        return nano / 1000000L;
    }
    
    private static void printTimes(final long start) {
        try {
            RunThreadCpu.cpu.gather(RunThreadCpu.sigar, 0L);
        }
        catch (SigarException e) {
            e.printStackTrace();
            return;
        }
        System.out.println(Thread.currentThread().getName() + ":");
        System.out.println("   real....." + (System.currentTimeMillis() - start) / 1000L);
        System.out.println("   sys......" + toMillis(RunThreadCpu.cpu.getSys()));
        System.out.println("   user....." + toMillis(RunThreadCpu.cpu.getUser()));
        System.out.println("   total...." + toMillis(RunThreadCpu.cpu.getTotal()));
    }
    
    private static void pause(final int sec) {
        try {
            Thread.sleep(sec * 1000);
        }
        catch (Exception ex) {}
    }
    
    private static void readRtJar() {
        final String rt = System.getProperty("java.home") + "/lib/rt.jar";
        int bytes = 0;
        final int max = 1500000;
        FileInputStream is = null;
        try {
            is = new FileInputStream(new File(rt));
            while (is.read() != -1 && bytes++ <= max) {}
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                }
                catch (Exception ex) {}
            }
        }
    }
    
    private static void scanDir() {
        for (int i = 0; i < RunThreadCpu.iter; ++i) {
            final String[] files = new File(".").list();
            for (int j = 0; j < files.length; ++j) {
                final File f = new File(files[j]);
                f.exists();
                if (f.isDirectory()) {
                    f.list();
                }
                f.lastModified();
            }
        }
    }
    
    public static void main(final String[] args) throws Exception {
        if (args.length == 1) {
            RunThreadCpu.iter = Integer.parseInt(args[0]);
        }
        final long start = System.currentTimeMillis();
        final Thread user = new Thread(new UserThread(), "user");
        final Thread read = new Thread(new ReadThread(), "read");
        final Thread scan = new Thread(new ScanThread(), "scan");
        final Thread real = new Thread(new RealThread(), "real");
        user.start();
        read.start();
        scan.start();
        real.start();
        user.join();
        read.join();
        scan.join();
        real.join();
        pause(3);
        printTimes(start);
    }
    
    static {
        RunThreadCpu.sigar = new Sigar();
        RunThreadCpu.cpu = new ThreadCpu();
        RunThreadCpu.iter = 5000;
    }
    
    static class RealThread implements Runnable
    {
        public void run() {
            final long start = System.currentTimeMillis();
            pause(2);
            printTimes(start);
        }
    }
    
    static class UserThread implements Runnable
    {
        public void run() {
            final long start = System.currentTimeMillis();
            pause(2);
            String s = "";
            for (int i = 0; i < RunThreadCpu.iter; ++i) {
                s += System.getProperty("java.home");
                for (int j = 0; j < s.length(); ++j) {
                    s.charAt(j);
                }
            }
            printTimes(start);
        }
    }
    
    static class ReadThread implements Runnable
    {
        public void run() {
            final long start = System.currentTimeMillis();
            pause(2);
            readRtJar();
            printTimes(start);
        }
    }
    
    static class ScanThread implements Runnable
    {
        public void run() {
            final long start = System.currentTimeMillis();
            pause(2);
            scanDir();
            printTimes(start);
        }
    }
}
