Hetzner Cloud API for Java
==========================

[![Build Status](https://travis-ci.org/TomSDEVSN/hetznercloud-java.svg?branch=master)](https://travis-ci.org/TomSDEVSN/hetznercloud-java)

Simple Java client for the Hetzner Cloud API.

The current version is **2.13.0**.

**Important message about this project [here](https://github.com/TomSDEVSN/hetznercloud-java/discussions/45)**

## Compile

You can simply compile it with Maven.

Just run ``mvn clean install`` to install it in your local Maven-repository.

## How to use

##### Maven

Dependency:

```xml
<dependencies>
    <dependency>
        <groupId>me.tomsdevsn</groupId>
        <artifactId>hetznercloud-api</artifactId>
        <version>2.13.0</version>
        <scope>compile</scope>
    </dependency>
</dependencies>
```

##### Gradle 

You have to edit the ``build.gradle``

```
repositories({
    mavenCentral()
})

dependencies({
    implementation "me.tomsdevsn:hetznercloud-api:2.13.0"
})
```

## How to run tests
```
mvn -Dhetzner.token=<YOUR HETZNER TOKEN> test
```

The tests require a token.
more on how to obtain the token [here](https://docs.hetzner.cloud/#getting-started).

## JavaDocs

The JavaDocs are available [here](https://docs.hcloud.siewert.io)

## Dependencies

The following dependencies were used in this project:
 * [jackson-databind](https://github.com/FasterXML/jackson-databind) under Apache2.0 License
 * [spring-web](https://github.com/spring-projects/spring-framework/tree/master/spring-web) under Apache 2.0 License
 * [Lombok](https://projectlombok.org) under MIT License
