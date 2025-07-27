package com.xtenzq.transactionplayground.management.config;

import static com.xtenzq.transactionplayground.management.utils.Constants.SELF_PROFILE;

import com.xtenzq.transactionplayground.management.service.SelfInjectionAccountService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile(SELF_PROFILE)
@Configuration
public class SelfInjectionConfig {

    @Bean
    public SelfInjectionAccountService selfInjectionAccountService(ApplicationContext applicationContext) {
        return new SelfInjectionAccountService(applicationContext);
    }
}
