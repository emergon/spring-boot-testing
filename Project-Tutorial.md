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
- JUnit Vintage is used for JUnit 4. If you only need JUnit 5 tests, exclude `junit-vintage` from the dependencies. Spring Boot 2.4 and later doesn't contain the dependency.
  ```xml
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
    <exclusions>
      <exclusion>
        <groupId>org.junit.vintage</groupId>
        <artifactId>junit-vintage-engine</artifactId>
      </exclusion>
    </exclusions>
  </dependency>
  ```
- JUnit 5 = JUnit Platform + JUnit Jupiter + JUnit Vintage
  Some basic annotations/methods are the following:
  - @Test
  - assertEquals()
  - @BeforeEach/@AfterEach
  - @ExtendWith
  - @ParametrizedTest

- **Mockito**: It is used to stub method calls and verify interaction on objects. Mocks dependencies of classes so that our tests can focus on one class.

- **Hamcrest**: It is an assertion library that follows a more sentence-like approach compared to the assertions of JUnit 5. Thus, the assertions are more readable.

- **AssertJ**: It is another assertion library that allows writing fluent assertions for Java tests.

- **JSONAssert**: It helps writing unit tests for JSON data structures. This can be really helpful when testing the API endpoints of our Spring Boot application.

- **JsonPath**: It enables us to extract specific parts of our JSON while using a JsonPath expression

- More info: [Guide to Testing With the Spring Boot Starter Test](https://rieckpil.de/guide-to-testing-with-spring-boot-starter-test/)
- Extra info: [How Spring Boot’s Autoconfigurations Work](https://www.marcobehler.com/guides/spring-boot-autoconfiguration)

### How Maven Plays a Role In Testing