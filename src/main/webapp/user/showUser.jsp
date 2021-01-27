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
            url : "${path}/user/queryByPage",
            editurl:"${path}/user/edit",
            datatype : "json",
            rowNum : 2,
            rowList : [ 2,5,10, 20, 30 ],
            pager : '#userPage',
            viewrecords : true,  //是否展示总条数
            styleUI:"Bootstrap",
            autowidth:true,
            height:"auto",
            colNames : [ 'ID', '手机号', '微信', '头像', '用户名','签名', '状态','注册时间' ],
            colModel : [
                {name : 'id',index : 'id',align : "center"},
                {name : 'phone',editable:true,index : 'invdate',align : "center"},
                {name : 'wechat',index : 'name asc, invdate',align : "center"},
                {name : 'headImg',editable:true,index : 'amount',align : "center",edittype:"file",
                    //cellvalue:列值,options:操作,rowObject:行对象数据
                    formatter:function(cellvalue, options, rowObject){
                        return "<img src='${path}/bootstrap/img/"+rowObject.headImg+"' width='100px' height='50px'  >";
                    }
                },
                {name : 'username',editable:true,index : 'tax',align : "center"},
                {name : 'sign',editable:true,index : 'total',align : "center"},
                {name : 'status',index : 'total',align : "center",
                    formatter:function(cellvalue, options, rowObject){
                        //判断状态
                        if(cellvalue==0){
                            //正常状态
                            return "<button class='btn btn-success' onclick='updateStatus(\""+rowObject.id+"\",\""+rowObject.status+"\")' >冻结</button>";
                        }else{
                            //冻结状态
                            return "<button class='btn btn-danger' onclick='updateStatus(\""+rowObject.id+"\",\""+rowObject.status+"\")' >解除冻结</button>";
                        }
                    }
                },
                {name : 'createTime',index : 'note',align : "center",sortable : false}
            ]
        });

        //分页工具栏
        $("#userTable").jqGrid('navGrid', '#userPage',
            {edit : true,add : true,del : true,addtext:"添加",deltext:'删除',edittext:"修改"},
            {
                closeAfterEdit:true, //关闭对话框
                afterSubmit:function(data){  //提交之后触发
                    //console.log(data.responseText);   添加数据的id
                    //文件上传   异步
                    $.ajaxFileUpload({
                        fileElementId: "headImg",    //需要上传的文件域的ID，即<input type="file" id="headImg" name="headImg" >的ID。
                        url: "${path}/user/uploadUserHeadImg", //后台方法的路径
                        type: 'post',   //当要提交自定义参数时，这个参数要设置成post
                        //dataType: 'json',   //服务器返回的数据类型。可以为xml,script,json,html。如果不填写，jQuery会自动判断。
                        data:{"id":data.responseText},
                        success: function() {   //提交成功后自动执行的处理函数，参数data就是服务器返回的数据。
                            //刷新表单
                            $("#userTable").trigger("reloadGrid");
                        }
                    });
                    return "hello";
                }
            },  //修改之后的额外操作
            {
                closeAfterAdd:true, //关闭对话框
                afterSubmit:function(data){  //提交之后触发
                    //console.log(data.responseText);   添加数据的id
                    //文件上传   异步
                    $.ajaxFileUpload({
                        fileElementId: "headImg",    //需要上传的文件域的ID，即<input type="file" id="headImg" name="headImg" >的ID。
                        url: "${path}/user/uploadUserHeadImg", //后台方法的路径
                        type: 'post',   //当要提交自定义参数时，这个参数要设置成post
                        //dataType: 'json',   //服务器返回的数据类型。可以为xml,script,json,html。如果不填写，jQuery会自动判断。
                        data:{"id":data.responseText},
                        success: function() {   //提交成功后自动执行的处理函数，参数data就是服务器返回的数据。
                            //刷新表单
                            $("#userTable").trigger("reloadGrid");
                        }
                    });
                    return "hello";
                }
            },  //添加之后的额外操作
            {}   //删除之后的额外操作
        );
    }


    //修改状态
    function updateStatus(id,status){

        if(status==1){
            $.post("${path}/user/edit",{"id":id,"status":"0","oper":"edit"},function(data){
                //刷新表单
                $("#userTable").trigger("reloadGrid");
            });
        }else{
            $.post("${path}/user/edit",{"id":id,"status":"1","oper":"edit"},function(data){
                //刷新表单
                $("#userTable").trigger("reloadGrid");
            });
        }
    }

</script>

<%--创建一个面板--%>
<div class="panel panel-info">

    <%--面板头--%>
    <div class="panel panel-heading">
        <h2>用户信息</h2>
    </div>

    <!-- 标签页 -->
    <ul class="nav nav-tabs" role="tablist">
        <li role="presentation" class="active"><a href="#home" >用户管理</a></li>
    </ul><br>

    <%--按钮组--%>
    <div>
        &emsp;&emsp;<button class="btn btn-success">导出用户信息</button>
        &emsp;&emsp;<button class="btn btn-info">导出用户信息</button>
        &emsp;&emsp;<button class="btn btn-danger">导出用户信息</button>
    </div><br>

    <%--创建表格--%>
    <table id="userTable" />

    <%--创建分页工具栏--%>
    <div id="userPage" />

</div>