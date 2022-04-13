package com.cognive.projects.casernkb;

import com.cognive.projects.casernkb.delegate.ObjectDiff;
import com.prime.db.rnkb.model.Bank;
import com.prime.db.rnkb.model.Payment;
import lombok.SneakyThrows;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.extension.mockito.delegate.DelegateExecutionFake;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

public class ObjectDiffTest {
    @Test
    @SneakyThrows
    void check() {

        Bank b = new Bank();
        b.setName("bank1");

        Bank bb = new Bank();
        bb.setName("bank1");

        Bank b2 = new Bank();
        b2.setName("bank2");
        b2.setBic("123423424");

        Payment p = new Payment();
        p.setName("dssdff");
        p.setAmount(BigDecimal.valueOf(123));
        p.setBankPayeeId(b);

        Payment p2 = new Payment();
        p2.setName("dssdff");
        p2.setAmount(BigDecimal.valueOf(123));
        p2.setBankPayeeId(bb);

        Payment work = new Payment();
        work.setName("Out");
        work.setAmount(BigDecimal.valueOf(123));
        work.setBankPayeeId(b2);

        DelegateExecution execution = new DelegateExecutionFake();
        execution.setVariable("diffWork", work);
        execution.setVariable("diffBase", p2);

        ObjectDiff diff = new ObjectDiff();
        diff.execute(execution);

        Set<String> diffResult = (Set<String>) execution.getVariable("diffPaths");

        Assertions.assertEquals(4, diffResult.size());
        Assertions.assertTrue(diffResult.contains("name"));
        Assertions.assertTrue(diffResult.contains("bankPayeeId"));
        Assertions.assertTrue(diffResult.contains("bankPayeeId.name"));
        Assertions.assertTrue(diffResult.contains("bankPayeeId.bic"));
    }

    @Test
    void t() {
        LocalDateTime t = LocalDateTime.now();

        LocalDateTime dd = LocalDateTime.of(LocalDate.now(), LocalTime.of(17, 0));


        Assertions.assertEquals(17, dd.getHour());
    }

}
