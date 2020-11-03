package com.github.easyrpc.common.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name ReflectionUtils
 * @Date 2020/10/30 9:38
 */
@Slf4j
public class ReflectionUtils {

    public static <T> Constructor<T> accessibleConstructor(Class<T> clazz, Class<?>... parameterTypes)
            throws NoSuchMethodException {

        Constructor<T> ctor = clazz.getDeclaredConstructor(parameterTypes);
        makeAccessible(ctor);
        return ctor;
    }

    public static Class<?> forName(String typeName, ClassLoader... classLoaders) {

        Class<?> aClass = null;

        for (final ClassLoader classLoader : classLoaders) {
            try {
                aClass = Class.forName(typeName, false, classLoader);
            } catch (Exception ex){
                log.warn(ex.getMessage(), ex);
            }
        }

        return aClass;
    }

     public static void makeAccessible(Constructor<?> ctor) {
        if ((!Modifier.isPublic(ctor.getModifiers()) ||
                !Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) && !ctor.isAccessible()) {
            ctor.setAccessible(true);
        }
    }

    public static void makeAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) ||
                !Modifier.isPublic(field.getDeclaringClass().getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    public static <T> T instantiate(String instanceClassName, Class<T> superClass, ClassLoader classLoader) {

        try {
            Class<?> instanceClass = Class.forName(instanceClassName, false, classLoader);
            if (superClass != null && !superClass.isAssignableFrom(instanceClass)) {
                throw new IllegalArgumentException(
                        "Class [" + instanceClassName + "] is not assignable to [" + superClass.getName() + "]");
            }
            return (T) ReflectionUtils.accessibleConstructor(instanceClass).newInstance();
        }
        catch (Throwable ex) {
            throw new IllegalArgumentException("Unable to instantiate factory class: " + superClass.getName(), ex);
        }
    }

}

