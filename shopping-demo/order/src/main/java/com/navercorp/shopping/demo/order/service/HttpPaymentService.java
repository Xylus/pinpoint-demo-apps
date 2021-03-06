package com.navercorp.shopping.demo.order.service;

import com.navercorp.shopping.demo.commons.ShoppingDemoConstants;
import com.navercorp.shopping.demo.commons.vo.payment.PaymentForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
public class HttpPaymentService implements PaymentService {

    private final RestTemplate restTemplate;

    @Autowired
    public HttpPaymentService(RestTemplate restTemplate) {
        this.restTemplate = Objects.requireNonNull(restTemplate, "restTemplate must not be null");
    }

    @Override
    public Boolean makePayment(String orderId, Long paymentAmount) {
        PaymentForm paymentForm = new PaymentForm();
        paymentForm.setAmount(paymentAmount);
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme(ShoppingDemoConstants.SCHEME_PAYMENT)
                .host(ShoppingDemoConstants.HOST_PAYMENT)
                .port(ShoppingDemoConstants.PORT_PAYMENT)
                .path("/pay/{orderId}")
                .buildAndExpand(orderId);
        URI uri = uriComponents.toUri();
        try {
            return restTemplate.postForObject(uri, paymentForm, Boolean.class);
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to call payment server", e);
        }
    }
}
