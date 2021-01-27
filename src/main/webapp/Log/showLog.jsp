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
            url : "${path}/log/queryByPage",
            datatype : "json",
            rowNum : 2,
            rowList : [ 2,5,10, 20, 30 ],
            pager : '#userPage',
            viewrecords : true,  //是否展示总条数
            styleUI:"Bootstrap",
            autowidth:true,
            height:"auto",
            colNames : [ 'ID', '管理员姓名', '操作时间', '操作方法', '状态'],
            colModel : [
                {name : 'id',index : 'id',align : "center"},
                {name : 'adminName',editable:true,index : 'invdate',align : "center"},
                {name : 'optionTime',index : 'name asc, invdate',align : "center"},
                {name : 'methodName',editable:true,index : 'tax',align : "center"},
                {name : 'status',index : 'total',align : "center"},
            ]
        });

        //分页工具栏
        $("#userTable").jqGrid('navGrid', '#userPage',
            {edit : false,add : false,del : false,addtext:"添加",deltext:'删除',edittext:"修改"},
            {},  //修改之后的额外操作
            {},  //添加之后的额外操作
            {}   //删除之后的额外操作
        );
    }


</script>

<%--创建一个面板--%>
<div class="panel panel-info">

    <%--面板头--%>
    <div class="panel panel-heading">
        <h2>日志信息</h2>
    </div>

    <!-- 标签页 -->
    <ul class="nav nav-tabs" role="tablist">
        <li role="presentation" class="active"><a href="#home" >日志管理</a></li>
    </ul><br>


    <%--创建表格--%>
    <table id="userTable" />

    <%--创建分页工具栏--%>
    <div id="userPage" />

</div>