package techpark.contoller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import techpark.user.UserProfile;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Варя on 30.03.2017.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
public class TestUserController {

    @Autowired
    protected MockMvc mockMvc;

    private final UserProfile user = new UserProfile("mail", "login", "password", null);

   @Before
    public void addUser() throws Exception{
        mockMvc.perform(post("api/registration")
        .requestAttr("login", user.getLogin())
        .requestAttr("mail", user.getMail())
        .requestAttr("password", user.getPassword())
        .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
    }


    @Test
    public void testNotFound() throws Exception {
        mockMvc.perform(
                get("/hello"))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testRegistration() throws Exception {
        mockMvc.perform(post("api/registration")
                .requestAttr("login", user.getLogin() + '1')
                .requestAttr("mail", user.getMail() + '1')
                .requestAttr("password", user.getPassword() + '1')
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void testRegistrationEmtyParam() throws Exception {
        mockMvc.perform(post("api/registration")
                .requestAttr("mail", user.getMail())
                .requestAttr("password", user.getPassword())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(flash().attribute("error", "Request param not found"));
    }

    @Test
    public void testRegistrationEmtyParamValue() throws Exception {
        mockMvc.perform(post("api/registration")
                .requestAttr("login", "")
                .requestAttr("mail", user.getMail())
                .requestAttr("password", user.getPassword())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(flash().attribute("error", "Wrong data"));
    }

    @Test
    public void testRegistIncorrectParamValue() throws Exception {
        mockMvc.perform(post("api/registration")
                .requestAttr("login", "asa")
                .requestAttr("mail", "")
                .requestAttr("password", "123")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(flash().attribute("error", "Wrong data"));
    }

    @Test
    public void testRegistrationSameLogin() throws Exception {
        mockMvc.perform(post("api/registration")
                .requestAttr("login", user.getLogin())
                .requestAttr("mail", user.getMail() + '2')
                .requestAttr("password", user.getPassword())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isConflict())
                .andExpect(flash().attribute("error", "Login already exist"));
    }

    @Test
    public void testRegistrationSameMail() throws Exception {
        mockMvc.perform(post("api/registration")
                .requestAttr("login", user.getLogin() + '2')
                .requestAttr("mail", user.getMail())
                .requestAttr("password", user.getPassword())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isConflict())
                .andExpect(flash().attribute("error", "E-mail already exist"));
    }


    @Test
    public void testLogin() throws Exception {
        mockMvc.perform(post("api/login")
                .requestAttr("login", user.getLogin())
                .requestAttr("password", user.getPassword())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void testLoginIncorrectLogin() throws Exception {
        mockMvc.perform(post("api/login")
                .requestAttr("login", user.getLogin()+ '3')
                .requestAttr("password", user.getPassword())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(flash().attribute("error", "Wrong login or password"));
    }

    @Test
    public void testLoginIncorrectPassword() throws Exception {
        mockMvc.perform(post("api/login")
                .requestAttr("login", user.getLogin())
                .requestAttr("password", user.getPassword() + '1')
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(flash().attribute("error", "Wrong login or password"));
    }

    @Test
    public void testEditUser() throws Exception {
        mockMvc.perform(post("api/settings")
                .cookie(new Cookie("login", user.getLogin()))
                .requestAttr("login", "newlogin")
                .requestAttr("mail", "newmail")
                .requestAttr("password", "newpassword")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void testEditUserOneValue() throws Exception {
        mockMvc.perform(post("api/settings")
                .cookie(new Cookie("login", user.getLogin()))
                .requestAttr("login", "newlogin")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void testBestUsers() throws Exception {
        mockMvc.perform(get("api/users"))
                .andExpect(status().isOk());
    }
}
