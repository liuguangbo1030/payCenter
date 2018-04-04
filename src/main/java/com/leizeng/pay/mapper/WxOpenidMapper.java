package com.leizeng.pay.mapper;

import com.leizeng.pay.entity.WxOpenid;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * @author cloudy
 * @version 1.0
 * @date 18/1/31 下午10:04
 */
@Mapper
public interface WxOpenidMapper {
    WxOpenid findWxOpenid(Map<String, String> filter);
    void addWxOpenid(WxOpenid wxOpenid);
}
