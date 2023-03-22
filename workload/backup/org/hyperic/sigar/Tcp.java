// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class Tcp implements Serializable
{
    private static final long serialVersionUID = 14992L;
    long activeOpens;
    long passiveOpens;
    long attemptFails;
    long estabResets;
    long currEstab;
    long inSegs;
    long outSegs;
    long retransSegs;
    long inErrs;
    long outRsts;
    
    public Tcp() {
        this.activeOpens = 0L;
        this.passiveOpens = 0L;
        this.attemptFails = 0L;
        this.estabResets = 0L;
        this.currEstab = 0L;
        this.inSegs = 0L;
        this.outSegs = 0L;
        this.retransSegs = 0L;
        this.inErrs = 0L;
        this.outRsts = 0L;
    }
    
    public native void gather(final Sigar p0) throws SigarException;
    
    static Tcp fetch(final Sigar sigar) throws SigarException {
        final Tcp tcp = new Tcp();
        tcp.gather(sigar);
        return tcp;
    }
    
    public long getActiveOpens() {
        return this.activeOpens;
    }
    
    public long getPassiveOpens() {
        return this.passiveOpens;
    }
    
    public long getAttemptFails() {
        return this.attemptFails;
    }
    
    public long getEstabResets() {
        return this.estabResets;
    }
    
    public long getCurrEstab() {
        return this.currEstab;
    }
    
    public long getInSegs() {
        return this.inSegs;
    }
    
    public long getOutSegs() {
        return this.outSegs;
    }
    
    public long getRetransSegs() {
        return this.retransSegs;
    }
    
    public long getInErrs() {
        return this.inErrs;
    }
    
    public long getOutRsts() {
        return this.outRsts;
    }
    
    void copyTo(final Tcp copy) {
        copy.activeOpens = this.activeOpens;
        copy.passiveOpens = this.passiveOpens;
        copy.attemptFails = this.attemptFails;
        copy.estabResets = this.estabResets;
        copy.currEstab = this.currEstab;
        copy.inSegs = this.inSegs;
        copy.outSegs = this.outSegs;
        copy.retransSegs = this.retransSegs;
        copy.inErrs = this.inErrs;
        copy.outRsts = this.outRsts;
    }
    
    public Map toMap() {
        final Map map = new HashMap();
        final String stractiveOpens = String.valueOf(this.activeOpens);
        if (!"-1".equals(stractiveOpens)) {
            map.put("ActiveOpens", stractiveOpens);
        }
        final String strpassiveOpens = String.valueOf(this.passiveOpens);
        if (!"-1".equals(strpassiveOpens)) {
            map.put("PassiveOpens", strpassiveOpens);
        }
        final String strattemptFails = String.valueOf(this.attemptFails);
        if (!"-1".equals(strattemptFails)) {
            map.put("AttemptFails", strattemptFails);
        }
        final String strestabResets = String.valueOf(this.estabResets);
        if (!"-1".equals(strestabResets)) {
            map.put("EstabResets", strestabResets);
        }
        final String strcurrEstab = String.valueOf(this.currEstab);
        if (!"-1".equals(strcurrEstab)) {
            map.put("CurrEstab", strcurrEstab);
        }
        final String strinSegs = String.valueOf(this.inSegs);
        if (!"-1".equals(strinSegs)) {
            map.put("InSegs", strinSegs);
        }
        final String stroutSegs = String.valueOf(this.outSegs);
        if (!"-1".equals(stroutSegs)) {
            map.put("OutSegs", stroutSegs);
        }
        final String strretransSegs = String.valueOf(this.retransSegs);
        if (!"-1".equals(strretransSegs)) {
            map.put("RetransSegs", strretransSegs);
        }
        final String strinErrs = String.valueOf(this.inErrs);
        if (!"-1".equals(strinErrs)) {
            map.put("InErrs", strinErrs);
        }
        final String stroutRsts = String.valueOf(this.outRsts);
        if (!"-1".equals(stroutRsts)) {
            map.put("OutRsts", stroutRsts);
        }
        return map;
    }
    
    public String toString() {
        return this.toMap().toString();
    }
}
