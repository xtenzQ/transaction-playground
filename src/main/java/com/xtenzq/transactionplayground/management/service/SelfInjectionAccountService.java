package com.xtenzq.transactionplayground.management.service;

import com.xtenzq.transactionplayground.base.exception.InsufficientFundsException;
import com.xtenzq.transactionplayground.base.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class SelfInjectionAccountService {

    private final ApplicationContext applicationContext;
    private final AccountRepository accountRepository;

    public String transfer(Long from, Long to, BigDecimal money) {
        var transactionName = TransactionSynchronizationManager.getCurrentTransactionName();
        log.info("Public transaction name is: \"{}\"", transactionName);
        SelfInjectionAccountService proxy = applicationContext.getBean(SelfInjectionAccountService.class);
        proxy.selfTransfer(from, to, money);
        return transactionName;
    }

    @Transactional
    public String selfTransfer(Long from, Long to, BigDecimal amount) {
        var fromAccount = accountRepository.findById(from)
                .orElseThrow(() -> new RuntimeException("Account not found: " + from));
        var toAccount = accountRepository.findById(to)
                .orElseThrow(() -> new RuntimeException("Account not found: " + to));

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Not enough money");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        var transactionName = TransactionSynchronizationManager.getCurrentTransactionName();
        log.info("Private transfer transaction name is: \"{}\"", transactionName);

        return TransactionSynchronizationManager.getCurrentTransactionName();
    }
}
