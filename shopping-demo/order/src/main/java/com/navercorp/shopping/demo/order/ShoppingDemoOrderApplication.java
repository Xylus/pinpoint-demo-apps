package com.navercorp.shopping.demo.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author HyunGil Jeong
 */
@SpringBootApplication
public class ShoppingDemoOrderApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ShoppingDemoOrderApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(ShoppingDemoOrderApplication.class, args);
    }
}
