package com.navercorp.shopping.demo.shopping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author HyunGil Jeong
 */
@SpringBootApplication
public class ShoppingDemoShoppingApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ShoppingDemoShoppingApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(ShoppingDemoShoppingApplication.class, args);
    }
}
