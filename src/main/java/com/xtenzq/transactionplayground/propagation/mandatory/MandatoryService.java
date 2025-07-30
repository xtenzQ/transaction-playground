package com.xtenzq.transactionplayground.propagation.mandatory;

import static com.xtenzq.transactionplayground.propagation.utils.Constants.MANDATORY_PROFILE;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Profile(MANDATORY_PROFILE)
public class MandatoryService {

    private final ApplicationContext applicationContext;

    @Transactional
    public void outer() {
        var proxy = applicationContext.getBean(MandatoryService.class);
        proxy.inner();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void inner() {

    }

    public void noTransactionMethod() {
        var proxy = applicationContext.getBean(MandatoryService.class);
        proxy.inner();
    }
}
