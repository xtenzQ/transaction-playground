package com.xtenzq.transactionplayground.service;

import com.xtenzq.transactionplayground.entity.Account;
import com.xtenzq.transactionplayground.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManualAccountService implements AccountService {

    private final PlatformTransactionManager transactionManager;
    private final AccountRepository accountRepository;

    @Override
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

            fromAccount.setBalance(fromAccount.getBalance().subtract(money));
            toAccount.setBalance(toAccount.getBalance().add(money));

            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);

            transactionManager.commit(transaction);
        } catch (Exception e) {
            transactionManager.rollback(transaction);
            throw e;
        }
    }

    @Override
    public BigDecimal getBalance(Long account) {
        return accountRepository.findById(account)
                .map(Account::getBalance)
                .orElseThrow(() -> new RuntimeException("Account not found: " + account));
    }
}
