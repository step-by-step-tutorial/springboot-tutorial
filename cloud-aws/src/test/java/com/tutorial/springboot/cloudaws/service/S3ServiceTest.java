package com.tutorial.springboot.cloudaws.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.services.s3.model.CreateBucketResponse;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class S3ServiceTest {

    private final Logger logger = LoggerFactory.getLogger(S3ServiceTest.class);

    @Autowired
    private S3Service systemUnderTest;

    @Nested
    class CreateBucket {

        @AfterEach
        void tearDown() {
            systemUnderTest.deleteBucket("testbucketforcreation");
        }

        @Test
        void givenName_whenCreateBucket_thenReturnsResponse() {
            var givenName = "testbucketforcreation";

            var actual = systemUnderTest.createBucket(givenName);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            actual.ifPresent(response -> {
                assertNotNull(response.location());
                assertTrue(response.sdkHttpResponse().isSuccessful());
                logger.info("Bucket created: {}", response.location());
            });


        }

    }

    @Nested
    class GetBucket {

        @BeforeEach
        void setUp() {
            systemUnderTest.createBucket("testbucketforlisting01");
            systemUnderTest.createBucket("testbucketforlisting02");
        }

        @AfterEach
        void tearDown() {
            systemUnderTest.deleteBucket("testbucketforlisting01");
            systemUnderTest.deleteBucket("testbucketforlisting02");
        }

        @Test
        void givenNoInput_whenListBuckets_thenReturnsBucketList() {
            var actual = systemUnderTest.listBuckets();
            assertNotNull(actual);
            assertFalse(actual.isEmpty());
            assertEquals(2, actual.size());
            logger.info("Buckets listed: {}", actual);
        }
    }

    @Nested
    class DeleteBucket {
        @BeforeEach
        void setUp() {
            systemUnderTest.createBucket("testbucketfordeletion");
        }

        @Test
        void givenBucketName_whenDeleteBucket_thenReturnsResponse() {
            var givenName = "testbucketfordeletion";
            var actual = systemUnderTest.deleteBucket(givenName);
            assertNotNull(actual);
            assertTrue(actual.isPresent());
            actual.ifPresent(response -> {
                assertTrue(response.sdkHttpResponse().isSuccessful());
            });
        }
    }
}