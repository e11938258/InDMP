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

Integrated The Machine-actionable Data Management planning application (InDMP) serves as a proof-of-concept for the thesis Framework for integration of RDM services using machine-actionable DMPs. The application allows to integrate RDM services using maDMPs to exchange information using a REST API, manage which services can modify what as well as to track the evolution of DMPs and provenance of information. The repository contains the mentioned application for integration (indmp-app) and an application (app-simulation) that simulates the behaviour of the DMP tool and the repository using test cases. You can see the structure of the repository in the following listing:

```
indmp-app
│   README.md
│   LICENSE
└───app-simulation
│   │   src
│   │   pom.xml
└───indmp-app
│   │   src
│   │   pom.xml
```

## Technologies
The applications were developed using Spring Boot in Java programming language version 11.0.13. The authorization between the  application for simulations and InDMP is done using the OAuth2 protocol, where [Keycloak](https://www.keycloak.org/) 6.0.1 was used as the authorization server. InDMP also uses the PostgreSQL 10.19 tool as information storage and [Temporal Tables Extension](https://github.com/arkhipov/temporal_tables) 1.2.0 for data versioning. Tests were performed operating system on Ubuntu 18.04.6.

## Setup

InDMP application has configuration file in /src/main/resources/application.properties. The simulation application has a basic configuration in the same location but in addition has two more configurations, /src/main/resources/application-dmp.properties and /src/main/resources/application-repository.properties, which are for the specific service they are supposed to simulate. For this reason, the second application is run twice each time as a simulation of a different tool. By default, they are configured  that all services run on the same machine with ports:

| Address| Service name |
| - | - |
| 127.0.0.1:8080 | InDMP app |
| 127.0.0.1:8081 | DMP app simulation |
| 127.0.0.1:8082 | Repository app simulation |
| 127.0.0.1:8090 | Keycloak |
| 127.0.0.1:5432 | PostgreSQL database |

#### PostgreSQL

The application InDMP uses the PostgreSQL 10 database system with [Temporal Tables Extension](https://github.com/arkhipov/temporal_tables) for versioning maDMP information. First you need to install this extension by the instructions on the developer site. After installation and logging into the system, check the following actions:

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

That's it! By default, the application uses the following configuration:

| Property | Value |
| - | - |
| Host | localhost |
| Port | 5432 |
| Database | indmp |
| Username | indmp |
| Password | indmp123 |

WARNING: The InDMP application deletes the content of the tables each time it starts.

#### Keycloak

RDM simulation services are authorized before communicating with InDMP using the OAuth2 protocol. In our case, it is done using the [Keycloak](https://www.keycloak.org/) application, which needs to be set up properly after installation.

WARNING: The default port of the application is 8080, it is necessary to change it to port 8090 before starting the application, for example using the input argument:

```
-Djboss.socket.binding.port-offset=10
```

1. Login to the Keycloak app, URL: http://127.0.0.1:8090/auth/admin/master/console/
2. Create a new realm with name "Services"
3. Select the realm "Services"
4. Create a new Client Scope with name "update"
5. Create two new clients: "dmp_app" and "repository_app" with settings:

| Property | For client | Value |
| - | - | - |
| Client protocol | dmp_app, repository_app | openid-connect |
| Valid Redirect URIs | dmp_app | http://127.0.0.1:8081/* |
| | repository_app | http://127.0.0.1:8082/* |
| Access Token Lifespan | dmp_app, repository_app | 1 day |
| Assigned Default Client Scopes | dmp_app, repository_app | update |

5. Create two new users: "dmp_app_1" and "repository_app_1" with passwords.

That's it! The first time you call a request, you will be redirected to a login page where you will enter your login details (depending on the service you are calling - dmp_app_1 or repository_app_1) to receive a token. When creating users, each of them will get a client id, which the InDMP application will use to recognize from which service the request was sent.

## How to run

If PostgreSQL and Keycloak setup are running, you can run individual applications using the following commands, one application per terminal:

1. Run InDMP

```console
> cd ./indmp-app
> mvn clean package
> java -jar target/INDMP-1.0.0.jar
```

2. Run app simulation with configuration for DMP (default)

```console
> cd ./app-simulation
> mvn clean package
> java -jar target/app-simulation-1.0.0.jar
```

3. Authorize the service using the acccount dmp_app_1 and register to InDMP, use URL: http://127.0.0.1:8081/init

4. Run app simulation with configuration for repository

```console
> cd ./app-simulation
> java -jar target/app-simulation-1.0.0.jar --spring.profiles.active=repository
```

5. Authorize the service using the acccount repository_app_1 and register to InDMP, use URL IN ANOTHER BROWSER or in an incognito window, since you are already logged in as dmp_app_1 in your current browser: http://127.0.0.1:8082/init .

## Test cases

To verify the functionality of InDMP, a set of functional and non-functional test cases was created to model common situations in DMP development during the research. With specific exceptions, every test case can be execute using a GET request to the DMP or repository service simulation app, therefore it can be run from any browser. Because of OAuth authorization, it is necessary to use two separate browsers each for one service. The result can be read from the response. For further verification, the log of all three applications can be found within folders.

#### Modification scope of services

In order to understand all the tests, it is necessary to mention the modification scope of each service, you can see them in the following table:

| maDMP class | DMP app simulation | Repository app simulation |
| - | - | - |
| contact | Yes | Yes |
| contributor | Yes | Yes |
| cost | Yes | No |
| dataset | Yes | Yes |
| distribution | Yes | Yes |
| dmp | Yes | No |
| funding | Yes | No |
| grant id | Yes | No |
| host | Yes | Yes |
| license | Yes | Yes |
| metadata | Yes | Yes |
| project | Yes | No |
| security and privacy | Yes | Yes |
| technical resource | Yes | No |

However, if the DMP is new, all values from the received data are stored.

#### Functional test cases

In the following table you can see the individual functional test cases with a short description and how to run it (URL). You can run these test cases from the service in which they are implemented.

| Test case | Name | Service scope | URL | Description |
| - | - | - | - | - |
| FTC1 | Send minimal maDMP | Both | http://localhost:8081/ftc1 or http://localhost:8082/ftc1 | Send maDMP with mandatory minimum properties |
| FTC2 | Send incomplete maDMP | Both | http://localhost:8081/ftc2 or http://localhost:8082/ftc2 | Send maDMP with incomplete mandatory minimum |
| FTC3 | Send maDMP out of modification scope | Repository app (only) | http://localhost:8082/ftc3 | Send maDMP with properties outside the modification scope of the repository |
| FTC4 | Send long maDMP | Both | http://localhost:8081/ftc4 or http://localhost:8082/ftc4 | Send maDMP with lots of property information |
| FTC5 | Change identifier of dataset | Both | http://localhost:8081/ftc5 or http://localhost:8082/ftc5 | Send maDMP with dataset information |
| FTC6 | Delete dataset instance | Both | http://localhost:8081/ftc6 or http://localhost:8082/ftc6  | Send maDMP with dataset information and then send a request to delete it |
| FTC7 | Get history of identifiers | Both | http://localhost:8081/ftc7 or http://localhost:8082/ftc7  | Send maDMP with datasets and distributions, then send an identifier change request, and finally send an identifier history request |

#### Non-Functional test cases

In the following table you can see the individual non-functional cases with a short description and how to run it (URL).

| Test case | Service scope | URL | Description |
| - | - | - | - |
| NFTC1 | Both | http://localhost:8081/nftc1 or http://localhost:8082/nftc1| Examine response time of updating minimal maDMP  |
| NFTC2 | Both | http://localhost:8081/nftc2 or http://localhost:8082/nftc2 | Examine response time of updating long maDMP |

## Limitations

The current version serves only as a proof-of-concept solution to verify the functionality of the proposed architecture. The implementation can register a service and restrict its rights to specific maDMP classes. It can also update the DMP using maDMP within registered and authorized services as well as change the identifier and delete the class instance. It can also list the history of identifiers. For testing purposes, a method for getting maDMP from the InDMP application is also implemented.

However, the solution cannot get older maDMP versions, it synchronizes the information between all registered services, therefore it does not restrict services to a specific maDMP, and also does not resolve user errors, such as sending a maDMP with an older modified date than the current version. At the same time, modification scope of services is not considered in the operations for identifier change and instance deletion, and privileges are restricted only to classes not to specific properties, where only exception is the modified property, which has to change all services.

## License

InDMP is licensed under the [MIT license](https://github.com/e11938258/InDMP/blob/main/LICENSE).
