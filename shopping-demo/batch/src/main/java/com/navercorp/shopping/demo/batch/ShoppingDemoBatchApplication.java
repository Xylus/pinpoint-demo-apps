package com.navercorp.shopping.demo.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author HyunGil Jeong
 */
@SpringBootApplication
@EnableScheduling
public class ShoppingDemoBatchApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ShoppingDemoBatchApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(ShoppingDemoBatchApplication.class, args);
    }
}
