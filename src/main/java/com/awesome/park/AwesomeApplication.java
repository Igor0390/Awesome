package com.awesome.park;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@OpenAPIDefinition
@ConfigurationPropertiesScan
public class AwesomeApplication {

    public static void main(String[] args) {
        SpringApplication.run(AwesomeApplication.class, args);
    }

}
