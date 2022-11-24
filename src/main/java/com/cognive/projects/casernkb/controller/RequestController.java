package com.cognive.projects.casernkb.controller;

import com.cognive.projects.casernkb.config.MinioConfig;
import com.cognive.projects.casernkb.model.ZkCreate;
import com.cognive.projects.casernkb.model.ZkRequestFileAnswer;
import com.cognive.projects.casernkb.service.DocService;
import com.cognive.projects.casernkb.service.MinioService;
import com.prime.db.rnkb.model.commucation.request.Attachment;
import com.prime.db.rnkb.model.commucation.request.Request;
import com.prime.db.rnkb.repository.communication.request.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/request")
@RequiredArgsConstructor
public class RequestController {
    private static final String FILE_NAME = "orig.docx";
    private final DocService docService;
    private final MinioConfig minioProperties;
    private final MinioService minioService;
    private final AttachmentRepository attachmentRepository;

    @PostMapping("/generate")
    public ResponseEntity<ZkRequestFileAnswer> getFile(@RequestBody @Valid ZkCreate request) throws IOException {
        var dto = docService.prepareDocDto(request);
        var file = docService.generateDoc(dto);
        var fileName = minioProperties.getFolderReestName() + "/" + dto.getId() + "/request/" + FILE_NAME;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        file.write(out);
        out.close();
        file.close();
        minioService.putRequestFile(new ByteArrayInputStream(out.toByteArray()), fileName);
        if (1 == request.getRequestType()) {
            var attachment = new Attachment();
            Request request1 = new Request();
            request1.setId(dto.getId());
            attachment.setRequestId(request1);
            attachment.setFileReference(fileName);
            attachment.setNameOfAttachment(FILE_NAME);
            attachment.setUploadDate(LocalDateTime.now());
            attachmentRepository.save(attachment);
        }
        var answer = new ZkRequestFileAnswer();
        answer.setRequestId(dto.getId());
        answer.setDateCreate(LocalDateTime.now());
        answer.setMinioUrl(fileName);
        return ResponseEntity.ok(answer);
    }
}
