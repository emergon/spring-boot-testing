package emergon.student;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;


@WebMvcTest(StudentController.class)//Use Sliced Test Annotation for testing StudentController
class StudentControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")//add SuppressWarnings because Intellij shows error in variable
    @Autowired
    private MockMvc mockMvc;

    @MockBean//SpringBoot test annotation to mock Service class
    private StudentService studentService;

    @Test
    public void shouldReturnAllStudents() throws Exception{
        //Step 1: Create a mock list of what the service returns when calling StudentService.getAllStudents() method
        Mockito.when(studentService.getAllStudents()).thenReturn(
          List.of(new Student(1L, "Nick", 38),
                  new Student(2L, "Patrick", 20),
                  new Student( 3L, "Jane", 25))
        );
        //Step 2: Use the MockMvc to do a REST API call without initializing an HTTP Server
        //Step 3: Use MockMvcRequestBuilders and MockMvcResultMatchers to create a JSON response.
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/students"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Nick"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(38))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name").value("Jane"));
    }
}