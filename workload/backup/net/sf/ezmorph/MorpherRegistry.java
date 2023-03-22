// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.ezmorph;

import java.util.ArrayList;
import java.lang.reflect.Method;
import java.util.Iterator;
import net.sf.ezmorph.object.IdentityObjectMorpher;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class MorpherRegistry implements Serializable
{
    private static final long serialVersionUID = -3894767123320768419L;
    private Map morphers;
    
    public MorpherRegistry() {
        this.morphers = new HashMap();
    }
    
    public synchronized void clear() {
        this.morphers.clear();
    }
    
    public synchronized void clear(final Class type) {
        final List registered = this.morphers.get(type);
        if (registered != null) {
            this.morphers.remove(type);
        }
    }
    
    public synchronized void deregisterMorpher(final Morpher morpher) {
        final List registered = this.morphers.get(morpher.morphsTo());
        if (registered != null && !registered.isEmpty()) {
            registered.remove(morpher);
            if (registered.isEmpty()) {
                this.morphers.remove(morpher.morphsTo());
            }
        }
    }
    
    public synchronized Morpher getMorpherFor(final Class clazz) {
        final List registered = this.morphers.get(clazz);
        if (registered == null || registered.isEmpty()) {
            return IdentityObjectMorpher.getInstance();
        }
        return registered.get(0);
    }
    
    public synchronized Morpher[] getMorphersFor(final Class clazz) {
        final List registered = this.morphers.get(clazz);
        if (registered == null || registered.isEmpty()) {
            return new Morpher[] { IdentityObjectMorpher.getInstance() };
        }
        final Morpher[] morphs = new Morpher[registered.size()];
        int k = 0;
        final Iterator i = registered.iterator();
        while (i.hasNext()) {
            morphs[k++] = i.next();
        }
        return morphs;
    }
    
    public Object morph(final Class target, final Object value) {
        if (value == null) {
            final Morpher morpher = this.getMorpherFor(target);
            if (morpher instanceof ObjectMorpher) {
                return ((ObjectMorpher)morpher).morph(value);
            }
            try {
                final Method morphMethod = morpher.getClass().getDeclaredMethod("morph", Object.class);
                return morphMethod.invoke(morpher, value);
            }
            catch (Exception e) {
                throw new MorphException(e);
            }
        }
        final Morpher[] morphers = this.getMorphersFor(target);
        for (int i = 0; i < morphers.length; ++i) {
            final Morpher morpher2 = morphers[i];
            if (morpher2.supports(value.getClass())) {
                if (morpher2 instanceof ObjectMorpher) {
                    return ((ObjectMorpher)morpher2).morph(value);
                }
                try {
                    final Method morphMethod2 = morpher2.getClass().getDeclaredMethod("morph", Object.class);
                    return morphMethod2.invoke(morpher2, value);
                }
                catch (Exception e2) {
                    throw new MorphException(e2);
                }
            }
        }
        return value;
    }
    
    public void registerMorpher(final Morpher morpher) {
        this.registerMorpher(morpher, false);
    }
    
    public synchronized void registerMorpher(final Morpher morpher, final boolean override) {
        List registered = this.morphers.get(morpher.morphsTo());
        if (override || registered == null) {
            registered = new ArrayList();
            this.morphers.put(morpher.morphsTo(), registered);
        }
        if (!registered.contains(morpher)) {
            registered.add(morpher);
        }
    }
}
