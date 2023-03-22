// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json.util;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONArray;

public abstract class CycleDetectionStrategy
{
    public static final JSONArray IGNORE_PROPERTY_ARR;
    public static final JSONObject IGNORE_PROPERTY_OBJ;
    public static final CycleDetectionStrategy LENIENT;
    public static final CycleDetectionStrategy NOPROP;
    public static final CycleDetectionStrategy STRICT;
    
    public abstract JSONArray handleRepeatedReferenceAsArray(final Object p0);
    
    public abstract JSONObject handleRepeatedReferenceAsObject(final Object p0);
    
    static {
        IGNORE_PROPERTY_ARR = new JSONArray();
        IGNORE_PROPERTY_OBJ = new JSONObject();
        LENIENT = new LenientCycleDetectionStrategy();
        NOPROP = new LenientNoRefCycleDetectionStrategy();
        STRICT = new StrictCycleDetectionStrategy();
    }
    
    private static final class LenientCycleDetectionStrategy extends CycleDetectionStrategy
    {
        public JSONArray handleRepeatedReferenceAsArray(final Object reference) {
            return new JSONArray();
        }
        
        public JSONObject handleRepeatedReferenceAsObject(final Object reference) {
            return new JSONObject(true);
        }
    }
    
    private static final class LenientNoRefCycleDetectionStrategy extends CycleDetectionStrategy
    {
        public JSONArray handleRepeatedReferenceAsArray(final Object reference) {
            return CycleDetectionStrategy.IGNORE_PROPERTY_ARR;
        }
        
        public JSONObject handleRepeatedReferenceAsObject(final Object reference) {
            return CycleDetectionStrategy.IGNORE_PROPERTY_OBJ;
        }
    }
    
    private static final class StrictCycleDetectionStrategy extends CycleDetectionStrategy
    {
        public JSONArray handleRepeatedReferenceAsArray(final Object reference) {
            throw new JSONException("There is a cycle in the hierarchy!");
        }
        
        public JSONObject handleRepeatedReferenceAsObject(final Object reference) {
            throw new JSONException("There is a cycle in the hierarchy!");
        }
    }
}
