
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<style>
    a {
        font-size: 30px;
        margin-left: 80px;
    }

    h1 {
        color: white;
    }

</style>
<head>
    <title>出库显示界面</title>
    <%String path = request.getContextPath();%>
    <script rel="script" src=<%=path + "/static/js/jquery-3.4.1.js"%>></script>
    <link rel="stylesheet" href="<%=path%>/static/lib/layui-v2.5.5/css/layui.css" media="all">
    <link rel="stylesheet" href="<%=path%>/static/css/public.css" media="all">
    <script src="<%=path%>/static/lib/layui-v2.5.5/layui.js" charset="utf-8"></script>
</head>
<body style="margin: 0">
<div style="height: 100%;width: 19%;float: left;border: 1px solid #92B8B1">
    <%--    <img src="https://i.loli.net/2020/06/09/dPjLE2aeGVKhADT.jpg" style="width: 100%;">--%>

    <div style="width: 100%;height: 57%;background-color: black">
        <br><br>
        <br><br>
        <a style="font-size:35px;font-family: '微软雅黑 Light';font-weight: bold ;color: yellow">菜鸟停车场</a>
        <br>
        <a style="font-size:50px;font-family: '微软雅黑 Light';font-weight: bold ;color: white">&nbsp&nbsp&nbsp出口</a>
        <br>
        <a style="font-size:50px;font-family: '微软雅黑 Light';font-weight: bold ;color: white">&nbspEXIT</a>
        <br>
        <a style="font-size:75px;font-family: 方正舒体;color: dodgerblue">欢&nbsp迎&nbsp</a>
        <a style="font-size:75px;font-family: 方正舒体;color: dodgerblue">&nbsp光临</a>
    </div>
</div>



<div  style="height: 100%;width: 80%;float:right;background-color: gray;background-image: url('${pageContext.request.contextPath}/static/img/bg.jpg')">

    <div id="img" style="margin: 4% auto;width: 60%;height: 40%;background-repeat:no-repeat ;background-size:100% 100%;background-image: url('${pageContext.request.contextPath}/static/img/CAINIAOLOG.jpg')">
        <input type="file" name="fileaot" id="fileaot"  onchange="preImg('fileaot','imgPre')"
               style="margin-top: -50px">
        <input type="button" value="确定" onclick="add()" id="btn"
               style="margin-top: -50px">

        <img src="" id="imgPre" style="width: 100%;height: 100%;margin-top:-20px">

    </div>

    <div id="msg" style="margin: 0 auto;width: 70%;height: 40%;background:wheat;border:1px solid cornflowerblue">
        <div style="width: 20%;height: 100%;background: chocolate;float: left">
            <div style="margin: 0 auto;width: 30%;height: 100%">
                <h1>用</h1>
                <h1>户</h1>
                <h1>信</h1>
                <h1>息</h1>
            </div>
        </div>
        <a id="carnumber">车牌号：</a><br>
        <a id="username">用户：</a><br>
        <a id="state">车辆情况：</a><br>
        <a id="ps">停车位：</a><br>
        <a id="entertime">入库时间：</a><br>
        <a id="exittime">出库时间：</a><br>
        <a id="time">停放时长：</a>
        <a id="money">应缴费：</a><br>
        <a style="color:red;font-family: 华文琥珀">温馨提醒：道路千万条，安全第一条。</a><br>
        <a style="color:red;font-family: 华文琥珀">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp行车不规范，亲人两行泪。</a>
    </div>
</div>


