package com.xtenzq.transactionplayground.propagation.requirenew;

import static com.xtenzq.transactionplayground.propagation.utils.Constants.REQUIRED_NEW_PROFILE;

import com.xtenzq.transactionplayground.base.entity.TransactionLog;
import com.xtenzq.transactionplayground.base.repository.TransactionLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Profile(REQUIRED_NEW_PROFILE)
public class RequireNewService {

    private final ApplicationContext applicationContext;
    private final TransactionLogRepository transactionLogRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public void outer() {
        TransactionLog logOuter = new TransactionLog(null, null, null, new BigDecimal("100.00"), "OUTER", LocalDateTime.now());
        transactionLogRepository.save(logOuter);

        var proxy = applicationContext.getBean(RequireNewService.class);
        proxy.inner();

        throw new RuntimeException("Forcing rollback in outer");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void inner() {
        TransactionLog logInner = new TransactionLog(null, null, null, new BigDecimal("200.00"), "INNER", LocalDateTime.now());
        transactionLogRepository.save(logInner);
    }
}
