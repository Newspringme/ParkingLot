<%--
  Created by IntelliJ IDEA.
  User: 86189
  Date: 2020/6/10
  Time: 10:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <title>地图</title>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <link href="${pageContext.request.contextPath}/static/Case/lib/bootstrap.min.css" rel="stylesheet" />
    <link href="${pageContext.request.contextPath}/static/Case/css/common.css" rel="stylesheet" />
    <link href="${pageContext.request.contextPath}/static/Case/css/iconfont/iconfont.css" rel="stylesheet" />


</head>
<style type="text/css">
    .viewmode-group {
        position: absolute;
        right: 12px;
        top: 32%;
        border-radius: 6px;
        border: none;
    }

    .viewmode-group button {
        display: inline-block;
        width: 38px;
        height: 38px;
        border-radius: 4px;
        border: none;
        background-image: url("${pageContext.request.contextPath}/static/Case/image/wedgets/3D.png");
    }

    .parking {
        width: 320px;
        height: 46px;
        line-height: 46px;
        left: 100px;
        bottom: 14px;
        border: 1px solid #083344;
        border-radius: 4px;
        color: rgb(255, 255, 255);
        background-color: rgba(71, 92, 105, 0.8);
        font-size: 16px;
        text-align: center;
    }

    .fix {
        position: fixed;
    }

    .codition {
        width: 120px;
        left: 10px;
        bottom: 120px;
    }

    .codition ul {
        width: 100%;
        padding: 6px;
        list-style-type: none;
    }

    .codition ul li {
        display: list-item;
        height: 36px;
        background-color: rgb(255, 255, 255);
        line-height: 36px;
        text-align: center;
    }

    .codition ul li span {
        display: inline-block;
        width: 18px;
        height: 18px;
        border-radius: 50%;
        margin-right: 10px;
        vertical-align: middle;
    }

    .codition-first {
        background-color: #f00;
    }

    .codition-second {
        background-color: #0f0;
    }

    .codition-third {
        background-color: rgb(126, 172, 202);
    }

    .i-test-tip {
        width: 360px;
        height: 46px;
        left: 470px;
        bottom: 14px;
        border-radius: 4px;
        overflow: hidden;
        /* position: relative; */
        background-color: rgba(71, 92, 105, 0.8);
        text-align: center;
    }

    .test-tip {
        position: absolute;
        top: 0;
        left: 100%;
        color: #fff;
        font-size: 24px;
        line-height: 46px;
        white-space: nowrap;
        word-break: keep-all;
        text-overflow: ellipsis;
    }
    .test-tip span {
        color: #0f0;
    }
    .search {
        position: absolute;
        padding-left: 10px;
        top: 60px;
        left: 150px;
        font-size: 13px;
        height: auto;
        border: 1px solid #e6e6e6;
        background: #fff;
        /* box-shadow: 3px 3px 5px #bdbdbd; */
        box-sizing: border-box;
        -webkit-box-sizing: border-box;
        -moz-box-sizing: border-box;
        z-index: 999;
        border-radius: 4px;
    }
    .searchText {
        width: 300px;
        height: 20px;
        outline: none;
        border: none;
        margin: 0 0 0 14px;
        font-size: 13px;
        -webkit-appearance: none;
    #router {
        position: absolute;
        padding: 20px;
        width: 457px;
        top: 120px;
        left: 150px;
        height: 130px;
        padding: 22px 18px;
        /*margin-left: px;*/
        border: 1px solid #e6e6e6;
        background: #fff;
        box-shadow: 3px 3px 5px #bdbdbd;
        box-sizing: border-box;
        -webkit-box-sizing: border-box;
        -moz-box-sizing: border-box;
        z-index: 999;
        display: none;
    }

    .router .routerInput {
        height: 40px;
        padding: 6px 0;
        box-sizing: border-box;
    }

    .router .routerInput .routerText {
        width: 250px;
        border: none;
        outline: 0;
        height: 20px;
        font-size: 16px;
        border-bottom: 1px solid #dedede;
        margin-left: 6px;
    }

    #shopSearch>ul {
        display: block;
        list-style-type: none;
        /* height: 0; */
        padding: 10px;
    }

    .list ul>li {
        list-style: none;
        padding: 10px 12px;
        font-size: 13px;
        color: #5d5d5d;
        cursor: pointer;
        box-sizing: border-box;
        -webkit-box-sizing: border-box;
        -moz-box-sizing: border-box;
    }

    .list ul>li>span {
        padding-right: 18px;
    }

    .list ul>li:hover {
        background: #bbb;
        color:#fff;
    }

    #navTo {
        position: absolute;
        top: 46px;
        left: 338px;

    }
