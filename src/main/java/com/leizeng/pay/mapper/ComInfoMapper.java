package com.leizeng.pay.mapper;

import com.leizeng.pay.entity.ComInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author cloudy
 * @version 1.0
 * @date 18/1/29 下午5:58
 */
@Mapper
public interface ComInfoMapper {
    ComInfo findComInfoById(@Param("id") Integer id);
}
