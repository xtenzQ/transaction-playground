package com.xtenzq.transactionplayground.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalDelegateService {

    private final AccountService accountService;

    @Transactional
    public void transfer(Long from, Long to, BigDecimal money) {
        log.info("Current transaction name is: \"{}\"", TransactionSynchronizationManager.getCurrentTransactionName());
        // transaction will continue
        accountService.transfer(from, to, money);
    }
}
