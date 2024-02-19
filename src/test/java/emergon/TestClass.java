package emergon;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

public class TestClass {

    @Test
    public void testsample(){
        Assertions.assertEquals(10, 10);
    }

    @Test
    @EnabledOnOs(value = OS.MAC, disabledReason = "Enabled only on MAC")
    public void testNumberFormatException(){
        Assertions.assertThrows(NumberFormatException.class, () -> {
            Integer.parseInt("Hello");
        });
    }
}
