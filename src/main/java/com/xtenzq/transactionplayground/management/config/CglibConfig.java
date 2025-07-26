package com.xtenzq.transactionplayground.management.config;

import static com.xtenzq.transactionplayground.management.utils.Constants.CGLIB_PROFILE;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@Profile(CGLIB_PROFILE)
@EnableTransactionManagement(proxyTargetClass = true)// cglib proxy
public class CglibConfig {

}
