package com.xtenzq.transactionplayground.repository;

import com.xtenzq.transactionplayground.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    // JpaRepository already provides save(), findById(), deleteAll(), etc.
    // You can add custom query methods here if needed
}
