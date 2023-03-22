// 
// Decompiled by Procyon v0.5.36
// 

package scs.pojo;

import java.io.Serializable;

public class TwoTuple<A, B> implements Cloneable, Serializable
{
    private static final long serialVersionUID = 1L;
    public A first;
    public B second;
    
    public TwoTuple(final A a, final B b) {
        this.first = a;
        this.second = b;
    }
    
    @Override
    public String toString() {
        return "(" + this.first + ", " + this.second + ")";
    }
    
    public TwoTuple<A, B> clone() throws CloneNotSupportedException {
        return new TwoTuple<A, B>(this.first, this.second);
    }
    
    public String toColonString() {
        return this.first + ":" + this.second;
    }
}
