// 
// Decompiled by Procyon v0.5.36
// 

package org.jsoup.internal;

import java.util.Locale;

public final class Normalizer
{
    public static String lowerCase(final String input) {
        return (input != null) ? input.toLowerCase(Locale.ENGLISH) : "";
    }
    
    public static String normalize(final String input) {
        return lowerCase(input).trim();
    }
}
