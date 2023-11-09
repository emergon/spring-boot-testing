package emergon;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

//@RunWith(SpringRunner.class)
@SpringBootTest
class TestServiceTest {

    @Autowired
    private TestService testService;
    @Test
    void testString() {
        Assertions.assertEquals("hello", testService.testString());
    }
}