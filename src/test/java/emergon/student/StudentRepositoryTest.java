package emergon.student;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.sql.DataSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class StudentRepositoryTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private DataSource dataSource;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void testCustomNativeQuery() {
        List<Student> students = studentRepository.findAll();
        assertEquals(3, students.size());

        assertNotNull(dataSource);
        assertNotNull(entityManager);
    }

    @Test
    public void testFindStudentsGreaterThan(){
        int age = 20;
        List<Student> students = studentRepository.findAllGreaterThan(age);
        assertEquals(2, students.size());
    }
}