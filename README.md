# InDMP

The software was developed by Filip Zoubek (https://orcid.org/0000-0003-1269-2668).

## Table of contents

* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [How to run](#how-to-run)
* [Limitations](#limitations)
* [Test cases](#test-cases)
* [License](#license)

## General info

Integrated The Machine-actionable Data Management planning application (InDMP) serves as a proof-of-concept for the thesis Framework for integration of RDM services using machine-actionable DMPs. The application allows to integrate RDM services using maDMPs to exchange information using a REST API, manage which services can modify what as well as to track the evolution of DMPs and provenance of information. The repository contains the mentioned application for integration and also the json file in which the test cases are stored ready for uploading to the Postman application. You can see the structure of the repository in the following listing:

```
indmp-app
│   src
│   LICENSE
│   pom.xml
│   README.md
│   test-cases.json
```

There is alsi API documentation of InDMP available on this site: https://app.swaggerhub.com/apis/e11938258/InDMP/1.0.0#/

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

5. Create two new users: "dmp_app_1" and "repository_app_1" with password same as username.

That's it! 

When creating users, each of them will get a client id, which the InDMP application will use to recognize from which service the request was sent.

## How to run

If PostgreSQL and Keycloak setup are running, you can firstly build the InDMP application using the following command in terminal:

```console
> mvn clean package
```

and then run:

```console
> java -jar target/indmp-app-1.0.0.jar
```

## Limitations

The current version serves only as a proof-of-concept solution to verify the functionality of the proposed architecture. The implementation can register a service and restrict its rights to specific maDMP classes. It can also update the DMP using maDMP within registered and authorized services as well as change the identifier and delete the class instance. It can also list the history of identifiers and get current version of maDMP from the InDMP application.

However, the solution cannot get older maDMP versions, it synchronizes the information between all registered services, therefore it does not restrict services to a specific maDMP, and also privileges are restricted only to classes not to specific properties.

## Test cases

To verify the functionality of InDMP, a set of functional and non-functional test cases were created to model common situations in DMP development during the research. If you have Postman installed, import the test cases into your environment via the File menu. You will need to obtain a token from the authorization server before running the test cases which is obtained at the collection level, where the necessary information is preloaded. There are also general variables that can be changed at will as needed. You will then need to send two requests in the Init folder that register services to the InDMP application, however, you need to change the values of accessRights, to the correct client id from the application Keycloak, and endpointURL which should point to the endpoint where InDMP will send the new maDMP. For testing purposes, this can be done using [Webhook.site](https://webhook.site/) , which will generate an API endpoint to receive requests. You can just create one for both services.

#### Modification scope of services

In order to understand all the tests, it is necessary to mention the modification scope of each service, you can see them in the following table:

| maDMP class | DMP app | Repository app |
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

### Test case descriptions

In the following two tables you can see the individual functional and non-functional test cases with steps and expected results. Each test case has its own folder in Postman, which consists of several consecutive requests. Requests also contain tests to verify the correctness of the response.

#### Functional test cases

| Test case | Description | Steps | Expected code | Expected body |
| - | - | - | - | - |
| FTC1 | Testing the operation "maDMP update" | Send minimal maDMP with incomplete body | 400 |  |
|  |  | Send a new minimal maDMP with wrong timing | 404 |  |
|  |  | Send a new minimal maDMP | 200 |  |
|  |  | Send a new minimal maDMP again with same ID | 409 |  |
|  |  | Send the minimal maDMP with invalid modified property | 409 |  |
|  |  | Send long maDMP with correct ID | 200 |  |
| FTC2 | Testing the operation "get maDMP" | Send a new minimal maDMP | 200 |  |
|  |  | Get maDMP with incomplete parameters | 400 |  |
|  |  | Get maDMP with wrong parameters | 400 |  |
|  |  | Get maDMP with wrong identifiers | 404 |  |
|  |  | Get current version of maDMP | 200 | n. 1 |
| FTC3 | Testing the operation "update identifier" | Send a new maDMP with dataset | 200 |  |
|  |  | Update identifier with incomplete parameters | 400 |  |
|  |  | Update identifier with incomplete body | 400 |  |
|  |  | Update identifier with wrong specialization | 400 |  |
|  |  | Update identifier of dataset | 200 |  |
|  |  | Get current version of maDMP and verify the modification | 200 | n. 2 |
| FTC4 | Testing the operation "delete instance" | Send a new maDMP with dataset | 200 |  |
|  |  | Delete instance with incomplete parameters | 400 |  |
|  |  | Delete instance without body | 400 |  |
|  |  | Delete instance with wrong identifier | 404 |  |
|  |  | Delete instance with wrong class identifier | 404 |  |
|  |  | Delete instance | 200 |  |
|  |  | Get current version of maDMP and verify the deletions | 200 | n. 3 |
| FTC5 | Testing the operation "get identifier history" | Send a new maDMP with dataset | 200 |  |
|  |  | Update identifier of dataset | 200 |  |
|  |  | Get identifier history with wrong parameters | 400 |  |
|  |  | Get identifier history | 200 | n. 4 |
| FTC6 |  |  |  |  |
|  |  |  |  |  |
| FTC7 |  |  |  |  |
|  |  |  |  |  |

##### Expected bodies

###### 1.

```json
{
    "dmp": {
        "created": "2022-04-14T10:00:50.000",
        "modified": "2022-04-14T10:00:50.000",
        "contributor": [],
        "cost": [],
        "dataset": [],
        "dmp_id": {
            "identifier": "https://doi.org/10.0000/00.0.1234"
        },
        "project": []
    }
}
```

###### 2.

```json
{
    "dmp": {
        "created": "2022-04-14T13:33:07.000",
        "modified": "2022-04-14T13:33:07.000",
        "contributor": [],
        "cost": [],
        "dataset": [
            {
                "description": "Some test scripts",
                "personal_data": "no",
                "sensitive_data": "no",
                "title": "Client application",
                "type": "Source code",
                "dataset_id": {
                    "identifier": "https://hdl.handle.net/0000/00.1234",
                    "type": "handle"
                },
                "distribution": [
                    {
                        "access_url": "https://hdl.handle.net/0000",
                        "available_until": "2030-09-30",
                        "byte_size": 1000000000,
                        "data_access": "open",
                        "title": "Planned distribution",
                        "host": {
                            "description": "GitHub is the best place to share code with friends, co-workers, classmates, and complete strangers. Over three million people use GitHub to build amazing things together. With the collaborative features of GitHub.com, our desktop and mobile apps, and GitHub Enterprise, it has never been easier for individuals and teams to write better code, faster. Originally founded by Tom Preston-Werner, Chris Wanstrath, and PJ Hyett to simplify sharing code, GitHub has grown into the largest code host in the world.",
                            "pid_system": [
                                "other"
                            ],
                            "storage_type": "repository",
                            "title": "GitHub",
                            "url": "https://www.re3data.org/repository/r3d100010375"
                        },
                        "license": [
                            {
                                "license_ref": "http://opensource.org/licenses/mit-license.php",
                                "start_date": "2020-09-30"
                            }
                        ]
                    }
                ],
                "metadata": [],
                "security_and_privacy": [],
                "technical_resource": []
            }
        ],
        "dmp_id": {
            "identifier": "https://doi.org/10.0000/11.2.22"
        },
        "project": []
    }
}
```

###### 3.

```json
{
    "dmp": {
        "created": "2022-04-14T14:48:39.000",
        "modified": "2022-04-14T14:48:39.000",
        "contributor": [],
        "cost": [],
        "dataset": [],
        "dmp_id": {
            "identifier": "https://doi.org/10.0000/33.3.12"
        },
        "project": []
    }
}
```

###### 4.

```json
[
    {
        "atLocation": "/https://doi.org/10.0002/11.1.123",
        "specializationOf": "dmp:identifier",
        "value": "https://doi.org/10.0002/11.1.123",
        "wasGeneratedBy": {
            "startedAtTime": "2022-04-14T14:55:40.000",
            "wasAssociatedWith": {
                "identifier": 1,
                "title": "dmp_app_1"
            }
        }
    },
    {
        "atLocation": "/https://doi.org/10.0002/11.1.123/https://hdl.handle.net/0000/00.1234",
        "specializationOf": "dataset:identifier",
        "value": "https://hdl.handle.net/0000/00.1234",
        "wasGeneratedBy": {
            "startedAtTime": "2022-04-14T14:55:41.000",
            "wasAssociatedWith": {
                "identifier": 1,
                "title": "dmp_app_1"
            }
        }
    },
    {
        "atLocation": "/https://doi.org/10.0002/11.1.123/https://hdl.handle.net/0000/00.00000",
        "specializationOf": "dataset:identifier",
        "value": "https://hdl.handle.net/0000/00.00000",
        "wasGeneratedBy": {
            "startedAtTime": "2022-04-14T14:55:40.000",
            "endedAtTime": "2022-04-14T14:55:41.000",
            "wasAssociatedWith": {
                "identifier": 1,
                "title": "dmp_app_1"
            }
        }
    },
    {
        "atLocation": "/https://doi.org/10.0002/11.1.123/https://hdl.handle.net/0000/00.00000",
        "specializationOf": "dataset:identifier",
        "value": "https://hdl.handle.net/0000/00.1234",
        "wasGeneratedBy": {
            "startedAtTime": "2022-04-14T14:55:41.000",
            "wasAssociatedWith": {
                "identifier": 1,
                "title": "dmp_app_1"
            }
        }
    },
    {
        "atLocation": "/https://doi.org/10.0002/11.1.123/https://hdl.handle.net/0000/00.1234/https://hdl.handle.net/0000",
        "specializationOf": "distribution:access_url",
        "value": "https://hdl.handle.net/0000",
        "wasGeneratedBy": {
            "startedAtTime": "2022-04-14T14:55:40.000",
            "wasAssociatedWith": {
                "identifier": 1,
                "title": "dmp_app_1"
            }
        }
    },
    {
        "atLocation": "/https://doi.org/10.0002/11.1.123/https://hdl.handle.net/0000/00.00000/https://hdl.handle.net/0000",
        "specializationOf": "distribution:access_url",
        "value": "https://hdl.handle.net/0000",
        "wasGeneratedBy": {
            "startedAtTime": "2022-04-14T14:55:40.000",
            "wasAssociatedWith": {
                "identifier": 1,
                "title": "dmp_app_1"
            }
        }
    }
]
```

#### Non-Functional test cases

| Test case | Description | Steps | Expected code | Expected body |
| - | - | - | - | - |
| NFTC1 |  |  |  |  |
|  |  |  |  |  |

## License

InDMP is licensed under the [MIT license](https://github.com/e11938258/InDMP/blob/main/LICENSE).
