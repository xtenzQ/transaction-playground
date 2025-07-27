package com.xtenzq.transactionplayground.management.config;

import static com.xtenzq.transactionplayground.management.utils.Constants.MANUAL_PROFILE;

import com.xtenzq.transactionplayground.management.service.ManualAccountService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.PlatformTransactionManager;

@Profile(MANUAL_PROFILE)
@Configuration
public class ManualConfig {

    @Bean
    public ManualAccountService manualAccountService(PlatformTransactionManager transactionManager) {
        return new ManualAccountService(transactionManager);
    }
}
