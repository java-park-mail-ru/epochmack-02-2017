package techpark.controller;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import techpark.service.AccountService;
import techpark.user.UserProfile;
import techpark.jsonResponse.* ;


import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Fedorova on 20/02/2017.
 */
@SuppressWarnings("unused")
@RestController
public class UserController {

    @NotNull
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("api/registration")
    public ResponseEntity<?> registration(@RequestBody GetBody body, HttpSession httpSession) throws InvocationTargetException, IllegalAccessException {
        final String login = body.getLogin();
        final String password = body.getPassword();
        final String mail = body.getMail();

        if(accountService.getUserByLogin(login)  != null)
            return  new ErrorResponse(HttpStatus.CONFLICT, "Login already exist").getMessage();
        if(accountService.getUserByMail(mail) != null)
            return  new ErrorResponse(HttpStatus.CONFLICT,"Email already exist").getMessage();
        accountService.register(mail, login, passwordEncoder.encode(password));
        httpSession.setAttribute("Login", login);
        return new OkResponse().getMessage();
    }

    @PostMapping("api/login")
    public ResponseEntity<?> login(@RequestBody GetBody body, HttpSession httpSession) throws InvocationTargetException, IllegalAccessException {
        final String login = body.getLogin();
        final String password = body.getPassword();
        if(!accountService.verifylogin(login))
            return  new ErrorResponse(HttpStatus.NOT_FOUND,"Wrong login or password").getMessage();
        final UserProfile currentUser = accountService.getUserByLogin(login);
        if(passwordEncoder.matches(password, currentUser.getPassword())){
            httpSession.setAttribute("Login", login);
            return new OkResponse().getMessage();
        }
        return  new ErrorResponse(HttpStatus.NOT_FOUND,"Wrong login or password").getMessage();
    }

    @GetMapping("api/user")
    public ResponseEntity<?> getUser (HttpSession httpSession){
        final String login = (String) httpSession.getAttribute("Login");
        if( login == null)
            return new LoginResponse("User not found").getMessage();
        return new LoginResponse(login).getMessage();
    }

    @GetMapping("api/logout")
    public ResponseEntity<?> logout( HttpSession httpSession)  {
        if(httpSession.getAttribute("Login") == null)
            return  new ErrorResponse(HttpStatus.NOT_FOUND,"User is not authorized").getMessage();
        httpSession.removeAttribute("Login");
        return new OkResponse().getMessage();
    }

    @PostMapping("api/settings")
    public ResponseEntity<?> editUser(@RequestBody GetBodySettings body,  HttpSession httpSession) throws NullPointerException {
        final String login = (String) httpSession.getAttribute("Login");
        final UserProfile currentUser = accountService.getUserByLogin(login);
        if(currentUser == null)
            return  new ErrorResponse(HttpStatus.NOT_FOUND,"User not found").getMessage();
        final String type = body.getType();
        final String value = body.getValue();
        if( type == null || type.isEmpty())
            return  new ErrorResponse(HttpStatus.NOT_FOUND,"Empty type").getMessage();
        if( value == null || value.isEmpty())
            return  new ErrorResponse(HttpStatus.NOT_FOUND,"Empty value").getMessage();
        if(accountService.verifylogin(login))
            return  new ErrorResponse(HttpStatus.CONFLICT,"Login already exist").getMessage();
        accountService.changeUser(currentUser, type, value);
        if (type.equals("login")) httpSession.setAttribute("Login", currentUser.getLogin());
        return new OkResponse().getMessage();
    }

    @PostMapping("api/setscore")
    public ResponseEntity<?> setScore(@RequestBody GetBodySettings body,  HttpSession httpSession)  {
        final String login = (String) httpSession.getAttribute("Login");
        if(login == null)
            return  new ErrorResponse(HttpStatus.NOT_FOUND,"Empty user").getMessage();
        final UserProfile currentUser = accountService.getUserByLogin(login);
        if(currentUser == null)
            return  new ErrorResponse(HttpStatus.NOT_FOUND,"User not found").getMessage();
        final Integer score = body.getScore();
        if(score == null)
            return  new ErrorResponse(HttpStatus.NOT_FOUND,"Empty score").getMessage();
        accountService.changeScore(currentUser, body.getScore());
        return new OkResponse().getMessage();

    }

    @GetMapping("api/getscore")
    public ResponseEntity<?> getScore (HttpSession httpSession){
        final String login = (String) httpSession.getAttribute("Login");
        if(login == null)
            return  new ErrorResponse(HttpStatus.NOT_FOUND,"Empty user").getMessage();
        final UserProfile currentUser = accountService.getUserByLogin(login);
        if(currentUser == null)
            return  new ErrorResponse(HttpStatus.NOT_FOUND,"User not found").getMessage();
        return new ScoreResponse(currentUser.getScore()).getMessage();
    }


    public UserController(@NotNull AccountService accountService, @NotNull PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
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
