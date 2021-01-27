<%@page pageEncoding="UTF-8" isELIgnored="false" contentType="text/html; UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<!doctype html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport"
              content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Document</title>
        <script type="text/javascript" src="https://cdn.goeasy.io/goeasy-1.2.1.js"></script>
        <script>
            /*初始化GoEasy对象*/
            var goeasy = GoEasy.getInstance({
                host:'hangzhou.goeasy.io', //应用所在的区域地址: 【hangzhou.goeasy.io |singapore.goeasy.io】
                appkey: "BC-bb0ba4338a924943b0e7a6a5862d936a" //替换为您的应用appkey
            });

            //建立GoEasy链接
            goeasy.connect({
                onSuccess: function () {  //连接成功
                    console.log("GoEasy connect successfully.") //连接成功
                },
                onFailed: function (error) { //连接失败
                    console.log("Failed to connect GoEasy, code:"+error.code+ ",error:"+error.content);
                },
                onProgress:function(attempts) { //连接或自动重连中
                    console.log("GoEasy is connecting", attempts);
                }
            });

            /*接收消息*/
            goeasy.subscribe({
                channel: "2008channel",//替换为您自己的channel
                onMessage: function (message) {
                    //GoEasy.jsp:38 Channel:2008channel content:Hello, GoEasy! 2008channel
                    //console.log("Channel:" + message.channel + " content:" + message.content);
                    alert("Channel:" + message.channel + " content:" + message.content);
                }
                /*onSuccess: function () {
                    console.log("Channel订阅成功。");
                },*/
                /*onFailed: function (error) {
                    console.log("Channel订阅失败, 错误编码：" + error.code + " 错误信息：" + error.content)
                }*/
            });

        </script>
    </head>
    <body>
        <h1>hehehe</h1>
    </body>
</html>