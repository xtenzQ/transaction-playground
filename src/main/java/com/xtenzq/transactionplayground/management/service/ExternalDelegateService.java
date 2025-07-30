package com.xtenzq.transactionplayground.management.service;

import static com.xtenzq.transactionplayground.management.utils.Constants.DELEGATE_PROFILE;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@Profile(DELEGATE_PROFILE)
public class ExternalDelegateService {

    private final AccountServiceImpl accountService;

    @Transactional
    public void transfer(Long from, Long to, BigDecimal money) {
        accountService.transfer(from, to, money);
    }
}
