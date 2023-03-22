// 
// Decompiled by Procyon v0.5.36
// 

package scs.util.tools;

import java.util.Random;

public class RandomString
{
    private Random rand;
    public static final String SOURCES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
    
    public RandomString() {
        this.rand = new Random();
    }
    
    public String generateString(final int length) {
        final char[] text = new char[length];
        for (int i = 0; i < length; ++i) {
            text[i] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890".charAt(this.rand.nextInt(62));
        }
        return new String(text);
    }
}
