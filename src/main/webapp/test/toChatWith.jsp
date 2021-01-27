<%@page pageEncoding="UTF-8" isELIgnored="false" contentType="text/html; UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<!doctype html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport"content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>聊天群</title>
        <script src="${path}/bootstrap/js/jquery.min.js"></script>
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

            $(function(){

                var messageData;

                //订阅消息
                goeasy.subscribe({
                    channel: "my2008Channel",//替换为您自己的channel
                    onMessage: function (message) {
                        //获取消息
                        var msgData=message.content;

                        //判断是否是自己发送的消息
                        if(messageData!=msgData){

                            //否  在左侧展示
                            //设置发送方内容并设置样式
                            var optionStyle="<br><span style='float: left;background-color: #4aaf51;border-radius: 80px'>"+msgData+"</span><br>";

                            //获取发送内容，将内容展示在页面左侧
                            $("#showMsg").append(optionStyle);
                        } //是  不在左侧继续追加
                    }
                });


                //点击发送按钮触发
                $("#sendMsg").click(function(){

                    //获取输入框内容
                    var content = $("#msg").val();

                    //将发送的内容保存到另一个变量中
                    messageData=content;

                    //清楚输入框
                    $("#msg").val("");

                    //使用GoEasy发送消息
                    goeasy.publish({
                        channel: "my2008Channel",//替换为您自己的channel
                        message: content,//替换为您想要发送的消息内容
                        onSuccess:function(){
                            //设置发送方内容并设置样式
                            var optionStyle="<br><span style='float: right;background-color: #46b8da;border-radius: 80px'>"+content+"</span><br>";

                            //获取发送内容，将内容展示在页面右侧
                            $("#showMsg").append(optionStyle);
                        },
                        onFailed: function (error) {
                            console.log("消息发送失败，错误编码："+error.code+" 错误信息："+error.content);
                        }
                    });

                });
            });
        </script>
    </head>
    <body>
        <div align="center">
            <h1>2008聊天群</h1>
            <%--聊天窗口--%>
            <div style="width: 600px;height: 700px;border: 3px #5bc0de solid">
                <%--内容展示区域--%>
                <div id="showMsg" style="width: 594px;height: 600px;border: 3px #acdd4a solid"></div>
                <%--内容编辑发送区域--%>
                <div style="width: 594px;height: 88px;border: 3px #ccaadd solid">
                    <%--内容编辑区--%>
                    <input id="msg" type="text" style="width: 500px;height: 82px;border: 3px #4aaf51 solid">
                    <%--发送按钮--%>
                    <button id="sendMsg" style="width: 75px;height: 82px;border: 3px #2694e8 solid" >发送</button>
                </div>
            </div>
        </div>
    </body>
</html>