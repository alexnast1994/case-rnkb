package com.cognive.projects.casernkb.service.impl;

import com.cognive.projects.casernkb.model.zk_request.*;
import com.cognive.projects.casernkb.service.KafkaService;
import com.cognive.projects.casernkb.service.ZkRequestService;
import com.prime.db.rnkb.model.Client;
import com.prime.db.rnkb.model.FormOfRequest;
import com.prime.db.rnkb.model.OperationsInRequest;
import com.prime.db.rnkb.model.commucation.midl.ChangingTimingTask;
import com.prime.db.rnkb.model.commucation.midl.Task;
import com.prime.db.rnkb.model.commucation.midl.TaskClient;
import com.prime.db.rnkb.model.commucation.request.Answer;
import com.prime.db.rnkb.model.commucation.request.PersonsInRequest;
import com.prime.db.rnkb.model.commucation.request.RequestComment;
import com.prime.db.rnkb.model.commucation.request.RequestedInformation;
import com.prime.db.rnkb.repository.BaseDictionaryRepository;
import com.prime.db.rnkb.repository.ClientRepository;
import com.prime.db.rnkb.repository.FormOfRequestRepository;
import com.prime.db.rnkb.repository.OperationsInRequestRepository;
import com.prime.db.rnkb.repository.communication.midl.ChangingTimingTasksRepository;
import com.prime.db.rnkb.repository.communication.midl.TaskClientRepository;
import com.prime.db.rnkb.repository.communication.midl.TaskRepository;
import com.prime.db.rnkb.repository.communication.request.*;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ZkRequestServiceImpl implements ZkRequestService {
    private final TaskRepository taskRepository;
    private final ChangingTimingTasksRepository changingTimingTasksRepository;
    private final TaskClientRepository taskClientRepository;
    private final BaseDictionaryRepository baseDictionaryRepository;
    private final ClientRepository clientRepository;
    private final RequestsRepository requestsRepository;
    private final FormOfRequestRepository formOfRequestRepository;
    private final RequestedInformationRepository requestedInformationRepository;
    private final RequestCommentRepository requestCommentRepository;
    private final OperationsInRequestRepository operationsInRequestRepository;
    private final AttachmentRepository attachmentRepository;
    private final PersonsInRequestRepository personsInRequestRepository;
    private final AnswerRepository answerRepository;
    @Setter(onMethod_ = {@Autowired})
    private KafkaService kafkaService;

    @Override
    public void createObjectsAndSend(AMLRequest request) {
        var answers = request.getRequestData().getRequests().stream()
                .map(this::createObjects)
                .collect(Collectors.toList());
        var response = new AMLResponse();
        response.setId(UUID.randomUUID().toString());
        response.setTimeStamp(LocalDateTime.now());
        response.setResponseData(new ResponseData(answers));
        response.setSourceSystemId(request.getSourceSystemId());
        response.setSourceSystem(request.getSourceSystem());
        response.setRequestType(request.getRequestType());
        response.setObjectType(request.getObjectType());
        response.setObjectSubType(request.getObjectSubType());
        response.setObjectDesc(request.getObjectDesc());
        response.setObjCreateDate(request.getObjCreateDate());
        response.setUser(request.getUser());
        response.setVersion(request.getVersion());
        kafkaService.sendZkRequestAnswer(response);
    }

    @Override
    public void createObjectsAndSend(AMLResponse request) {
        var answers = request.getResponseData().getResponse().stream()
                .map(this::createObjects)
                .collect(Collectors.toList());
        var response = new AMLResponse();
        response.setId(UUID.randomUUID().toString());
        response.setTimeStamp(LocalDateTime.now());
        response.setResponseData(new ResponseData(answers));
        response.setSourceSystemId(request.getSourceSystemId());
        response.setSourceSystem(request.getSourceSystem());
        response.setRequestType(request.getRequestType());
        response.setObjectType(request.getObjectType());
        response.setObjectSubType(request.getObjectSubType());
        response.setObjectDesc(request.getObjectDesc());
        response.setObjCreateDate(request.getObjCreateDate());
        response.setUser(request.getUser());
        response.setVersion(request.getVersion());
        kafkaService.sendZkResponseAnswer(response);
    }

    @Override
    @Transactional
    public void saveStatus(AMLRequestStatus request) {
        var answers = request.getRequestStatusData().getRequestStatus().stream()
                .map(requestStatus -> {
                    Answer answer = new Answer();
                    com.prime.db.rnkb.model.commucation.request.Request request1 = new com.prime.db.rnkb.model.commucation.request.Request();
                    request1.setId(requestStatus.getIdRequest());
                    answer.setRequestId(request1);
                    answer.setDateOfResponse(requestStatus.getDateOfResponse());
                    answer.setStatusClient(requestStatus.getStatus());
                    return answer;
                }).collect(Collectors.toList());
        answerRepository.saveAll(answers);

    }

    @Transactional
    Request createObjects(Request request) {
        Task savedTask = null;
        ChangingTimingTask savedTimingTask = null;
        try {
            var taskStatusType = 184;
            var taskStatusCode = "1";
            var typeOfTaskType = 185;
            var typeOfTaskCode = "4";
            var taskStatus = baseDictionaryRepository.getBaseDictionary(taskStatusCode, taskStatusType);
            var taskTypeOfTask = baseDictionaryRepository.getBaseDictionary(typeOfTaskCode, typeOfTaskType);

            Task task = new Task();
            task.setStatusId(taskStatus);
            task.setCreationDate(request.getDateOfFormation());
            task.setTypeOfTask(taskTypeOfTask);
            savedTask = taskRepository.save(task);
            if (request.getClientId() != null) {
                var clients = clientRepository.findAllByExClientId(request.getClientId());
                Task finalSavedTask = savedTask;
                var taskClients = clients.stream()
                        .map(client -> getTaskClient(finalSavedTask, client))
                        .collect(Collectors.toList());
                taskClientRepository.saveAll(taskClients);
            }
            ChangingTimingTask changingTimingTask = new ChangingTimingTask();
            changingTimingTask.setIssueId(savedTask);
            changingTimingTask.setIsActive(true);
            changingTimingTask.setCreationDate(request.getDateOfFormation());
            savedTimingTask = changingTimingTasksRepository.save(changingTimingTask);
        } catch (Exception exception) {
            log.error("Can't create Task or ChangingTimingTask: ", exception);
        }
        com.prime.db.rnkb.model.commucation.request.Request zkRequest;

        zkRequest = request.getRequestId() == null
                ? new com.prime.db.rnkb.model.commucation.request.Request()
                : requestsRepository.findById(Long.valueOf(request.getRequestId())).orElseThrow();
        zkRequest.setDateOfFormation(request.getDateOfFormation());
        if (savedTask != null) {
            zkRequest.setZkTaskId(savedTask);
        }
        var requestStatusType = 190;
        var requestStatusCode = "1";
        var complOfInfoType = 192;
        var complOfInfoCode = "3";
        var requestStatus = baseDictionaryRepository.getBaseDictionary(requestStatusCode, requestStatusType);
        var complOfInfo = baseDictionaryRepository.getBaseDictionary(complOfInfoCode, complOfInfoType);
        zkRequest.setCompletenessOfInformation(complOfInfo);
        zkRequest.setStatusOfRequest(requestStatus);
        zkRequest = requestsRepository.save(zkRequest);

        FormOfRequest formOfRequest = new FormOfRequest();
        formOfRequest = formOfRequestRepository.save(formOfRequest);

        RequestedInformation requestedInformation = new RequestedInformation();
        var statusOfProvidingType = 191;
        var statusOfProvidingCode = "2";
        var statusOfProviding = baseDictionaryRepository.getBaseDictionary(statusOfProvidingCode, statusOfProvidingType);
        requestedInformation.setRequestId(zkRequest);
        requestedInformation.setStatusOfProviding(statusOfProviding);
        requestedInformation.setFormOfReq(formOfRequest);
        requestedInformation = requestedInformationRepository.save(requestedInformation);

        RequestComment requestComment = new RequestComment();
        requestComment.setRequest(zkRequest);
        requestCommentRepository.save(requestComment);

        OperationsInRequest operation = new OperationsInRequest();
        operation.setRequest(zkRequest);
        operationsInRequestRepository.save(operation);

        com.prime.db.rnkb.model.commucation.request.Request finalZkRequest = zkRequest;
        var attachments = request.getAttachment().stream()
                .map(a -> getAttachment(finalZkRequest, a))
                .collect(Collectors.toList());
        attachmentRepository.saveAll(attachments);

        PersonsInRequest personsInRequest = new PersonsInRequest();
        personsInRequest.setRequestId(zkRequest);
        if (request.getClientId() != null) {
            var clients = clientRepository.findAllByExClientId(request.getClientId());
            if (!clients.isEmpty()) {
                var client = clients.get(0);
                personsInRequest.setClientId(client);
                personsInRequest.setFullName(request.getFullName());
                personsInRequest.setInn(client.getInn());
            }
        }
        personsInRequestRepository.save(personsInRequest);

        request.setRequestId(String.valueOf(zkRequest.getId()));
        request.setDateOfResponse(LocalDateTime.now());
        return request;
    }

    @NotNull
    private com.prime.db.rnkb.model.commucation.request.Attachment getAttachment(com.prime.db.rnkb.model.commucation.request.Request finalZkRequest, com.cognive.projects.casernkb.model.zk_request.Attachment a) {
        com.prime.db.rnkb.model.commucation.request.Attachment attachment = new com.prime.db.rnkb.model.commucation.request.Attachment();
        attachment.setRequestId(finalZkRequest);
        attachment.setNameOfAttachment(a.getNameOfAttachment());
        attachment.setUploadDate(a.getUploadDate());
        attachment.setFileReference(a.getFileReference());
        return attachment;
    }

    @NotNull
    private TaskClient getTaskClient(Task savedTask, Client client) {
        TaskClient taskClient = new TaskClient();
        taskClient.setClientId(client);
        taskClient.setIssueId(savedTask);
        return taskClient;
    }
}
