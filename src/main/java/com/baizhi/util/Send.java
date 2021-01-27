package com.baizhi.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

/**
 *
 * @author Administrator
 */
public class Send {

    /*
    UTF-8编码发送接口地址
        http://sms.106jiekou.com/utf8/sms.aspx?account=用户账号&password=接口密码&mobile=号码&content=您的订单编码：888888。如需帮助请联系客服。

        postData : account=用户账号&password=接口密码&mobile=号码&content=您的订单编码：888888。如需帮助请联系客服。
        postUrl : http://sms.106jiekou.com/utf8/sms.aspx
    * */

    /*
    调用106接口发送短信验证码
    参数：
        postData(String):
        postUrl(String):
    * */
    public static String SMS(String postData, String postUrl) {
        try {
            //发送POST请求
            URL url = new URL(postUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setUseCaches(false);
            conn.setDoOutput(true);

            conn.setRequestProperty("Content-Length", "" + postData.length());
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            out.write(postData);
            out.flush();
            out.close();

            //获取响应状态
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println("connect failed!");
                return "";
            }
            //获取响应内容体
            String line, result = "";
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            while ((line = in.readLine()) != null) {
                result += line + "\n";
            }
            in.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
        return "";
    }

    //生成随机的数
    public static String getRandom(int n){
        char[] code =  "0123456789".toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(code[new Random().nextInt(code.length)]);
        }
        return sb.toString();
    }

    public static String sendMobileCode(String mobile,String code){
                /*
        postData : account=用户账号&password=接口密码&mobile=号码&content=您的订单编码：888888。如需帮助请联系客服。
        postUrl : http://sms.106jiekou.com/utf8/sms.aspx
        * */

        /*
         * 参数:
         *   account:登录账号
         *   password:接口密码
         *   mobile:手机号
         *   content:短信模板
         * */
        String postData="account=zhangcn&password=nanans&mobile="+mobile+"&content=您的验证码是："+code+"。请不要把验证码泄露给其他人。如非本人操作，可不用理会！";
        String postUrl="http://sms.106jiekou.com/utf8/sms.aspx";

        //调用方法
        String sms = SMS(postData, postUrl);

        return sms;
    }

    public static void main(String[] args) {

        //生生随机数
        String random = getRandom(6);
        System.out.println(random);
        String message = sendMobileCode("15236674712", random);
        System.out.println(message);

        /*
        【云注册验证】您的验证码是：779694。请不要把验证码泄露给其他人。如非本人操作，可不用理会！
        * */

    }
}
