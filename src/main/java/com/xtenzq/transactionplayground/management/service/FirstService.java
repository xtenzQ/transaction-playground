package com.xtenzq.transactionplayground.management.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class FirstService {

    private final SecondService secondService;

    @Transactional
    public void execute() {
        log.info("FirstService execute");
        secondService.execute();
    }
}
