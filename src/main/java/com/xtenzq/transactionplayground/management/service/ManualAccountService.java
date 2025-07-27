package com.xtenzq.transactionplayground.management.service;

import com.xtenzq.transactionplayground.base.entity.Account;
import com.xtenzq.transactionplayground.base.exception.InsufficientFundsException;
import com.xtenzq.transactionplayground.base.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
public class ManualAccountService {

    private final PlatformTransactionManager transactionManager;
    private final AccountRepository accountRepository;

    public void transfer(Long from, Long to, BigDecimal money) {
        var transaction = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            log.info("Current transaction name is: \"{}\"", TransactionSynchronizationManager.getCurrentTransactionName());
            var fromAccount = accountRepository.findById(from)
                    .orElseThrow(() -> new RuntimeException("Account not found: " + from));
            var toAccount = accountRepository.findById(to)
                    .orElseThrow(() -> new RuntimeException("Account not found: " + to));

            if (fromAccount.getBalance().compareTo(money) < 0) {
                throw new InsufficientFundsException("Not enough money");
            }

            saveTransfer(fromAccount, toAccount, money);

            transactionManager.commit(transaction);
        } catch (Exception e) {
            transactionManager.rollback(transaction);
            throw e;
        }
    }

    private void saveTransfer(Account fromAccount, Account toAccount, BigDecimal money) {
        log.info("Current transaction name is: \"{}\"", TransactionSynchronizationManager.getCurrentTransactionName());
        fromAccount.setBalance(fromAccount.getBalance().subtract(money));
        toAccount.setBalance(toAccount.getBalance().add(money));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }

    public BigDecimal getBalance(Long account) {
        return accountRepository.findById(account)
                .map(Account::getBalance)
                .orElseThrow(() -> new RuntimeException("Account not found: " + account));
    }
}
