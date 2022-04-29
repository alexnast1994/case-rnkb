package com.cognive.projects.casernkb.service.impl;

import com.cognive.projects.casernkb.config.MinioConfig;
import com.cognive.projects.casernkb.service.MinioService;
import com.cognive.projects.casernkb.utils.MinioUtil;
import io.minio.MinioClient;
import io.minio.messages.Bucket;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class MinioServiceImpl implements MinioService {

    private final MinioUtil minioUtil;
    private final MinioClient minioClient;
    private final MinioConfig minioProperties;

    @Autowired
    public MinioServiceImpl(MinioUtil minioUtil, MinioClient minioClient, MinioConfig minioProperties) {
        this.minioUtil = minioUtil;
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
    }

    @Override
    public boolean bucketExists(String bucketName) {
        return minioUtil.bucketExists(bucketName);
    }


    @Override
    public void makeBucket(String bucketName) {
        minioUtil.makeBucket(bucketName);
    }

    @Override
    public List<String> listBucketName() {
        return minioUtil.listBucketNames();
    }

    @Override
    public List<Bucket> listBuckets() {
        return minioUtil.listBuckets();
    }

    @Override
    public boolean removeBucket(String bucketName) {
        return minioUtil.removeBucket(bucketName);
    }


    @Override
    public List<String> listObjectNames(String bucketName) {
        return minioUtil.listObjectNames(bucketName);
    }


    @Override
    public String putObject(MultipartFile file, String bucketName,String folderName,String fileType) {
        try {
            bucketName = StringUtils.isNotBlank(bucketName) ? bucketName : minioProperties.getBucketName();
            if (!this.bucketExists(bucketName)) {
                this.makeBucket(bucketName);
            }
            String fileName = file.getOriginalFilename();
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy-HH.mm.ss");
            Date date = new Date();
            String objectName = fileName.substring(0,fileName.lastIndexOf(".")) + "-" + formatter.format(date)
                    + fileName.substring(fileName.lastIndexOf("."));
            minioUtil.putObject(bucketName, file, folderName+"/"+objectName,fileType);
            return minioProperties.getEndpoint()+"/"+bucketName+"/"+folderName+"/"+objectName;
        } catch (Exception e) {
            e.printStackTrace();
            return " Upload failed ";
        }
    }
    @Override
    public String putObject(InputStream file, String bucketName, String folderName, String objectName, String fileType) {
        try {
            bucketName = StringUtils.isNotBlank(bucketName) ? bucketName : minioProperties.getBucketName();
            if (!this.bucketExists(bucketName)) {
                this.makeBucket(bucketName);
            }
            String fileName = objectName;
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy-HH.mm.ss");
            Date date = new Date();
            String fileObjectName = fileName.substring(0,fileName.lastIndexOf(".")) + "-" + formatter.format(date)
                    + fileName.substring(fileName.lastIndexOf("."));
            minioUtil.putObject(bucketName, folderName+"/"+fileObjectName,file,fileType);
            return minioProperties.getEndpoint()+"/"+bucketName+"/"+folderName+"/"+objectName;
        } catch (Exception e) {
            e.printStackTrace();
            return " Upload failed ";
        }
    }

    @Override
    public InputStream downloadObject(String bucketName, String folderName ,String objectName) {
        return minioUtil.getObject(bucketName,folderName +"/"+objectName);
    }

    @Override
    public boolean removeObject(String bucketName, String objectName) {
        return minioUtil.removeObject(bucketName, objectName);
    }

    @Override
    public boolean removeListObject(String bucketName, List<String> objectNameList) {
        return minioUtil.removeObject(bucketName,objectNameList);
    }

    @Override
    public String getObjectUrl(String bucketName,String objectName) {
        return minioUtil.getObjectUrl(bucketName, objectName);
    }

    @Override
    public boolean objectExists(String bucketName,String folderName, String objectName) {
        return minioUtil.statObject(bucketName,folderName+"/"+objectName)!=null? true: false;
    }
}
