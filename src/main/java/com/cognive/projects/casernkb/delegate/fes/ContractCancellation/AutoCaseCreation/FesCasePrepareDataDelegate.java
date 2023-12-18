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

import static com.cognive.projects.casernkb.constant.FesConstants.WRONG_CLIENT_TYPE;

@Component
@RequiredArgsConstructor
public class FesCasePrepareDataDelegate implements JavaDelegate {

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
                objectMapper.convertValue(data.get("payload"), FesAutoContractsCancellationDto.class).getFesCaseAutoSaveDto() :
                objectMapper.convertValue(data, FesCaseAutoSaveDto.class);

        var rejectType = fesCaseAutoSaveDto.getRejectType();
        var isOperationRejection = false;
        var isOperation = false;
        Client client;
        Client clientPayee;
        if (fesCaseAutoSaveDto.getPaymentId() != null) {
            Payment payment = paymentRepository.findById(fesCaseAutoSaveDto.getPaymentId()).orElseThrow();
            client = payment.getPayerClientId();
            clientPayee = payment.getPayeeClientId();
            execution.setVariable("payment", payment);

            if (rejectType == null) {
                isOperation = true;
                execution.setVariable("responsibleUser", fesCaseAutoSaveDto.getResponsibleUser());
                execution.setVariable("operationStatus", fesCaseAutoSaveDto.getOperationStatus());
                execution.setVariable("operationType", fesCaseAutoSaveDto.getOperationType());
                execution.setVariable("additionalOperationType", fesCaseAutoSaveDto.getAdditionalOperationType());

            } else {
                isOperationRejection = true;
                execution.setVariable("baseRejectCode", fesCaseAutoSaveDto.getBaseRejectCode());
                execution.setVariable("causeReject", fesCaseAutoSaveDto.getCauseReject());
                execution.setVariable("codeUnusualOp", fesCaseAutoSaveDto.getCodeUnusualOp());
                execution.setVariable("conclusion", fesCaseAutoSaveDto.getConclusion());
                execution.setVariable("clientPayee", clientPayee);
            }
        } else {
            var clientId = fesCaseAutoSaveDto.getClientId();
            client = clientRepository.findById(clientId).orElseThrow();
        }
        if (client != null) {
            var clientTypeCode = client.getClientType() != null ? client.getClientType().getCode() : WRONG_CLIENT_TYPE;
            String clientType = fesService.checkClientType(client);
            execution.setVariable("clientType", clientType);
            execution.setVariable("clientTypeCode", clientTypeCode);
        }

        execution.setVariable("isOperationRejection", isOperationRejection);
        execution.setVariable("isOperation", isOperation);
        execution.setVariable("rejectType", rejectType);
        execution.setVariable("client", client);
    }
}
