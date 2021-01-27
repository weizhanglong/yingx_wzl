<%@page pageEncoding="UTF-8" isELIgnored="false" contentType="text/html; UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>


<script>
    $(function(){
        //初始化表格
        initUserTable();
    });

    //配置表格的相关参数
    function initUserTable(){
        $("#userTable").jqGrid({
            url : "${path}/feedback/queryByPage",
            datatype : "json",
            rowNum : 2,
            rowList : [ 2,5,10, 20, 30 ],
            pager : '#userPage',
            viewrecords : true,  //是否展示总条数
            styleUI:"Bootstrap",
            autowidth:true,
            height:"auto",
            colNames : [ 'ID', '标题', '内容', '用户ID'],
            colModel : [
                {name : 'id',align : "center"},
                {name : 'title',align : "center"},
                {name : 'content',align : "center"},
                {name : 'userId',align : "center"},
            ]
        });

        //分页工具栏
        $("#userTable").jqGrid('navGrid', '#userPage',
            {edit : false,add : false,del : false},
            {},  //修改之后的额外操作
            {},  //添加之后的额外操作
            {}   //删除之后的额外操作
        );
    }
    //导出数据
    function Exports () {
        $.post("${path}/feedback/Exports",
            function (arr) {
                alert(arr);
            },
        );
    };

    //导入数据
    function Import () {
        $.post("${path}/feedback/Import",
            function (arr) {
                alert(arr);
            },
        );
    };


</script>

<%--创建一个面板--%>
<div class="panel panel-info">

    <%--面板头--%>
    <div class="panel panel-heading">
        <h2>反馈信息</h2>
    </div>

    <!-- 标签页 -->
    <ul class="nav nav-tabs" role="tablist">
        <li role="presentation" class="active"><a href="#home" >反馈管理</a></li>
    </ul><br>

    <%--按钮组--%>
    <div>
        &emsp;&emsp;<button id="exportFeedback" onclick="Exports()" class="btn btn-success">导出反馈信息</button>
        &emsp;&emsp;<button id="ImportFeedback" onclick="Import()" class="btn btn-info">导入反馈信息</button>
    </div><br>

    <%--创建表格--%>
    <table id="userTable" />

    <%--创建分页工具栏--%>
    <div id="userPage" />

</div>