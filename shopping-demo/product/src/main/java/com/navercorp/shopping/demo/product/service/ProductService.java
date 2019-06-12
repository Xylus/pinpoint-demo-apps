package com.navercorp.shopping.demo.product.service;

import com.navercorp.shopping.demo.commons.vo.product.ProductInfo;

import java.util.List;

/**
 * @author HyunGil Jeong
 */
public interface ProductService {

    List<ProductInfo> getAllProducts();

    ProductInfo getProduct(Integer productId);
}
