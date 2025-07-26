package com.xtenzq.transactionplayground.management.service;

import com.xtenzq.transactionplayground.base.exception.InsufficientFundsException;
import com.xtenzq.transactionplayground.base.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public void transfer(Long from, Long to, BigDecimal amount) {
        log.info("Current transaction name is: \"{}\"", TransactionSynchronizationManager.getCurrentTransactionName());

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
    }

    @Override
    @Transactional
    public BigDecimal getBalance(Long accountId) {
        var account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountId));
        return account.getBalance();
    }
}
