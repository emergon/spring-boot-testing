package emergon.student;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest(properties = {
        "spring.test.database.replace=NONE", //replaces the @AutoConfigureTestDatabase
        "spring.datasource.url=jdbc:tc:postgresql:12:///springboot"//replaces the @Testcontainers (note the tc) @Container and @DynamicPropertySource
})
//@Testcontainers//Junit annotation to start, stop and configure Containers
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StudentRepositoryShortTest {

//    @Container//Configure a Postgresql Container to be used with TestContainers
//    static PostgreSQLContainer database = new PostgreSQLContainer("postgres:12")
//            .withDatabaseName("springboot")
//            .withUsername("springboot")
//            .withPassword("springboot");
//    @DynamicPropertySource//set properties for Spring Context without the need to define them in application.properties
//    static void setDatasourceProperties(DynamicPropertyRegistry propertyRegistry){
//        propertyRegistry.add("spring.datasource.url", database::getJdbcUrl);
//        propertyRegistry.add("spring.datasource.username", database::getUsername);
//        propertyRegistry.add("spring.datasource.password", database::getPassword);
//    }

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private DataSource dataSource;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void contextLoads(){
        assertNotNull(entityManager);
        assertNotNull(dataSource);
    }

    @Test
    @Sql("/scripts/INIT_STUDENTS.sql")
    public void testFindStudentsGreaterThan(){
        int age = 30;
        List<Student> students = studentRepository.findAllGreaterThan(age);
        assertEquals(3, students.size());//Also check students inserted in data.sql
    }
}