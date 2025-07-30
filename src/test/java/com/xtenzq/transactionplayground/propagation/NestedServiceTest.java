package com.xtenzq.transactionplayground.propagation;

import static com.xtenzq.transactionplayground.propagation.utils.Constants.NESTED_PROFILE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.xtenzq.transactionplayground.base.entity.TransactionLog;
import com.xtenzq.transactionplayground.base.repository.TransactionLogRepository;
import com.xtenzq.transactionplayground.propagation.nested.NestedService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;

@SpringBootTest
@ActiveProfiles(NESTED_PROFILE)
public class NestedServiceTest {

    @Autowired
    private NestedService nestedService;

    @Autowired
    private TransactionLogRepository transactionLogRepository;

    @Test
    void testNestedPropagation() {
        try {
            nestedService.outer();
        } catch (RuntimeException e) {
            // expected
        }

        List<TransactionLog> innerLogs = transactionLogRepository.findAll().stream()
                .filter(log -> "INNER".equals(log.getStatus()))
                .toList();
        List<TransactionLog> outerLogs = transactionLogRepository.findAll().stream()
                .filter(log -> "OUTER".equals(log.getStatus()))
                .toList();

        assertEquals(0, innerLogs.size(), "INNER log should be rolled back");
        assertEquals(1, outerLogs.size(), "OUTER log should be committed");
    }
}
