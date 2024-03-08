package emergon.student;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers//Junit annotation to start, stop and configure Containers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StudentRepositoryTest {

    @Container//Configure a Postgresql Container to be used with TestContainers
    static PostgreSQLContainer database = new PostgreSQLContainer("postgres:12")
            .withDatabaseName("springboot")
            .withUsername("springboot")
            .withPassword("springboot");
    @DynamicPropertySource//set properties for Spring Context without the need to define them in application.properties
    static void setDatasourceProperties(DynamicPropertyRegistry propertyRegistry){
        propertyRegistry.add("spring.datasource.url", database::getJdbcUrl);
        propertyRegistry.add("spring.datasource.username", database::getUsername);
        propertyRegistry.add("spring.datasource.password", database::getPassword);
    }

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
    public void testFindStudentsGreaterThan(){
        studentRepository.save(createStudent("Nick", 31));
        studentRepository.save(createStudent("Jane", 25));
        studentRepository.save(createStudent("John", 32));
        int age = 30;
        List<Student> students = studentRepository.findAllGreaterThan(age);
        assertEquals(3, students.size());//Also check students inserted in data.sql
    }

    private Student createStudent(String name, int age){
        return new Student(name, age);
    }
}