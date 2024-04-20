package com.rematec.voucher.voucherbackapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ConfigCors {

    private static final String[] HEADERS = { "Origin", "Content-Type", "X-Requested-With", "Accept", "Authorization"};
    @Bean
    public WebMvcConfigurer corsLocalConfig() {


        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200")
                        .allowedMethods("GET", "PUT", "OPTIONS", "POST", "DELETE", "PATCH")
                        .maxAge(900)
                        .allowCredentials(true)
                        .allowedHeaders(HEADERS);

            }
        };
    }
}
