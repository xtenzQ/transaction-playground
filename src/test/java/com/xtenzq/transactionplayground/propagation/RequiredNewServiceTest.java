package com.xtenzq.transactionplayground.propagation;

import static com.xtenzq.transactionplayground.propagation.utils.Constants.REQUIRED_NEW_PROFILE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.xtenzq.transactionplayground.base.entity.TransactionLog;
import com.xtenzq.transactionplayground.base.repository.TransactionLogRepository;
import com.xtenzq.transactionplayground.propagation.requirenew.RequireNewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;

@ActiveProfiles(REQUIRED_NEW_PROFILE)
@SpringBootTest
public class RequiredNewServiceTest {

    @Autowired
    private RequireNewService requireNewService;
    @Autowired
    private TransactionLogRepository transactionLogRepository;

    @Test
    void testRequiredNew() {
        try {
            requireNewService.outer();
        } catch (RuntimeException e) {
            // expected
        }

        List<TransactionLog> innerLogs = transactionLogRepository.findAll().stream()
                .filter(log -> "INNER".equals(log.getStatus()))
                .toList();
        List<TransactionLog> outerLogs = transactionLogRepository.findAll().stream()
                .filter(log -> "OUTER".equals(log.getStatus()))
                .toList();

        assertEquals(1, innerLogs.size(), "INNER log should be committed");
        assertEquals(0, outerLogs.size(), "OUTER log should be rolled back");
    }
}
