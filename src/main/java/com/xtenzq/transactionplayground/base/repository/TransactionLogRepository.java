package com.xtenzq.transactionplayground.base.repository;

import com.xtenzq.transactionplayground.base.entity.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {
}

