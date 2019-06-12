package com.navercorp.shopping.demo.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author HyunGil Jeong
 */
@SpringBootApplication
public class ShoppingDemoProductApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ShoppingDemoProductApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(ShoppingDemoProductApplication.class, args);
    }
}
