package com.cognive.projects.casernkb.scheduler;


import com.cognive.projects.casernkb.enums.CamundaVariables;
import com.cognive.projects.casernkb.model.RuleModelJob;
import com.cognive.projects.casernkb.model.RuleResultCO;
import com.cognive.projects.casernkb.service.BPMProcessService;
import com.cognive.projects.casernkb.service.MinioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class FileReadScheduler {

    private static final String mimeType = "application/json";
    private boolean isClosed = false;

    /*@Value("${app.files.path}" )
    private String jsonPath;*/

    /*@Value("${app.files.archive}" )
    private String jsonPathArchive;*/

    /*@Value("${app.files.error}" )
    private String jsonPathError;*/

    @Value("${minio.bucket-name}")
    private String minioBucketName;

    @Value("${minio.caseFolder}")
    private String minioFolderName;

    @Value("${minio.successCaseFolder}")
    private String minioSuccessFolderName;

    @Value("${minio.errorCaseFolder}")
    private String minioErrorFolderName;

    @Value("${minio.fileName}")
    private String minioFileName;


    private final BPMProcessService bpmProcessService;
    private final MinioService minioService;

    @Autowired
    public FileReadScheduler(BPMProcessService bpmProcessService, MinioService minioService) {
        this.bpmProcessService = bpmProcessService;
        this.minioService = minioService;
    }

    @Scheduled(fixedRateString = "${app.files.readTime}")
    public void readJsonFromFile() throws IOException {
        //File file = new File(jsonPath);
        ObjectMapper mapper = new ObjectMapper();
        if (minioService.bucketExists(minioBucketName) && minioService.objectExists(minioBucketName,minioFolderName,minioFileName)) {
            InputStream inputStreamObject = minioService.downloadObject(minioBucketName,minioFolderName,minioFileName);
            InputStream inputStreamForSave = minioService.downloadObject(minioBucketName,minioFolderName,minioFileName);
            try {
                RuleModelJob listObject = mapper.readValue(inputStreamObject, RuleModelJob.class);
                HashMap<String, Object> variables = new HashMap<>();
                List<RuleResultCO> operations = Optional.ofNullable(listObject.getOperation()).orElseGet(ArrayList::new);
                //String json = mapper.writeValueAsString(operations);
                //log.info(json);
                if(operations.size()>0) {
                    variables.put(CamundaVariables.PIPELINE_JSON.getName(), Variables.objectValue(operations).serializationDataFormat(Variables.SerializationDataFormats.JAVA).create());
                    bpmProcessService.startProcess("operationCreateCaseJob", "", variables);
                    minioService.removeObject(minioBucketName,minioFolderName+"/"+minioFileName);
                    minioService.putObject(inputStreamForSave,minioBucketName,minioSuccessFolderName,"success-"+minioFileName,mimeType);
                    isClosed = true;
                }else{
                    log.error("Operations in JSON file is empty");
                    minioService.putObject(inputStreamForSave,minioBucketName,minioErrorFolderName,"error-"+minioFileName,mimeType);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (Exception e){
                e.printStackTrace();
            }finally {
                if(!isClosed) {
                    minioService.putObject(inputStreamForSave,minioBucketName,minioErrorFolderName,"error-"+minioFileName,mimeType);
                    isClosed=false;
                }
            }
        }
    }
    /*
    private void replaceArchiveFile() throws IOException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy-HH.mm.ss");
        Date date = new Date();
        Files.move(new File(jsonPath).toPath(), new File(jsonPathArchive + formatter.format(date) + ".json").toPath(),
                StandardCopyOption.REPLACE_EXISTING);
    }
    private void replaceErrorFile() throws IOException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy-HH.mm.ss");
        Date date = new Date();
        Files.move(new File(jsonPath).toPath(), new File(jsonPathError + formatter.format(date) + ".json").toPath(),
                StandardCopyOption.REPLACE_EXISTING);
    }*/
}
