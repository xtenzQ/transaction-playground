package com.xtenzq.transactionplayground.propagation.notsupported;

import static com.xtenzq.transactionplayground.propagation.utils.Constants.NOT_SUPPORTED_PROFILE;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Profile(NOT_SUPPORTED_PROFILE)
public class NotSupportedService {

    private final ApplicationContext applicationContext;

    @Transactional
    public void outer() {
        var proxy = applicationContext.getBean(NotSupportedService.class);
        proxy.inner();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void inner() {

    }
}
