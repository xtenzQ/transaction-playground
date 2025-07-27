package com.xtenzq.transactionplayground.management.service;

import java.math.BigDecimal;

public interface AccountService {

    void transfer(Long from, Long to, BigDecimal money);

    BigDecimal getBalance(Long account);
}