</style>
<body ms-controller = "ctrl" class="ms-controller">
<div id="map-container"></div>
<div class="loading">
    <div class="lodingImg"></div>
</div>
<nav class="navbar navbar-inverse">
    <div class="container-fluid">
        <h1>
            <a href="" title="菜鸟室内三维地图">菜鸟室内三维地图</a>
        </h1>
    </div>
</nav>
<!-- 搜索 -->
<div class="search">
    <span id="btnSearch" class="glyphicon glyphicon-search" aria-hidden="true"></span>
    <input id="searchText" type="text" class="searchText" placeholder="搜索关键字"> |
    <button type="button" id="startnav" class="btn btn-default" style="border: none">
        <span id="btnNav" class="glyphicon glyphicon-map-marker"></span>
        导航
    </button>
</div>
<!-- 查询显示列表 -->
<div id="shopSearch" class="layer list">
    <ul>
    </ul>
</div>
<!--  导航选点 -->
<div id="router" class="layer router">
    <div class="routerInput">
        <span class="glyphicon glyphicon-flag"></span>
        <input id="startText" class="routerText" type="" name="" placeholder="点击地图可选择起点" readonly>
    </div>
    <div class="routerInput">
        <span class="glyphicon glyphicon-flag"></span>
        <input id="endText" class="routerText" type="" name="" placeholder="点击地图可选择终点" readonly>
    </div>
    <div id="navigation" class="navigation">
        <button type="button" id="navTo" class="btn btn-default">
            <span class="glyphicon glyphicon-share-alt"></span>
            导航
        </button>
    </div>
</div>
<%--3D/2D切换--%>
<div class="viewmode-group">
    <button id="btn3D" class="btn btn-default"></button>
</div>
<%--车位情况--%>
<div class="parking fix" id="parking"><span id="park_id"></span>车位情况：<span id="park_state"></span></div>
<div class="codition fix">
    <ul>
        <li><span class="codition-first"></span>已停车</li>
        <li><span class="codition-second"></span>未停车</li>
    </ul>
</div>
<%--停车场车位余量--%>
<div class="i-test-tip fix" id="i-test-tip">
    <div class="test-tip">
        停车场车位总数：<span id="total"></span>个，当前剩余车位数 <span id="free"></span>。
    </div>
</div>

