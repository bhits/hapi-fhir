# Short Description
The HAPI FHIR JPA server is used as as a FHIR server in Consent2Share.

# Full Description

# Supported Source Code Tags and Current `Dockerfile` Link

[`2.3-01`](https://github.com/bhits/hapi-fhir/releases/tag/2.3-01)

[`Current Dockerfile`](https://github.com/bhits/hapi-fhir/blob/master/hapi-fhir-jpaserver-example/src/main/docker/Dockerfile)

For more information about this image, the source code, and its history, please see the [GitHub repository](https://github.com/bhits/hapi-fhir).

# What is the HAPI FHIR JPA Server?

The HAPI FHIR JPA server is used as a FHIR server in Consent2Share V3. Consent2Share V3 publishes FHIR patient resource and FHIR consent resource to HAPI FHIR JPA server. This HAIPI FHIR JPA server provides HL7 FHIR DSTU3 models and provides a complete RESTful server implementation with MySQL as datasource.

For more information and related downloads for Consent2Share, please visit [Consent2Share](https://bhits.github.io/consent2share/).

# How to use this image

## Start a HAPI FHIR JPA server instance

Be sure to familiarize yourself with the repository's [README.md](https://github.com/bhits/hapi-fhir) file before starting the instance.

`docker run  --name hapi-fhir-jpaserver -d bhits/hapi-fhir-jpaserver <additional program arguments>`

*NOTE: In order for this API to fully function as a microservice in the Consent2Share application, it is required to setup the dependency microservices and the support level infrastructure. Please refer to the Consent2Share Deployment Guide in the corresponding Consent2Share release (see [Consent2Share Releases Page](https://github.com/bhits/consent2share/releases)) for instructions to setup the Consent2Share infrastructure.*

## Configure

When you start the hapi-fhir-jpaserver image, you can edit the configuration of the instance by passing one or more environment variables on the command line. 

### database properties

This HAPI FHIR JPA server is using MySQL as data source with [default database configuration](https://github.com/bhits/hapi-fhir/blob/master/hapi-fhir-jpaserver-example/src/main/resources/database.properties). To overwrite [default database configuration](https://github.com/bhits/hapi-fhir/blob/master/hapi-fhir-jpaserver-example/src/main/resources/database.properties), pass environment variable `CATALINA_OPTS=-Djdbc.url=<url>`.

`docker run --name hapi-fhir-jpaserver -e "CATALINA_OPTS=-Djdbc.url=jdbc:mysql://hapi-fhir-jpaserver-db:3306/hapifhir" --link hapi-fhir-jpaserver-db:hapi-fhir-jpaserver-db -d bhits/hapi-fhir-jpaserver:<tag>`

In a `docker-compose.yml`, this can be provided as follows:
```yml
version: '2'
services:
...
    hapi-fhir:
      image: bhits/hapi-fhir
      environment:
        CATALINA_OPTS: -Djdbc.url=jdbc:mysql://hapi-fhir-db:3306/hapifhir -Djdbc.password=strongpassword
      ports:
        - "8080:8080"
      restart: always
      
    hapi-fhir-db:
      image: mysql:5.7
      environment:
        MYSQL_ROOT_PASSWORD: strongpassword
        MYSQL_DATABASE: hapifhir
      restart: always
...
```

# Supported Docker versions
This image is officially supported on Docker version 1.13.0.

Support for older versions (down to 1.6) is provided on a best-effort basis.

Please see the [Docker installation documentation](https://docs.docker.com/engine/installation/) for details on how to upgrade your Docker daemon.

# License
View [license](https://github.com/bhits/hapi-fhir/blob/master/LICENSE.txt) information for the software contained in this image.

# User Feedback

## Documentation 
Documentation for this image is stored in the [bhits/hapi-fhir](https://github.com/bhits/hapi-fhir) GitHub repository. Be sure to familiarize yourself with the repository's README.md file before attempting a pull request.

## Issues

If you have any problems with or questions about this image, please contact us through a [GitHub issue](https://github.com/bhits/hapi-fhir/issues).

