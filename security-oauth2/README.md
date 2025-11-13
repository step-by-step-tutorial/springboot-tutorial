# <p align="center"> Implement OAuth 2.0 Server and Client</p>

<p style="text-align: justify;">

This tutorial is about implementing OAuth 2.0 Server and Client with Springboot framework.

</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [OAuth 2.0](#oauth-20)
* [Install Applications on Docker](#install-appllications-on-docker)
* [Install Applications on Kubernetes](#install-appllications-on-kubernetes)
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

```shell
openssl genpkey -algorithm RSA -out private-key.pem
openssl rsa -pubout -in private-key.pem -out public-key.pem
openssl pkcs8 -topk8 -inform PEM -outform PEM -in private-key.pem -out private-key.pem -nocrypt
```

#### Build

```shell
# server
mvn -f security-oauth2-server/pom.xml clean package -DskipTests=true
```

```shell
# client
mvn -f security-oauth2-client/pom.xml clean package -DskipTests=true
```

#### Test

```shell
# server
mvn -f security-oauth2-server/pom.xml test
```

```shell
# client
mvn -f security-oauth2-client/pom.xml test
```

#### Run

```shell
# server
mvn -f security-oauth2-server/pom.xml spring-boot:run
```

### Test Client

#### Register the Client

For a single server version.

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

For server and client version.

```shell
curl -v -X POST "http://localhost:8080/api/v1/clients" \
  -u "test:test" \
  -H "Content-Type: application/json" \
  -d '{
        "clientId": "testClient",
        "clientSecret": "password",
        "redirectUri": "http://securityoauth2client.localhost/login/oauth2/code/testClient",
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

Copy the authorization code displayed in the console after executing the command.

```shell
curl -v -X GET "http://localhost:8080/oauth2/authorize?response_type=code&client_id=testClient&scope=read&redirect_uri=http://localhost:8080/login/oauth2/code/testClient" \
  -b cookies.txt \
  -H "Content-Type: application/x-www-form-urlencoded"

```

#### Get a JWT Bearer Token

Assign the authorization code from the last step to the parameter named code.
Copy the access token displayed in the console after executing the command.

```shell
 curl -X POST "http://localhost:8080/oauth2/token" \
  -u "testClient:password" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=authorization_code" \
  -d "code=???" \
  -d "redirect_uri=http://localhost:8080/login/oauth2/code/testClient"
```

#### Request a Protected Resource

Assign the access token from the last step in front of Bearer.

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
WWW-Authenticate: Bearer realm="example", error="invalid_token", error_description="The access token expired"
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

<p style="text-align: justify;">

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

<p style="text-align: justify;">

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
version: "3.8"

services:
  nginx:
    container_name: nginx
    image: nginx
    restart: always
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - "./nginx.conf:/etc/nginx/conf.d/default.conf"
  securityoauth2client:
    image: samanalishiri/securityoauth2client:latest
    build:
      context: security-oauth2-client
      dockerfile: /Dockerfile
    container_name: securityoauth2client
    hostname: securityoauth2client
    restart: always
    ports:
      - "8081:8081"
    environment:
      APP_HOST: "0.0.0.0"
      APP_PORT: "8081"
      AUTHORIZATION_SERVER: "http://securityoauth2server.localhost"
      RESOURCE_SERVER: "http://securityoauth2server:8080"
      CLIENT_APP: "http://securityoauth2client.localhost"
      APP_PROFILES: h2
  securityoauth2server:
    image: samanalishiri/securityoauth2server:latest
    build:
      context: security-oauth2-server
      dockerfile: /Dockerfile
    container_name: securityoauth2server
    hostname: securityoauth2server
    restart: always
    ports:
      - "8080:8080"
    environment:
      APP_HOST: "0.0.0.0"
      APP_PORT: "8080"
      AUTHORIZATION_SERVER: "http://securityoauth2server.localhost"
      RESOURCE_SERVER: "http://securityoauth2server:8080"
      APP_PROFILES: h2
```

### Apply Docker Compose

Execute the following command to install Applications.

```shell
docker compose --file ./docker-compose.yml --project-name oauth-20 up --build -d
```

## Install Applications on Kubernetes

Create the following files for installing Applications.

### Kube Files

[postgres-pvc.yml](/kube/postgres-pvc.yml       )

```yaml
#pvc.yml
``` 

[postgres-configmap.yml](/kube/postgres-configmap.yml )

```yaml
#configmap.yml
``` 

[postgres-secrets.yml](/kube/postgres-secrets.yml   )

```yaml
#secrets.yml
``` 

[postgres-deployment.yml](/kube/postgres-deployment.yml)

```yaml
#deployment.yml
``` 

[postgres-service.yml](/kube/postgres-service.yml   )

```yaml
#service.yml
```

[pgadmin-secrets.yml](/kube/pgadmin-secrets.yml    )

```yaml
#secrets.yml
``` 

[pgadmin-deployment.yml](/kube/pgadmin-deployment.yml )

```yaml
#deployment.yml
``` 

[pgadmin-service.yml](/kube/pgadmin-service.yml    )

```yaml
#service.yml
```

[server-deployment.yml](/kube/server-deployment.yml  )

```yaml
#deployment.yml
```

[server-service.yml](/kube/server-service.yml     )

```yaml
#service.yml
```

[client-deployment.yml](/kube/client-deployment.yml  )

```yaml
#deployment.yml
```

[client-service.yml](/kube/client-service.yml     )

```yaml
#service.yml
```

### Apply Kube Files

Execute the following commands to install the tools on Kubernetes.

```shell
kubectl apply -f ./kube/postgres-pvc.yml
kubectl apply -f ./kube/postgres-configmap.yml
kubectl apply -f ./kube/postgres-secrets.yml
kubectl apply -f ./kube/postgres-deployment.yml
kubectl apply -f ./kube/postgres-service.yml
kubectl apply -f ./kube/pgadmin-secrets.yml
kubectl apply -f ./kube/pgadmin-deployment.yml
kubectl apply -f ./kube/pgadmin-service.yml
kubectl apply -f ./kube/server-deployment.yml
kubectl apply -f ./kube/server-service.yml
kubectl apply -f ./kube/client-deployment.yml
kubectl apply -f ./kube/client-service.yml
```

### Check Status

```shell
kubectl get all
```

### Port Forwarding

<p style="text-align: justify;">

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

## Appendix

### Makefile

```shell
build:
	mvn -f security-oauth2-server/pom.xml clean package -DskipTests=true
	mvn -f security-oauth2-client/pom.xml clean package -DskipTests=true

test:
	mvn -f security-oauth2-server/pom.xml test
	mvn -f security-oauth2-client/pom.xml test

run:
	mvn -f security-oauth2-server/pom.xml spring-boot:run

docker-build:
	docker build -t samanalishiri/securityoauth2client:latest -f security-oauth2-server/Dockerfile .
	docker build -t samanalishiri/securityoauth2client:latest -f security-oauth2-client/Dockerfile .

DockerComposeDeploy:
	mvn -f security-oauth2-server/pom.xml clean package -DskipTests=true
	mvn -f security-oauth2-client/pom.xml clean package -DskipTests=true
	mvn -f security-oauth2-server/pom.xml test
	mvn -f security-oauth2-client/pom.xml test
	docker compose --file ./docker-compose.yml --project-name securityoauth2 up --build -d

docker-remove-container:
	docker rm securityoauth2server --force
	docker rm securityoauth2client --force
	docker rm nginx --force
	docker rm pgadmin --force
	docker rm postgres --force

DockerRemoveImage:
	docker image rm samanalishiri/securityoauth2server:latest
	docker image rm samanalishiri/securityoauth2client:latest

kube-deploy:
	kubectl apply -f ./kube/postgres-pvc.yml
	kubectl apply -f ./kube/postgres-configmap.yml
	kubectl apply -f ./kube/postgres-secrets.yml
	kubectl apply -f ./kube/postgres-deployment.yml
	kubectl apply -f ./kube/postgres-service.yml
	kubectl apply -f ./kube/pgadmin-secrets.yml
	kubectl apply -f ./kube/pgadmin-deployment.yml
	kubectl apply -f ./kube/pgadmin-service.yml
	kubectl apply -f ./kube/server-deployment.yml
	kubectl apply -f ./kube/server-service.yml
	kubectl apply -f ./kube/client-deployment.yml
	kubectl apply -f ./kube/client-service.yml

kube-delete:
	kubectl delete all --all
	kubectl delete secrets postgres-secrets
	kubectl delete configMap postgres-configmap
	kubectl delete persistentvolumeclaim postgres-pvc
	kubectl delete secrets pgadmin-secrets
	kubectl delete ingress pgadmin

kube-port-forward-server:
	kubectl port-forward service/securityoauth2server 8080:8080
kube-port-forward-client:
	kubectl port-forward service/securityoauth2client 8081:8081

kube-port-forward-postgres:
	kubectl port-forward service/postgres 5432:5432

kube-port-forward-pgadmin:
	kubectl port-forward service/pgadmin 9090:80

```

##

**<p align="center"> [Top](#implement-oauth-20-server-and-client) </p>**