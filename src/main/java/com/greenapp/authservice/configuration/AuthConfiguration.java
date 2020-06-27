package com.greenapp.authservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AuthConfiguration {


    public static String CLIENT_SERVICE_URI = "https://greenapp-client-provider.herokuapp.com/client-provider/client";
    public static String CLIENT_SERVICE_URI_local = "localhost:8080/client-provider/client";

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
