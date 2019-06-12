package com.navercorp.shopping.demo.order.mapper;

import com.navercorp.shopping.demo.commons.domain.order.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @author HyunGil Jeong
 */
@Mapper
@Component
public interface OrderMapper {

    Order selectOrder(@Param("orderId") String orderId);

    Order selectOrderForUpdate(@Param("orderId") String orderId);

    void insertOrder(Order order);

    void updateOrderComplete(@Param("orderId") String orderId);

    void updateOrderNotComplete(@Param("orderId") String orderId, @Param("delay") int delaySeconds);
}
