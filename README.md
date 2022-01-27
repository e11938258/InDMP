# InDMP

The software was developed by Filip Zoubek (https://orcid.org/0000-0003-1269-2668).

## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [License](#license)

## General info

Integrated The Machine-actionable Data Management planning application (InDMP) serves as a proof-of-concept for the thesis Framework for integration of RDM services using machine-actionable DMPs. The application allows to integrate RDM services using maDMPs to exchange information using a REST API and can track the evolution of maDMPs from the first version to the final one.

## Technologies
The applications were developed using Spring Boot in Java programming language version 11.

## Setup

The application uses the PostgreSQL 11 database system. You need to install it first, create a user and a database and set the correct data in the application.properties file or use the following default values:

| Property    | Value |
| ----------- | ----------- |
| Host      | localhost       |
| Port      | 5432       |
| Database      | indmp       |
| Username   | indmp        |
| Password   | indmp123        |

If you have set up and enabled the database system, you can build and run the application using the command:

```
$ mvn clean package && java -jar target/INDMP-1.0.0.jar
```

## License

InDMP is licensed under the [MIT license](https://github.com/e11938258/InDMP/blob/main/LICENSE).
