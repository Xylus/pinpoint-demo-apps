package com.navercorp.shopping.demo.product.service;

import com.navercorp.shopping.demo.commons.domain.product.Product;
import com.navercorp.shopping.demo.commons.vo.product.ProductInfo;
import com.navercorp.shopping.demo.product.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author HyunGil Jeong
 */
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;

    @Autowired
    public ProductServiceImpl(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    @Override
    public List<ProductInfo> getAllProducts() {
        List<Product> products = productMapper.selectAllProducts();
        if (CollectionUtils.isEmpty(products)) {
            return Collections.emptyList();
        }
        return products.stream()
                .filter(Objects::nonNull)
                .map(ProductInfo::fromProduct)
                .collect(Collectors.toList());
    }

    @Override
    public ProductInfo getProduct(Integer productId) {
        Product product = productMapper.selectProductById(productId);
        return ProductInfo.fromProduct(product);
    }
}
