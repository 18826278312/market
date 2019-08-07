package com.example.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

public class HttpUtil {
	
	/**
     * 向指定URL发送GET方法的请求
     * @param url	发送请求的URL
     * @param param	请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     */
    public static String sendGet(String url) {
        String result = "";
        String line;
        StringBuffer sb=new StringBuffer();
        BufferedReader in = null;
        try {
        	// 创建代理服务器  
            InetSocketAddress addr = new InetSocketAddress(ResourceConfig.PROXY_HOST, ResourceConfig.PROXY_PORT);  
            Proxy proxy = new Proxy(Proxy.Type.HTTP, addr); //配置HTTP代理，内网需要此步骤
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            //URLConnection conn = realUrl.openConnection(proxy);
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性 设置请求格式
            conn.setRequestProperty("contentType", "utf-8"); 
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // 建立实际的连接
            conn.connect();
            // 定义 BufferedReader输入流来读取URL的响应,设置接收格式
            in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(),"utf-8"));
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            result=sb.toString();
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
    
    /**
     * 向指定URL发送GET方法的请求
     * @param url	发送请求的URL
     * @param param	请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     */
    public static String sendGetCloseProxy(String url) {
        String result = "";
        String line;
        StringBuffer sb=new StringBuffer();
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性 设置请求格式
            conn.setRequestProperty("contentType", "utf-8"); 
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // 建立实际的连接
            conn.connect();
            // 定义 BufferedReader输入流来读取URL的响应,设置接收格式
            in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(),"utf-8"));
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            result=sb.toString();
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

}
