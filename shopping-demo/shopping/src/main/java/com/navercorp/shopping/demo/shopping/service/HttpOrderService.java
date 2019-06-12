package com.navercorp.shopping.demo.shopping.service;

import com.navercorp.shopping.demo.commons.ShoppingDemoConstants;
import com.navercorp.shopping.demo.commons.vo.order.OrderCreateParam;
import com.navercorp.shopping.demo.commons.vo.order.OrderInfo;
import com.navercorp.shopping.demo.commons.vo.order.OrderPaymentParam;
import com.navercorp.shopping.demo.shopping.exception.InvalidOrderException;
import com.navercorp.shopping.demo.shopping.exception.OrderNotFoundException;
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
public class HttpOrderService implements OrderService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RestTemplate restTemplate;

    @Autowired
    public HttpOrderService(RestTemplate restTemplate) {
        this.restTemplate = Objects.requireNonNull(restTemplate, "restTemplate must not be null");
    }

    @Override
    public OrderInfo getOrder(String orderId) {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme(ShoppingDemoConstants.SCHEME_ORDER)
                .host(ShoppingDemoConstants.HOST_ORDER)
                .port(ShoppingDemoConstants.PORT_ORDER)
                .path("/orders/{orderId}")
                .buildAndExpand(orderId);
        URI uri = uriComponents.toUri();
        try {
            return restTemplate.getForObject(uri, OrderInfo.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new OrderNotFoundException(orderId);
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to get order info", e);
        }
    }

    @Override
    public OrderInfo createOrder(List<Integer> productIds) {
        OrderCreateParam orderCreateParam = new OrderCreateParam();
        orderCreateParam.setProductIds(productIds);
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme(ShoppingDemoConstants.SCHEME_ORDER)
                .host(ShoppingDemoConstants.HOST_ORDER)
                .port(ShoppingDemoConstants.PORT_ORDER)
                .path("/orders")
                .build();
        URI uri = uriComponents.toUri();
        try {
            ResponseEntity<Void> responseEntity = restTemplate.postForEntity(uri, orderCreateParam, Void.class);
            URI location = responseEntity.getHeaders().getLocation();
            return restTemplate.getForObject(location, OrderInfo.class);
        } catch (HttpClientErrorException.BadRequest e) {
            throw new InvalidOrderException("Unable to create order");
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to create order", e);
        }
    }

    @Override
    public Boolean processOrder(String orderId, Long paymentAmount) {
        OrderPaymentParam orderPaymentParam = new OrderPaymentParam();
        orderPaymentParam.setPaymentAmount(paymentAmount);
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme(ShoppingDemoConstants.SCHEME_ORDER)
                .host(ShoppingDemoConstants.HOST_ORDER)
                .port(ShoppingDemoConstants.PORT_ORDER)
                .path("/orders/{orderId}")
                .buildAndExpand(orderId);
        URI uri = uriComponents.toUri();
        try {
            HttpEntity<OrderPaymentParam> httpEntity = new HttpEntity<>(orderPaymentParam);
            ResponseEntity<Boolean> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.PATCH,
                    httpEntity,
                    ParameterizedTypeReference.forType(Boolean.class));
            return responseEntity.getBody();
        } catch (HttpClientErrorException.BadRequest e) {
            throw new InvalidOrderException("Unable to process order : " + orderId);
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to process order", e);
        }
    }
}
