package com.leizeng.pay.mapper;

import com.leizeng.pay.entity.PayCallback;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author cloudy
 * @version 1.0
 * @date 18/2/6 上午10:50
 */
@Mapper
public interface PayCallbackMapper {
    void addPayCallback(PayCallback payCallback);
}
