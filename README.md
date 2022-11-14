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

The Integrated DMP (InDMP) application serves as a proof-of-concept for the thesis Integrating RDM services using machine-actionable Data  Management Plans. The application allows to integrate RDM services using maDMPs to exchange information through the REST API, manage property modification of each service as well as to track the evolution of DMPs and provenance of information. The repository contains the mentioned application as well as the JSON file in which the test cases are stored ready for uploading to the Postman application. You can see the structure of the repository in the following listing:

```
indmp-app
│   src
│   LICENSE
│   pom.xml
│   README.md
│   test-cases.json
```

There is API documentation of the InDMP application available on the [SwaggerHub](https://app.swaggerhub.com/apis/e11938258/InDMP/1.0.0#/).

## Technologies

The application was developed using Spring Boot in Java programming language version 11.0.13. The authorization between InDMP and Postman (client) is done using the OAuth2 protocol, where [Keycloak](https://www.keycloak.org/) 6.0.1 was used as the authorization server. The InDMP application also uses the PostgreSQL 10.19 tool as data storage. Tests were performed operating system on Ubuntu 18.04.6.

## Setup

The InDMP application has configuration file in /src/main/resources/application.properties. By default, all applications are configured that can run on the same machine with ports:

| Address| Service name |
| - | - |
| 127.0.0.1:8080 | InDMP app |
| 127.0.0.1:8090 | Keycloak |
| 127.0.0.1:5432 | PostgreSQL database |

### Keycloak

Every request has to be authorized before communicating with the application using the OAuth2 protocol. This is done using the [Keycloak](https://www.keycloak.org/) application, which needs to be set up properly after installation.

NOTE: The default port of the application is 8080, it is necessary to change it to port 8090 before starting the application, for example using the input argument:

```
-Djboss.socket.binding.port-offset=10
```

You need to perform the following steps:

1. Login to the Keycloak application, URL: http://127.0.0.1:8090/auth/admin/master/console/
2. Create a new realm with name "Services"
3. Select the realm "Services"
4. Create a new client scope with name "update"
5. Create a new client: "indmp_service" with settings:

| Property | Value |
| - | - |
| Client protocol | openid-connect |
| Valid Redirect URIs | * |
| Access Token Lifespan | 1 day |
| Assigned Default Client Scopes | update |

5. Create two new users: "dmp_app_1" and "repository_app_1" with password same as username.

That's it! 

When creating users, each of them will get a client id, which the InDMP application will use to identify the RDM service.

### PostgreSQL

The InDMP application uses the PostgreSQL 10 database system. After installation and logging into this system, you have to create a new user with database and grant neccessary privileges. Therefore, you need to perform the following actions:

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

That's it! By default, InDMP uses the following configuration:

| Property | Value |
| - | - |
| Host | 127.0.0.1 |
| Port | 5432 |
| Database | indmp |
| Username | indmp |
| Password | indmp123 |

NOTE: The InDMP application deletes the content of the tables each time it starts by default. If you want to change it, you need to modify the parameter spring.jpa.hibernate.ddl-auto in the application configuration from create-drop to update.

## How to run

If PostgreSQL and Keycloak are running and are properly configured, you first build the InDMP application using the following command in terminal:

```console
> mvn clean package
```

and then run it:

```console
> java -jar target/indmp-app-1.0.0.jar
```

## Limitations

InDMP in its current version cannot retrieve older versions of maDMP and also has insufficient management of RDM services.

## Test cases

To verify the functionality of the InDMP application, a set of functional and non-functional test cases were created to model common situations in DMP development during the research. 

If you have Postman installed, import the test cases from the repository into your environment via the File menu. You will need to obtain a token from the authorization server before running the test cases. It can be get at the collection level, where the necessary information is preloaded. There are also general variables that can be changed at will as needed. Test case 6 contains one step/request (the second one) which has a different authorization due to the modification scope test. Therefore it is necessary to generate a new token, which can be obtained at the level of this request.

Each new run of the application you have to send at least two requests in the INIT folder that register services - dmp tool and data repository - and two requests that set them property rights. However, first you need to change the values of accessRights, to the correct client id from the application Keycloak, and endpointURL which should point to the endpoint where InDMP will send the new maDMP information after each modification. For testing purposes, this can be done using the [Webhook.site](https://webhook.site/) application, which will generate an API endpoint to receive requests. You just need one for both services. Test cases can also run all at once within the entire collection.

### Modification scope of services

Rights to individual maDMP properties are set for RDM services. How the RDM service rights are set for testing purposes can be found in the INIT folder in the request bodies for setting rights.

NOTE: If the DMP is new, all values from the received data are stored.

### Functional test cases

In the following table you can see the individual functional test cases with steps and expected results. Each test case has its own folder in Postman, which consists of several consecutive requests. Requests also contain simple tests to verify the correctness of the response.

| Test case | Description | Steps | Expected code | Expected body |
| - | - | - | - | - |
| FTC1 | Test the use case to modify the maDMP information for proper functionality. | 1. Send a minimal maDMP with the incomplete body. | 400 |  |
|  |  | 2. Send a new minimal maDMP with wrong timing. | 404 |  |
|  |  | 3. Send a new minimal maDMP. | 200 |  |
|  |  | 4. Send a new minimal maDMP again with the same identifier. | 409 |  |
|  |  | 5. Send the minimal maDMP with the invalid modified property. | 409 |  |
|  |  | 6. Send a long maDMP with the same identifier. | 200 |  |
| FTC2 | Test the use case to get the maDMP for proper functionality. | 1. Send a new minimal maDMP. | 200 |  |
|  |  | 2. Get the maDMP with incomplete parameters. | 400 |  |
|  |  | 3. Get the maDMP with wrong parameters. | 400 |  |
|  |  | 4. Get the maDMP with wrong identifiers. | 404 |  |
|  |  | 5. Get the current version of the maDMP. | 200 | n. 1 |
| FTC3 | Test the use case to modify the maDMP object identifier for proper functionality. | 1. Send a new maDMP with the dataset. | 200 |  |
|  |  | 2. Update the identifier with incomplete parameters. | 400 |  |
|  |  | 3. Update the identifier with the incomplete body. | 400 |  |
|  |  | 4. Update the identifier with the wrong specialization. | 403 |  |
|  |  | 5. Update the identifier of the dataset.| 200 |  |
|  |  | 6. Get the current version of the maDMP and verify the modification. | 200 | n. 2 |
| FTC4 | Test the use case to remove the maDMP object for proper functionality. | 1. Send a new maDMP with the dataset. | 200 |  |
|  |  | 2. Delete the object with incomplete parameters. | 400 |  |
|  |  | 3. Delete the object without the body. | 400 |  |
|  |  | 4. Delete the object with the wrong identifier. | 404 |  |
|  |  | 5. Delete the object with the wrong identifier. | 400 |  |
|  |  | 6. Delete the object. | 200 |  |
|  |  | 7. Get the current version of the maDMP and verify the deletions. | 200 | n. 3 |
| FTC5 | Test the use case to get provenance information for proper functionality. | 1. Send a new maDMP with dataset. | 200 |  |
|  |  | 2. Update the identifier of the dataset. | 200 |  |
|  |  | 3. Get identifier history with wrong parameters. | 400 |  |
|  |  | 4. Get identifier history of the dataset.| 200 | n. 4 |
| FTC6 | Test the use case to set RDM service rights for proper functionality. | 1. Send a new minimal maDMP. | 200 |  |
|  |  | 2. Send a new maDMP out of the modification scope. | 200 |  |
|  |  | 3. Get the current version of the maDMP and verify the modification scope. | 200 | n. 5 |
| FTC7 | Simulate common situations that may happen in a production environment. | 1. Send a new minimal maDMP. | 200 |  |
|  |  | 2. Update the maDMP with a long body. | 200 |  |
|  |  | 3. Delete the dataset. | 200 |  |
|  |  | 4. Update the maDMP with the deleted dataset. | 200 |  |
|  |  | 5. Update the maDMP with the old modified property. | 409 |  |
| FTC8 | Simulate common situations that may happen in a production environment. | 1. Send a new long maDMP with 3 datasets. | 200 |  |
|  |  | 2. Delete the dataset 0. | 200 |  |
|  |  | 3. Delete the dataset 1. | 200 |  |
|  |  | 4. Update the identifier of the dataset 2 to 0. | 200 |  |
|  |  | 5. Delete the dataset 0. | 200 |  |
|  |  | 6. Delete project information. | 200 |  |
|  |  | 7. Get the current version of the maDMP. | 200 | n. 6 |

#### Expected bodies

##### 1.

```json
{
    "dmp": {
        "created": "2022-04-14T10:00:50.000",
        "modified": "2022-04-14T10:00:50.000",
        "contributor": [],
        "cost": [],
        "dataset": [],
        "dmp_id": {
            "identifier": "https://doi.org/10.0000/00.0.9843"
        },
        "project": []
    }
}
```

##### 2.

```json
{
    "dmp": {
        "created": "2022-04-14T13:33:07.000",
        "modified": "2022-04-14T13:45:08.000",
        "contributor": [],
        "cost": [],
        "dataset": [
            {
                "title": "Client application",
                "dataset_id": {
                    "identifier": "https://hdl.handle.net/0000/00.1234",
                    "type": "handle"
                },
                "distribution": [],
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

##### 3.

```json
{
    "dmp": {
        "created": "2022-04-14T14:48:39.000",
        "modified": "2022-04-14T14:50:20.000",
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

##### 4.

```json
[
    {
        "atLocation": "/https://doi.org/10.0002/11.1.123/https://hdl.handle.net/0000/00.1234",
        "specializationOf": "dataset:identifier",
        "value": "https://hdl.handle.net/0000/00.00000",
        "wasGeneratedBy": {
            "startedAtTime": "2022-04-14T14:55:40.000",
            "endedAtTime": "2022-04-14T14:55:41.000",
            "wasStartedBy": {
                "title": "dmp_app_1"
            },
            "wasEndedBy": {
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
            "wasStartedBy": {
                "title": "dmp_app_1"
            }
        }
    }
]
```

##### 5.

```json
{
    "dmp": {
        "created": "2022-04-25T07:04:05.000",
        "modified": "2022-04-25T07:27:43.000",
        "contributor": [],
        "cost": [],
        "dataset": [],
        "dmp_id": {
            "identifier": "https://doi.org/10.0002/17.7.189"
        },
        "project": []
    }
}
```

##### 6.

```json
{
    "dmp": {
        "created": "2022-04-25T07:38:01.000",
        "description": "This DMP is for our new project.",
        "ethical_issues_description": "Ethical issues are handled by ...",
        "ethical_issues_exist": "yes",
        "ethical_issues_report": "https://docs.google.com/document/d/xyz",
        "language": "eng",
        "modified": "2022-04-25T07:39:13.000",
        "title": "DMP for our new project",
        "contact": {
            "mbox": "john.smith@tuwien.ac.at",
            "name": "John Smith",
            "contact_id": {
                "identifier": "https://www.tiss.tuwien.ac.at/person/2351952424",
                "type": "other"
            }
        },
        "contributor": [
            {
                "mbox": "leo.messi@barcelona.com",
                "name": "Leo Messi",
                "role": [
                    "ProjectLeader"
                ],
                "contributor_id": {
                    "identifier": "https://orcid.org/0000-0002-0000-0000",
                    "type": "orcid"
                }
            },
            {
                "mbox": "robert@bayern.de",
                "name": "Robert Lewandowski",
                "role": [
                    "ContactPerson",
                    "DataManager"
                ],
                "contributor_id": {
                    "identifier": "https://orcid.org/0000-0002-4929-7875",
                    "type": "orcid"
                }
            },
            {
                "mbox": "CR@juve.it",
                "name": "Cristiano Ronaldo",
                "role": [
                    "DataCurator"
                ],
                "contributor_id": {
                    "identifier": "https://www.tiss.tuwien.ac.at/person/305962565",
                    "type": "other"
                }
            }
        ],
        "cost": [],
        "dataset": [],
        "dmp_id": {
            "identifier": "https://doi.org/17.1992/13.5.666"
        },
        "project": []
    }
}
```

## License

The InDMP application is licensed under the [MIT license](https://github.com/e11938258/InDMP/blob/main/LICENSE).
