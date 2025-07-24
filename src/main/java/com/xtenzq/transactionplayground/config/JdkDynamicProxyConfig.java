package com.xtenzq.transactionplayground.config;

import static com.xtenzq.transactionplayground.utils.Constants.JDK_PROXY_PROFILE;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@Profile(JDK_PROXY_PROFILE)
@EnableTransactionManagement // JDK dynamic proxy
public class JdkDynamicProxyConfig {

}
