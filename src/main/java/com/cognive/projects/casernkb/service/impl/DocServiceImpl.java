package com.cognive.projects.casernkb.service.impl;

import com.cognive.projects.casernkb.config.property.ZkProperties;
import com.cognive.projects.casernkb.model.ZkCreate;
import com.cognive.projects.casernkb.model.docx.DocxRequestData;
import com.cognive.projects.casernkb.repo.BaseDictRepo;
import com.cognive.projects.casernkb.service.DocService;
import com.prime.db.rnkb.model.BaseDictionary;
import com.prime.db.rnkb.model.commucation.request.Request;
import com.prime.db.rnkb.repository.ClientRepository;
import com.prime.db.rnkb.repository.PaymentRepository;
import com.prime.db.rnkb.repository.communication.request.RequestsRepository;
import com.prime.db.rnkb.service.BaseDictionaryService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocServiceImpl implements DocService {
    private final ZkProperties zkProperties;
    private final BaseDictionaryService baseDictionaryService;
    private final ClientRepository clientRepository;
    private final PaymentRepository paymentRepository;
    private final RequestsRepository requestsRepository;
    private final BaseDictRepo baseDictionaryRepository;

    @Override
    public DocxRequestData prepareDocDto(ZkCreate zkCreate) {
        var client = clientRepository.findById(zkCreate.getClient()).orElseThrow();
        var request = new Request();
        request = requestsRepository.save(request);
        var id = request.getId();
        var date = LocalDateTime.now();
        var legalName = client.getFullName();
        var legalInn = client.getInn();
        var header = baseDictionaryService.getName(baseDictionaryRepository.findById(zkCreate.getHeader()).orElseThrow());
        var payments = paymentRepository.findAllById(zkCreate.getOperations());
        var conclusion = baseDictionaryToString(getBdListSaveOrder(zkCreate.getConclusion()));
        var additionalInfo = baseDictionaryToString(getBdListSaveOrder(zkCreate.getAdditionally()));
        var requestInfo = baseDictionaryToString(getBdListSaveOrder(zkCreate.getRequestedInformation()));
        var individualBd = baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(24, "4");
        if (zkCreate.getLastDate() != null) {
            String mask = "ХХ.ХХ.ХХХХ";
            header = header.replaceAll(mask, zkCreate.getLastDate());
            requestInfo = requestInfo.replaceAll(mask, zkCreate.getLastDate());
            additionalInfo = additionalInfo.replaceAll(mask, zkCreate.getLastDate());
            conclusion = conclusion.replaceAll(mask, zkCreate.getLastDate());
        }
        boolean isIndividual = false;
        if (client.getClientType() != null) {
            isIndividual = individualBd.getId().equals(client.getClientType().getId());
        }
        return new DocxRequestData(id, date, legalName, legalInn, header, payments, requestInfo, additionalInfo, conclusion, isIndividual);
    }

    @Override
    public XWPFDocument generateDoc(DocxRequestData docxRequestData) throws IOException {
        var dataParams = DocxRequestData.getDataParams(docxRequestData);
        var path = getDocxPattern(docxRequestData);
        var is = FileUtils.openInputStream(new File(path));
        try {
            XWPFDocument xwpfDocument = new XWPFDocument(is);
            replacePlaceholdersInParagraphs(dataParams, xwpfDocument);
            fillTable(docxRequestData, xwpfDocument);
            return xwpfDocument;
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private void replacePlaceholdersInParagraphs(Map<String, String> dataParams, XWPFDocument xwpfDocument) {
        for (Map.Entry<String, String> entry : dataParams.entrySet()) {
            for (XWPFParagraph paragraph : xwpfDocument.getParagraphs()) {
                for (XWPFRun run : paragraph.getRuns()) {
                    String text = run.text();
                    if (text != null
                            && text.contains(entry.getKey())) {
                        if (entry.getValue() == null
                                || entry.getValue().isEmpty()) {
                            text = text.replace(entry.getKey(), "");
                            run.setText(text, 0);
                        } else {
                            text = text.replace(entry.getKey(), entry.getValue());
                            if (text.contains("\n")) {
                                getLineForEachElements(text, run);
                            } else {
                                run.setText(text, 0);
                            }
                        }
                    }
                }
            }
        }
    }

    private void getLineForEachElements(String text, XWPFRun run) {
        String[] lines = text.split("\n");
        run.addTab();
        run.setText(lines[0], 0);
        for (int i = 1; i < lines.length; i++) {
            run.addBreak();
            run.addTab();
            run.setText(lines[i]);
        }
    }

    private void fillTable(DocxRequestData docxRequestData, XWPFDocument xwpfDocument) {
        for (XWPFTable xwpfTable : xwpfDocument.getTables()) {
            DocxRequestData.setTableParams(docxRequestData, xwpfTable);
        }
    }

    private String baseDictionaryToString(List<BaseDictionary> baseDictionaries) {
        return baseDictionaries.stream()
                .map(baseDictionaryService::getName)
                .collect(Collectors.joining("\n"));
    }

    private List<BaseDictionary> getBdListSaveOrder(List<Long> idsList) {
        return idsList.stream().map(str -> baseDictionaryRepository.findById(str).orElseThrow())
                .collect(Collectors.toList());
    }

    private String getDocxPattern(DocxRequestData docxRequestData) {
        String path;
        if (docxRequestData.getPayments().size() > 0) {
            if (docxRequestData.isIndividual()) {
                path = zkProperties.getIndividualDocPatternTable();
            } else {
                path = zkProperties.getLegalDocPatternTable();
            }
        } else {
            if (docxRequestData.isIndividual()) {
                path = zkProperties.getIndividualDocPattern();
            } else {
                path = zkProperties.getLegalDocPattern();
            }
        }
        return path;
    }
}
