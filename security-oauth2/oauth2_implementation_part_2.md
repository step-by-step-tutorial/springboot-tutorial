
## 4. Create Repositories

### `OAuthClientDetailsRepository`:

```java
import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuthClientDetailsRepository extends JpaRepository<OAuthClientDetails, String> {
}
```

### `OAuthAccessTokenRepository`:

```java
import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuthAccessTokenRepository extends JpaRepository<OAuthAccessToken, String> {
}
```

### `OAuthRefreshTokenRepository`:

```java
import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuthRefreshTokenRepository extends JpaRepository<OAuthRefreshToken, String> {
}
```

### `OAuthCodeRepository`:

```java
import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuthCodeRepository extends JpaRepository<OAuthCode, String> {
}
```

### `OAuthApprovalRepository`:

```java
import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuthApprovalRepository extends JpaRepository<OAuthApproval, OAuthApprovalId> {
}
```

### `OAuthClientDetailsResourceRepository`:

```java
import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuthClientDetailsResourceRepository extends JpaRepository<OAuthClientDetailsResource, Long> {
}
```

## 5. Create Services

### OAuthClientDetailsService:

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OAuthClientDetailsService {

    @Autowired
    private OAuthClientDetailsRepository repository;

    public OAuthClientDetails registerClient(OAuthClientDetails clientDetails) {
        return repository.save(clientDetails);
    }

    // Additional methods to retrieve, update, delete clients
}
```

### OAuthClientDetailsResourceService:

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OAuthClientDetailsResourceService {

    @Autowired
    private OAuthClientDetailsResourceRepository repository;

    public OAuthClientDetailsResource registerResource(OAuthClientDetailsResource resource) {
        return repository.save(resource);
    }

    // Additional methods to retrieve, update, delete resources
}
```

## 6. Create Controllers

### Dynamic Client Registration Controller:

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register/client")
public class DynamicClientRegistrationController {

    @Autowired
    private OAuthClientDetailsService clientDetailsService;

    @PostMapping
    public String registerClient(@RequestBody OAuthClientDetails clientDetails) {
        clientDetailsService.registerClient(clientDetails);
        return "Client registered successfully!";
    }
}
```

### Dynamic Resource Registration Controller:

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register/resource")
public class DynamicResourceRegistrationController {

    @Autowired
    private OAuthClientDetailsResourceService resourceService;

    @PostMapping
    public String registerResource(@RequestBody OAuthClientDetailsResource resource) {
        resourceService.registerResource(resource);
        return "Resource registered successfully!";
    }
}
```

## 7. Authorization Server Configuration

Configure the authorization server to use your custom services for managing clients and tokens.

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private OAuthClientDetailsService clientDetailsService;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // You can wire in your custom ClientDetailsService here
        clients.withClientDetails(clientId -> clientDetailsService.findByClientId(clientId));
    }

    // Additional configuration for tokens, endpoints, etc.
}
```

## 8. Resource Server Configuration

Configure the resource server to protect registered resources dynamically.

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.stereotype.Component;

@Component
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private OAuthClientDetailsResourceService resourceService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // Dynamically configure security based on registered resources
        resourceService.getAllResources().forEach(resource -> {
            try {
                http.authorizeRequests()
                    .antMatchers(resource.getResourcePath()).access("#oauth2.hasScope('" + resource.getScopes() + "')");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        http.authorizeRequests().anyRequest().authenticated();
    }
}
```

## 9. Testing the Implementation

Now you can test the dynamic client and resource registration. Use tools like Postman or `curl` to send POST requests to the `/register/client` and `/register/resource` endpoints with the required payload.

For example, to register a client:
```json
POST /register/client
{
  "clientId": "new-client",
  "clientSecret": "new-secret",
  "scope": "read,write",
  "authorizedGrantTypes": "password,authorization_code,refresh_token,client_credentials"
}
```

To register a resource:
```json
POST /register/resource
{
  "clientId": "new-client",
  "resourceName": "New Resource",
  "resourcePath": "/api/new-resource",
  "scopes": "read"
}
```

## Conclusion

This implementation covers the full process of setting up a dynamic OAuth2 system in a Spring Boot application. You can dynamically register clients and resources, manage tokens, and protect your API endpoints using OAuth2. This setup is highly flexible and can be extended to include more features such as custom token handling, additional grant types, or integration with third-party identity providers.
