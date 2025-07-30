package com.xtenzq.transactionplayground.propagation;

import static com.xtenzq.transactionplayground.propagation.utils.Constants.REQUIRED_PROFILE;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Profile(REQUIRED_PROFILE)
@RequiredArgsConstructor
public class RequiredService implements BaseService {

    private final ApplicationContext applicationContext;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void outer() {
        var proxy = applicationContext.getBean(RequiredService.class);
        proxy.inner();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void inner() {

    }
}
