package com.navercorp.shopping.demo.product.mapper;

import com.navercorp.shopping.demo.commons.domain.product.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author HyunGil Jeong
 */
@Mapper
@Component
public interface ProductMapper {

    List<Product> selectAllProducts();

    Product selectProductById(@Param("productId") Integer productId);
}
