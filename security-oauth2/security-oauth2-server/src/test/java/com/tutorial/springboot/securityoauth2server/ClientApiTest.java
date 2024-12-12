package com.tutorial.springboot.securityoauth2server;

import com.tutorial.springboot.securityoauth2server.dto.ClientDto;
import com.tutorial.springboot.securityoauth2server.enums.GrantType;
import com.tutorial.springboot.securityoauth2server.service.impl.ClientService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static com.tutorial.springboot.securityoauth2server.ClientApiTest.Fixture.*;
import static com.tutorial.springboot.securityoauth2server.test_utils.SecurityTestUtils.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles({"test", "h2"})
public class ClientApiTest {

    static final String APP_HOST = "http://localhost";

    static final String BASE_PATH = "/api/v1/clients";

    @LocalServerPort
    int port;

    @Autowired
    ClientService testAssistant;

    static class Fixture {

        static final String TEST_CLIENT_ID = "testClient";

        static final String ADMIN_CLIENT_ID = "adminClient";

        static ClientDto createTestClient() {
            return new ClientDto()
                    .setClientId(TEST_CLIENT_ID)
                    .setClientSecret("password")
                    .setRedirectUri("http://client.host/login/oauth2/code/testClient")
                    .setGrantTypes(GrantType.allType())
                    .setScopes(Arrays.asList("read", "write"))
                    .setAccessTokenValiditySeconds(3600)
                    .setRefreshTokenValiditySeconds(1209600);
        }

        static ClientDto createAdminClient() {
            return new ClientDto()
                    .setClientId(ADMIN_CLIENT_ID)
                    .setClientSecret("password")
                    .setRedirectUri("http://client.host/login/oauth2/code/adminClient")
                    .setGrantTypes(GrantType.allType())
                    .setScopes(Arrays.asList("read", "write"))
                    .setAccessTokenValiditySeconds(3600)
                    .setRefreshTokenValiditySeconds(1209600);
        }
    }

    @Test
    void givenClient_whenSaveOne_thenReturnIdWithCreatedStatus() {
        var givenBody = createTestClient();

        RestAssured.given()
                .contentType(ContentType.JSON)
                .auth().basic(TEST_USERNAME, TEST_PASSWORD)
                .baseUri(APP_HOST).port(port).basePath(BASE_PATH)
                .body(givenBody)
                .when().post()
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", containsString(BASE_PATH))
                .body("", not(emptyOrNullString()))
                .body("", greaterThan(0));

        var actual = testAssistant.getByClientId(TEST_CLIENT_ID);

        assertNotNull(actual);
        assertTrue(actual.isPresent());
        actual.ifPresent(client -> {
            assertNotNull(client.getId());
            assertNotNull(client.getCreatedBy());
            assertNotNull(client.getCreatedAt());
            assertNotNull(client.getVersion());
        });
    }

    @Nested
    class FindByClientIdTests {

        static final String RELATIVE_PATH = "/findByClientId/{clientId}";

        static final String PARAM = "clientId";

        @BeforeEach
        void setUp() {
            loginToTestEnv();
            testAssistant.save(createTestClient());
        }

        @Test
        void givenClientId_whenFindByClientId_thenReturnClientDtoWithOKStatus() {
            var givenClientId = TEST_CLIENT_ID;

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .auth().basic(TEST_USERNAME, TEST_PASSWORD)
                    .baseUri(APP_HOST).port(port)
                    .basePath(BASE_PATH + RELATIVE_PATH).pathParam(PARAM, givenClientId)
                    .when().get()
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("", notNullValue())
                    .body("clientId", equalTo(TEST_CLIENT_ID))
                    .body("clientSecret", emptyOrNullString())
                    .body("redirectUri", equalTo("http://client.host/login/oauth2/code/testClient"))
                    .body("grantTypes", hasSize(6))
                    .body("scopes", hasSize(2))
                    .body("accessTokenValiditySeconds", equalTo(3600))
                    .body("refreshTokenValiditySeconds", equalTo(1209600));
        }
    }

    @Nested
    class FindAllClientsTests {

        @BeforeEach
        void setUp() {
            loginToTestEnv();
            testAssistant.saveBatch(List.of(createTestClient(), createAdminClient()));
        }

        @Test
        void givenNothing_whenGetAll_thenReturnListOfDtoWithOkStatus() {
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .auth().basic(TEST_USERNAME, TEST_PASSWORD)
                    .baseUri(APP_HOST).port(port).basePath(BASE_PATH)
                    .when().get()
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("", notNullValue())
                    .body("", hasSize(2));
        }
    }
}