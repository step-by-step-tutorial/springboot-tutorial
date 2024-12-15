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

OAuth is a delegation protocol, a means of letting someone who controls a resource allow a software application to
access that resource on their behalf without impersonating them. The application requests authorization from the owner
of the resource and receives tokens that it can use to access the resource. This all happens without the application
needing to impersonate the person who controls the resource, since the token explicitly represents a delegated right of
access.

### What is OAuth 2.0?

The OAuth 2.0 authorization framework enables a third-party application to obtain limited access to an HTTP service,
either on behalf of a resource owner by orchestrating an approval interaction between the resource owner and the HTTP
service, or by allowing the third-party application to obtain access on its own behalf.

### Token

#### Bearer Token

A security token with the property that any party in possession of the token (a "bearer") can use the token in any way
that any other party in possession of it can. Using a bearer token does not require a bearer to prove possession of
cryptographic key material.

#### Access Token

An access token is issued by the authorization server and the client can access the protected resources. Type of access
tokens used with OAuth2 are Bearer.
In order to send an access token to the client by authorization server, there are three method as follows:

1- Use the “Authorization” header field in the HTTP request.

```text
Example:	
   GET /resource HTTP/1.1
   Host: server.example.com
   Authorization: Bearer mF_9.B5f-4.1JqM
```

2- Use HTTP request body, To achieve that it must set the “Content-Type” header field with
“application/x-www-form-urlencoded” and assign the token value to the access-token parameter in the request body.

```text
Example:					
   header
   POST /resource HTTP/1.1
   Host: server.example.com
   Content-Type: application/x-www-form-urlencoded
   
   body
   access_token=mF_9.B5f-4.1JqM
```

3- URI query parameter, so to get the access token, the authorization server sends the token as part of URI via a
parameter named access_token.

```text
Example:
    GET /resource?access_token=mF_9.B5f-4.1JqM HTTP/1.1
    Host: server.example.com
    
    https://server.example.com/resource?access_token=mF_9.B5f-4.1JqM&p=q
```

Tip
When the client uses the URI query parameter, it should use the “Cache-Control” header field containing “no-store”.

Error
If the client send a request to the resource server without “Authorization” header field or access_token parameter, the
resource server must send a response with “WWW-Authenticate” header field

```text
Example:
HTTP/1.1 401 Unauthorized
WWW-Authenticate: Bearer realm="example"
```

It means the client was not authenticated by the authorization server.
If the client use the expired access token then the resource server send a response as follows:

```text
HTTP/1.1 401 Unauthorized
WWW-Authenticate: Bearer realm="example",					
                      error="invalid_token",
                       error_description="The access token expired"
```

**Example of Access Token**

```text
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8
Cache-Control: no-store
Pragma: no-cache

{
"access_token":"mF_9.B5f-4.1JqM",
"token_type":"Bearer",
"expires_in":3600,
"refresh_token":"tGzv3JOkF0XG5Qx2TlKWIA"				
}
```

**Security Consideration**

* The client must validate the TLS certificate chain when making requests to protected resources.
* The client must always use the TLS (HTTPS) or the other transport security.
* Token never stores cookies that can be sent in the clear.
* Token servers should issue short-lived (one hour or less) bearer tokens.
* Bearer tokens should not be passed in page URLs.
* Issue scoped bearer tokens

#### Refresh Token

A refresh token is a credential used to obtain access token when current access token becomes expired or invalid, also
it is used to obtain additional access token with identical or narrow scope.
Refresh tokens are intended for use only with authorization servers and are never sent to the resource server.

### Roles

The protected resource is the component that the resource owner has access to. This can take many different forms, but
for the most part it’s a web API of some kind, these APIs can allow read, write, and other operations just as well.

OAuth 2.0 defines four roles:

#### Resource owner (the User)

An entity capable of granting access to a protected resource, when the resource owner is a person, it is referred to as
an end-user.

#### Resource server (the API server)

The server hosting the protected resources, capable of accepting and responding to protected resource requests using
access tokens.

#### Authorization server

The server issues access tokens to the client after successfully authenticating the resource owner and obtaining
authorization.

#### Client

An application making protected resource requests on behalf of the resource owner and with its authorization.

#### Authorization-Code Flow

<p align="justify">

<img src="https://github.com/step-by-step-tutorial/springboot-tutorial/blob/main/security-oauth2/doc/authorization-code-flow.png" alt="OAuth 2.0 Authorization-Code Flow" width="50%" height="50%">

</p>

### Authorization Grant

An authorization grant type is a credential that represents an access method to get a token from the authorization
server. In [RFC 6749] defined four grant types, there is also a mechanism in order to define additional types and every
grant type supports the protocol flow.

#### Authorization Code Grant

The client directs the resource owner to the authorization server to get the access token, authorization server
authenticates the resource owner and obtains the authorization then shares the token with the client.

**Tip**

* Only the resource owner can authenticate to the authorization server.
* The authorization server transmits access token directly to the client without passing it to the resource owner.
* The authorization server has a potential to expose the access token to others such as the resource owner.

#### Implicit Grant

The Implicit grant type is a simplified authorization code flow optimized for a client implemented as an in-browser
application.
In the Implicit grant type mechanism it does not need to authenticate the client by authorization server. In some cases
use the redirect URI in order to validate the client.

#### Resource Owner Password Credentials Grant

The client uses resource owner password credentials directly to obtain access tokens from the authorization server. Use
this grant type when there is a high degree of trust between resource owner and client.

#### Client Credentials Grant

The Client credentials grant type is used by clients to obtain an access token typically the protected resources are
under the control of the client.

It is not recommended to use this grant type to access a user's resources.
As well as exist an extensibility mechanism for defining additional types.

#### **Which OAuth 2.0 grant should be used?**

A grant is a method of acquiring an access token. Deciding which grants to implement depends on the type of client the
end user will be using, and the experience you want for your users.

<p align="justify">

<img src="https://github.com/step-by-step-tutorial/springboot-tutorial/blob/main/security-oauth2/doc/oauth2-usage.png" alt="OAuth 2.0 Usage" width="75%" height="75%">

</p>

### Client

The clients must have client type and redirection URI.

#### Client Types

* Confidential
* Public

### Data Model

#### Authorization Server

The following data elements are stored or accessible on the authorization server:

* usernames
* passwords
* client ids
* secrets
* client-specific refresh tokens
* client-specific access tokens
* HTTPS certificate/key
* per-authorization process: "redirect_uri", "client_id", "authorization code"

#### Resource Server

The following data elements are stored or accessible on the resource server:

* user data
* HTTPS certificate/key
* either authorization server credentials or authorization server shared secret/public key
* access tokens (per request)

It is assumed that a resource server has no knowledge of refresh tokens, user passwords, or client secrets.

#### Client

* client id (and client secret or corresponding client credential)
* one or more refresh tokens (persistent) and access tokens (transient) per end user or other security-context or
  delegation context
* trusted certification authority (CA) certificates (HTTPS)
* per-authorization process: "redirect_uri", authorization "code"

### References

* [OAuth.Net](https://oauth.net/2)
* [The OAuth 2.0 Authorization Framework (RFC 6749)](https://tools.ietf.org/html/rfc6749)
* [Bearer Token Usage (RFC 6750)](https://tools.ietf.org/html/rfc6750)
* [OAuth Security Topics](https://tools.ietf.org/html/draft-ietf-oauth-security-topics-11)
* [YANG Data Models](https://tools.ietf.org/html/draft-ietf-teas-yang-te-18)

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