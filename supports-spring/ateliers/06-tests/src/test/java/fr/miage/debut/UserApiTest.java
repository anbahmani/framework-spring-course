package fr.miage.debut;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest(properties={"spring.datasource.url=jdbc:h2:mem:tests", "spring.jpa.hibernate.ddl-auto=create-drop"}) @AutoConfigureMockMvc
class UserApiTest {
    @Autowired MockMvc mvc;
    @Test void createReadUpdateDelete() throws Exception {
        String location = mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Ana\"}"))
            .andExpect(status().isCreated()).andReturn().getResponse().getHeader("Location");
        mvc.perform(get(location)).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Ana"));
        mvc.perform(put(location).contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Lea\"}"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Lea"));
        mvc.perform(delete(location)).andExpect(status().isNoContent());
        mvc.perform(get(location)).andExpect(status().isNotFound());
    }
    @Test void rejectEmptyName() throws Exception {
        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\" \"}"))
            .andExpect(status().isBadRequest());
    }
}
