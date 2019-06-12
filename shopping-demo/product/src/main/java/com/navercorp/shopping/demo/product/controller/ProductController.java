package com.navercorp.shopping.demo.product.controller;

import com.navercorp.shopping.demo.commons.vo.product.ProductInfo;
import com.navercorp.shopping.demo.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * @author HyunGil Jeong
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = Objects.requireNonNull(productService, "productService must not be null");
    }

    @GetMapping
    public List<ProductInfo> getProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductInfo> getProduct(@PathVariable("productId") Integer productId) {
        ProductInfo productInfo = productService.getProduct(productId);
        if (productInfo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productInfo);
    }
}
