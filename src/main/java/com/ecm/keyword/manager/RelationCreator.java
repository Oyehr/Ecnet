package com.ecm.keyword.manager;

import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RelationCreator {
    public static JSONObject getJoints(JSONObject data) throws IOException {

        URL url = new URL("http://114.212.240.199:5000/getFactLinkpoints");
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
        //String json="{\"evidenceList\":[{\"id\":296,\"content\":\"test\",\"type\":\"其他\",\"keyWordMap\":{\"what\":[],\"how much\":[],\"where\":[],\"when\":[],\"who\":[]},\"headList\":[]},{\"id\":297,\"content\":\"托尔斯泰\",\"type\":\"其他\",\"keyWordMap\":{\"what\":[],\"how much\":[],\"where\":[],\"when\":[],\"who\":[\"托尔斯泰\"]},\"headList\":[]}]}";
        String json=data.toString();
//        String answer1=json;
        System.out.println("请求json串："+json);
        //answer1=answer1.substring(1,answer1.length()-1);
        //转换为字节数组
        byte[] dataBytes = json.getBytes();
        // 设置文件长度
        conn.setRequestProperty("Content-Length", String.valueOf(dataBytes.length));

        // 设置文件类型:
        conn.setRequestProperty("contentType", "application/json");
        conn.connect();
        OutputStream out1 = conn.getOutputStream();
        // 写入请求的字符串
        out1.write(json.getBytes());
        out1.flush();
        out1.close();

        JSONObject jsonObject=null;
        // System.out.println(conn.getResponseCode());
        if (conn.getResponseCode() == 200) {
            System.out.println("连接成功");
            // 请求返回的数据
            try {

                String result="{\"factList\":";
                BufferedReader test = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = test.readLine()) != null) {
                    result += line;
                }
                result+="}";

                jsonObject=JSONObject.fromObject(result);

            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }  else if (conn.getResponseCode() == 500) {
            System.out.println("服务器内部错误");
        } else{
            System.out.println("连接失败");
        }
        return jsonObject;
    }
}
