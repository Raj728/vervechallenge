package com.technicalchallenge;

import org.junit.jupiter.api.Test; // For JUnit 5 test cases
import org.junit.jupiter.api.BeforeEach; // For setup methods (optional)
import org.springframework.beans.factory.annotation.Autowired; // For dependency injection
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest; // For controller-specific testing
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean; // For mocking service layer beans
import org.springframework.http.MediaType; // For specifying content types
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;




@SpringBootTest
@AutoConfigureMockMvc
public class RequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAcceptRequest_Success() throws Exception {
        mockMvc.perform(get("/api/verve/accept")
                        .param("id", "101")
                        .param("endpoint", "http://test.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("ok"));
    }

    @Test
    public void testAcceptRequest_MissingId() throws Exception {
        mockMvc.perform(get("/api/verve/accept"))
                .andExpect(status().isBadRequest());
    }
}