<script src="${pageContext.request.contextPath}/static/Case/lib/esmap-1.6.min.js"></script>
<script src="${pageContext.request.contextPath}/static/Case/lib/jquery-2.1.4.min.js"></script>
<script src="${pageContext.request.contextPath}/static/Case/lib/jquery.qrcode.min.js"></script>
<script src="${pageContext.request.contextPath}/static/Case/lib/tips_controls.js"></script>
<script src="${pageContext.request.contextPath}/static/Case/lib/bootstrap.min.js"></script>
<script type="text/javascript">
    //定义全局map变量
    var map;
    var esmapID = 'cai_niao_parkinglot';
    var styleid = 1005;
    var floorControl; // 楼层控制控件配置参数（几楼）
    var ctlOpt = new esmap.ESControlOptions({
        position: esmap.ESControlPositon.RIGHT_TOP,
        imgURL: "${pageContext.request.contextPath}/static/Case/image/wedgets/"
    });
    // 放大、缩小控件配置
    var ctlOpt1 = new esmap.ESControlOptions({
        position: esmap.ESControlPositon.LEFT_TOP, // 位置 左上角
        // 位置x,y的偏移量
        offset: {
            x: 20,
            y: 80
        },
        imgURL: "${pageContext.request.contextPath}/static/Case/image/wedgets/"
    });
    var dataSrc = "${pageContext.request.contextPath}/static/Case/data";
    var themeSrc = "${pageContext.request.contextPath}/static/Case/data/theme";
    map = new esmap.ESMap({
        container: $("#map-container")[0], // 渲染dom
        mapDataSrc: dataSrc, //地图数据位置
        mapThemeSrc: themeSrc, //主题数据位置
        focusAlphaMode: true, // 对不可见图层启用透明设置 默认为true
        focusAnimateMode: true, // 开启聚焦层切换的动画显示
        focusAlpha: 0.4, // 对不聚焦图层启用透明设置，当focusAlphaMode = true时有效
        focusFloor: 1,
        mapAudioSrc: '${pageContext.request.contextPath}/static/Case/lib',
        token:'20200611',
        // visibleFloors: "all",
        themeID: styleid //自定义样式主题ID
    });
    map.openMapById(esmapID); //打开地图
    map.showCompass = true; //显示指南针

    var parkData;
    var color = ["#ff0000", "#00ff00"];
    //地图加载完成回调
    map.on("loadComplete", function () {
        floorControl = new esmap.ESScrollFloorsControl(map, ctlOpt);
        var zoomControl = new esmap.ESZoomControl(map, ctlOpt1);
        bingEvents();
        marquee();
        // //先执行显示一次
        setTimeout(function () {getParkData();},10);
        //开启定时器从后台获取数据
        setInterval(function () {getParkData();}, 15000);});

    function getParkData() {
        $.getJSON("${pageContext.request.contextPath}/parkController/getParkData",function (data) {
            console.log(data);
            parkData = data.put;
            console.log(parkData);
            //解析数据
            var total = 0;
            var d = {"idList":[[],[],[]]};
            for (var i = 0; i < parkData.length; i++)
            {
                var park = parkData[i];
                console.log(park);
                var parkState;
                if (park.parkState == "已停车")
                {
                    parkState = 1;
                }else{
                    parkState = 2;
                }
                total++;
                d.idList[parkState].push(park.eventId);
            }
            console.log(d);
            var showText = "";

            //调用批量修改颜色接口来修改
            map.changeModelColor({
                id: d.idList[1],
                color: color[0]
            });
            map.changeModelColor({
                id: d.idList[2],
                color: color[1]
            });
            showText += ":"+d.idList[2].length+"个";
            //3.显示更新统计
            $("#free").html(showText); //滚动字幕 相应楼层剩余停车位数
            $("#total").html(total);
        });
    }

    //地图点击事件
    map.on("mapClickNode", function (event) {
        if (event.nodeType == esmap.ESNodeType.NONE ||
            event.nodeType == esmap.ESNodeType.FLOOR ||
            event.name == "楼梯"){
            return;
        }
        $("#parking").css("fontSize", "18px").html();
        $("#park_id").css("color", "rgb(255, 255, 0)").html(event.name); //停车位ID(parkName)
        for (var i = 0; i < parkData.length; ++i) {
            if (event.ID == parkData[i].eventId) {
                $("#park_state").html(parkData[i].parkState);

            }
        }
    });

    //滚动字幕
    function marquee() {
        var scrollWidth = $('#i-test-tip').width();
        var textWidth = $('.test-tip').width();
        var i = scrollWidth;
        setInterval(function () {
            i--;
            if (i < -textWidth) {
                i = scrollWidth;
            }
            $('.test-tip').animate({
                'left': i + 'px'
            }, 8);
        }, 8);
    }

    //绑定事件
    function bingEvents() {
        document.getElementById("btn3D").onclick = function () {
            if (map.viewMode == esmap.ESViewMode.MODE_2D) {
                map.viewMode = esmap.ESViewMode.MODE_3D; //2D-->3D
                document.getElementById("btn3D").style.backgroundImage =
                    "url('${pageContext.request.contextPath}/static/Case/image/wedgets/3D.png')";
            } else {
                map.viewMode = esmap.ESViewMode.MODE_2D; //3D-->2D
                document.getElementById("btn3D").style.backgroundImage =
                    "url('${pageContext.request.contextPath}/static/Case/image/wedgets/2D.png')";
            }
        };
    }
</script>
</body>
</html>
