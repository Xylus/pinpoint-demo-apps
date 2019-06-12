package com.navercorp.shopping.demo.batch.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author HyunGil Jeong
 */
@Mapper
@Component
public interface OrderMapper {

    void deleteAllOrders();
}
