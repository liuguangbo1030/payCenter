<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no" name="viewport">
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta content="telephone=no" name="format-detection">
<meta content="email=no" name="format-detection">
<title>当前订单</title>
<script src="${request.contextPath}/js/jquery.min.js" type="text/javascript"></script>
<link rel="stylesheet" href="${request.contextPath}/css/prepay.css">
<link rel="stylesheet" href="${request.contextPath}/css/weui.min.css">
<link rel="stylesheet" href="${request.contextPath}/css/jquery-weui.min.css">
<style type="text/css">
.error {
	color: red;
	font-size: 15px;
	margin-top:5%;
}
.wx_pay{
	border-radius:5px;
	width:96%;
	margin-left:2%;
	height:40px;
	margin-top:5%;
	font-size:15px;
	background-color:#04BE02;
	color:white;
}

</style>
</head>
<body>
	<dl class="my-lpn">
		<dt class="title">我的车牌</dt>
		<dd class="lpn"><span>${carnumber}</span><span id ="mycar"><a class="change-btn" href="/pay/carnumber/${comid}/${passid}">修改</a></span></dd>
	</dl>
	<section class="main" >
	    <#if order??>
            <fieldset id = "maindiv">
                <div class="info-area">
                    <dl class="totle" style="border-bottom:0px">
                        <dt class="totle-title" id="pay_title">停车费</dt>
                        <dd class="totle-num" style="color:#04BE02;" id ="pay_money">￥<span id="money">${order.total-order.reduceAmount}</span></dd>
                        <div class="sweepcom hide" style="border-bottom: 1px solid #E0E0E0;"></div>
                    </dl>
                    <ul class="info-list" style="padding-top:1px;">
                        <#if companyName??>
                            <li class="list" id='parknameli'><span class="list-title" >车场名称</span><span class="list-content">${companyName}</span></li>
                        </#if>
                        <li class="list"><span class="list-title">入场时间</span><span class="list-content">${addTime}</span></li>
                        <li class="list" id="prepaytime_title"><span class="list-title" id="prepaidtime" >已停时长</span><span class="list-content" id="parktime" >${duration}</span></li>
                        <li class="list" id="derate_money_title"><span class="list-title" id="derate_money" >减免金额</span><span class="list-content" id="derate_money_span" >${order.reduceAmount}元</span></li>
                        <li class="list"><span class="list-title">车牌号码</span><span class="list-content">${carnumber}</span></li>

                    </ul>
                </div>
                <div style="height:15px"></div>
                <form method="get" action="/pay/topay">
                    <input type="hidden" name="comid" value="${comid?c}" />
                    <input type="hidden" name="carnumber" value="${carnumber}" />
                    <input type="hidden" name="id" value="${order.id?c}" />
                    <button id="wx_pay" class="weui-btn weui-btn_primary" style="width:95%" ><i id ='loading_img' class="weui-loading" style='display:none' ></i><span id='go_pay'>去支付</span></button>
                </form>
                <div class="tips"></div>
            </fieldset>
            <div style="text-align:center;" id="error" class="error">10分钟之内离场免费</div>
		<#else>
            <div style="text-align:center;" id="error" class="error">当前无订单</div>
        </#if>
	</section>
</body>
</html>