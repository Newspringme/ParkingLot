<%--
  Created by IntelliJ IDEA.
  User: 96581
  Date: 2020/6/6
  Time: 13:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>Insert title here</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="<%=path%>/static/lib/layui-v2.5.5/css/layui.css" media="all">
    <link rel="stylesheet" href="<%=path%>/static/css/public.css" media="all">
    <script src="<%=path%>/static/lib/layui-v2.5.5/layui.js" charset="utf-8"></script>
    <style>
        .laytable-cell-1-0-2{
            height: 100%;
            max-width: 100%;
        }
    </style>
</head>
<body>
<div class="layuimini-container">
    <div class="layuimini-main">

        

        <script type="text/html" id="toolbarDemo">
            <div class="layui-btn-container">
                <button class="layui-btn layui-btn-normal layui-btn-sm data-add-btn" lay-event="add"> 添加 </button>
            </div>
        </script>

        <table class="layui-hide" id="currentTableId" lay-filter="currentTableFilter"></table>

        <script type="text/html" id="currentTableBar">
            <a class="layui-btn layui-btn-normal layui-btn-xs data-count-edit" lay-event="edit">编辑</a>
            <a class="layui-btn layui-btn-xs layui-btn-danger data-count-delete" lay-event="delete">删除</a>
        </script>

    </div>
</div>
<script type="text/html" id="img">
    <div><img src="{{ d.url }}" style="width: 150px;" onclick="show_img('{{ d.url }}')"></div>
</script>

<script>

    var load=layui.use(['form', 'table'], function () {
        var $ = layui.jquery,
            form = layui.form,
            table = layui.table;

        var getlist=table.render({
            elem: '#currentTableId',
            url: '/parkinglot/AdminController/querySlideShow',
            toolbar: '#toolbarDemo',
            defaultToolbar: ['filter', 'exports', 'print', {
                title: '提示',
                layEvent: 'LAYTABLE_TIPS',
                icon: 'layui-icon-tips'
            }],
            cols: [[
                {field: 'sid', width: 150, title: 'ID', sort: true},
                {field: 'title',  maxwidth:150,minWidth: 100, title: '名称'},
                {field: 'url',  templet: "#img", title: '图片'},
                {field: 'weight', maxwidth: 150,minWidth: 100, title: '权限值'},
                {field: 'starttime', maxwidth: 150,minWidth: 100, title: '开始时间'},
                {field: 'endtime', maxwidth: 620,minWidth: 100, title: '结束时间'},
                {title: '操作', minWidth: 150, toolbar: '#currentTableBar', align: "center"}
            ]],
            limits: [10, 15, 20, 25, 50, 100],
            limit: 10,
            page: true,
            skin: 'line'
        });

   

       

        /**
         * toolbar监听事件
         */
        table.on('toolbar(currentTableFilter)', function (obj) {
            if (obj.event === 'add') {  // 监听添加操作
                var index = layer.open({
                    title: '添加轮播图',
                    type: 2,
                    shade: 0.2,
                    maxmin:true,
                    shadeClose: true,
                    area: ['100%', '100%'],
                    content: '/parkinglot/pages/table/slideshow-add.jsp',
                });
                $(window).on("resize", function () {
                    layer.full(index);
                });
            } else if (obj.event === 'delete') {  // 监听删除操作
                var checkStatus = table.checkStatus('currentTableId')
                    , data = checkStatus.data;
                layer.alert(JSON.stringify(data));
            }
        });

        //监听表格复选框选择
        table.on('checkbox(currentTableFilter)', function (obj) {
            console.log(obj)
        });

        table.on('tool(currentTableFilter)', function (obj) {
            var data = obj.data;
            var starttime = format(data.starttime,'yyyy-MM-dd');
            var endtime = format(data.endtime,'yyyy-MM-dd');
            if (obj.event === 'edit') {

                var index = layer.open({
                    title: '编辑用户',
                    type: 2,
                    shade: 0.2,
                    maxmin:true,
                    shadeClose: true,
                    area: ['100%', '100%'],
                    content: '/parkinglot/pages/table/slideshow-edit.jsp?sid='+data.sid+'&title='+data.title+'&url='+data.url+'&weight='+data.weight+'&starttime='+starttime+'&endtime='+endtime,
                });
                $(window).on("resize", function () {
                    layer.full(index);
                });
                return false;
            } else if (obj.event === 'delete') {
                layer.confirm('真的删除'+obj.data.title, function (index) {
                    $.ajax({
                        url:"${pageContext.request.contextPath}/AdminController/deleteSlideShow"
                        ,type:"POST"
                        ,dataType:"text"
                        ,data:{
                            sid:data.sid
                        },
                        success:function (msg) {
                            msg=msg+"";
                            if (msg=='true'){
                                layer.alert("删除成功");
                                obj.del();
                                load.get
                            }else {
                                layer.alert("删除失败");
                            }
                        }
                    })
                    layer.close(index);
                });
            }
        });

    });

    function show_img(t) {

        // 页面层
        layer.open({
            type: 1,
            title: '区域图片',
            area: ['100%', '100%'], //宽高 t.width() t.height()
            shadeClose: true, //开启遮罩关闭
            end: function (index, layero) {
                return false;
            },
            content:  '<div style="text-align:center;width: 100%;height: 100%"><img src="' + t + '" /></div>'
        });
    }



    //封装时间格式
    function format(time, format) {
        var t = new Date(time);
        var tf = function (i) {
            return (i < 10 ? '0' : '') + i
        };
        return format.replace(/yyyy|MM|dd|HH|mm|ss/g, function (a) {
            switch (a) {
                case 'yyyy':
                    return tf(t.getFullYear());
                    break;
                case 'MM':
                    return tf(t.getMonth() + 1);
                    break;
                case 'mm':
                    return tf(t.getMinutes());
                    break;
                case 'dd':
                    return tf(t.getDate());
                    break;
                case 'HH':
                    return tf(t.getHours());
                    break;
                case 'ss':
                    return tf(t.getSeconds());
                    break;
            }
        })
    }
</script>

</body>
</html>