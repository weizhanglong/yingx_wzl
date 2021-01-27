<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<script>
    //延迟加载
    $(function(){
        pageInit();
    });

    //创建表格
    function pageInit(){
        $("#adminTable").jqGrid({
            url : "${path}/admin/queryAdminPage",  //接收  page=当前页   rows=每页展示条数   返回  page=当前页   rows=[User,User]数据    tolal=总页数   records=总条数
            editurl:"${path}/admin/edit",  //增删改走的路径  oper:add ,edit,del
            datatype : "json", //数据格式
            rowNum : 2,  //每页展示条数
            rowList : [ 2,10, 20, 30 ],  //可选没夜战是条数
            pager : '#adminPage',  //分页工具栏
            sortname : 'id', //排序
            type : "post",  //请求方式
            styleUI:"Bootstrap", //使用Bootstrap
            autowidth:true,  //宽度自动
            height:"auto",   //高度自动
            viewrecords : true, //是否展示总条数
            colNames : [ 'Id', '用户名', '密码','状态', '盐'],
            colModel : [
                {name : 'id',width : 55},
                {name : 'username',editable:true,width : 90},
                {name : 'password',editable:true,width : 100},
                {name : 'status',width : 80,align : "center",
                    formatter:function(cellvalue, options, rowObject){
                        if(cellvalue==0){
                            //正常  展示冻结（绿色）
                            return "<button class='btn btn-danger' onclick='updateStatus(\""+rowObject.id+"\",\""+rowObject.status+"\")' >冻结</button>";
                        }else{
                            //冻结  展示解除冻结（红色）
                            return "<button class='btn btn-success' onclick='updateStatus(\""+rowObject.id+"\",\""+rowObject.status+"\")'>解除冻结</button>";
                        }
                    }
                },
                {name : 'salt',width : 150}
            ]
        });

        //分页工具栏
        $("#adminTable").jqGrid('navGrid', '#adminPage',
            {edit : true,add : true,del : true,addtext:"添加",edittext:"编辑",deltext:"删除"},
            {
                closeAfterEdit:true,  //关闭对话框
            },  //修改之后的额外操作
            {
                closeAfterAdd:true,  //关闭对话框
            }, //添加之后的额外操作
            {}  //删除之后的额外操作
        );
    }

    //修改状态
    function updateStatus(id,status){
        if(status==1){
            $.post("${path}/admin/edit",{"id":id,"status":"0","oper":"edit"},function(data){
                $("#adminTable").trigger("reloadGrid");
            })
        }else{
            $.post("${path}/admin/edit",{"id":id,"status":"1","oper":"edit"},function(data){
                $("#adminTable").trigger("reloadGrid");
            })
        }
    }
</script>

<%--创建一个面板--%>
<div class="panel panel-primary">

    <%--面板头--%>
    <div class="panel panel-heading">
        <span>管理员信息</span>
    </div>

    <!-- Nav tabs -->
    <ul class="nav nav-tabs" role="tablist">
        <li role="presentation" class="active"><a href="#home" aria-controls="home" role="tab" data-toggle="tab">管理员信息</a></li>
    </ul><br>

    <%--创建表格--%>
    <table id="adminTable" />

    <%--分页工具栏--%>
    <div id="adminPage"/>

</div>