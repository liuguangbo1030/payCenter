package com.leizeng.pay.service;

import com.leizeng.pay.mapper.CarInfoMapper;
import com.leizeng.pay.mapper.ComWorksiteMapper;
import com.leizeng.pay.mapper.OrderMapper;
import com.leizeng.pay.entity.CarInfo;
import com.leizeng.pay.entity.ComPass;
import com.leizeng.pay.entity.ComWorksite;
import com.leizeng.pay.entity.Order;
import com.leizeng.pay.util.CommonUtil;
import com.leizeng.pay.mapper.ComPassMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cloudy
 * @version 1.0
 * @date 18/1/23 下午9:03
 */
@Component
public class OrderService {
    @Autowired
    private CarInfoMapper carInfoMapper;

    @Autowired
    private ComPassMapper comPassMapper;

    @Autowired
    private ComWorksiteMapper comWorksiteMapper;

    @Autowired
    private OrderMapper orderMapper;

    private Logger logger = LoggerFactory.getLogger(OrderService.class);

    /**
     * 查询设备信息
     * @param deviceId 设备id
     * @return
     */
    public ComPass getComPass(Integer deviceId) {
        if(null == deviceId || 0 >= deviceId)
            return null;
        ComPass comPass = comPassMapper.findComPass(deviceId);
        logger.info("查询设备信息的设备id：{},设备信息：{}", deviceId, comPass);
        return comPass;
    }

    /**
     * 查询工作站信息
     * @param worksiteId 工作站id
     * @return
     */
    public ComWorksite getComWorksite(Integer worksiteId) {
        if(null == worksiteId || 0 >= worksiteId)
            return null;
        ComWorksite comWorksite = comWorksiteMapper.findComWorksite(worksiteId);
        logger.info("查询工作站信息的工作站id：{},工作站信息：{}", worksiteId, comWorksite);
        return comWorksite;
    }

    /**
     * 车牌信息
     * @param carNumber
     * @return
     */
    public CarInfo getCarInfo(String carNumber) {
        if(CommonUtil.isStrEmpty(carNumber))
            return null;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("car_number", carNumber);
        CarInfo carInfo = carInfoMapper.findCarInfo(map);
        logger.info("查询车牌信息的车牌号：{},车牌信息：{}", carNumber, carInfo);
        return carInfo;
    }

    /**
     * 添加订单
     * @param order
     * @return
     */
    public Integer addOrder(Order order) {
        if(null == order)
            return null;
        logger.info("添加的订单信息：{}", order.toString());
        orderMapper.addOrder(order);
        return order.getId();
    }

    /**
     * 查询订单
     * @param filter
     * @return
     */
    public Order findOrder(Map<String, Object> filter) {
        if(null == filter)
            return null;
        Order order = orderMapper.findOrder(filter);
        logger.info("查询订单信息的条件：{},订单信息：{}", filter, order);
        return order;
    }

    /**
     * 修改订单
     * @param order
     * @return
     */
    public Integer updateOrderStateById(Order order) {
        if(null == order)
            return null;
        Integer flag = orderMapper.updateOrderStateById(order);
        logger.info("修改订单信息的订单信息：{}，订单修改状态：{}", order, flag);
        return flag;
    }

}
