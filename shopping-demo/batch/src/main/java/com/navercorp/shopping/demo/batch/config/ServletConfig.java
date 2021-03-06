package com.navercorp.shopping.demo.batch.config;

import com.navercorp.shopping.demo.commons.ShoppingDemoConstants;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

/**
 * @author HyunGil Jeong
 */
@Configuration
public class ServletConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.setPort(ShoppingDemoConstants.PORT_VENDOR);
        factory.setContextPath("");
    }
}
