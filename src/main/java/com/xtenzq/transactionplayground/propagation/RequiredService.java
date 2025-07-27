package com.xtenzq.transactionplayground.propagation;

import static com.xtenzq.transactionplayground.propagation.utils.Constants.REQUIRED_PROFILE;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;

@Profile(REQUIRED_PROFILE)
@RequiredArgsConstructor
public class RequiredService implements BaseService {

    private final ApplicationContext applicationContext;

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED)
    public void outer() {
        var proxy = applicationContext.getBean(RequiredService.class);
        proxy.inner();
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED)
    public void inner() {

    }
}
