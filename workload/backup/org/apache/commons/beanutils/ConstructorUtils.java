// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.beanutils;

import java.lang.reflect.Modifier;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ConstructorUtils
{
    private static final Class[] EMPTY_CLASS_PARAMETERS;
    private static final Object[] EMPTY_OBJECT_ARRAY;
    
    public static Object invokeConstructor(final Class klass, final Object arg) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        final Object[] args = { arg };
        return invokeConstructor(klass, args);
    }
    
    public static Object invokeConstructor(final Class klass, Object[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (null == args) {
            args = ConstructorUtils.EMPTY_OBJECT_ARRAY;
        }
        final int arguments = args.length;
        final Class[] parameterTypes = new Class[arguments];
        for (int i = 0; i < arguments; ++i) {
            parameterTypes[i] = args[i].getClass();
        }
        return invokeConstructor(klass, args, parameterTypes);
    }
    
    public static Object invokeConstructor(final Class klass, Object[] args, Class[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (parameterTypes == null) {
            parameterTypes = ConstructorUtils.EMPTY_CLASS_PARAMETERS;
        }
        if (args == null) {
            args = ConstructorUtils.EMPTY_OBJECT_ARRAY;
        }
        final Constructor ctor = getMatchingAccessibleConstructor(klass, parameterTypes);
        if (null == ctor) {
            throw new NoSuchMethodException("No such accessible constructor on object: " + klass.getName());
        }
        return ctor.newInstance(args);
    }
    
    public static Object invokeExactConstructor(final Class klass, final Object arg) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        final Object[] args = { arg };
        return invokeExactConstructor(klass, args);
    }
    
    public static Object invokeExactConstructor(final Class klass, Object[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (null == args) {
            args = ConstructorUtils.EMPTY_OBJECT_ARRAY;
        }
        final int arguments = args.length;
        final Class[] parameterTypes = new Class[arguments];
        for (int i = 0; i < arguments; ++i) {
            parameterTypes[i] = args[i].getClass();
        }
        return invokeExactConstructor(klass, args, parameterTypes);
    }
    
    public static Object invokeExactConstructor(final Class klass, Object[] args, Class[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (args == null) {
            args = ConstructorUtils.EMPTY_OBJECT_ARRAY;
        }
        if (parameterTypes == null) {
            parameterTypes = ConstructorUtils.EMPTY_CLASS_PARAMETERS;
        }
        final Constructor ctor = getAccessibleConstructor(klass, parameterTypes);
        if (null == ctor) {
            throw new NoSuchMethodException("No such accessible constructor on object: " + klass.getName());
        }
        return ctor.newInstance(args);
    }
    
    public static Constructor getAccessibleConstructor(final Class klass, final Class parameterType) {
        final Class[] parameterTypes = { parameterType };
        return getAccessibleConstructor(klass, parameterTypes);
    }
    
    public static Constructor getAccessibleConstructor(final Class klass, final Class[] parameterTypes) {
        try {
            return getAccessibleConstructor(klass.getConstructor((Class[])parameterTypes));
        }
        catch (NoSuchMethodException e) {
            return null;
        }
    }
    
    public static Constructor getAccessibleConstructor(final Constructor ctor) {
        if (ctor == null) {
            return null;
        }
        if (!Modifier.isPublic(ctor.getModifiers())) {
            return null;
        }
        final Class clazz = ctor.getDeclaringClass();
        if (Modifier.isPublic(clazz.getModifiers())) {
            return ctor;
        }
        return null;
    }
    
    private static Constructor getMatchingAccessibleConstructor(final Class clazz, final Class[] parameterTypes) {
        try {
            final Constructor ctor = clazz.getConstructor((Class[])parameterTypes);
            try {
                ctor.setAccessible(true);
            }
            catch (SecurityException ex) {}
            return ctor;
        }
        catch (NoSuchMethodException e) {
            final int paramSize = parameterTypes.length;
            final Constructor[] ctors = clazz.getConstructors();
            for (int i = 0, size = ctors.length; i < size; ++i) {
                final Class[] ctorParams = ctors[i].getParameterTypes();
                final int ctorParamSize = ctorParams.length;
                if (ctorParamSize == paramSize) {
                    boolean match = true;
                    for (int n = 0; n < ctorParamSize; ++n) {
                        if (!MethodUtils.isAssignmentCompatible(ctorParams[n], parameterTypes[n])) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        final Constructor ctor2 = getAccessibleConstructor(ctors[i]);
                        if (ctor2 != null) {
                            try {
                                ctor2.setAccessible(true);
                            }
                            catch (SecurityException ex2) {}
                            return ctor2;
                        }
                    }
                }
            }
            return null;
        }
    }
    
    static {
        EMPTY_CLASS_PARAMETERS = new Class[0];
        EMPTY_OBJECT_ARRAY = new Object[0];
    }
}
