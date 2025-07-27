package com.xtenzq.transactionplayground.management.config;

import static com.xtenzq.transactionplayground.management.utils.Constants.DELEGATE_PROFILE;

import com.xtenzq.transactionplayground.management.service.FirstService;
import com.xtenzq.transactionplayground.management.service.SecondService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile(DELEGATE_PROFILE)
@Configuration
public class DelegateConfig {

    @Bean
    public FirstService firstService(SecondService secondService) {
        return new FirstService(secondService);
    }

    @Bean
    public SecondService secondService() {
        return new SecondService();
    }
}
