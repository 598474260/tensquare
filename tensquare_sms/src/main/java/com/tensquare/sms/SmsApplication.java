package com.tensquare.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import util.SmsUtils;

@SpringBootApplication
@EnableEurekaClient
public class SmsApplication {

    public static void main(String[] args) {

        SpringApplication.run(SmsApplication.class, args);
    }

    @Bean
    public SmsUtils smsUtils(){
        return new SmsUtils();
    }
}
