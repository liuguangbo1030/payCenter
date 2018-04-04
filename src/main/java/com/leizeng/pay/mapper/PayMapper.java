package com.leizeng.pay.mapper;

import com.leizeng.pay.entity.Pay;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author cloudy
 * @version 1.0
 * @date 18/1/27 上午10:14
 */
@Mapper
public interface PayMapper {
    Pay findPayByPayid(String unqid);
    void addPay(Pay pay);
    void updatePayByPayId(Pay pay);
}
