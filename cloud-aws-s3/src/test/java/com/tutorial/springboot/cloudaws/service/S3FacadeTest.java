package com.tutorial.springboot.cloudaws.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static com.tutorial.springboot.cloudaws.TestFixtureFactory.createTestInputstream;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class S3FacadeTest {

    private final Logger logger = LoggerFactory.getLogger(S3FacadeTest.class);

    @Autowired
    private S3Facade systemUnderTest;

    @Nested
    class CreateBucketTest {

        public static final String BUCKET_NAME = UUID.randomUUID().toString();

        @AfterEach
        void tearDown() {
            systemUnderTest.deleteBucket(BUCKET_NAME);
        }

        @Test
        void givenName_whenCreateBucket_thenReturnsBucketPath() {
            var givenName = BUCKET_NAME;

            var actual = systemUnderTest.createBucket(givenName);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            actual.ifPresent(response -> {
                assertNotNull(response);
                logger.info("Bucket created: {}", response);
            });
        }

    }

    @Nested
    class ReadBucketTest {

        public static final String BUCKET_NAME_1 = UUID.randomUUID().toString();
        public static final String BUCKET_NAME_2 = UUID.randomUUID().toString();

        @BeforeEach
        void setUp() {
            systemUnderTest.createBucket(BUCKET_NAME_1);
            systemUnderTest.createBucket(BUCKET_NAME_2);
        }

        @AfterEach
        void tearDown() {
            systemUnderTest.deleteBucket(BUCKET_NAME_1);
            systemUnderTest.deleteBucket(BUCKET_NAME_2);
        }

        @Test
        void givenNoInput_whenListOfBucketName_thenReturnsListOfBucketNames() {
            var actual = systemUnderTest.listOfBucketName();
            assertNotNull(actual);
            assertFalse(actual.isEmpty());
            assertEquals(2, actual.size());
            logger.info("Buckets listed: {}", actual);
        }

        @Test
        void givenExistingBucketName_whenIsBucketExists_thenReturnsTrue() {
            var givenName = BUCKET_NAME_1;
            var actual = systemUnderTest.isBucketExists(givenName);
            assertTrue(actual);
        }

        @Test
        void givenNonExistingBucketName_whenIsBucketExists_thenReturnsFalse() {
            var givenName = "invalid-bucket-name";
            var actual = systemUnderTest.isBucketExists(givenName);
            assertFalse(actual);
        }
    }

    @Nested
    class DeleteBucketTest {
        public static final String BUCKET_NAME = UUID.randomUUID().toString();

        @BeforeEach
        void setUp() {
            systemUnderTest.createBucket(BUCKET_NAME);
        }

        @Test
        void givenBucketName_whenDeleteBucket_thenRunSuccessfully() {
            var givenName = BUCKET_NAME;
            assertDoesNotThrow(() -> {
                systemUnderTest.deleteBucket(givenName);
            });
        }
    }

    @Nested
    class DeleteBucketWithFilesTest {
        public static final String BUCKET_NAME = UUID.randomUUID().toString();
        public static final String FILE_NAME_1 = "testfile1.txt";
        public static final String FILE_NAME_2 = "testfile2.txt";

        @BeforeEach
        void setUp() {
            systemUnderTest.createBucket(BUCKET_NAME);
            systemUnderTest.uploadFile(BUCKET_NAME, FILE_NAME_1, createTestInputstream());
            systemUnderTest.uploadFile(BUCKET_NAME, FILE_NAME_2, createTestInputstream());
        }

        @Test
        void givenBucketName_whenDeleteBucketWithFiles_thenRunSuccessfully() {
            var givenName = BUCKET_NAME;
            assertDoesNotThrow(() -> {
                systemUnderTest.deleteBucketWithFiles(givenName);
            });
        }
    }

    @Nested
    class UploadFileTest {
        public static final String BUCKET_NAME = UUID.randomUUID().toString();

        @BeforeEach
        void setUp() {
            systemUnderTest.createBucket(BUCKET_NAME);
        }

        @AfterEach
        void tearDown() {
            systemUnderTest.deleteBucketWithFiles(BUCKET_NAME);
        }

        @Test
        void givenBucketNameAndFileNameAndFileStream_whenUploadFile_thenReturnsResponse() {
            var givenBucketName = BUCKET_NAME;
            var givenFileName = "testfile.txt";
            var givenFileStream = createTestInputstream();

            var actual = systemUnderTest.uploadFile(givenBucketName, givenFileName, givenFileStream);
            assertNotNull(actual);
            assertTrue(actual.isPresent());
            actual.ifPresent(response -> {
                assertNotNull(response.getLocation());
                logger.info("File uploaded to: /{}/{}", response.getLocation().getBucket(), response.getLocation().getObject());
            });


        }

    }

    @Nested
    class ReadFileTest {
        public static final String BUCKET_NAME = UUID.randomUUID().toString();
        public static final String FILE_NAME_1 = "testfile1.txt";
        public static final String FILE_NAME_2 = "testfile2.txt";

        @BeforeEach
        void setUp() {
            systemUnderTest.createBucket(BUCKET_NAME);
            systemUnderTest.uploadFile(BUCKET_NAME, FILE_NAME_1, createTestInputstream());
            systemUnderTest.uploadFile(BUCKET_NAME, FILE_NAME_2, createTestInputstream());
        }

        @AfterEach
        void tearDown() {
            systemUnderTest.deleteBucketWithFiles(BUCKET_NAME);
        }

        @Test
        void givenBucketNameAndFileName_whenDownloadFile_thenReturnsResource(){
            var givenBucketName = BUCKET_NAME;
            var givenFileName = FILE_NAME_1;

            var actual = systemUnderTest.downloadFile(givenBucketName, givenFileName);
            assertNotNull(actual);
            assertTrue(actual.isPresent());
            actual.ifPresent(resource -> {
                assertDoesNotThrow(resource::getInputStream);
                logger.info("File downloaded: {}", resource.getFilename());
            });
        }


        @Test
        void givenExistingBucketNameAndFileName_whenIsFileExists_thenReturnsTrue() {
            var givenBucketName = BUCKET_NAME;
            var givenFileName = FILE_NAME_1;

            var actual = systemUnderTest.isFileExists(givenBucketName, givenFileName);
            assertTrue(actual);
        }

        @Test
        void givenBucketNameAndNonExistingFileName_whenIsFileExists_thenReturnsFalse() {
            var givenBucketName = BUCKET_NAME;
            var givenFileName = "invalid-file-name";

            var actual = systemUnderTest.isFileExists(givenBucketName, givenFileName);
            assertFalse(actual);
        }

        @Test
        void givenBucketName_whenListOfFileName_thenReturnsListOfFileNames() {
            var givenBucketName = BUCKET_NAME;

            var actual = systemUnderTest.listOfFileName(givenBucketName);
            assertNotNull(actual);
            assertFalse(actual.isEmpty());
            assertEquals(2, actual.size());
        }

    }

    @Nested
    class DeleteFileTest {
        public static final String BUCKET_NAME = UUID.randomUUID().toString();
        public static final String FILE_NAME = "testfile.txt";

        @BeforeEach
        void setUp() {
            systemUnderTest.createBucket(BUCKET_NAME);
            systemUnderTest.uploadFile(BUCKET_NAME, FILE_NAME, createTestInputstream());
        }

        @AfterEach
        void tearDown() {
            systemUnderTest.deleteBucket(BUCKET_NAME);
        }

        @Test
        void givenBucketNameAndFileName_whenDeleteFile_thenRunSuccessfully() {
            var givenBucketName = BUCKET_NAME;
            var givenFileName = FILE_NAME;

            assertDoesNotThrow(() -> {
                systemUnderTest.deleteFile(givenBucketName, givenFileName);
            });
        }
    }

}