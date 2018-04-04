package com.leizeng.pay.mapper;

import com.leizeng.pay.entity.CarInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * @author cloudy
 * @version 1.0
 * @date 18/1/22 下午2:45
 */
@Mapper
public interface CarInfoMapper {
    CarInfo findCarInfo(Map<String, Object> filter);
}
