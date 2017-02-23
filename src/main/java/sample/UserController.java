package sample;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;



import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Solovyev on 09/02/2017.
 */
@RestController
public class UserController {

    @NotNull
    @Autowired
    private final AccountService accountService;
    private Map<Long, String> userIdToUserCookie = new HashMap<>();
    /**
     * Данный метод вызывается с помощью reflection'a, поэтому Spring позволяет инжектить в него аргументы.
     * Подробнее можно почитать в сорцах к аннотации {@link RequestMapping}. Там описано как заинжектить различные атрибуты http-запроса.
     * Возвращаемое значение можно так же варьировать. Н.п. Если отдать InputStream, можно стримить музыку или видео
     */

    @RequestMapping(path = "api/registration", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> Registration(@RequestBody GetBody body, HttpSession httpSession)  {
        final String login = body.getLogin();
        final String password = body.getPassword();
        final String mail = body.getMail();

        if(accountService.getUserByLogin(login)  != null)
            return  ResponseEntity.status(HttpStatus.CONFLICT).body("Login already exist");
        if(accountService.getUserByMail(mail) != null)
            return  ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exist");

        UserProfile currentUser = accountService.register(mail, login, passwordEncoder().encode(password));
        userIdToUserCookie.put(currentUser.getId(), httpSession.getId());
        httpSession.setAttribute("Login", login);
        return ResponseEntity.ok("OK");
    }

    @RequestMapping(path = "api/login", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> Login(@RequestBody GetBody body, HttpSession httpSession)  {
        final String login = body.getLogin();
        final String password = body.getPassword();
        UserProfile currentUser = accountService.getUserByLogin(login);

        if(currentUser  == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wrong login or password");
        if(passwordEncoder().matches(password, currentUser.getPassword())
                & userIdToUserCookie.get(currentUser.getId()) == httpSession.getId()){
            httpSession.setAttribute("Login", login);
            return ResponseEntity.ok("OK");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wrong login or password");
    }

    @RequestMapping(path = "api/user", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public String getUser (HttpSession httpSession){
        String login = (String) httpSession.getAttribute("Login");
        if( login == null)
            return null;
        return login;
    }

    @RequestMapping(path = "api/logout", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> logout( HttpSession httpSession)  {
        if(httpSession.getAttribute("Login") == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User is not authorized");
        httpSession.removeAttribute("Login");
        return ResponseEntity.ok("OK");
    }

    @RequestMapping(path = "api/settings", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> changeUser(@RequestBody GetBodySettings body,  HttpSession httpSession)  {
        String login = (String) httpSession.getAttribute("Login");
        if( login == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        UserProfile currentUser = accountService.getUserByLogin(login);
        if(currentUser == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        if(httpSession.getId() != userIdToUserCookie.get(currentUser.getId()) )
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User cookie wrong");
        if(body.getType() == null | body.getValue() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Settings not found");
        accountService.changeUser(currentUser, body.getType(), body.getValue());
        return ResponseEntity.ok("OK");
    }

    @RequestMapping(path = "api/score", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> changeScore(@RequestBody GetBodySettings body,  HttpSession httpSession)  {
        String login = (String) httpSession.getAttribute("Login");
        if( login == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        UserProfile currentUser = accountService.getUserByLogin(login);
        if(currentUser == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        if(httpSession.getId() != userIdToUserCookie.get(currentUser.getId()) )
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User cookie wrong");
        if(body.getScore() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Settings not found");
        accountService.changeScore(currentUser, body.getScore());
        return ResponseEntity.ok("OK");

    }
    /**
     * Конструктор тоже будет вызван с помощью reflection'а. Другими словами, объект создается через ApplicationContext.
     * Поэтому в нем можно использовать DI. Подробнее про это расскажу на лекции.
     */
    @Autowired
    public UserController(@NotNull AccountService accountService) {
        this.accountService = accountService;
    }

    private static final class GetBody {
        private String mail, login, password;

        @JsonCreator
        @SuppressWarnings({"unused", "null"})
        GetBody(@JsonProperty("login") String login, @JsonProperty("password") String password, @JsonProperty("password") String mail ) {
            this.login = login;
            this.mail = mail;
            this.password = password;
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }

        public String getMail() {
            return mail;
        }
    }

    private static final class GetBodySettings {
        private String  type, value;
        private Integer score;

        @JsonCreator
        @SuppressWarnings({"unused", "null"})
        GetBodySettings(@JsonProperty("settingstype") String type, @JsonProperty("settingsvalue") String value, @JsonProperty("score") Integer score) {
            this.type = type;
            this.value = value;
            this.score = score;
        }

        public String getType() {
            return type;
        }

        public String getValue() {
            return value;
        }

        public Integer getScore() {
            return score;
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
