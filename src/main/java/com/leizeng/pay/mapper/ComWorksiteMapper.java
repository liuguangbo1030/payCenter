package com.leizeng.pay.mapper;

import com.leizeng.pay.entity.ComWorksite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author cloudy
 * @version 1.0
 * 岗亭表
 * @date 18/1/22 下午4:11
 */
@Mapper
public interface ComWorksiteMapper {
    ComWorksite findComWorksite(@Param("id") Integer id);
}
