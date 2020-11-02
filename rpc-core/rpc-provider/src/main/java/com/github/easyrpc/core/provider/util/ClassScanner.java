package com.github.easyrpc.core.provider.util;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name ClassScanner
 * @Date 2020/09/15 17:23
 */
public class ClassScanner {

    private static final ClassLoader classLoader = ClassScanner.class.getClassLoader();

    public static List<Class> scan(String basePackage, Predicate<Class> predicate) {

        final List<Class> list = new ArrayList<>();

        URI uri = null;

        try {
            uri = classLoader.getResource(basePackage.replace(".", "/")).toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        new File(uri).listFiles((File pathname) -> {

            if(pathname.isDirectory()){
                scan(basePackage + "." + pathname.getName(), predicate);
            }

            if(pathname.getName().endsWith(".class")){
                try {
                    Class<?> clazz = classLoader.loadClass(basePackage + "." + pathname.getName().replace(".class", ""));
                    if (predicate.test(clazz)){
                        list.add(clazz);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                return true;
            }
            return false;
        });

        return list;
    }

}
