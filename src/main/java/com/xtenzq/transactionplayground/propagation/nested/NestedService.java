package com.xtenzq.transactionplayground.propagation.nested;

import static com.xtenzq.transactionplayground.propagation.utils.Constants.NESTED_PROFILE;

import com.xtenzq.transactionplayground.base.entity.TransactionLog;
import com.xtenzq.transactionplayground.base.repository.TransactionLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Profile(NESTED_PROFILE)
public class NestedService {

    private final TransactionLogRepository transactionLogRepository;
    private final ApplicationContext applicationContext;

    @Transactional
    public void outer() {
        TransactionLog logOuter = new TransactionLog(null, null, null, new BigDecimal("100.00"), "OUTER", LocalDateTime.now());
        transactionLogRepository.save(logOuter);

        var proxy = applicationContext.getBean(NestedService.class);
        try {
            proxy.inner();
        } catch (Exception e) {
            // log or handle, but do NOT rethrow
        }
    }

    @Transactional(propagation = Propagation.NESTED)
    public void inner() {
        TransactionLog logInner = new TransactionLog(null, null, null, new BigDecimal("200.00"), "INNER", LocalDateTime.now());
        transactionLogRepository.save(logInner);

        throw new RuntimeException("Forcing rollback in inner");
    }
}
