package com.xtenzq.transactionplayground.management.config;

import static com.xtenzq.transactionplayground.management.utils.Constants.ASPECTJ_PROFILE;

import com.xtenzq.transactionplayground.base.repository.AccountRepository;
import com.xtenzq.transactionplayground.management.service.AccountService;
import com.xtenzq.transactionplayground.management.service.AccountServiceImpl;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@Profile(ASPECTJ_PROFILE)
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
public class AspectJConfig {

    @Bean
    public AccountService accountService(AccountRepository accountRepository) {
        return new AccountServiceImpl(accountRepository);
    }
}
