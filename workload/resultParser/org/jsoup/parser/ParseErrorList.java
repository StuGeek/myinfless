// 
// Decompiled by Procyon v0.5.36
// 

package org.jsoup.parser;

import java.util.ArrayList;

public class ParseErrorList extends ArrayList<ParseError>
{
    private static final int INITIAL_CAPACITY = 16;
    private final int maxSize;
    
    ParseErrorList(final int initialCapacity, final int maxSize) {
        super(initialCapacity);
        this.maxSize = maxSize;
    }
    
    boolean canAddError() {
        return this.size() < this.maxSize;
    }
    
    int getMaxSize() {
        return this.maxSize;
    }
    
    public static ParseErrorList noTracking() {
        return new ParseErrorList(0, 0);
    }
    
    public static ParseErrorList tracking(final int maxSize) {
        return new ParseErrorList(16, maxSize);
    }
}
