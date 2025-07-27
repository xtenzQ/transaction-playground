package com.xtenzq.transactionplayground.management.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SecondService {

    @Transactional
    public void execute() {
        log.info("SecondService execute");
    }
}
