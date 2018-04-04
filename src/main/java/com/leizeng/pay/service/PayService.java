package com.leizeng.pay.service;

import com.leizeng.pay.config.WxpayProperties;
import com.leizeng.pay.entity.*;
import com.leizeng.pay.mapper.*;
import com.leizeng.pay.util.CommonUtil;
import com.leizeng.pay.util.DateUtils;
import com.leizeng.pay.util.wx.WXPayUtil;
import com.leizeng.pay.util.wx.WXRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author cloudy
 * @version 1.0
 * @date 18/1/29 下午3:14
 */
@Component
public class PayService {

    private Logger logger = LoggerFactory.getLogger(PayService.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private ComInfoMapper comInfoMapper;

    @Autowired
    private WxpayProperties wxpayProperties;

    @Autowired
    private PayMapper payMapper;

    @Autowired
    private WxOpenidMapper wxOpenidMapper;

    @Autowired
    private ComPayInfoMapper comPayInfoMapper;

    @Autowired
    private PayCallbackMapper payCallbackMapper;

    /**
     * 获取未支付的订单信息
     * @param filter
     * @return
     */
    public Order getNotPayOrder(Map<String, Object> filter) {
        if(null == filter)
            return null;
        return orderService.findOrder(filter);
    }

    /**
     * 根据id获取订单信息
     * @param id
     * @return
     */
    public Order getOrderById(Integer id) {
        if(null == id)
            return null;
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put("id", id);
        return orderService.findOrder(filter);
    }

    /**
     * 添加支付信息
     * @param pay
     */
    public void addPay(Pay pay) {
        if(null != pay) {
            logger.info("添加支付信息：{}", pay.toString());
            payMapper.addPay(pay);
        }
    }

    /**
     * 获取停车场信息
     * @param id
     * @return
     */
    public ComInfo getComInfo(Integer id) {
        if(null == id)
            return null;
        ComInfo comInfo = comInfoMapper.findComInfoById(id);
        logger.info("查询停车场信息的id：{},停车场信息：{}", id, comInfo);
        return comInfo;
    }

    /**
     * 获取微信openid信息
     * @param carnumber
     * @param appid
     * @return
     */
    public WxOpenid getWxOpenid(String carnumber, String appid) {
        if(null == carnumber || appid == null)
            return null;
        Map<String, String> filter = new HashMap<String, String>();
        filter.put("carnumber", carnumber);
        filter.put("appid", appid);
        WxOpenid wxOpenid = wxOpenidMapper.findWxOpenid(filter);
        logger.info("查询微信openid信息的carnumber：{},appid:{},停车场信息：{}", carnumber, appid, wxOpenid);
        return wxOpenid;
    }

    /**
     * 添加微信信息
     * @param appid
     * @param carnumber
     * @param openid
     */
    public void addWxOpenid(String appid, String carnumber, String openid) {
        logger.info("添回微信openid信息的wxOpenid：{},{},{}", appid,carnumber,openid);
        if(null != appid && null != carnumber && null != openid) {
            WxOpenid wxOpenid = new WxOpenid();
            wxOpenid.setAppid(appid);
            wxOpenid.setCarnumber(carnumber);
            wxOpenid.setOpenid(openid);
            wxOpenid.setCreatetime(DateUtils.getCurrentTimeMills());
            wxOpenidMapper.addWxOpenid(wxOpenid);
        }
    }

    /**
     * 获取时间差的中文信息
     * @param duartion
     * @return
     */
    public String getChineseTime(Integer duartion) {
        String durationTime = "";
        duartion = duartion/1000;
        Integer day = 0;
        Integer hour = 0;
        Integer minute = 0;
        if(duartion>86400) {
            day = duartion/86400;
            hour = (duartion - day*86400)/3600;
            minute = (duartion - (day*86400+hour*3600))/60;
        } else if(duartion > 3600) {
            hour = duartion/3600;
            minute = (duartion-hour*3600)/60;
        } else if(duartion > 60) {
            minute = duartion/60;
        }
        if(day > 0) {
            durationTime = day + "天";
        }
        if(hour > 0 || durationTime != "") {
            durationTime = durationTime + hour + "时";
        }
        if(minute > 0 || durationTime != "") {
            durationTime = durationTime + minute + "分";
        }
        return durationTime;
    }

    /**
     * 去支付
     * @param order
     * @param type
     * @return
     */
    public Map<String, String> toPay(Order order, Integer type, String ip, ComPayInfo comPayInfo, String openid) throws Exception{
        ComInfo comInfo = getComInfo(order.getComid());
        BigDecimal payAmount = order.getTotal().subtract(order.getReduceAmount());
        String title = "车牌为" + order.getCarNumber() + "的车主在"+comInfo.getCompanyName()+"支付停车费";
        String payId = CommonUtil.generateUUID();
        Pay pay = new Pay();
        pay.setPayId(payId);
        pay.setOrderId(order.getId());
        pay.setAmount(payAmount);
        pay.setProductName(title);
        pay.setCreatedAt(DateUtils.getCurrentTimeMills());
        pay.setPayType(type);
        addPay(pay);
        if(type.equals(1)) {
            return toWeixinPay(order, comPayInfo, title, ip, payAmount, payId, openid);
        }else if(type.equals(2)) {
            return toAliPay(order, comPayInfo);
        }
        return null;
    }


    public Map<String, String> toWeixinPay(Order order, ComPayInfo comPayInfo, String title, String ip, BigDecimal payAmount, String payId, String openid) throws Exception{
        String key = wxpayProperties.getKey();
        String appid = wxpayProperties.getAppid();
        String mch_id = wxpayProperties.getMchid();
        Map<String, String> reqData = new HashMap<String, String>();
        if(comPayInfo != null) {
            key = comPayInfo.getKey();
            appid = comPayInfo.getAppid();
            mch_id = comPayInfo.getMchId();

        }
        Integer totalFee = payAmount.multiply(new BigDecimal(100)).intValue();
        reqData.put("appid", appid);
        reqData.put("mch_id", mch_id);
        reqData.put("body", title);
        reqData.put("out_trade_no", payId);
        reqData.put("total_fee", totalFee.toString());
        reqData.put("spbill_create_ip", ip);
        reqData.put("notify_url", wxpayProperties.getNotifyurl());
        reqData.put("openid", openid);
        reqData.put("trade_type", "JSAPI");
        Map<String, String> map = WXRequest.unifiedOrder(reqData, wxpayProperties.getTimeout(),
                key, wxpayProperties.getUnifiedorderurl());
        if(map != null) {
            if(map.get("code_url") != null) {
                return map;
            } else if(map.get("prepay_id") != null){
                Map<String, String> pmap = new HashMap<String, String>();
                pmap.put("appId", appid);
                pmap.put("timeStamp", DateUtils.getCurrentTimeMills().toString());
                pmap.put("nonceStr", CommonUtil.generateUUID());
                pmap.put("package", "prepay_id="+map.get("prepay_id"));
                pmap.put("signType", "MD5");
                pmap.put("paySign", WXPayUtil.generateSignature(pmap, key, WXPayUtil.SignType.MD5));
                logger.info("pmap: {}", pmap);
                return pmap;
            }
        }
        return null;
    }

    public Map<String, String> toAliPay(Order order, ComPayInfo comPayInfo) {
        return null;
    }

    public Integer getPayType(String userAgent) {
        Integer payType = 1;
        if(userAgent.indexOf("AlipayClient") >= 0) {
            payType = 2;//支付宝
        } else if(userAgent.indexOf("MicroMessenger") >= 0) {
            payType = 1;//微信
        }
        return payType;
    }

    public Map<String, String> convertWXCallback(InputStream inputStream) {
        try {
            StringBuffer sb = new StringBuffer();
            String s;
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            while ((s = in.readLine()) != null) {
                sb.append(s);
            }
            in.close();
            inputStream.close();
            logger.info("微信回调信息：" + sb.toString());
            Map<String, String> m = WXPayUtil.xmlToMap(sb.toString());

            Map<String,String> packageParams = new HashMap<String, String>();
            Iterator it = m.keySet().iterator();
            while (it.hasNext()) {
                String parameter = (String) it.next();
                String parameterValue = m.get(parameter);

                String v = "";
                if(null != parameterValue) {
                    v = parameterValue.trim();
                }
                packageParams.put(parameter, v);
            }
            return packageParams;
        } catch (Exception ex) {
            logger.error("处理微信回调信息异常：", ex);
        }
        return null;
    }

    //@Transactional(isolation = Isolation.DEFAULT, timeout = 7200)
    public String wxcallback(Map<String, String> respData) throws Exception {
        Map<String, String> returnData = new HashMap<String, String>();
        if (!respData.containsKey("result_code") || !respData.get("result_code").equals("SUCCESS")) {
            returnData.put("return_code", "FAIL");
            returnData.put("return_msg", "报文不正确");
            return WXPayUtil.mapToXml(returnData);
        }
        if(!respData.containsKey("out_trade_no") || respData.get("out_trade_no") == null) {
            returnData.put("return_code", "FAIL");
            returnData.put("return_msg", "报文不正确");
            return WXPayUtil.mapToXml(returnData);
        }
        String payid = respData.get("out_trade_no");
        Pay pay = payMapper.findPayByPayid(payid);
        if(pay == null) {
            returnData.put("return_code", "FAIL");
            returnData.put("return_msg", "报文不正确");
            return WXPayUtil.mapToXml(returnData);
        }
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put("id", pay.getOrderId());
        Order order = orderService.findOrder(filter);
        if(order == null) {
            returnData.put("return_code", "FAIL");
            returnData.put("return_msg", "报文不正确");
            return WXPayUtil.mapToXml(returnData);
        }
        ComPayInfo comPayInfo = comPayInfoMapper.findComPayInfoByComid(order.getComid(), 1);
        String key = wxpayProperties.getKey();
        if(comPayInfo != null) {
            key = comPayInfo.getKey();
        }
        if(!WXPayUtil.isSignatureValid(respData, key)) {
            returnData.put("return_code", "FAIL");
            returnData.put("return_msg", "签名不正确");
            return WXPayUtil.mapToXml(returnData);
        }

        PayCallback payCallback = new PayCallback();
        payCallback.setAppid(respData.get("appid"));
        payCallback.setMchId(respData.get("mch_id"));
        payCallback.setNonceStr(respData.get("nonce_str"));
        payCallback.setOpenid(respData.get("openid"));
        payCallback.setTradeType(respData.get("trade_type"));
        payCallback.setTotalFee(Integer.valueOf(respData.get("total_fee")));
        payCallback.setTransactionId(respData.get("transaction_id"));
        payCallback.setOutTradeNo(respData.get("out_trade_no"));
        payCallback.setTimeEnd(respData.get("time_end"));
        payCallbackMapper.addPayCallback(payCallback);

        pay.setPayStatus(2);
        payMapper.updatePayByPayId(pay);

        order.setState(2);
        orderService.updateOrderStateById(order);

        returnData.put("return_code", "SUCCESS");
        returnData.put("return_msg", "OK");
        return WXPayUtil.mapToXml(returnData);
    }
}
