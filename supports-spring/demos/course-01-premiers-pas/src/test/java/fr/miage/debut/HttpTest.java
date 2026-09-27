package fr.miage.debut;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest @AutoConfigureMockMvc
class HttpTest {
    @Autowired MockMvc mvc;
    @Test void routeWorks() throws Exception {
        mvc.perform(get("/hello")).andExpect(status().isOk()).andExpect(content().string("Bonjour Spring"));

    }
}
