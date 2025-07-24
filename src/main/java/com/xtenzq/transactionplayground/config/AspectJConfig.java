package com.xtenzq.transactionplayground.config;

import static com.xtenzq.transactionplayground.utils.Constants.ASPECTJ_PROFILE;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@Profile(ASPECTJ_PROFILE)
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
public class AspectJConfig {

}
