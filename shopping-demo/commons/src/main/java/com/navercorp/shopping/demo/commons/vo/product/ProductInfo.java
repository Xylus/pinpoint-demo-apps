package com.navercorp.shopping.demo.commons.vo.product;

import com.navercorp.shopping.demo.commons.domain.product.Product;

/**
 * @author HyunGil Jeong
 */
public class ProductInfo {

    private int productId;
    private String productName;
    private long productPrice;

    public static ProductInfo fromProduct(Product product) {
        if (product == null) {
            return null;
        }
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId(product.getProductId());
        productInfo.setProductName(product.getProductName());
        productInfo.setProductPrice(product.getProductPrice());
        return productInfo;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public long getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(long productPrice) {
        this.productPrice = productPrice;
    }
}
