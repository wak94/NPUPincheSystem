package com.devhub.pinchesystemback.service.impl;

import com.devhub.pinchesystemback.domain.Info;
import com.devhub.pinchesystemback.domain.Order;
import com.devhub.pinchesystemback.domain.User;
import com.devhub.pinchesystemback.mapper.InfoMapper;
import com.devhub.pinchesystemback.mapper.OrderMapper;
import com.devhub.pinchesystemback.mapper.UserMapper;
import com.devhub.pinchesystemback.service.AdminService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author wak
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Resource
    private InfoMapper mapper;

    @Resource
    private RedisTemplate<String, String> template;

    @Resource
    private UserMapper userMapper;

    @Resource
    private OrderMapper orderMapper;

    /**
     * 查询一段时间内所有拼车订单或者某位车主的所有拼车订单
     *
     * @param begin   起始时间
     * @param end     截止时间
     * @param ownerId 车主id
     * @return 拼车信息列表
     */
    @Override
    public List<Info> getAllInfosByTime(Date begin, Date end, Long ownerId) {
        return mapper.selectInfoList(begin, end, ownerId);
    }

    @Override
    public List<Order> getAllRecords(Date begin, Date end, Long ownerId) {
        List<Order> orders = new ArrayList<>();
        if (begin == null || end == null || begin.after(end)) {
            return orders;
        }
        List<Long> infoIds;
        if (ownerId != null) {
            infoIds = mapper.selectInfoIdsByTime(begin, end, ownerId);
        } else {
            infoIds = mapper.selectInfoIdsByTime(begin, end, null);
        }
        return orderMapper.selectByInfoIds(infoIds);
    }

    /**
     * 生成一段时间内的车主排名
     *
     * @param begin 起始时间
     * @param end   截止时间
     * @return 车主排名列表
     */
    @Override
    public List<User> getOwnerRank(Date begin, Date end) {

        // 先查出所有车主
        List<User> owners = userMapper.selectAllOwners();
        ArrayList<User> rank = new ArrayList<>(owners.size());
        Map<Double, User> map = new HashMap<>();
        for (User owner : owners) {
            List<Order> orderList = getAllRecords(begin, end, owner.getId());
            double total = accumulatePrice(orderList);
            map.put(total, owner);
        }

        List<Map.Entry<Double, User>> entries = new ArrayList<>(map.entrySet());
        entries.sort((entry1, entry2) -> entry2.getKey().compareTo(entry1.getKey()));

        for (Map.Entry<Double, User> entry : entries) {
            rank.add(entry.getValue());
        }
        return rank;
    }

    private double accumulatePrice(List<Order> orderList) {
        if (orderList == null || orderList.isEmpty()) {
            return 0d;
        }
        double total = 0d;
        for (Order order : orderList) {
            Double price = order.getPrice();
            if (price != null) {
                total += price;
            }
        }
        return total;
    }
}
