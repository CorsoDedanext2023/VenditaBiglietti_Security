package it.dedagroup.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.dedagroup.security.dto.request.LoginDTORequest;
import it.dedagroup.security.model.Ruolo;
import it.dedagroup.security.model.Utente;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

@SpringBootTest
@ContextConfiguration(classes = SecurityVenditaBigliettiApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
class SecurityVenditaBigliettiApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private MockMvc mvc;

    ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    @Test
    void loginOk() throws Exception{
        LoginDTORequest req = new LoginDTORequest();
        req.setEmail("azzaro@gmail.com");
        req.setPassword("azzaroMa99_!");
        String json = mapper.writeValueAsString(req);
        mvc.perform(MockMvcRequestBuilders.post("/utente/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andReturn();
    }

    @Test
    void loginFail() throws Exception{
        LoginDTORequest req = new LoginDTORequest();
        req.setEmail("luc@gmail.com");
        req.setPassword("luc9000_!");
        String json = mapper.writeValueAsString(req);
        mvc.perform(MockMvcRequestBuilders.post("/utente/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    }

    @Test
    void loginSenzaBody() throws Exception{
        mvc.perform(MockMvcRequestBuilders.post("/utente/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @Test @Order(1)
    void aggiungiUtenteOk() throws Exception{
        Utente u = new Utente();
        u.setNome("Lorenzo");
        u.setCognome("Favij");
        u.setTelefono("3489901245");
        u.setCancellato(false);
        u.setDataDiNascita(LocalDate.of(2000, 3, 21));
        u.setEmail("favij@gmail.com");
        u.setPassword("jivaf90$$$");
        u.setRuolo(Ruolo.CLIENTE);
        u.setVersion(0);
        String json = mapper.writeValueAsString(u);
        mvc.perform(MockMvcRequestBuilders.post("/utente/aggiungiUtente")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andReturn();
    }

    @Test
    void aggiungiUtenteFailEmail() throws Exception{
        Utente u = new Utente();
        u.setNome("Marco");
        u.setCognome("Rossini");
        u.setTelefono("3490013489");
        u.setCancellato(false);
        u.setDataDiNascita(LocalDate.of(2004, 12, 9));
        u.setEmail("rossiMat@gmail.com");
        u.setPassword("rox90$$$");
        u.setRuolo(Ruolo.CLIENTE);
        u.setVersion(0);
        String json = mapper.writeValueAsString(u);
        mvc.perform(MockMvcRequestBuilders.post("/utente/aggiungiUtente")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @Test
    void aggiungiUtenteCampoVuoto() throws Exception{
        Utente u = new Utente();
        u.setNome("Marco");
        u.setCognome("Rossini");
        u.setTelefono("");
        u.setCancellato(false);
        u.setDataDiNascita(LocalDate.of(2004, 12, 9));
        u.setEmail("rox@gmail.com");
        u.setPassword("rox90$$$");
        u.setRuolo(Ruolo.CLIENTE);
        u.setVersion(0);
        String json = mapper.writeValueAsString(u);
        mvc.perform(MockMvcRequestBuilders.post("/utente/aggiungiUtente")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @Test
    void modificaUtenteOk() throws Exception{
        Utente u = new Utente();
        u.setNome("Marco");
        u.setCognome("Rossini");
        u.setTelefono("3446674901");
        u.setCancellato(false);
        u.setDataDiNascita(LocalDate.of(2004, 12, 9));
        u.setEmail("rox@gmail.com");
        u.setPassword("rox90$$$");
        u.setRuolo(Ruolo.CLIENTE);
        u.setVersion(0);
        String json = mapper.writeValueAsString(u);
        mvc.perform(MockMvcRequestBuilders.post("/utente/modificaUtente")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @Test
    void findByEmail() throws Exception{
        String email = "pacello@gmail.com";
        mvc.perform(MockMvcRequestBuilders.post("/utente/email/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    void findByEmailFail() throws Exception{
        String email = "iosca@gmail.com";
        mvc.perform(MockMvcRequestBuilders.post("/utente/email/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    }

    @Test
    void findByTelefono() throws Exception{
        String telefono = "3487651244";
        mvc.perform(MockMvcRequestBuilders.post("/utente/trovaPerTelefono")
                        .param("telefono", telefono)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    void findByTelefonoFail() throws Exception{
        String telefono = "3481230000";
        mvc.perform(MockMvcRequestBuilders.post("/utente/trovaPerTelefono")
                        .param("telefono", telefono)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    }
}
