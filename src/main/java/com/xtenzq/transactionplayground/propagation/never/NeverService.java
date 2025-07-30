package com.xtenzq.transactionplayground.propagation.never;

import static com.xtenzq.transactionplayground.propagation.utils.Constants.NEVER_PROFILE;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Profile(NEVER_PROFILE)
public class NeverService {

    private final ApplicationContext applicationContext;

    @Transactional
    public void outer() {
        var proxy = applicationContext.getBean(NeverService.class);
        proxy.inner();
    }

    @Transactional(propagation = Propagation.NEVER)
    public void inner() {

    }

    public void noTransactionMethod() {
        var proxy = applicationContext.getBean(NeverService.class);
        proxy.inner();
    }
}
