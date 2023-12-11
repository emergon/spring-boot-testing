package emergon;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SampleIntegrationIT {

    @Test
    public void testIntegration(){
        String name = "Jack".toUpperCase();
        Assertions.assertEquals("JACK", name);
    }
}
