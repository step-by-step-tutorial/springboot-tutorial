package com.tutorial.springboot.cloudaws.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketResponse;
import software.amazon.awssdk.services.s3.model.DeleteBucketResponse;

import java.util.List;
import java.util.Optional;

@Service
public class S3Service {

    private final S3Client s3Client;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public Optional<CreateBucketResponse> createBucket(String name) {
        return Optional.ofNullable(s3Client.createBucket(b -> b.bucket(name)));
    }

    public  Optional<DeleteBucketResponse> deleteBucket(String name) {
        return Optional.ofNullable(s3Client.deleteBucket(b -> b.bucket(name)));
    }

    public List<String> listBuckets() {
        return s3Client.listBuckets()
                .buckets()
                .stream()
                .map(b -> b.name())
                .toList();
    }


}

