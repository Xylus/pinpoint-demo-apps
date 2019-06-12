package com.navercorp.shopping.demo.payment.service;

import com.navercorp.shopping.demo.commons.ShoppingDemoConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Objects;

/**
 * @author HyunGil Jeong
 */
@Service
public class VendorPaymentService implements PaymentService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RestTemplate restTemplate;

    public VendorPaymentService(RestTemplate restTemplate) {
        this.restTemplate = Objects.requireNonNull(restTemplate, "restTemplate must not be null");
    }

    @Override
    public boolean makePayment(Long amount) {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme(ShoppingDemoConstants.SCHEME_VENDOR)
                .host(ShoppingDemoConstants.HOST_VENDOR)
                .port(ShoppingDemoConstants.PORT_VENDOR)
                .path("/payment")
                .build();
        URI uri = uriComponents.toUri();
        Boolean paymentSuccessful = restTemplate.postForObject(uri, null, Boolean.class);
        return paymentSuccessful;
    }
}
