<%@page pageEncoding="UTF-8" isELIgnored="false" contentType="text/html; UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>


<script>
    $(function(){
        pageInit();
    });

    function pageInit(){
        $("#cateTable").jqGrid({
            url : "${path}/category/queryOneByPage",
            editurl:"${path}/category/edit?levelCate=1",
            datatype : "json",
            rowNum : 2,
            rowList : [ 2, 5, 20, 30 ],
            pager : '#catePage',
            viewrecords : true,
            styleUI:"Bootstrap",
            autowidth:true,
            height:"auto",
            colNames : [ 'Id', '类别名', '级别', '父类别ID' ],
            colModel : [
                {name : 'id',index : 'id',  width : 55},
                {name : 'cateName',editable:true,index : 'invdate',width : 90},
                {name : 'levels',index : 'name',width : 100},
                {name : 'parentId',index : 'note',width : 150,sortable : false}
            ],
            subGrid : true,  //是否开启子表格
            /*
            subgrid_id：是在创建表数据时创建的div标签的ID
            row_id：    是该行的ID
            * */
            subGridRowExpanded : function(subgrid_id, row_id) {  //设置展开子表格的参数
                addSubGird(subgrid_id, row_id);
            }
        });

        //父表格分页工具栏
        $("#cateTable").jqGrid('navGrid', '#catePage',
            {edit : true,add : true,del : true,addtext:"添加",deltext:'删除',edittext:"修改"},
            {
                closeAfterEdit:true, //关闭对话框

            },  //修改之后的额外操作
            {
                closeAfterAdd:true, //关闭对话框

            },  //添加之后的额外操作
            {}   //删除之后的额外操作
        );
    }


    //设置子表格的属性
    function addSubGird(subgridId, rowId){

        //生命变量并赋值  子表格表格的id
        var subgridTableId = subgridId+"Table";
        //子表格分页工具栏id
        var pagerId = subgridId+"Page";

        //在子表格的div中创建一个table和一个div
        $("#"+subgridId).html("<table id="+subgridTableId+" /><div id="+pagerId+" />");

        //设置习表哥的属性
        $("#" + subgridTableId).jqGrid({
            //url : "/category/queryByTwoCategory?categoryId=" + rowId,  rowId根据以及类别查询二级类别时使用
            url : "${path}/category/queryTwoByPage?categoryId=" + rowId,
            editurl:"${path}/category/edit?levelCate=2&categoryId=" + rowId,
            datatype : "json",
            rowNum : 20,
            pager : "#"+pagerId,
            styleUI:"Bootstrap",
            autowidth:true,
            height:"auto",
            colNames : [ 'Id', '类别名', '级别', '父类别ID' ],
            colModel : [
                {name : 'id',index : 'id',  width : 55},
                {name : 'cateName',editable:true,index : 'invdate',width : 90},
                {name : 'levels',index : 'name',width : 100},
                {name : 'parentId',index : 'note',width : 150,sortable : false}
            ],
        });
        //子表格分页工具栏
        $("#" + subgridTableId).jqGrid('navGrid',"#" + pagerId,
            {edit : true,add : true,del : true,addtext:"添加",deltext:'删除',edittext:"修改"},
            {
                closeAfterEdit:true, //关闭对话框

            },  //修改之后的额外操作
            {
                closeAfterAdd:true, //关闭对话框

            },  //添加之后的额外操作
            {}   //删除之后的额外操作
        );

    }

</script>

<%--创建一个面板--%>
<div class="panel panel-success">

    <%--面板头--%>
    <div class="panel panel-heading">
        <h2>类别信息</h2>
    </div>

    <!-- 标签页 -->
    <ul class="nav nav-tabs" role="tablist">
        <li role="presentation" class="active"><a href="#home" >类别管理</a></li>
    </ul>

    <%--创建表格--%>
    <table id="cateTable" />

    <%--创建分页工具栏--%>
    <div id="catePage" />

</div>