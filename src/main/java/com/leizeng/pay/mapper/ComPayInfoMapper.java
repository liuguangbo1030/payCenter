package com.leizeng.pay.mapper;

import com.leizeng.pay.entity.ComPayInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author cloudy
 * @version 1.0
 * @date 18/2/6 上午10:38
 */
@Mapper
public interface ComPayInfoMapper {
    ComPayInfo findComPayInfoByComid(@Param("comid") Integer comid, @Param("type") Integer type);
}
