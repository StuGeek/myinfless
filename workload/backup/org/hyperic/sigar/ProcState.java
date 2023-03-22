// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class ProcState implements Serializable
{
    private static final long serialVersionUID = 7805L;
    char state;
    String name;
    long ppid;
    int tty;
    int nice;
    int priority;
    long threads;
    int processor;
    public static final char SLEEP = 'S';
    public static final char RUN = 'R';
    public static final char STOP = 'T';
    public static final char ZOMBIE = 'Z';
    public static final char IDLE = 'D';
    
    public ProcState() {
        this.state = '\0';
        this.name = null;
        this.ppid = 0L;
        this.tty = 0;
        this.nice = 0;
        this.priority = 0;
        this.threads = 0L;
        this.processor = 0;
    }
    
    public native void gather(final Sigar p0, final long p1) throws SigarException;
    
    static ProcState fetch(final Sigar sigar, final long pid) throws SigarException {
        final ProcState procState = new ProcState();
        procState.gather(sigar, pid);
        return procState;
    }
    
    public char getState() {
        return this.state;
    }
    
    public String getName() {
        return this.name;
    }
    
    public long getPpid() {
        return this.ppid;
    }
    
    public int getTty() {
        return this.tty;
    }
    
    public int getNice() {
        return this.nice;
    }
    
    public int getPriority() {
        return this.priority;
    }
    
    public long getThreads() {
        return this.threads;
    }
    
    public int getProcessor() {
        return this.processor;
    }
    
    void copyTo(final ProcState copy) {
        copy.state = this.state;
        copy.name = this.name;
        copy.ppid = this.ppid;
        copy.tty = this.tty;
        copy.nice = this.nice;
        copy.priority = this.priority;
        copy.threads = this.threads;
        copy.processor = this.processor;
    }
    
    public Map toMap() {
        final Map map = new HashMap();
        final String strstate = String.valueOf(this.state);
        if (!"-1".equals(strstate)) {
            map.put("State", strstate);
        }
        final String strname = String.valueOf(this.name);
        if (!"-1".equals(strname)) {
            map.put("Name", strname);
        }
        final String strppid = String.valueOf(this.ppid);
        if (!"-1".equals(strppid)) {
            map.put("Ppid", strppid);
        }
        final String strtty = String.valueOf(this.tty);
        if (!"-1".equals(strtty)) {
            map.put("Tty", strtty);
        }
        final String strnice = String.valueOf(this.nice);
        if (!"-1".equals(strnice)) {
            map.put("Nice", strnice);
        }
        final String strpriority = String.valueOf(this.priority);
        if (!"-1".equals(strpriority)) {
            map.put("Priority", strpriority);
        }
        final String strthreads = String.valueOf(this.threads);
        if (!"-1".equals(strthreads)) {
            map.put("Threads", strthreads);
        }
        final String strprocessor = String.valueOf(this.processor);
        if (!"-1".equals(strprocessor)) {
            map.put("Processor", strprocessor);
        }
        return map;
    }
    
    public String toString() {
        return this.toMap().toString();
    }
}
