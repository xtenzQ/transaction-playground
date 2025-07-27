package com.xtenzq.transactionplayground.propagation.config;

import com.xtenzq.transactionplayground.propagation.RequiredService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RequiredConfig {

    @Bean
    public RequiredService requiredService(ApplicationContext applicationContext) {
        return new RequiredService(applicationContext);
    }
}
