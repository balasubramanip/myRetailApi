package com.target.myretail;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.server.adapter.AbstractReactiveWebInitializer;

@SpringBootApplication
public class ProductApplication extends AbstractReactiveWebInitializer {
    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class,
                              args);
    }

    @Override
    protected Class<?>[] getConfigClasses() {
        return new Class[]{ProductApplication.class};

    }
}
