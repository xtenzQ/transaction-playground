package com.xtenzq.transactionplayground.propagation.supports;

import static com.xtenzq.transactionplayground.propagation.utils.Constants.SUPPORTS_PROFILE;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Profile(SUPPORTS_PROFILE)
public class SupportsService {

    private final ApplicationContext applicationContext;

    @Transactional
    public void outer() {
        var proxy = applicationContext.getBean(SupportsService.class);
        proxy.inner();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void inner() {

    }

    public void noTransaction() {
        var proxy = applicationContext.getBean(SupportsService.class);
        proxy.inner();
    }
}
