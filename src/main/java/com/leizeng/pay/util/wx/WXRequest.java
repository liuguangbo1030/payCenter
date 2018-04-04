package com.leizeng.pay.util.wx;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @date 18/1/27 下午10:10
 */
public class WXRequest {

    private static final Logger logger = LoggerFactory.getLogger(WXRequest.class);

    /**
     * 统一下单
     * @param reqData
     * @param timeout
     * @param key
     * @param url
     * @return
     * @throws Exception
     */
    public static Map<String, String> unifiedOrder(Map<String, String> reqData, int timeout, String key, String url) throws Exception {
        reqData = fillRequestData(reqData, key);
        String reqBody = WXPayUtil.mapToXml(reqData);
        logger.info("请求微信支付信息：" + reqBody);
        String resXml = request(url, reqData.get("mch_id"), reqBody, timeout);
        return processResponseXml(resXml, key);
    }

    public static Map<String, String> fillRequestData(Map<String, String> reqData, String key) throws Exception {
        reqData.put("nonce_str", WXPayUtil.generateUUID());
        reqData.put("sign_type", "MD5");
        reqData.put("sign", WXPayUtil.generateSignature(reqData, key, WXPayUtil.SignType.MD5));
        return reqData;
    }

    private static String request(String url, String mchID, String data, int timeout) throws Exception {
        Exception exception = null;
        long reqTime = 0;
        long startTimestampMs = WXPayUtil.getCurrentTimestampMs();
        try {
            BasicHttpClientConnectionManager connManager = new BasicHttpClientConnectionManager(
                    RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("http", PlainConnectionSocketFactory.getSocketFactory())
                            .register("https", SSLConnectionSocketFactory.getSocketFactory())
                            .build(),
                    null,
                    null,
                    null
            );
            HttpClient httpClient = HttpClientBuilder.create().setConnectionManager(connManager).build();
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout)
                    .setConnectTimeout(timeout).build();
            httpPost.setConfig(requestConfig);
            StringEntity postEntity = new StringEntity(data, "UTF-8");
            httpPost.addHeader("Content-Type", "text/xml");
            // TODO: 很重要，用来检测 sdk 的使用情况，要不要加上商户信息？
            httpPost.addHeader("User-Agent", "wxpay sdk java v1.0 " + mchID);
            httpPost.setEntity(postEntity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            String result = EntityUtils.toString(httpEntity, "UTF-8");
            reqTime = WXPayUtil.getCurrentTimestampMs()-startTimestampMs;
            logger.info("reqtime:{}, req return message{}", reqTime, result);
            return result;
        }
        catch (UnknownHostException ex) {
            //dns 解析错误，或域名不存在
            exception = ex;
            reqTime = WXPayUtil.getCurrentTimestampMs()-startTimestampMs;
            logger.error("reqtime:{}, UnknownHostException for dataInfo {}", reqTime, data);
        }
        catch (ConnectTimeoutException ex) {
            exception = ex;
            reqTime = WXPayUtil.getCurrentTimestampMs()-startTimestampMs;
            logger.error("reqtime:{}, connect timeout happened for dataInfo {}", reqTime, data);
        }
        catch (SocketTimeoutException ex) {
            exception = ex;
            reqTime = WXPayUtil.getCurrentTimestampMs()-startTimestampMs;
            WXPayUtil.getLogger().warn("reqtime: {}, timeout happened for dataInfo {}", reqTime, data);
        } catch (Exception ex) {
            exception = ex;
            reqTime = WXPayUtil.getCurrentTimestampMs()-startTimestampMs;
            logger.error("reqtime: {}, error: {}", reqTime, exception);
        }
        throw exception;
    }

    /**
     * 处理 HTTPS API返回数据，转换成Map对象。return_code为SUCCESS时，验证签名。
     * @param xmlStr API返回的XML格式数据
     * @return Map类型数据
     * @throws Exception
     */
    public static Map<String, String> processResponseXml(String xmlStr, String key) throws Exception {
        String RETURN_CODE = "return_code";
        String return_code;
        Map<String, String> respData = WXPayUtil.xmlToMap(xmlStr);
        if (respData.containsKey(RETURN_CODE)) {
            return_code = respData.get(RETURN_CODE);
        } else {
            throw new Exception(String.format("No `return_code` in XML: %s", xmlStr));
        }

        if (return_code.equals("FAIL")) {
            return respData;
        } else if (return_code.equals("SUCCESS")) {
            if (WXPayUtil.isSignatureValid(respData, key, WXPayUtil.SignType.MD5)) {
                return respData;
            } else {
                throw new Exception(String.format("Invalid sign value in XML: %s", xmlStr));
            }
        } else {
            throw new Exception(String.format("return_code value %s is invalid in XML: %s", return_code, xmlStr));
        }
    }
}
