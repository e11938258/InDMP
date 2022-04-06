# InDMP

The software was developed by Filip Zoubek (https://orcid.org/0000-0003-1269-2668).

## Table of contents

* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [How to run](#how-to-run)
* [Test cases](#test-cases)
* [Limitations](#limitations)
* [License](#license)

## General info

Integrated The Machine-actionable Data Management planning application (InDMP) serves as a proof-of-concept for the thesis Framework for integration of RDM services using machine-actionable DMPs. The application allows to integrate RDM services using maDMPs to exchange information using a REST API, manage which services can modify what as well as to track the evolution of DMPs and provenance of information. The repository contains the mentioned application for integration and also the json file in which the test cases are stored ready for uploading to Postman. You can see the structure of the repository in the following listing:

```
indmp-app
│   src
│   LICENSE
│   pom.xml
│   README.md
│   test-cases.json
```

API documentation is also available on this site: https://app.swaggerhub.com/apis/e11938258/InDMP/1.0.0#/

## Technologies
The application was developed using Spring Boot in Java programming language version 11.0.13. The authorization between InDMP and Postman (client) is done using the OAuth2 protocol, where [Keycloak](https://www.keycloak.org/) 6.0.1 was used as the authorization server. InDMP also uses the PostgreSQL 10.19 tool as information storage and [Temporal Tables Extension](https://github.com/arkhipov/temporal_tables) 1.2.0 for data versioning. Tests were performed operating system on Ubuntu 18.04.6.

## Setup

The InDMP application has configuration file in /src/main/resources/application.properties. By default, all applications are configured that can run on the same machine with ports:

| Address| Service name |
| - | - |
| 127.0.0.1:8080 | InDMP app |
| 127.0.0.1:8090 | Keycloak |
| 127.0.0.1:5432 | PostgreSQL database |

#### PostgreSQL

The InDMP application uses the PostgreSQL 10 database system with [Temporal Tables Extension](https://github.com/arkhipov/temporal_tables) for versioning maDMP information. First you need to install this extension by the instructions on the developer site. After installation and logging into the system, check the following actions:

1. Create a new user:

```SQL
CREATE USER indmp WITH PASSWORD 'indmp123';
```

2. Create a new database:

```SQL
CREATE DATABASE indmp WITH ENCODING 'UNICODE' LC_COLLATE 'C' LC_CTYPE 'C' TEMPLATE template0;
```

3. Grant all privileges on database to the user:

```SQL
GRANT ALL PRIVILEGES ON DATABASE indmp TO indmp;
```

4. Connect into the database:

```SQL
\c indmp
```

5. Activate Temporal Tables Extension for the database:

```SQL
CREATE EXTENSION temporal_tables;
```

6. Grant execution on versioning function to the user:

```SQL
GRANT EXECUTE ON FUNCTION versioning() TO indmp;
```

That's it! By default, InDMP uses the following configuration:

| Property | Value |
| - | - |
| Host | 127.0.0.1 |
| Port | 5432 |
| Database | indmp |
| Username | indmp |
| Password | indmp123 |

WARNING: The InDMP application deletes the content of the tables each time it starts.

#### Keycloak

Postman is authorized before communicating with InDMP using the OAuth2 protocol. In our case, it is done using the [Keycloak](https://www.keycloak.org/) application, which needs to be set up properly after installation.

WARNING: The default port of the application is 8080, it is necessary to change it to port 8090 before starting the application, for example using the input argument:

```
-Djboss.socket.binding.port-offset=10
```

1. Login to the Keycloak app, URL: http://127.0.0.1:8090/auth/admin/master/console/
2. Create a new realm with name "Services"
3. Select the realm "Services"
4. Create a new client scope with name "update"
5. Create a new client: "indmp_service" with settings:

| Property | Value |
| - | - |
| Client protocol | openid-connect |
| Valid Redirect URIs | https://oauth.pstmn.io/v1/* |
| Access Token Lifespan | 1 day |
| Assigned Default Client Scopes | update |

5. Create two new users: "dmp_app_1" and "repository_app_1" with same password.

That's it! 

When creating users, each of them will get a client id, which the InDMP application will use to recognize from which service the request was sent.

## How to run

If PostgreSQL and Keycloak setup are running, you can run the InDMP application using the following commands in terminal:

```console
> mvn clean package
> java -jar target/indmp-app-1.0.0.jar
```

## Test cases

To verify the functionality of InDMP, a set of functional and non-functional test cases were created to model common situations in DMP development during the research.

#### How to run

#### Functional test cases


#### Non-Functional test cases


## Limitations

The current version serves only as a proof-of-concept solution to verify the functionality of the proposed architecture. The implementation can register a service and restrict its rights to specific maDMP classes. It can also update the DMP using maDMP within registered and authorized services as well as change the identifier and delete the class instance. It can also list the history of identifiers. For testing purposes, a method for getting maDMP from the InDMP application is also implemented.

However, the solution cannot get older maDMP versions, it synchronizes the information between all registered services, therefore it does not restrict services to a specific maDMP, and also privileges are restricted only to classes not to specific properties.

## License

InDMP is licensed under the [MIT license](https://github.com/e11938258/InDMP/blob/main/LICENSE).
