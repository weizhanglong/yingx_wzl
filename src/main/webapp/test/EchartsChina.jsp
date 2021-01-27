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
        <script src="${path}/js/echarts.js"></script>
        <script src="${path}/js/china.js"></script>
        <script src="${path}/bootstrap/js/jquery.min.js"></script>
        <script type="text/javascript">
            $(function(){
                // 基于准备好的dom，初始化echarts实例
                var myChart = echarts.init(document.getElementById('main'));
                    // 指定图表的配置项和数据
                    var option = {
                        title : {
                            text: '用户注册量分布图',
                            subtext: '纯属虚构',
                            left: 'center'
                        },
                        tooltip : {
                            trigger: 'item'
                        },
                        legend: {
                            orient: 'vertical',
                            left: 'left',
                            data:['小男生','小姑娘']
                        },
                        visualMap: {
                            min: 0,
                            max: 1000,
                            left: 'left',
                            top: 'bottom',
                            text:['高','低'],           // 文本，默认为数值文本
                            calculable : true
                        },
                        toolbox: {
                            show: true,
                            orient : 'vertical',
                            left: 'right',
                            top: 'center',
                            feature : {
                                mark : {show: true},
                                dataView : {show: true, readOnly: false},
                                restore : {show: true},
                                saveAsImage : {show: true}
                            }
                        },
                        series : [
                            {
                                name: '小男生',
                                type: 'map',
                                mapType: 'china',
                                roam: false,
                                label: {
                                    normal: {
                                        show: false
                                    },
                                    emphasis: {
                                        show: true
                                    }
                                },
                                data:[
                                    {name: '北京',value: 34},
                                    {name: '天津',value: 45},
                                    {name: '上海',value: 32},
                                    {name: '安徽',value: 24},
                                    {name: '香港',value: 42},
                                    {name: '澳门',value: 23}
                                ]
                            },
                            {
                                name: '小姑娘',
                                type: 'map',
                                mapType: 'china',
                                label: {
                                    normal: {
                                        show: false
                                    },
                                    emphasis: {
                                        show: true
                                    }
                                },
                                data:[
                                    {name: '北京',value: 23},
                                    {name: '天津',value: 20},
                                    {name: '香港',value: 23},
                                    {name: '澳门',value: 32}
                                ]
                            }
                        ]
                    };

                    // 使用刚指定的配置项和数据显示图表。
                    myChart.setOption(option);
            });
        </script>
    </head>
    <body>
        <div align="center">
            <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
            <div id="main" style="width: 600px;height:400px;" ></div>
        </div>
    </body>
</html>