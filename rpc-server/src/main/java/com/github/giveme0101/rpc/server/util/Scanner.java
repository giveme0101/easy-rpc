package com.github.giveme0101.rpc.server.util;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author kevin xiajun94@FoxMail.com
 * @Description
 * @name Scanner
 * @Date 2020/09/15 17:23
 */
public class Scanner {

    private static final ClassLoader classLoader = Scanner.class.getClassLoader();

    public static List<Class> scan(String basePackage) {

        final List<Class> list = new ArrayList<>();

        URI uri = null;

        try {
            uri = classLoader.getResource(basePackage.replace(".", "/")).toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        new File(uri).listFiles((File pathname) -> {

            if(pathname.isDirectory()){
                scan(basePackage + "." + pathname.getName());
            }

            if(pathname.getName().endsWith(".class")){
                try {
                    Class<?> clazz = classLoader.loadClass(basePackage + "." + pathname.getName().replace(".class", ""));
                    list.add(clazz);
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
