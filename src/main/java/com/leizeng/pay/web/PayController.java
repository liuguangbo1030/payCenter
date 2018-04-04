package com.leizeng.pay.web;

import com.alibaba.fastjson.JSONObject;
import com.leizeng.pay.config.WxpayProperties;
import com.leizeng.pay.mapper.ComPayInfoMapper;
import com.leizeng.pay.entity.ComPayInfo;
import com.leizeng.pay.entity.Order;
import com.leizeng.pay.entity.WxOpenid;
import com.leizeng.pay.service.PayService;
import com.leizeng.pay.util.CommonUtil;
import com.leizeng.pay.util.DateUtils;
import com.leizeng.pay.mapper.ComInfoMapper;
import com.leizeng.pay.entity.ComInfo;
import com.leizeng.pay.util.HttpRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cloudy
 * @version 1.0
 * @date 18/1/27 下午2:29
 */
@Controller
@RequestMapping("/pay")
public class PayController {

    private Logger logger = LoggerFactory.getLogger(PayController.class);

    @Autowired
    private PayService payService;

    @Autowired
    private ComInfoMapper comInfoMapper;

    @Autowired
    private WxpayProperties wxpayProperties;

    @Autowired
    private ComPayInfoMapper comPayInfoMapper;

    @RequestMapping(value = "/carnumber/{comid:\\d+}/{passid:\\d+}", method = RequestMethod.GET)
    public String carnumber(Model model, @PathVariable Integer comid, @PathVariable Integer passid) {
        model.addAttribute("comid", comid);
        model.addAttribute("passid", passid);
        return "pay/carnumber";
    }

    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public String order(Model model, HttpServletRequest request) {
        Map<String, Object> filter = new HashMap<String, Object>();
        Integer comid = Integer.valueOf(request.getParameter("comid"));
        String carnumber = java.net.URLDecoder.decode(request.getParameter("carnumber"));
        filter.put("carnumber", carnumber);
        filter.put("comid", comid);
        filter.put("state", 0);
        Order order = payService.getNotPayOrder(filter);
        if (null != order) {
            ComInfo comInfo = payService.getComInfo(comid);
            model.addAttribute("companyName", comInfo.getCompanyName());
            long addtime = new Long(String.valueOf(order.getCreateTime()) + "000");
            model.addAttribute("addTime", DateUtils.getDateTimeByTimestamp(addtime));
            model.addAttribute("duration", payService.getChineseTime(order.getDuration()));
            model.addAttribute("payMoney", order.getTotal().subtract(order.getReduceAmount()));
        }
        model.addAttribute("carnumber", filter.get("carnumber"));
        model.addAttribute("comid", filter.get("comid"));
        model.addAttribute("passid", request.getParameter("passid"));
        model.addAttribute("order", order);
        return "pay/order";
    }

    @RequestMapping(value = "/topay", method = RequestMethod.GET)
    public String topay(Model model, HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        try {
            String comid = request.getParameter("comid");
            String carnumber = request.getParameter("carnumber");
            Order order = payService.getOrderById(Integer.valueOf(id));
            if (null != order) {
                if (order.getCarNumber().equals(carnumber) && order.getComid().equals(Integer.valueOf(comid))) {
                    Integer payType = payService.getPayType(request.getHeader("User-Agent"));
                    String appId = wxpayProperties.getAppid();
                    ComPayInfo comPayInfo = comPayInfoMapper.findComPayInfoByComid(order.getComid(), payType);
                    if(comPayInfo != null) {
                        appId = comPayInfo.getAppid();
                    }
                    if (payType.equals(1)) {//微信支付
                        WxOpenid wxOpenid = payService.getWxOpenid(order.getCarNumber(), appId);
                        if (wxOpenid == null) {
                            String code = request.getParameter("code");
                            if (CommonUtil.isStrEmpty(code)) {
                                String redirecturl = java.net.URLEncoder.encode(request.getRequestURL() + "?" + request.getQueryString());
                                String wxurl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri=" + redirecturl + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirec";
                                response.sendRedirect(wxurl);
                            } else {
                                String appsecrect = wxpayProperties.getAppsecret();
                                String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId + "&secret=" + appsecrect + "&code=" + code + "&grant_type=authorization_code";
                                JSONObject object = HttpRequestUtil.doGet(url);
                                String openid = "";
                                if (object != null) {
                                    openid = object.get("openid").toString();
                                    payService.addWxOpenid(appId, order.getCarNumber(), openid);
                                }
                                Map<String, String> wxmap = payService.toPay(order, payType, CommonUtil.getIpAddr(request), comPayInfo, openid);
                                model.addAttribute("wxpay", wxmap);
                            }
                        } else {
                            Map<String, String> wxmap = payService.toPay(order, payType, CommonUtil.getIpAddr(request), comPayInfo, wxOpenid.getOpenid());
                            model.addAttribute("wxpay", wxmap);
                        }
                    } else if (payType.equals(2)) {//支付宝支付

                    }
                    return "pay/topay";
                }
            }
        } catch (Exception ex) {
            logger.error("订单" + id + "支付异常：", ex);
        }
        return null;
    }

    @RequestMapping(value = "/wxcallback", method = RequestMethod.POST)
    public String wxcallback(Model model, HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, String> respData = payService.convertWXCallback(request.getInputStream());
            String resXml = payService.wxcallback(respData);
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            out.write(resXml.getBytes());
            out.flush();
            out.close();
        } catch(Exception ex) {
            logger.error("微信支付回调异常：", ex);
            return null;
        }
        return null;
    }


    @RequestMapping(value = "/alicallback", method = RequestMethod.POST)
    public String alicallback(Model model, HttpServletRequest request, HttpServletResponse response) {
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5433/zldetc",
                    "postgres", "123456");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
        return null;
    }
}
