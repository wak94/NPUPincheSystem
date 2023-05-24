package com.devhub.pinchesystemback.service;

import com.devhub.pinchesystemback.domain.Info;
import com.devhub.pinchesystemback.domain.User;

import java.util.Date;
import java.util.List;

/**
 * @author wak
 */
public interface AdminService {

    /**
     * 查询一段时间内所有拼车订单或者某位车主的所有拼车订单
     *
     * @param begin   起始时间
     * @param end     截止时间
     * @param ownerId 车主id
     * @return 拼车信息列表
     */
    List<Info> getAllInfosByTime(Date begin, Date end, Long ownerId);

    /**
     * 生成一段时间内的车主排名
     *
     * @param begin 起始时间
     * @param end   截止时间
     * @return 车主排名列表
     */
    List<User> getOwnerRank(Date begin, Date end);
}
