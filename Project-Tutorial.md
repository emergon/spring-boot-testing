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

### More info: 

- [Guide to Testing With the Spring Boot Starter Test](https://rieckpil.de/guide-to-testing-with-spring-boot-starter-test/)  
  For integrations test we need more dependencies: **WireMock**, **Testcontainers**, or **Selenium**.
- [Spring Boot Unit and Integration Testing Overview](https://rieckpil.de/spring-boot-unit-and-integration-testing-overview/)
   - **Unit Testing with Spring Boot**: Spring Boot contains by default the basic dependencies for unit testing
   - **Tests With a Sliced Spring Context**: If JUnit is not enough, Spring provides the *TestContext* framework to test various parts of the app (*@WebMvcTest*, *@DataMongoTest*, *@DataJpaTest*, *@JsonTest*).
   - **JUnit 4 vs. JUnit 5 Pitfall**: Try not to mix JUnit 4 and 5 Tests. Use **Maven Enforcer Plugin** to define a dependency as Banned.
   - **Integration Tests With Spring Boot: @SpringBootTest**: Use *@SpringBootTest* to populate the [entire application context](https://rieckpil.de/guide-to-springboottest-for-spring-boot-integration-tests/), use [TestContainers](https://www.testcontainers.org/) to manage Docker containers, use [WireMock](https://rieckpil.de/spring-boot-integration-tests-with-wiremock-and-junit-5/) to stub and prepare HTTP responses to simulate the existence of a remote system.
   - **End-to-End Tests with Spring Boot**: These tests involve user experience and a browser. *Selenium* is the de facto choice. [Selenide](https://rieckpil.de/write-concise-web-tests-with-selenide-for-java-projects/) is an abstraction for better code.

- [Spring Boot Test Slices: Overview and Usage](https://rieckpil.de/spring-boot-test-slices-overview-and-usage/)
   - Spring Boot offers the capability to create a **Spring Context** with only a subset of beans by applying only specific auto-configurations and isolate parts of the app (Web Layer, JPA components)
   - **Testing the Web Layer With @WebMvcTest**: This annotation creates a **Spring Context** with components for testing Spring MVC (*@Controller, @ControllerAdvice, @JsonComponent, Converter, Filter, WebMvcConfigurer*).
   - **Testing your JPA Components With @DataJpaTest**: Test any JPA related class (*@Repository, EntityManager, TestEntityManager, DataSource*).
   - **Testing JSON Serialization with @JsonTest**: Used for testing serialization/deserialization with *@JsonComponent,ObjectMapper, Module* from Jackson
   - **Testing the Entire Application With @SpringBootTest**: Allows to write tests for the whole application.

- [Guide to @SpringBootTest for Spring Boot Integration Tests](https://rieckpil.de/guide-to-springboottest-for-spring-boot-integration-tests/)
   - @SpringBootTest is a powerful tool to write integration tests
   - not every part of your application should be tested with this expensive test setup
   - be aware that the default web environment is MOCK (no port and no Tomcat)
   - there are multiple strategies to tweak the context configuration and environment
   - be aware of the increased build time when creating different Spring TestContext configurations

- [Guide to Testing Spring Boot Applications With MockMvc](https://rieckpil.de/guide-to-testing-spring-boot-applications-with-mockmvc/)
   - Test Web Layer (*@Controller, @RestController, Filter, @ControllerAdvice, WebMvcConfigurer, etc*).
   - Need *spring-boot-starter-web, spring-boot-starter-test, spring-security-test*
   - Use *@WebMvcTest* to auto-configure a MockMvc.

- [Test Your Spring Boot JPA Persistence Layer With @DataJpaTest](https://rieckpil.de/test-your-spring-boot-jpa-persistence-layer-with-datajpatest/)
   - Do not test methods provided by the framework (*findAll(), save(), update(), delete()*)
   - Do not test methods where SQL is derived by the method name(validated at application startup)
   - Test native queries
   - Use **TestContainers** to use a Docker DB instead of an embedded DB.

- [Fix No Qualifying Spring Bean Error For Spring Boot Tests](https://rieckpil.de/fix-no-qualifying-spring-bean-error-for-spring-boot-tests/)
   - When testing different slices, use **@MockBean** to mock dependencies (*Service*).

- [How Spring Boot’s Autoconfigurations Work](https://www.marcobehler.com/guides/spring-boot-autoconfiguration)
- [Spring Testing Documentation](https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html)

### How Maven Plays a Role In Testing