package com.navercorp.shopping.demo.apigw.service;

import com.navercorp.shopping.demo.commons.ShoppingDemoConstants;
import com.navercorp.shopping.demo.commons.vo.order.OrderCreateParam;
import com.navercorp.shopping.demo.commons.vo.order.OrderInfo;
import com.navercorp.shopping.demo.commons.vo.order.OrderPaymentParam;
import com.navercorp.shopping.demo.commons.vo.product.ProductInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;

/**
 * @author HyunGil Jeong
 */
@Service
public class ShoppingServiceImpl implements ShoppingService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RestTemplate restTemplate;

    @Autowired
    public ShoppingServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = Objects.requireNonNull(restTemplate, "restTemplate must not be null");
    }

    @Override
    public ResponseEntity<List<ProductInfo>> getProducts() {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme(ShoppingDemoConstants.SCHEME_SHOPPING)
                .host(ShoppingDemoConstants.HOST_SHOPPING)
                .port(ShoppingDemoConstants.PORT_SHOPPING)
                .path("/shopping/products")
                .build();
        URI uri = uriComponents.toUri();
        long startTimestamp = System.currentTimeMillis();
        try {
            return restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<ProductInfo>>() {});
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to get products", e);
        } finally {
            logger.info("{} {} complete, took {}ms", HttpMethod.GET, uri, System.currentTimeMillis() - startTimestamp);
        }
    }

    @Override
    public ResponseEntity<OrderInfo> createOrder(OrderCreateParam orderCreateParam) {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme(ShoppingDemoConstants.SCHEME_SHOPPING)
                .host(ShoppingDemoConstants.HOST_SHOPPING)
                .port(ShoppingDemoConstants.PORT_SHOPPING)
                .path("/shopping/orders")
                .build();
        URI uri = uriComponents.toUri();
        long startTimestamp = System.currentTimeMillis();
        try {
            return restTemplate.postForEntity(uri, orderCreateParam, OrderInfo.class);
        } catch (HttpClientErrorException.BadRequest e) {
            return ResponseEntity.badRequest().build();
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to create order", e);
        } finally {
            logger.info("{} {} complete, took {}ms", HttpMethod.POST, uri, System.currentTimeMillis() - startTimestamp);
        }
    }

    @Override
    public ResponseEntity<OrderInfo> getOrder(String orderId) {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme(ShoppingDemoConstants.SCHEME_SHOPPING)
                .host(ShoppingDemoConstants.HOST_SHOPPING)
                .port(ShoppingDemoConstants.PORT_SHOPPING)
                .path("/shopping/orders/{orderId}")
                .buildAndExpand(orderId);
        URI uri = uriComponents.toUri();
        long startTimestamp = System.currentTimeMillis();
        try {
            return restTemplate.getForEntity(uri, OrderInfo.class);
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.notFound().build();
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to get order", e);
        } finally {
            logger.info("{} {} complete, took {}ms", HttpMethod.GET, uri, System.currentTimeMillis() - startTimestamp);
        }
    }

    @Override
    public ResponseEntity<Boolean> processOrder(String orderId, OrderPaymentParam orderPaymentParam) {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme(ShoppingDemoConstants.SCHEME_SHOPPING)
                .host(ShoppingDemoConstants.HOST_SHOPPING)
                .port(ShoppingDemoConstants.PORT_SHOPPING)
                .path("/shopping/orders/{orderId}")
                .buildAndExpand(orderId);
        URI uri = uriComponents.toUri();
        long startTimestamp = System.currentTimeMillis();
        try {

            HttpEntity<OrderPaymentParam> httpEntity = new HttpEntity<>(orderPaymentParam);
            return restTemplate.exchange(
                    uri,
                    HttpMethod.PATCH,
                    httpEntity,
                    ParameterizedTypeReference.forType(Boolean.class));
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.notFound().build();
        } catch (HttpClientErrorException.BadRequest e) {
            return ResponseEntity.badRequest().build();
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to create order", e);
        } finally {
            logger.info("{} {} complete, took {}ms", HttpMethod.PATCH, uri, System.currentTimeMillis() - startTimestamp);
        }
    }
}
