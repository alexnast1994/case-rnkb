package com.cognive.projects.casernkb;

import com.cognive.projects.casernkb.delegate.ObjectDiff;
import com.cognive.projects.casernkb.model.PaymentDto;
import com.prime.db.rnkb.model.Bank;
import com.prime.db.rnkb.model.Payment;
import lombok.SneakyThrows;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.extension.mockito.delegate.DelegateExecutionFake;
import org.camunda.spin.plugin.variable.SpinValues;
import org.camunda.spin.plugin.variable.value.JsonValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
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
    @SneakyThrows
    void check2() {
        final String jsonData = "{\"OldPayment\":{\"ID\":162,\"TYPE\":865,\"PAYMENTCATEGORY\":null,\"REQUESTEDSETTLEMENTDATE\":null,\"EXID\":\"2956426178\",\"SOURCESYSTEMS\":null,\"OPERATIONNUMBER\":null,\"PAYERTYPE\":null,\"PAYEETYPE\":null,\"PAYERNAME\":\"Общество с ограниченной ответственностью 'Вистер'\",\"PAYEENAME\":\"Левин Арсений Петрович\",\"PAYERINN\":\"7708388320\",\"PAYEEINN\":\"7728168971\",\"PAYERKPP\":null,\"PAYEEKPP\":null,\"BANKPAYERID\":21,\"BANKPAYEEID\":null,\"PAYEEACCOUNTNUMBER\":\"40817810206960040616\",\"PAYERACCOUNTNUMBER\":\"40702810342950000318\",\"PAYEEADDRESS\":null,\"PAYERADDRESS\":null,\"EXTERNALPAYERID\":null,\"EXTERNALPAYEEID\":null,\"PURPOSE\":\"Взыск. по ИЛ Комиссия по Трудовым Спорам 09 от 16.09.2021  дело № 09  от 16.09.2021 (вз-ль Левин Арсений Петрович) без НДС». Задолженность по заработной плате. Сумма плат.док. 1484529\",\"CURRENCY\":4104,\"EXCHANGERATECB\":null,\"AMOUNT\":1407228.27,\"AMOUNTNATIONALCURRENCY\":1407228.27,\"COMISSION\":null,\"PAYERCARD\":null,\"PAYEECARD\":null,\"ISEMERGENCYPAYMENT\":null,\"ISSALARYPAYMENT\":null,\"PAYMENTSYSTEM\":null,\"PAYEEPHONENUMBER\":null,\"TEMPLATEID\":null,\"TEMPLATENAMEPERSONAL\":null,\"PAYTKINDGROUP\":null,\"PAYERSTATUS\":null,\"BGTRANSFERTYPE\":null,\"BGTRANSFERKBK\":null,\"REASONCODE\":null,\"BGTRANSFEROKTMO\":null,\"BASEBGPAYTYPE\":null,\"BGTRANSFERTAXDATE\":null,\"BGTRANSFERDOCDATE\":null,\"UIP\":null,\"CUSTOMSCODE\":null,\"BGPAYTYPE\":null,\"BGTRANSFERINN\":null,\"BGTRANSFERFIO\":null,\"BGTRANSFERADDRESS\":null,\"BGTRANSFERCOMMENT\":null,\"AMOUNTACCOUNTCURRENCY\":1407228.27,\"TOTALAMOUNT\":1407228.27,\"DATEIN\":\"2021-12-13 10:52:00.0\",\"DATEINSERT\":\"2021-12-13 10:52:00.0\",\"DATEUPDATE\":null,\"SCHEDULEPAYMENT\":null,\"NAME\":null,\"SUBTYPE\":null,\"TIMESTAMPREQUEST\":null,\"VERSION\":null,\"BRANCHBIC\":null,\"USERID\":null,\"CHALLENGEDATE\":null,\"PAYEECLIENTID\":null,\"PAYERCLIENTID\":null,\"BRANCHID\":401,\"BGTRANSFERDOCNUM\":null,\"BANKPAYERCOUNTRY\":null,\"BANKPAYEECOUNTRY\":null,\"CHECKFLAG\":null,\"DATEOPER\":null,\"DOCCREATEDATE\":\"2021-12-13 10:52:00.0\",\"IDENTITYDOCTYPE\":null,\"IDENTITYDOCSERIES\":null,\"IDENTITYDOCNUM\":null,\"IDENTITYDOCISSUEDEP\":null,\"IDENTITYDOCRAW\":null,\"PAYMENTCODE\":null,\"BRANCHCODE\":null,\"BRANCHNAME\":null,\"STATUSHISTORY\":null,\"DTACCOUNTNUMBER\":\"40702810342950000318\",\"KTACCOUNTNUMBER\":\"40817810206960040616\",\"SCORE\":null,\"PAYMENTCREATIONDATE\":null,\"PAYMENTSOURCESTATUS\":875,\"DATE_TRANSACTION_ACT\":\"2021-12-13 10:52:00.0\",\"ISFRAUD\":null,\"CUROPTYPECODE\":null,\"PAYMENTSUBTYPE\":null,\"PAYMENTREFERENCE\":\"2956426178\",\"PAYMENTROOTID\":\"0\",\"BATCHNUMBER\":\"0\",\"STATUSUPDATEDATE\":\"2021-12-13 10:52:00.0\",\"RACEWAYTYPE\":null,\"EXCHANGERATE\":null,\"COMISSIONACCNUMBER\":null,\"OPERATIONMARKER\":null,\"SELLCURCODE\":\"810\",\"SELLCURAMNT\":null,\"IDSESSION\":null,\"PAYERCRDNUMMASKED\":null,\"IDENTITYDOCISSUEDATE\":null,\"PAYEECRDNUMBMASKED\":null,\"PAYMENTCOMMENT\":null,\"BALANCE\":0,\"DOCEDITDATE\":\"2021-12-13 10:52:00.0\",\"PAYEEPHONE\":null,\"PAYEESERVICEID\":null,\"PAYERPHONE\":null},\"NewPayment\":{\"AMOUNT\":1407228.27,\"AMOUNTACCOUNTCURRENCY\":1407228.27,\"AMOUNTNATIONALCURRENCY\":1407228.27,\"BALANCE\":0,\"смотреть таблицу BANK\":[{\"Type\":\"BankPayer\",\"Name\":\"РНКБ БАНК (ПАО)\",\"BIC\":\"043510607\",\"CorrespondentAccount\":\"30101810335100000607\"},{\"Type\":\"BankPayee\",\"Name\":\"АО 'АЛЬФА-БАНК'\",\"BIC\":\"044525593\",\"CorrespondentAccount\":\"30101810200000000593\"}],\"BATCHNUMBER\":\"0\",\"BRANCHID\":\"401\",\"CURRENCY\":\"810\",\"DATEIN\":\"2021-12-13T10:52:00.000Z\",\"DATEINSERT\":\"2021-12-13T10:52:00.000Z\",\"DATE_TRANSACTION_ACT\":\"2021-12-13T10:52:00.000Z\",\"DOCCREATEDATE\":\"2021-12-13T10:52:00.000Z\",\"DOCEDITDATE\":\"2021-12-13T10:52:00.000Z\",\"DTACCOUNTNUMBER\":\"40702810342950000318\",\"IDENTITYDOCISSUEDATE\":\"\",\"KTACCOUNTNUMBER\":\"40817810206960040616\",\"PAYEEACCOUNTNUMBER\":\"40817810206960040616\",\"PAYEEINN\":\"7728168971\",\"PAYEENAME\":\"Левин Арсений Петрович\",\"PAYERACCOUNTNUMBER\":\"40702810342950000318\",\"PAYERCLIENTID\":\"U32080156\",\"PAYERINN\":\"7708388320\",\"PAYERNAME\":\"Общество с ограниченной ответственностью 'Вистер'\",\"PAYMENTREFERENCE\":\"2956426178\",\"PAYMENTROOTID\":\"0\",\"PAYMENTSOURCESTATUS\":\"3\",\"PURPOSE\":\"Взыск. по ИЛ Комиссия по Трудовым Спорам 09 от 16.09.2021  дело № 09  от 16.09.2021 (вз-ль Левин Арсений Петрович) без НДС». Задолженность по заработной плате. Сумма плат.док. 1484529\",\"SELLCURCODE\":\"810\",\"EXID\":\"2956426178\",\"SOURCESYSTEMS\":\"MBANK\",\"STATUSUPDATEDATE\":\"2021-12-13T10:52:00.000Z\",\"TYPE\":\"16\",\"USERID\":\"1403\"}}";
        JsonValue jsonValue = SpinValues.jsonValue(jsonData).create();

        PaymentDto oldPayment = jsonValue.getValue().prop("OldPayment").mapTo(PaymentDto.class);
        PaymentDto newPayment = jsonValue.getValue().prop("NewPayment").mapTo(PaymentDto.class);

        DelegateExecution execution = new DelegateExecutionFake();
        execution.setVariable("diffWork", newPayment);
        execution.setVariable("diffBase", oldPayment);

        ObjectDiff diff = new ObjectDiff();
        diff.execute(execution);

        Set<String> diffResult = (Set<String>) execution.getVariable("diffPaths");

        Assertions.assertEquals(14, diffResult.size());
    }

}
