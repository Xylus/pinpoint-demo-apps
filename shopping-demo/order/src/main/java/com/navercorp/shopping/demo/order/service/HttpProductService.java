package com.navercorp.shopping.demo.order.service;

import com.navercorp.shopping.demo.commons.ShoppingDemoConstants;
import com.navercorp.shopping.demo.commons.vo.product.ProductInfo;
import com.navercorp.shopping.demo.order.exception.InvalidOrderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Objects;

/**
 * @author HyunGil Jeong
 */
@Service
public class HttpProductService implements ProductService {

    private final RestTemplate restTemplate;

    @Autowired
    public HttpProductService(RestTemplate restTemplate) {
        this.restTemplate = Objects.requireNonNull(restTemplate, "restTemplate must not be null");
    }

    @Override
    public ProductInfo getProductInfo(Integer productId) {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme(ShoppingDemoConstants.SCHEME_PRODUCT)
                .host(ShoppingDemoConstants.HOST_PRODUCT)
                .port(ShoppingDemoConstants.PORT_PRODUCT)
                .path("/products/{productId}")
                .buildAndExpand(productId);
        URI uri = uriComponents.toUri();
        try {
            return restTemplate.getForObject(uri, ProductInfo.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new InvalidOrderException("Unknown product : " + productId);
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to get product info", e);
        }
    }
}
