// 
// Decompiled by Procyon v0.5.36
// 

package org.hyperic.sigar.ptql;

import org.hyperic.sigar.util.ReferenceMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Map;

public class StringPattern
{
    private static Map patterns;
    
    public static boolean matches(final String source, final String regex) {
        Pattern pattern = StringPattern.patterns.get(regex);
        if (pattern == null) {
            pattern = Pattern.compile(regex);
            StringPattern.patterns.put(regex, pattern);
        }
        final Matcher matcher = pattern.matcher(source);
        return matcher.find();
    }
    
    static {
        StringPattern.patterns = ReferenceMap.synchronizedMap();
    }
}
