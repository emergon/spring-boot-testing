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


### [How Maven Plays a Role In Testing](https://rieckpil.de/maven-setup-for-testing-java-applications/)

- Maven Archetypes can be used to create projects. Rieckpil offers this [repository](https://github.com/rieckpil/custom-maven-archetypes/tree/master/testing-toolkit-archetype) with a maven archetype project with basic testing capabilities. We can create our own archetype
- Basic Folders and Files in java for testing
  - **src/test/java**: contains the test classes in the same package structure as normal classes.
  - **src/test/resources**: static files that are necessary for the tests like *csv*, *json* or *configuration* files.
  - **target/test-classes**: this is where maven places the compiled test classes and test resources. Run `mvn test-compile`

#### Maven and Java Testing Naming Conventions
The **Maven Surefire Plugin** is designed to run our unit tests. These conventions will be detected by the plugin:
- **/Test*.java
- **/*Test.java
- **/*Tests.java
- **/*TestCase.java  

Generaly a test is NOT a unit test if it:
- talks to the database
- communicates across the network
- touches the file system
- can't run at the same time as any of your other unit tests
- or you have to do special things to your environment (such as editing config files) to run it.

The **Maven Failsafe Plugin** is designed to run our integration tests. These conventions will be detected by the plugin:
- **/IT*.java
- **/*IT.java
- **/*ITCase.java

#### When are Java Tests executed?
Maven contains these three built-in lifecycles:
- *default*: handling project building and deployment
- *clean*: project cleaning
- *site*: the creation of our project's (documentation) site

The **default** lifecycle contains the following build phases:
- *validate*: validate that our project setup is correct (e.g., we have the correct Maven folder structure)
- *compile*: compile our source code with javac
- *test*: run our unit tests with the **Maven Surefire Plugin**
- *package*: build our project in its distributable format (e.g., JAR or WAR)
- *verify*: run our integration tests and further checks (e.g., the OWASP dependency check) with the **Maven Failsafe Plugin**
- *install*: install the distributable format into our local repository (~/.m2 folder)
- *deploy*: deploy the project to a remote repository (e.g., Maven Central or a company hosted Nexus Repository/Artifactory)

#### The Maven Surefire and Failsafe Plugins
The **Maven Surefire Plugin** picks up tests based on the naming conventions. It's good to override the version to include latest updates:
```xml
  <build>
    <plugins>
      <plugin>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>3.0.0-M5</version>
      </plugin>
    </plugins>
  </build>
  ```
  Some commands follow for maven:
  - `mvn test`: run build phases up to phase **test** and run only unit tests with *maven surefire plugin*
  - `mvn surefire:test`: run unit tests but skip previous build phases (*validate, compile*). Keep in mind that test classes should be compiled.
  - `mvn package -DskipTests`: package project but skip unit tests
  - Explicitly run a test class or test method:
    - mvn test -Dtest=MainTest
    - mvn test -Dtest=MainTest#testMethod
    - mvn surefire:test -Dtest=MainTest

The **Maven Failsafe Plugin** is not by default part of the project as **Surefire** is. So it needs to be manually included in order to execute integration tests:
```xml
  <build>
      <!-- further plugins -->
      <plugin>
        <artifactId>maven-failsafe-plugin</artifactId>
        <version>3.0.0-M5</version>
        <executions>
          <execution>
            <goals>
              <goal>integration-test</goal>
              <goal>verify</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
```
In the *executions* section the goals need to be specified. Without the *verify* goal, the plugin will run our integration tests but won't fail the build if there are test failures.  
Commands for maven failsafe plugin:
- `mvn failsafe:integration-test failsafe:verify`: execute only integration tests.
- `mvn verify -DskipITs`: skip integration tests.
- Execute specific integration tests:
  - mvn -Dit.test=MainIT failsafe:integration-test failsafe:verify
  - mvn -Dit.test=MainIT#firstTest failsafe:integration-test failsafe:verify

### [Getting Familiar with JUnit 5 and Mockito](https://rieckpil.de/course/tsbap-getting-familiar-with-junit-5-and-mockito/)

#### JUnit
JUnit 5 was launched in 2017 and is composed of several modules:
- JUnit Platform: Foundation to launch testing frameworks & it defines the **TestEngine** API for developing testing frameworks
- JUnit Jupiter: New programming model and extension model and provides the **JupiterTest Engine** that implements the **TestEngine** interface to run tests on the JUnit Platform
- JUnit Vintage: Provides a TestEngine to run both JUnit 3 and JUnit 4 tests

Sources:
- [JUnit 5 Crash Course (YouTube)](https://www.youtube.com/watch?v=flpmSXVTqBI)
  - ```
    Assertions.assertThrows(NumberFormatException.class, () -> {
    Integer.parseInt("Hello");
    });
    ```
  - `@EnabledOnOs(value = OS.MAC, disabledReason = "Enabled only on MAC")`
  - `Assumptions.assumeTrue()`
  - `@RepeatedTest(value = 5)`
    
- [Five JUnit 5 Features You Might Not Know Yet (Rieckpil)](https://rieckpil.de/five-junit-5-features-you-might-not-know-yet/)
  - Test Execution Ordering  
  We can order Tests based on our configuration.
    
  ```java
  @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
  public class OrderedExecutionTest {
  
  @Test
  @Order(2)
  public void testTwo() {
  System.out.println("Executing testTwo");
  assertEquals(4, 2 + 2);
  }
  
  @Test
  @Order(1) //Lower number has higher priority
  public void testOne() {
  System.out.println("Executing testOne");
  assertEquals(4, 2 + 2);
  }
  
  @Test
  @Order(3)
  public void testThree() {
  System.out.println("Executing testThree");
  assertEquals(4, 2 + 2);
  }
  }
  ```
  - Nesting Tests
  
- [JUnit 5 Official Documentation](https://junit.org/junit5/docs/current/user-guide/)

#### Mockito
The Basic Workflow of Mockito is:
- Create mocks for collaborators of our class under test (e.g., using @Mock)
- Stub the behavior of the mocks as they'll otherwise return a default value (when().thenReturn())
- (Optionally) Verify the interaction of our mocks (verify())

The [Four Golder Rules of Mockito](https://github.com/mockito/mockito/wiki) about when and what to mock:
- Do not mock types you don't own
- Don't mock values objects
- Don't mock everything
- Show some love with your tests

```java
//Step 1: Annotate class with 
@ExtendWith(MockitoExtension.class)
class PricingServiceTest{
  //Step 2: Mock Dependencies
  @Mock
  private PricingRepository pricingRepository;
  
  //Step 3: Inject mocks into class we need
  @InjectMocks
  private PricingService pricingService;
  
  //Step 4: Create tests
  @Test
  void shouldReturnExpensivePriceWhenProductIsComputer() {
    Mockito.when(pricingRepository.getPricing("computer"))
            .thenReturn(new BigDecimal(10.0));
    assertEquals(new BigDecimal(10.0), pricingService.getPricing("computer"));
  }
}
```

### [High-Level Overview of Testing Spring Boot Applications](https://rieckpil.de/course/tsbap-high-level-overview-of-testing-spring-boot-applications/)
Some of the best resources for writing good tests (contain books, courses, articles):
- [Recommended Resources on Testing Java Applications](https://rieckpil.de/recommended-resources-for-testing-java-applications/)

Great Talk for Spring Boot Testing by Pivotal
- [Test Driven Development with Spring Boot](https://www.youtube.com/watch?v=s9vt6UJiHg4&t=2239s)

## Intermediate Topics

### [Spring Boot Test Slice Annotations](https://rieckpil.de/course/tsbap-spring-boot-test-slice-annotations/)
If we want to test different parts of our application in isolation, Spring provides *test slice annotations* where Spring Boot bootstraps an opinionated Spring TestContext for us. 
By using this, you do not have to bootstrap the whole Spring Context. 
The following guide provides an overview of these annotations for testing in isolation the Web or Persistence Layers.

- [Spring Boot Test Slices: Overview and Usage](https://rieckpil.de/spring-boot-test-slices-overview-and-usage/)  
Using Spring Boot Test Slices means that technically Spring creates a Spring Context with only a subset of beans by applying only specific auto-configurations.

<details>
<summary>Testing the Web Layer With @WebMvcTest</summary>

The annotation `@WebMvcTest` lets you test part of the Spring MVC Application. It also supports the rules for Spring Security.  
It creates a Spring Test Context that contains:
- @Controller
- @ControllerAdvice
- @JsonComponent
- Converter
- Filter
- WebMvcConfigurer

It **DOES NOT** contain:
- @Service
- @Component
- @Repository

**@WebMvcTest** provides a mocked Servlet Environment, so there is no port to access the application like with a *RestTemplate*.
So for access to the endpoint we use the auto-configured *MockMvc*

```java
@WebMvcTest(ShoppingCartController.class) //tells Spring to focus only on testing this controller class
class ShoppingCartControllerTest {
 
  @Autowired
  private MockMvc mockMvc;//Helps Test Controller endpoints without starting a full HTTP Server. Simulates HTTP requests and responses
 
  @MockBean
  private ShoppingCartRepository shoppingCartRepository;
 
  @Test
  public void shouldReturnAllShoppingCarts() throws Exception {
    when(shoppingCartRepository.findAll()).thenReturn(
      List.of(new ShoppingCart("42",
        List.of(new ShoppingCartItem(
          new Item("MacBook", 999.9), 2)
        ))));
 
    this.mockMvc.perform(get("/api/carts"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].id", Matchers.is("42")))
      .andExpect(jsonPath("$[0].cartItems.length()", Matchers.is(1)))
      .andExpect(jsonPath("$[0].cartItems[0].item.name", Matchers.is("MacBook")))
      .andExpect(jsonPath("$[0].cartItems[0].quantity", Matchers.is(2)));
  }
}
```
</details>

<details>
<summary>Testing your JPA Components With @DataJpaTest</summary>

The annotation `@DataJpaTest` lets you test any JPA-related parts of the application. 
A good example is to verify that a native query is working as expected.
By default, this annotation tries to auto-configure use an embedded database (e.g., H2) as the DataSource.

It creates a Spring Test Context that contains beans:
- @Repository
- EntityManager
- TestEntityManager
- DataSource

It **DOES NOT** contain beans of: 
- @Service
- @Component
- @Controller

Example in [StudentRepositoryTest.java](src/test/java/emergon/student/StudentRepositoryTest.java) class.  

*We can disable the in-memory DB by using `@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)`*.  
*Also all tests run inside a transaction and get rolled back after their execution.*

</details>

### [Testing the Web Layer](https://rieckpil.de/course/tsbap-testing-the-web-layer/)

- [Guide to Testing Spring Boot Applications With MockMvc](https://rieckpil.de/guide-to-testing-spring-boot-applications-with-mockmvc/)

Dependencies needed for testing Web Layer:
- org.springframework.boot:spring-boot-starter-web
- org.springframework.boot:spring-boot-starter-test (scope=test)
- org.springframework.security:spring-security-test (scope=test) (If Spring Security is used)

Check the above link to find information on how to use MockMvc to:
- Use JsonPath to create Json Responses and validate Json
- Test Secure REST API Endpoints if Spring Security is used
- Use [WebTestClient](https://rieckpil.de/spring-webtestclient-for-efficient-testing-of-your-rest-api/) to perform real HTTP requests to the running application

YouTube Video Resources:
- [Setup MockMvc to Test Your Spring MVC @Controller and @RestController in Isolation](https://www.youtube.com/watch?v=sxWNJJ4dKJo&t=1s)
- [Perform HTTP GET and POST Requests MockMvc to Test a @RestController](https://www.youtube.com/watch?v=Aasp0mWT3Ac)
- [Test Protected (Spring Security) Controller Endpoints With MockMvc](https://www.youtube.com/watch?v=oLtXe1wgSC8)

### [Testing the Persistence Layer](https://rieckpil.de/course/tsbap-testing-the-persistence-layer/)

For Testing the Persistence Layer we need the `@DataJpaTest` annotation. The Spring Test Context contains the components:
- @Repository or any class extending a Spring Data repository
- EntityManager and TestEntityManager
- DataSource

Behind the scenes, this Annotation tries to connect to an embedded database (H2 or Derby). But, we shouldn't use a different DB than what we're using in production.

- [Guide to Testing Spring Boot JPA Persistence Layer](https://rieckpil.de/test-your-spring-boot-jpa-persistence-layer-with-datajpatest/)

What not to Test with @DataJpaTest
- CRUD functionalities provided by Spring Data JPA
- Queries derived by method name (These are validated on Application Startup)
- Schema Mappings between Entities and DB Tables (use `spring.jpa.hibernate.ddl-auto=validate`)

Testcontainers makes it convenient to start any Docker container of choice for testing purposes.
Testcontainers guides:
- [Testing Spring Boot Applications with Testcontainers and JUnit](https://rieckpil.de/howto-write-spring-boot-integration-tests-with-a-real-database/)
- [Reuse Containers With Testcontainers for Fast Integration Tests](https://rieckpil.de/reuse-containers-with-testcontainers-for-fast-integration-tests/)

YouTube Video Resources:
- [Introduction to @DataJpaTest and Pitfalls of In-Memory Databases For Testing](https://www.youtube.com/watch?v=DwBgx30ZWVc)
- [Replacing the Default In-Memory Database of @DataJpaTest Using Testcontainers](https://www.youtube.com/watch?v=2fPDw0PVbso&t=1s)
- [Test a Native Query of Your Spring Data JPA Repository With @DataJpaTest](https://www.youtube.com/watch?v=EPxII6TeqTQ)
- [Short And Concise Test Setup With @DataJpaTest and Testcontainers](https://www.youtube.com/watch?v=zFkTA95w0oo)
 
### [Further Test Slice Annotations](https://rieckpil.de/course/tsbap-further-test-slice-annotations/)

- [Testing the Spring RestTemplate With @RestClientTest](https://rieckpil.de/testing-your-spring-resttemplate-with-restclienttest/)
- [Testing JSON Serialization With @JsonTest and Spring Boot](https://rieckpil.de/testing-your-json-serialization-with-jsontest/)
- [Test Spring WebClient with MockWebServer from OkHttp](https://rieckpil.de/test-spring-webclient-with-mockwebserver-from-okhttp/)

## Advanced Topics

### [Infrastructure Setup for Integration Tests With Testcontainers](https://rieckpil.de/course/tsbap-infrastructure-setup-for-integration-tests-with-testcontainers/)

Various articles about Testcontainers
- [Testcontainers in Rieckpil](https://rieckpil.de/tag/testcontainers/)

YouTube Video Resources
- [Introduction to Testcontainers with JUnit 5 and Spring Boot](https://www.youtube.com/watch?v=-mYJKwrySOw&t=1s)

### [Integration Tests with @SpringBootTest](https://rieckpil.de/course/tsbap-integration-tests-with-springboottest/)

- [Spring Boot Integration Tests With WireMock and JUnit 5](https://rieckpil.de/spring-boot-integration-tests-with-wiremock-and-junit-5/)
- [Guide to @SpringBootTest for Spring Boot Integration Tests](https://rieckpil.de/guide-to-springboottest-for-spring-boot-integration-tests/)
- [Improve Build Times with Context Caching from Spring Test](https://rieckpil.de/improve-build-times-with-context-caching-from-spring-test/)

### [Writing End-To-End Tests](https://rieckpil.de/course/tsbap-writing-end-to-end-tests/)

End-To-End Tests are used to test an entire user journey like logging in, creating items, deleting items and logging out.
For end-to-end tests that somehow interact with a browser, the usual first choice would be **Selenium**.  
Another tool that can be used is **Selenide**.

YouTube Resources:
- [Introduction Selenide for Writing Concise Web Tests for Java Projects](https://www.youtube.com/watch?v=T9xns1iMbPI)
- [Use the Testcontainers Webdriver Module together with Selenide](https://www.youtube.com/watch?v=wzF1lfrGHtk)