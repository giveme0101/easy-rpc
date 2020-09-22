package com.github.giveme0101.rpc.core.common.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * http://www.itstack.org
 * create by fuzhengwei on 2019/5/7
 */
public class NetUtil {

    public static boolean isPortUsing(int port) {
        boolean flag = false;
        try {
            Socket socket = new Socket("localhost", port);
            socket.close();
            flag = true;
        } catch (IOException e) {

        }
        return flag;
    }

    public static String getHost() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "127.0.0.1";
        }
    }

}
