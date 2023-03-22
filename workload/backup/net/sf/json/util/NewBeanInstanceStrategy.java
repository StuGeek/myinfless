// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.json.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import net.sf.json.JSONObject;

public abstract class NewBeanInstanceStrategy
{
    public static final NewBeanInstanceStrategy DEFAULT;
    
    public abstract Object newInstance(final Class p0, final JSONObject p1) throws InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, InvocationTargetException;
    
    static {
        DEFAULT = new DefaultNewBeanInstanceStrategy();
    }
    
    private static final class DefaultNewBeanInstanceStrategy extends NewBeanInstanceStrategy
    {
        private static final Object[] EMPTY_ARGS;
        private static final Class[] EMPTY_PARAM_TYPES;
        
        public Object newInstance(final Class target, final JSONObject source) throws InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, InvocationTargetException {
            if (target != null) {
                final Constructor c = target.getDeclaredConstructor((Class[])DefaultNewBeanInstanceStrategy.EMPTY_PARAM_TYPES);
                c.setAccessible(true);
                try {
                    return c.newInstance(DefaultNewBeanInstanceStrategy.EMPTY_ARGS);
                }
                catch (InstantiationException e) {
                    String cause = "";
                    try {
                        cause = ((e.getCause() != null) ? ("\n" + e.getCause().getMessage()) : "");
                    }
                    catch (Throwable t) {}
                    throw new InstantiationException("Instantiation of \"" + target + "\" failed. " + "It's probably because class is an interface, " + "abstract class, array class, primitive type or void." + cause);
                }
            }
            return null;
        }
        
        static {
            EMPTY_ARGS = new Object[0];
            EMPTY_PARAM_TYPES = new Class[0];
        }
    }
}
