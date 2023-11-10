# Testing Spring Boot Applications Primer

## Getting Started

### Introduction

- Clone the repository of [Testing Spring Boot Applications Primer](https://github.com/rieckpil/testing-spring-boot-applications-primer).

### Prerequisities

1. Install Java 17 (*extra: Configure $PATH and $JAVA_HOME*)
2. Use *jenv* for setting default java. Follow this [YouTube video](<https://www.youtube.com/watch?v=9FVZyeFDXo0>).
3. Install Docker. Follow [official instructions](https://www.docker.com/products/docker-desktop/).
4. Install Maven. Follow [official instructions](https://maven.apache.org/install.html).

## Basic Topics

### Creating a New Spring Boot Project

- Create a Spring Boot Project by using Spring Initializer

  ```bash
  curl https://start.spring.io/starter.zip \
  -d dependencies=web,actuator,data-jpa,h2 \
  -d javaVersion=17 \
  -d name=spring-boot-testing-primer \
  -d artifactId=spring-boot-testing-primer \
  -d groupId=de.rieckpil.blog \
  -d packageName=de.rieckpil.blog \
  -d type=maven-project \
  -o spring-boot-testing-primer.zip

  unzip spring-boot-testing-primer.zip
  ```

- Run the project

  ```bash
  mvn spring-boot:run
  ./mvnw spring-boot:run
  ./mvnw -Djava.net.useSystemProxies=true spring-boot:run
  ```

- Check if the project is running (type URL in browser)

  ```txt
  localhost:8080/actuator/health
  ```

### Inspecting the Spring Boot Starter Test

- Is there a testing library in the Spring Boot Project?
  - Yes, Spring Boot always includes the Spring Boot Starter Test in every project. Check `pom.xml`.

    ```xml
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
    </dependency>
    ```

- `spring-boot-starter-test` contains libraries for Unit Testing `JUnit`, `Mockito`, `Hamcrest`, `AssertJ`, `JSONassert`, and `JsonPath`. For writing Integration Tests, additional libraries are needed like `WireMock`, `Testcontainers`, or `Selenium`.
- Inspect all libraries and their transitive dependencies with

  ```bash
  mvn dependency:tree
  ```

- More info: [Guide to Testing With the Spring Boot Starter Test](https://rieckpil.de/guide-to-testing-with-spring-boot-starter-test/)
- Extra info: [How Spring Boot’s Autoconfigurations Work](https://www.marcobehler.com/guides/spring-boot-autoconfiguration)
