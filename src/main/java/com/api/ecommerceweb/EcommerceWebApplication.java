package com.api.ecommerceweb;

import com.api.ecommerceweb.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({FileStorageProperties.class})
public class EcommerceWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceWebApplication.class, args);
    }

}
