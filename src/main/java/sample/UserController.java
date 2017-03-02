package sample;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.lang.reflect.*;



import javax.servlet.http.HttpSession;

/**
 * Created by Fedorova on 20/02/2017.
 */
@SuppressWarnings("unused")
@RestController
public class UserController {

    @NotNull
    private final AccountService accountService;
    private final Class PaswordEncoder;
    private Method passwordEncoder;


    @RequestMapping(path = "api/registration", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> registration(@RequestBody GetBody body, HttpSession httpSession) throws InvocationTargetException, IllegalAccessException {
        final String login = body.getLogin();
        final String password = body.getPassword();
        final String mail = body.getMail();

        if(accountService.getUserByLogin(login)  != null)
            return  ResponseEntity.status(HttpStatus.CONFLICT).body("{\"error\": \"Login already exist\" }");
        if(accountService.getUserByMail(mail) != null)
            return  ResponseEntity.status(HttpStatus.CONFLICT).body("{\"error\": \"Email already exist\" }");
        final PasswordEncoder passEncoder = (PasswordEncoder) passwordEncoder.invoke(PaswordEncoder);
        accountService.register(mail, login, passEncoder.encode(password));
        httpSession.setAttribute("Login", login);
        return ResponseEntity.ok("{\"OK\": \"OK\"}");
    }

    @RequestMapping(path = "api/login", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> login(@RequestBody GetBody body, HttpSession httpSession) throws InvocationTargetException, IllegalAccessException {
        final String login = body.getLogin();
        final String password = body.getPassword();
        final PasswordEncoder passEncoder = (PasswordEncoder) passwordEncoder.invoke(PaswordEncoder);
        if(!accountService.verifylogin(login))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"Wrong login or password\"}");
        final UserProfile currentUser = accountService.getUserByLogin(login);
        if(passEncoder.matches(password, currentUser.getPassword())){
            httpSession.setAttribute("Login", login);
            return ResponseEntity.ok("{\"OK\": \"OK\"}");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Wrong login or password\"}");
    }

    @RequestMapping(path = "api/user")
    public ResponseEntity<?> getUser (HttpSession httpSession){
        final String login = (String) httpSession.getAttribute("Login");
        if( login == null)
            return ResponseEntity.status(HttpStatus.OK).body("{\"Login\": \"User not found\"}");
        return ResponseEntity.status(HttpStatus.OK).body("{\"Login\": \"" + login + "\"}");
    }

    @RequestMapping(path = "api/logout")
    public ResponseEntity<?> logout( HttpSession httpSession)  {
        if(httpSession.getAttribute("Login") == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"User is not authorized\"}");
        httpSession.removeAttribute("Login");
        return ResponseEntity.ok("{\"OK\": \"OK\"}");
    }

    @RequestMapping(path = "api/settings", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> editUser(@RequestBody GetBodySettings body,  HttpSession httpSession) throws NullPointerException {
        final String login = (String) httpSession.getAttribute("Login");
        final UserProfile currentUser = accountService.getUserByLogin(login);
        if(currentUser == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"User not found\"}");
        final String type = body.getType();
        final String value = body.getValue();
        if( type == null || type.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"Empty type\"}");
        if( value == null || value.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"Empty value\"}");
        if(accountService.verifylogin(login))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"error\": \"Login already exist\"}");
        accountService.changeUser(currentUser, type, value);
        if (type.equals("login")) httpSession.setAttribute("Login", currentUser.getLogin());
        return ResponseEntity.ok("{\"OK\": \"OK\"}");
    }

    @RequestMapping(path = "api/setscore", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> setScore(@RequestBody GetBodySettings body,  HttpSession httpSession)  {
        final String login = (String) httpSession.getAttribute("Login");
        if(login == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"Empty user\"}");
        final UserProfile currentUser = accountService.getUserByLogin(login);
        if(currentUser == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"User not found\"}");
        final Integer score = body.getScore();
        if(score == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"Empty score\"}");
        accountService.changeScore(currentUser, body.getScore());
        return ResponseEntity.ok("{\"OK\": \"OK\"}");

    }

    @RequestMapping(path = "api/getscore")
    public ResponseEntity<?> getScore (HttpSession httpSession){
        final String login = (String) httpSession.getAttribute("Login");
        if(login == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"Empty user\"}");
        final UserProfile currentUser = accountService.getUserByLogin(login);
        if(currentUser == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"User not found\"}");
        return  ResponseEntity.status(HttpStatus.OK).body("{\"score\":\"" + currentUser.getScore() + "\"}");
    }


    public UserController(@NotNull AccountService accountService) throws NoSuchMethodException, ClassNotFoundException{
        this.accountService = accountService;
        this.PaswordEncoder = Class.forName("sample.Application");
        final  Class[] param = new Class[]{};
        this.passwordEncoder = this.PaswordEncoder.getMethod("passwordEncoder", param);
    }

    private static final class GetBody {
        private final String mail;
        private final String login;
        private final String password;

        @JsonCreator
        @SuppressWarnings({"unused", "null"})
        GetBody(@JsonProperty("login") String login, @JsonProperty("password") String password, @JsonProperty("mail") String mail ) {
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
        private final String  type;
        private final String  value;
        private final Integer score;

        @JsonCreator
        @SuppressWarnings({"unused", "null"})
        GetBodySettings(@JsonProperty("type") String type, @JsonProperty("value") String value, @JsonProperty("score") Integer score) {
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


}
