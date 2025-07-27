package com.xtenzq.transactionplayground.management.service;

import static com.xtenzq.transactionplayground.management.utils.Constants.SELF_PROFILE;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Profile(SELF_PROFILE)
@RequiredArgsConstructor
public class SelfInjectionAccountService {

    private final ApplicationContext applicationContext;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String outer() {
        var transactionName = TransactionSynchronizationManager.getCurrentTransactionName();
        SelfInjectionAccountService proxy = applicationContext.getBean(SelfInjectionAccountService.class);
        proxy.inner();
        return transactionName;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void inner() {
        log.info("Current transaction name is: \"{}\"", TransactionSynchronizationManager.getCurrentTransactionName());
    }
}
