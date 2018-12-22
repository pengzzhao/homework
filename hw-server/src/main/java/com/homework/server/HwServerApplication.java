package com.homework.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class HwServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HwServerApplication.class, args);
    }

}

