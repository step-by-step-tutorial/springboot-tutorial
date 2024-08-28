
# OAuth2 Implementation with Dynamic Client and Resource Registration in Spring Boot

## 1. Project Setup

First, ensure your Spring Boot project includes the necessary dependencies in your `pom.xml` or `build.gradle` file:

**Maven Dependencies:**
```xml
<dependencies>
    <!-- Spring Boot and Spring Security -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-oauth2-authorization-server</artifactId>
        <version>1.0.0</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
    </dependency>

    <!-- Spring Data JPA and H2 Database -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
</dependencies>
```

## 2. Database Configuration

Set up the database in your `application.properties`:

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.h2.console.enabled=true
```

## 3. Create the Entities

### `OAuthClientDetails` Entity:

```java
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "oauth_client_details")
public class OAuthClientDetails {

    @Id
    private String clientId;

    private String resourceIds;
    private String clientSecret;
    private String scope;
    private String authorizedGrantTypes;
    private String webServerRedirectUri;
    private String authorities;
    private Integer accessTokenValidity;
    private Integer refreshTokenValidity;
    private String additionalInformation;
    private String autoapprove;

    // Getters and Setters
}
```

### `OAuthAccessToken` Entity:

```java
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "oauth_access_token")
public class OAuthAccessToken {

    @Id
    private String tokenId;

    @Lob
    private byte[] token;

    private String authenticationId;
    private String userName;
    private String clientId;

    @Lob
    private byte[] authentication;

    private String refreshToken;

    // Getters and Setters
}
```

### `OAuthRefreshToken` Entity:

```java
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "oauth_refresh_token")
public class OAuthRefreshToken {

    @Id
    private String tokenId;

    @Lob
    private byte[] token;

    @Lob
    private byte[] authentication;

    // Getters and Setters
}
```

### `OAuthCode` Entity:

```java
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "oauth_code")
public class OAuthCode {

    @Id
    private String code;

    @Lob
    private byte[] authentication;

    // Getters and Setters
}
```

### `OAuthApproval` Entity:

```java
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "oauth_approvals")
@IdClass(OAuthApprovalId.class)
public class OAuthApproval {

    @Id
    private String userId;

    @Id
    private String clientId;

    @Id
    private String scope;

    private String status;
    private LocalDateTime expiresAt;
    private LocalDateTime lastModifiedAt;

    // Getters and Setters
}

class OAuthApprovalId implements Serializable {
    private String userId;
    private String clientId;
    private String scope;

    // Getters, Setters, hashCode, equals
}
```

### `OAuthClientDetailsResource` Entity (Optional):

```java
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "oauth_client_details_resource")
public class OAuthClientDetailsResource {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String clientId;
    private String resourceName;
    private String resourcePath;
    private String scopes;

    // Getters and Setters
}
```
