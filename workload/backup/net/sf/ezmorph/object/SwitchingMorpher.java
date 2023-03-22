// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.ezmorph.object;

import org.apache.commons.lang.builder.HashCodeBuilder;
import java.util.Iterator;
import net.sf.ezmorph.MorphException;
import java.util.HashMap;
import net.sf.ezmorph.MorpherRegistry;
import java.util.Map;
import net.sf.ezmorph.ObjectMorpher;

public class SwitchingMorpher implements ObjectMorpher
{
    private Map classMap;
    private MorpherRegistry morpherRegistry;
    static /* synthetic */ Class class$0;
    
    public SwitchingMorpher(final Map classMap, final MorpherRegistry morpherRegistry) {
        this.classMap = new HashMap();
        this.morpherRegistry = morpherRegistry;
        if (classMap == null || classMap.isEmpty()) {
            throw new MorphException("Must specify at least one mapping");
        }
        this.classMap.putAll(classMap);
    }
    
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof NumberMorpher)) {
            return false;
        }
        final SwitchingMorpher other = (SwitchingMorpher)obj;
        if (this.classMap.size() != other.classMap.size()) {
            return false;
        }
        for (final Map.Entry entry : this.classMap.entrySet()) {
            if (!other.classMap.containsKey(entry.getKey())) {
                return false;
            }
            if (!entry.getValue().equals(other.classMap.get(entry.getKey()))) {
                return false;
            }
        }
        return true;
    }
    
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        for (final Map.Entry entry : this.classMap.entrySet()) {
            builder.append(entry.getKey());
            builder.append(entry.getValue());
        }
        return builder.toHashCode();
    }
    
    public Object morph(final Object value) {
        if (value == null) {
            return null;
        }
        final Class target = this.classMap.get(value.getClass());
        return this.morpherRegistry.morph(target, value);
    }
    
    public Class morphsTo() {
        Class class$0;
        if ((class$0 = SwitchingMorpher.class$0) == null) {
            try {
                class$0 = (SwitchingMorpher.class$0 = Class.forName("java.lang.Object"));
            }
            catch (ClassNotFoundException ex) {
                throw new NoClassDefFoundError(ex.getMessage());
            }
        }
        return class$0;
    }
    
    public boolean supports(final Class clazz) {
        return true;
    }
}
