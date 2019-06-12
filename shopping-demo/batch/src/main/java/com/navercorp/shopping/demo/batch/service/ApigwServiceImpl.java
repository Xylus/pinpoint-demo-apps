package com.navercorp.shopping.demo.batch.service;

import com.navercorp.shopping.demo.commons.ShoppingDemoConstants;
import com.navercorp.shopping.demo.commons.vo.order.OrderCreateParam;
import com.navercorp.shopping.demo.commons.vo.order.OrderInfo;
import com.navercorp.shopping.demo.commons.vo.order.OrderPaymentParam;
import com.navercorp.shopping.demo.commons.vo.product.ProductInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author HyunGil Jeong
 */
@Service
public class ApigwServiceImpl implements ApigwService {

    private final RestTemplate restTemplate;

    @Autowired
    public ApigwServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = Objects.requireNonNull(restTemplate, "restTemplate must not be null");
    }

    @Override
    public List<ProductInfo> getShoppingProducts() {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme(ShoppingDemoConstants.SCHEME_APIGW)
                .host(ShoppingDemoConstants.HOST_APIGW)
                .port(ShoppingDemoConstants.PORT_APIGW)
                .path("/v1/shopping/products")
                .build();
        URI uri = uriComponents.toUri();
        return restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ProductInfo>>() {
                }).getBody();
    }

    @Override
    public OrderInfo createShoppingOrder(List<ProductInfo> products) {
        List<Integer> productIds = products.stream()
                .map(ProductInfo::getProductId)
                .collect(Collectors.toList());
        OrderCreateParam orderCreateParam = new OrderCreateParam();
        orderCreateParam.setProductIds(productIds);
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme(ShoppingDemoConstants.SCHEME_APIGW)
                .host(ShoppingDemoConstants.HOST_APIGW)
                .port(ShoppingDemoConstants.PORT_APIGW)
                .path("/v1/shopping/orders")
                .build();
        URI uri = uriComponents.toUri();
        return restTemplate.postForObject(uri, orderCreateParam, OrderInfo.class);
    }

    @Override
    public OrderInfo getShoppingOrder(String orderId) {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme(ShoppingDemoConstants.SCHEME_APIGW)
                .host(ShoppingDemoConstants.HOST_APIGW)
                .port(ShoppingDemoConstants.PORT_APIGW)
                .path("/v1/shopping/orders/{orderId}")
                .buildAndExpand(orderId);
        URI uri = uriComponents.toUri();
        return restTemplate.getForObject(uri, OrderInfo.class);
    }

    @Override
    public Boolean processShoppingPayment(String orderId, Long paymentAmount) {
        OrderPaymentParam orderPaymentParam = new OrderPaymentParam();
        orderPaymentParam.setPaymentAmount(paymentAmount);
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme(ShoppingDemoConstants.SCHEME_APIGW)
                .host(ShoppingDemoConstants.HOST_APIGW)
                .port(ShoppingDemoConstants.PORT_APIGW)
                .path("/v1/shopping/orders/{orderId}")
                .buildAndExpand(orderId);
        URI uri = uriComponents.toUri();
        return restTemplate.patchForObject(uri, orderPaymentParam, Boolean.class);
    }
}
