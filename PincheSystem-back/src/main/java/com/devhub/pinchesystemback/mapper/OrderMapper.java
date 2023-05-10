package com.devhub.pinchesystemback.mapper;

import com.devhub.pinchesystemback.domain.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wak
 */
@Mapper
public interface OrderMapper {

    Long insert(Order order);

    int deleteByOrderId(Long id);

    int updateByOrderId(Order order);

    Order getOrderByOrderId(Long orderId);

    List<Order> selectAll();

    List<Order> selectAllByInfoId(Long infoId);
}
