<!DOCTYPE html>

<html>
<head>
<title>去支付</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=1">
<meta content="yes" name="apple-mobile-web-app-capable">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta http-equiv="x-ua-compatible" content="IE=edge">
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-control" content="no-cache">
<meta http-equiv="Cache" content="no-cache">
</head>
<body style="background-color:#F2F2F2">

</body>
<script src="${request.contextPath}/js/jquery.min.js"></script>
<script type="text/javascript">
<#if wxpay??>
  function jsApiCall() {
      WeixinJSBridge.invoke(
          'getBrandWCPayRequest', {
              "appId": "${wxpay.appId}",     //公众号名称，由商户传入
              "timeStamp": "${wxpay.timeStamp}",         //时间戳，自1970年以来的秒数
              "nonceStr": "${wxpay.nonceStr}", //随机串
              "package": "${wxpay.package}",
              "signType": "${wxpay.signType}",         //微信签名方式
              "paySign": "${wxpay.paySign}" //微信签名
          },
          function(res){
              WeixinJSBridge.log(res.err_msg);
              if(res.err_msg == "get_brand_wcpay_request:ok") {

              } else if(res.err_msg == "get_brand_wcpay_request:cancel") {

              } else if(res.err_msg == "get_brand_wcpay_request:fail") {

              }
          }
      );
  }

  function callpay() {
      if (typeof WeixinJSBridge == "undefined") {
          if( document.addEventListener ) {
              document.addEventListener('WeixinJSBridgeReady', jsApiCall, false);
          } else if (document.attachEvent) {
              document.attachEvent('WeixinJSBridgeReady', jsApiCall);
              document.attachEvent('onWeixinJSBridgeReady', jsApiCall);
          }
      }else{
          jsApiCall();
      }
  }

  callpay()
</#if>
</script>
</html>