</body>
<script>
    /**
     * 将本地图片 显示到浏览器上
     */

    var img_str;
    function preImg(sourceId, targetId) {
        <%--$.ajax({--%>
        <%--            url:"${pageContext.request.contextPath}/ChackphotoController/img",--%>
        <%--            async: "true",--%>
        <%--            type: "Post",--%>
        <%--            data: "",--%>
        <%--            dataType: "text",--%>
        <%--            success: function (res) {--%>
        <%--                alert(res);--%>

        <%--                var imgPre = document.getElementById('imgPre');--%>
        <%--                alert(imgPre.src);--%>
        <%--                imgPre.src ="data:image/png;base64,"+ res;--%>
        <%--                alert(imgPre.src);--%>
        <%--            },--%>
        <%--            error: function () {--%>
        <%--            }--%>
        <%--        }--%>
        <%--);--%>

        var url = getFileUrl(sourceId);
        var imgPre = document.getElementById(targetId);
        imgPre.src = url;
    }

    //从 file 域获取 本地图片 url
    function getFileUrl(sourceId) {
        var url = '';
        var img = document.getElementsByName('img').value;
        if (navigator.userAgent.indexOf("MSIE") >= 1) { // IE
            url = document.getElementById(sourceId).value;
        } else if (navigator.userAgent.indexOf("Firefox") > 0) { // Firefox
            url = window.URL.createObjectURL(document.getElementById(sourceId).files.item(0));
        } else if (navigator.userAgent.indexOf("Chrome") > 0) { // Chrome
            url = window.URL.createObjectURL(document.getElementById(sourceId).files.item(0));
        }
        console.log(document.getElementById(sourceId).files.item(0))
        return url;
    }

    function add() {
        var formData = new FormData();
        formData.append('file2', $('#fileaot')[0].files[0]);  //添加图片信息的参数
        $.ajax({
            url: "${pageContext.request.contextPath}/ChackphotoController/File2",
            type: "post",
            cache: false,
            data: formData,
            processData: false,// 不处理数据
            contentType: false, // 不设置内容类型
            success: function (data) {
                if (data == "NO") {
                    // var file = document.getElementById("fileaot");
                    // var btn = document.getElementById("btn");
                    // file.style.display = "none";
                    // btn.style.display = "none";
                    alert("车位已满")
                }else if(data=="NOCAR"){
                    alert("无识别到车牌")
                }else if (data=="HAVEING") {
                    alert("车辆已出库")
                }else {
                    var carnumber = data.split(",")[0];
                    var username = data.split(",")[1];
                    var state = data.split(",")[2];
                    var ps = data.split(",")[3];
                    var entertime = data.split(",")[4];
                    var exittime = data.split(",")[5];
                    var time = data.split(",")[6];
                    var money = data.split(",")[7];
                    document.getElementById("carnumber").innerHTML = "车牌号 :  " + carnumber;
                    document.getElementById("username").innerHTML = "用户名：  " + username;
                    document.getElementById("state").innerHTML = "车辆情况 : " + state;
                    document.getElementById("ps").innerHTML = "停车位: " + ps;
                    document.getElementById("entertime").innerHTML = "入库时间: " + entertime;
                    document.getElementById("exittime").innerHTML = "出库时间: " + exittime;
                    document.getElementById("time").innerHTML = "停放时长 :  " + time;
                    document.getElementById("money").innerHTML = "应缴费:  " + money;
                    $(".enter").val(entertime);
                    $(".exit").val(exittime);
                    $(".carnum").val(carnumber);
                    $(".username").val(username);
                    $(".money").val(money);


                }
            }
        })

    }


    layui.use(['form'], function () {
        var form = layui.form,
            layer = layui.layer,
            $ = layui.$;

        //监听提交
        form.on('submit(saveBtn)', function (msg) {
            $.ajax({
                url:"${pageContext.request.contextPath}/ChackphotoController/search"
                ,type:"POST"
                ,dataType:"text"
                ,data:{
                    carnum:msg.field.carnum,
                },
                success:function (data) {
                    if (data == "NO") {
                        // var file = document.getElementById("fileaot");
                        // var btn = document.getElementById("btn");
                        // file.style.display = "none";
                        // btn.style.display = "none";
                        alert("车位已满")
                    }else if(data=="NOCAR"){
                        alert("无识别到车牌")
                    }else if (data=="HAVEING") {
                        alert("车辆已出库")
                    }else {
                        var carnumber = data.split(",")[0];
                        var username = data.split(",")[1];
                        var state = data.split(",")[2];
                        var ps = data.split(",")[3];
                        var entertime = data.split(",")[4];
                        var exittime = data.split(",")[5];
                        var time = data.split(",")[6];
                        var money = data.split(",")[7];
                        document.getElementById("carnumber").innerHTML = "车牌号 :  " + carnumber;
                        document.getElementById("username").innerHTML = "用户名：  " + username;
                        document.getElementById("state").innerHTML = "车辆情况 : " + state;
                        document.getElementById("ps").innerHTML = "停车位: " + ps;
                        document.getElementById("entertime").innerHTML = "入库时间: " + entertime;
                        document.getElementById("exittime").innerHTML = "出库时间: " + exittime;
                        document.getElementById("time").innerHTML = "停放时长 :  " + time;
                        document.getElementById("money").innerHTML = "应缴费:  " + money;
                        $(".enter").val(entertime);
                        $(".exit").val(exittime);
                        $(".carnum").val(carnumber);
                        $(".username").val(username);
                        $(".money").val(money);
                    }
                }
            })
            return false;
        });

    });
    function zhifu() {
        if ($(".money").val()==0){

        }else {
            window.open("${pageContext.request.contextPath}/alipay?type=car&carNum="+$(".carnum").val());
        }

    }

</script>
<script>//websocket
var websocket = null;
if('WebSocket' in window){
    websocket = new WebSocket("ws://39.102.35.36:8080/parkinglot/websocket/ip");
    // websocket = new WebSocket("ws://localhost:8080/parkinglot/websocket/ip");
}
else{
    alert("您的浏览器不支持websocket");
}

websocket.onerror = function(){
    setMessageInHtml("send error！");
}

websocket.onopen = function(){
    setMessageInHtml("connection success!")
}

websocket.onmessage  = function(event){
    setMessageInHtml(event.data);
    console.log(event);
}

websocket.onclose = function(){
    setMessageInHtml("closed websocket!")
}

window.onbeforeunload = function(){
    clos();
}

// 接收信息
function setMessageInHtml(message){
    document.getElementById('message').innerHTML += message;
}

//关闭连接
function clos(){
    websocket.close(3000,"强制关闭");
}

//发送信息
function send(msg){
    alert(msg);
    websocket.send(msg);
}

function upcarimg(index) {

    var upload = layui.upload;
    console.log(upload);
    //执行实例
    var uploadInst = upload.render({
        elem: '#upload' //绑定元素
        ,url: '${pageContext.request.contextPath}/AdminController/uploadcarImg' //上传接口
        ,data:{adminId:obj}
        ,done: function(res){
            layer.alert(res);
        }
        ,error: function(){
            //请求异常回调
        }
        ,accept: 'images'
        ,size: 5120 //限制文件大小，单位 KB
    });

}
</script>
</html>
