# <p align="center">Implement OAuth 2.0 Server and Client</p>

<p align="justify">

This tutorial is about implementing OAuth 2.0 Server and Client with Springboot framework.

</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [OAuth 2.0](#tools_name)
* [OAuth 2.0 Use Cases](#tools_name-use-cases)
* [Install OAuth 2.0 on Docker](#install-tools_name-on-docker)
* [Install OAuth 2.0 on Kubernetes](#install-tools_name-on-kubernetes)
* [How To Set up Spring Boot](#how-to-set-up-spring-boot)
* [How To Set up Spring Boot Test](#how-to-set-up-spring-boot-test)
* [License](#license)
* [Appendix](#appendix )

## Getting Started

### Prerequisites

* [Java 21](https://www.oracle.com/java/technologies/downloads/)
* [Maven 3](https://maven.apache.org/index.html)
* [Docker](https://www.docker.com/)
* [Kubernetes](https://kubernetes.io/)

### Pipeline

#### Generate Certificate

```bash
openssl genpkey -algorithm RSA -out private-key.pem
openssl rsa -pubout -in private-key.pem -out public-key.pem
openssl pkcs8 -topk8 -inform PEM -outform PEM -in private-key.pem -out private-key.pem -nocrypt
```

#### Build

```bash
# server
mvn -f security-oauth2-server/pom.xml clean package -DskipTests=true
```

```bash
# client
mvn -f security-oauth2-client/pom.xml clean package -DskipTests=true
```

#### Test

```bash
# server
mvn -f security-oauth2-server/pom.xml test
```

```shell
# client
mvn -f security-oauth2-client/pom.xml test
```

#### Run

```bash
# server
mvn -f security-oauth2-server/pom.xml spring-boot:run
```

#### Register a Client

```shell
curl -v -X POST "http://localhost:8080/api/v1/clients" \
  -u "test:test" \
  -H "Content-Type: application/json" \
  -d '{
        "clientId": "testClient",
        "clientSecret": "password",
        "redirectUri": "http://localhost:8080/login/oauth2/code/testClient",
        "grantTypes": ["authorization_code", "password", "client_credentials", "refresh_token"],
        "scopes": ["read", "write"],
        "accessToken_validity_seconds": 3600,
        "refreshToken_validity_seconds": 1209600
      }'
```

#### Create a Login Session

```shell
curl -v -X POST "http://localhost:8080/login" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=test&password=test" \
  -c cookies.txt
```

#### Request an Authorization Code

Copy authorization code from console.

```shell
curl -v -X GET "http://localhost:8080/oauth2/authorize?response_type=code&client_id=testClient&scope=read&redirect_uri=http://localhost:8080/login/oauth2/code/testClient" \
  -b cookies.txt \
  -H "Content-Type: application/x-www-form-urlencoded"

```

#### Get a JWT Bearer Token

Use authorization code from last step.
Copy token from console.

```shell
 curl -X POST "http://localhost:8080/oauth2/token" \
  -u "testClient:password" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=authorization_code" \
  -d "code=???" \
  -d "redirect_uri=http://localhost:8080/login/oauth2/code/testClient"
```

#### Request a Protected Resource

Use token from last step.

```shell
# Check health status
curl -v -X GET "http://localhost:8080/api/v1/health" \
  -H "Authorization: Bearer ???"
```

## OAuth 2.0

### What is OAuth?

OAuth is a **delegation protocol**, a method allowing a resource owner to authorize a software application to access
their resource without impersonation. The application requests authorization, and the resource owner grants access by
issuing tokens. This ensures the application operates on delegated rights, not impersonation.

### What is OAuth 2.0?

OAuth 2.0 is an authorization framework that allows third-party applications to gain limited access to HTTP services.
This can be achieved:

- On behalf of a resource owner through an approval interaction.
- Directly by the application itself.

#### **Token**

##### **Bearer Token**

A bearer token is a security token with the property that any party possessing the token (a "bearer") can use it to
access the resource without needing to prove possession of cryptographic key material.

##### **Access Token**

An access token is issued by the authorization server to enable the client to access protected resources. OAuth 2.0
typically uses Bearer tokens.

###### **Methods of Sending an Access Token**

1. **Authorization Header**  
   Include the access token in the `Authorization` header of the HTTP request.  
   **Example**:
   ```http
   GET /resource HTTP/1.1
   Host: server.example.com
   Authorization: Bearer mF_9.B5f-4.1JqM
   ```

2. **HTTP Request Body**  
   Include the token in the request body and set the `Content-Type` header to `application/x-www-form-urlencoded`.  
   **Example**:  
   **Header**:
   ```http
   POST /resource HTTP/1.1
   Host: server.example.com
   Content-Type: application/x-www-form-urlencoded
   ```  
   **Body**:
   ```
   access_token=mF_9.B5f-4.1JqM
   ```

3. **URI Query Parameter**  
   Pass the token as a query parameter in the URI.  
   **Example**:
   ```http
   GET /resource?access_token=mF_9.B5f-4.1JqM HTTP/1.1
   Host: server.example.com
   ```
    - **Tip**: When using URI query parameters, use the `Cache-Control: no-store` header to prevent caching.

###### **Error Responses**

- **Missing or Invalid Token**:
   ```http
   HTTP/1.1 401 Unauthorized
   WWW-Authenticate: Bearer realm="example"
   ```
- **Expired Token**:
   ```http
   HTTP/1.1 401 Unauthorized
   WWW-Authenticate: Bearer realm="example", error="invalid_token", error_description="The access token expired"
   ```

###### **Example Access Token Response**

```json
{
  "access_token": "mF_9.B5f-4.1JqM",
  "token_type": "Bearer",
  "expires_in": 3600,
  "refresh_token": "tGzv3JOkF0XG5Qx2TlKWIA"
}
```

###### **Security Considerations**

- Validate the TLS certificate chain when making requests.
- Always use HTTPS (TLS) for secure communication.
- Avoid storing tokens in cookies that can be transmitted in plaintext.
- Use short-lived tokens (e.g., one hour or less).
- Avoid passing tokens in URLs.
- Issue scoped tokens.

##### **Refresh Token**

A refresh token is used to obtain new access tokens when the current ones expire or become invalid. Refresh tokens are
only sent to the authorization server and are never shared with the resource server.

#### **Roles**

##### **Resource Owner (User)**

An entity capable of granting access to a protected resource. When the resource owner is a person, they are referred to
as an end-user.

##### **Resource Server (API Server)**

The server hosting the protected resources and capable of responding to requests made with access tokens.

##### **Authorization Server**

The server responsible for authenticating the resource owner and issuing access tokens to the client.

##### **Client**

An application that makes requests to access protected resources on behalf of the resource owner.

##### **Authorization-Code Flow**

<img src="https://github.com/step-by-step-tutorial/springboot-tutorial/blob/main/security-oauth2/doc/authorization-code-flow.png" alt="OAuth 2.0 Authorization-Code Flow" width="50%" height="50%">

#### **Authorization Grant**

##### **Authorization Code Grant**

The client directs the resource owner to the authorization server to obtain an access token. The authorization server
authenticates the resource owner and issues the token directly to the client.

##### **Implicit Grant**

The implicit grant type is a simplified flow optimized for browser-based applications. It does not require client
authentication and may use a redirect URI for validation.

##### **Resource Owner Password Credentials Grant**

The client directly uses the resource owner's credentials to obtain access tokens. This flow is suitable when there is a
high level of trust between the client and the resource owner.

##### **Client Credentials Grant**

This grant type is used by clients to obtain access tokens for resources under their control. It is not recommended for
accessing user-specific resources.

##### **Which OAuth 2.0 grant should be used?**
A grant is a method of acquiring an access token. Deciding which grants to implement depends on the type of client the end user will be using, and the experience you want for your users.

<img src="https://github.com/step-by-step-tutorial/springboot-tutorial/blob/main/security-oauth2/doc/oauth2-usage.png" alt="OAuth 2.0 Usage" width="50%" height="50%">

### **Client**

#### **Client Types**

* **Confidential Clients**: Capable of securely storing credentials.
* **Public Clients**: Cannot securely store credentials.

### **Data Model**

#### **Authorization Server**

Stores or accesses:

* Usernames and passwords.
* Client IDs, secrets, refresh tokens, and access tokens.
* HTTPS certificates and keys.
* Authorization-specific details (e.g., `redirect_uri`, `client_id`, authorization `code`).

#### **Resource Server**

Stores or accesses:

* User data.
* HTTPS certificates and keys.
* Access tokens for each request.
* Shared credentials or secrets with the authorization server.

**Note**: Resource servers do not store refresh tokens or client secrets.

#### **Client**

Stores:

* Client ID and secrets.
* Refresh tokens (persistent) and access tokens (transient).
* Trusted HTTPS certificates.
* Authorization-specific details (`redirect_uri`, authorization `code`).

### **References**

* [The OAuth 2.0 Authorization Framework (RFC 6749)](https://tools.ietf.org/html/rfc6749)
* [Bearer Token Usage (RFC 6750)](https://tools.ietf.org/html/rfc6750)
* [OAuth Security Topics](https://tools.ietf.org/html/draft-ietf-oauth-security-topics-11)
* [YANG Data Models](https://tools.ietf.org/html/draft-ietf-teas-yang-te-18)

## OAuth 2.0 Use Cases

## Install Applications on Docker

Create a file named `docker-compose.yml` with the following configuration.

### Docker Compose

[docker-compose.yml](docker-compose.yml)

```yaml
#docker-compose.yml
```

### Apply Docker Compose

Execute the following command to install Applications.

```shell
docker compose --file ./docker-compose.yml --project-name tools_name up --build -d
```

## Install Applications on Kubernetes

Create the following files for installing Applications.

### Kube Files

[tools_name-deployment.yml](/kube/tools_name-deployment.yml)

```yaml
#deployment.yml
```

[tools_name-service.yml](/kube/tools_name-service.yml)

```yaml
#service.yml
```

### Apply Kube Files

Execute the following commands to install the tools on Kubernetes.

```shell
kubectl apply -f ./kube/tools_name-deployment.yml
kubectl apply -f ./kube/tools_name-service.yml
```

### Check Status

```shell
kubectl get all
```

### Port Forwarding

<p align="justify">

In order to connect to Applications from localhost through the web browser use the following command and dashboard of
Applications is available on:

* Server: [http://securityoauth2server.localhost](http://securityoauth2server.localhost)
* Client: [http://securityoauth2client.localhost](http://securityoauth2client.localhost)

</p>

```shell
kubectl port-forward service/securityoauth2server 8080:8080
```

```shell
kubectl port-forward service/securityoauth2client 8081:8081
```

## How To Set up Spring Boot

### Dependencies

```xml
```

### Application Properties

```yaml
```

## How To Set up Spring Boot Test

### Dependencies

```xml
```

### Application Properties

```yaml
```

## License

## Appendix

### Makefile

```shell
build:
	mvn clean package -DskipTests=true

test:
	mvn test

run:
	mvn spring-boot:run
	
docker-compose-deploy:
	docker compose --file docker-compose.yml --project-name tools-name up --build -d

docker-remove-container:
	docker rm tools-name --force

docker-remove-image:
	docker image rm image-name

kube-deploy:
	kubectl apply -f ./kube/tools-name-deployment.yml
	kubectl apply -f ./kube/tools-name-service.yml

kube-delete:
	kubectl delete all --all

kube-port-forward-db:
	kubectl port-forward service/tools-name port:port
```

##

**<p align="center"> [Top](#title) </p>**