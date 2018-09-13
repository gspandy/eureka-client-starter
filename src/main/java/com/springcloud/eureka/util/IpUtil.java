package com.springcloud.eureka.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by 李雷 on 2018/9/13.
 */
public class IpUtil {

    public static String getCurrentIp(String network){
        Enumeration<NetworkInterface> networkInterfaceEnumeration;
        try {
            networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaceEnumeration.hasMoreElements()){
                NetworkInterface ifc = networkInterfaceEnumeration.nextElement();
                if(ifc.isUp()){
                    Enumeration<InetAddress> inetAddressEnumeration = ifc.getInetAddresses();
                    while (inetAddressEnumeration.hasMoreElements()){
                        InetAddress inetAddress = inetAddressEnumeration.nextElement();
                        if(inetAddress instanceof Inet4Address){
                            String hostAddress = inetAddress.getHostAddress();
                            if(hostAddress.startsWith(network)) {
                                return hostAddress;
                            }
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return null;
    }
}
