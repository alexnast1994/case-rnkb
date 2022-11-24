package com.cognive.projects.casernkb.service;

import com.cognive.projects.casernkb.model.ZkCreate;
import com.cognive.projects.casernkb.model.docx.DocxRequestData;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.IOException;

public interface DocService {

    DocxRequestData prepareDocDto(ZkCreate zkCreate);

    XWPFDocument generateDoc(DocxRequestData docxRequestData) throws IOException;

}
