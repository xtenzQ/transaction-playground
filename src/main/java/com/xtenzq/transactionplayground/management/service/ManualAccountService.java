package com.xtenzq.transactionplayground.management.service;

import com.xtenzq.transactionplayground.util.service.MethodContext;
import com.xtenzq.transactionplayground.util.service.TransactionTracker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@RequiredArgsConstructor
public class ManualAccountService {

    private final PlatformTransactionManager transactionManager;

    public void execute() {
        var transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setName("manualTransaction");
        var transaction = transactionManager.getTransaction(transactionDefinition);
        try {
            TransactionTracker.push(
                    new MethodContext(transactionDefinition.getName(),
                            this.getClass().getMethod("execute"),
                            this.getClass(),
                            false,
                            TransactionTracker.isTransactionActive()
                                    ? TransactionTracker.peek().transactionId().equals(
                                    TransactionSynchronizationManager.getCurrentTransactionName()
                            ) ? null : TransactionTracker.peek() : null)
            );
            log.info("Transaction started...");
        } catch (Exception e) {
            transactionManager.rollback(transaction);
        } finally {
            transactionManager.commit(transaction);
            log.info("Transaction committed.");
        }
    }
}
