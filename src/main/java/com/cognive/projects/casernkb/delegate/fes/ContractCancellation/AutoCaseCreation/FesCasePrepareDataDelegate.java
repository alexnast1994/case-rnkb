package com.cognive.projects.casernkb.delegate.fes.ContractCancellation.AutoCaseCreation;

import com.cognive.projects.casernkb.model.fes.FesAutoContractsCancellationDto;
import com.cognive.projects.casernkb.model.fes.FesCaseAutoSaveDto;
import com.cognive.projects.casernkb.service.FesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prime.db.rnkb.model.Client;
import com.prime.db.rnkb.model.Payment;
import com.prime.db.rnkb.repository.ClientRepository;
import com.prime.db.rnkb.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class FesCasePrepareDataDelegate implements JavaDelegate {
    public static final String WRONG_CLIENT_TYPE = "0";

    private final ClientRepository clientRepository;
    private final ObjectMapper objectMapper;
    private final FesService fesService;
    private final PaymentRepository paymentRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        var jsonData = execution.getVariable("payload");

        Map<String, Object> data = (jsonData instanceof Map)
                ? (Map<String, Object>) jsonData
                : objectMapper.readValue((String) jsonData, Map.class);


        FesCaseAutoSaveDto fesCaseAutoSaveDto = data.containsKey("payload") ?
            objectMapper.convertValue(data.get("payload"), FesAutoContractsCancellationDto.class).getFesCaseAutoSaveDto():
            objectMapper.convertValue(data, FesCaseAutoSaveDto.class);

        var rejectType = fesCaseAutoSaveDto.getRejectType();
        var isOperationRejection = false;
        Client client;
        if (rejectType.equals("1")) {
            isOperationRejection = true;
            Payment payment = paymentRepository.findById(fesCaseAutoSaveDto.getPaymentId()).orElseThrow();
            client = payment.getPayerClientId();
            execution.setVariable("payment", payment);
            execution.setVariable("baseRejectCode", fesCaseAutoSaveDto.getBaseRejectCode());
            execution.setVariable("causeReject", fesCaseAutoSaveDto.getCauseReject());
            execution.setVariable("codeUnusualOp", fesCaseAutoSaveDto.getCodeUnusualOp());
            execution.setVariable("conclusion", fesCaseAutoSaveDto.getConclusion());
        } else {
            var clientId = fesCaseAutoSaveDto.getClientId();
            client = clientRepository.findById(clientId).orElseThrow();
        }
        var clientTypeCode = client.getClientType() != null ? client.getClientType().getCode() : WRONG_CLIENT_TYPE;
        String clientType = fesService.checkClientType(client);

        execution.setVariable("isOperationRejection", isOperationRejection);
        execution.setVariable("rejectType", rejectType);
        execution.setVariable("clientType", clientType);
        execution.setVariable("clientTypeCode", clientTypeCode);
        execution.setVariable("client", client);
    }
}
