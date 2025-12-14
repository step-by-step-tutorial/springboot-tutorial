package com.tutorial.springboot.cloudaws.service;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
public class S3Facade {

    private final S3Client s3Client;

    private final S3Template template;

    public S3Facade(S3Client s3Client, S3Template template) {
        this.s3Client = s3Client;
        this.template = template;
    }

    public Optional<String> createBucket(String name) {
        return Optional.of(template.createBucket(name));
    }

    public List<String> listOfBucketName() {
        return s3Client.listBuckets()
                .buckets()
                .stream()
                .map(Bucket::name)
                .toList();
    }

    public boolean isBucketExists(String name) {
        return template.bucketExists(name);
    }

    public void deleteBucket(String name) {
        template.deleteBucket(name);
    }

    public void deleteBucketWithFiles(String name) {
        template.listObjects(name, "")
                .forEach(resource -> {
                    template.deleteObject(name, resource.getFilename());
                });
        template.deleteBucket(name);
    }

    public Optional<S3Resource> uploadFile(String bucketName, String fileName, InputStream file) {
        return Optional.of(template.upload(bucketName, fileName, file, ObjectMetadata.builder().build()));
    }

    public Optional<S3Resource> downloadFile(String bucketName, String fileName) {
        return Optional.of(template.download(bucketName, fileName));
    }

    public boolean isFileExists(String bucketName, String fileName) {
        return template.objectExists(bucketName, fileName);
    }

    public List<String> listOfFileName(String bucketName) {
        return template.listObjects(bucketName, "").stream().map(S3Resource::getFilename).toList();
    }

    public void deleteFile(String bucketName, String fileName) {
        template.deleteObject(bucketName, fileName);
    }

}

