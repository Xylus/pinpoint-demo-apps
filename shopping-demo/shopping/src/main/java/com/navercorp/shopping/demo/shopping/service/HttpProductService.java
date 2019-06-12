package com.navercorp.shopping.demo.shopping.service;

import com.navercorp.shopping.demo.commons.ShoppingDemoConstants;
import com.navercorp.shopping.demo.commons.vo.product.ProductInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
public class HttpProductService implements ProductService {

    private final RestTemplate restTemplate;

    @Autowired
    public HttpProductService(RestTemplate restTemplate) {
        this.restTemplate = Objects.requireNonNull(restTemplate, "restTemplate must not be null");
    }

    @Override
    public List<ProductInfo> getProductInfos() {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme(ShoppingDemoConstants.SCHEME_PRODUCT)
                .host(ShoppingDemoConstants.HOST_PRODUCT)
                .port(ShoppingDemoConstants.PORT_PRODUCT)
                .path("/products")
                .build();
        URI uri = uriComponents.toUri();
        try {
            ResponseEntity<List<ProductInfo>> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<ProductInfo>>() {});
            return responseEntity.getBody();
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to get product infos", e);
        }
    }
}
