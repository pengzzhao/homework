package com.homework.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class HwSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(HwSearchApplication.class, args);
    }

}

