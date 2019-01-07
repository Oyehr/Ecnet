package com.ecm.keyword.manager;

import net.sf.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class HeadCreator {
    public static JSONObject getHead(JSONObject evidenceList) throws IOException {
        JSONObject jsonObject=evidenceList;
        URL url = new URL("http://114.212.240.199:5000/getHeadOfEvidence");
        // 建立http连接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 设置允许输出
        conn.setDoOutput(true);
        conn.setDoInput(true);
        // 设置不用缓存
        conn.setUseCaches(false);
        // 设置传递方式
        conn.setRequestMethod("POST");
        // 设置维持长连接
        conn.setRequestProperty("Connection", "Keep-Alive");
        // 设置文件字符集:
        conn.setRequestProperty("Charset", "UTF-8");
        String json=evidenceList.toString();
        //转换为字节数组
        byte[] data = json.getBytes();
        // 设置文件长度
        conn.setRequestProperty("Content-Length", String.valueOf(data.length));
        // 设置文件类型:
        conn.setRequestProperty("contentType", "application/json");
        conn.connect();
        OutputStream out1 = conn.getOutputStream();
        // 写入请求的字符串
        out1.write(json.getBytes());
        out1.flush();
        out1.close();
        if (conn.getResponseCode() == 200) {
            System.out.println("连接成功");
            // 请求返回的数据
           try {
                String result="";
                BufferedReader test = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = test.readLine()) != null) {
                    result += line;
                }
                jsonObject=JSONObject.fromObject(result);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return null;
            }
        }  else if (conn.getResponseCode() == 500) {
            System.out.println("服务器内部错误");
           return null;
        } else{
            System.out.println("连接失败");
            return null;
        }
        return jsonObject;
    }
}
