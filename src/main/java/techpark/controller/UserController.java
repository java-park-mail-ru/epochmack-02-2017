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
import techpark.user.UserToInfo;


import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

/**
 * Created by Fedorova on 20/02/2017.
 */

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://gem-td.herokuapp.com", "http://gem-td.herokuapp.com"})
@SuppressWarnings("unused")
public class UserController {

    @NotNull
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("api/registration")
    public ResponseEntity<?> registration(@RequestBody GetBody body, HttpSession httpSession) throws InvocationTargetException, IllegalAccessException {
        final String login = body.getLogin();
        final String password = body.getPassword();
        final String mail = body.getMail();

        if(login == null || mail == null || password == null)
            return new ResponseEntity<>(new ErrorResponse("Request param not found"), HttpStatus.NOT_FOUND);
        if(login.length() < 5 || mail.isEmpty() || password.length() < 4)
            return new ResponseEntity<>(new ErrorResponse("Wrong data"), HttpStatus.BAD_REQUEST);
        if(accountService.getUserByLogin(login)  != null)
            return new ResponseEntity<>(new ErrorResponse("Login already exist"), HttpStatus.CONFLICT);
        if(accountService.verifyMail(body.getMail()))
            return new ResponseEntity<>(new ErrorResponse("E-mail already exist"), HttpStatus.CONFLICT);
        accountService.register(mail, login, passwordEncoder.encode(password));
        httpSession.setAttribute("Login", login);
        return new ResponseEntity<>(new OkResponse(), HttpStatus.OK);
    }

    @PostMapping("api/login")
    public ResponseEntity<?> login(@RequestBody GetBody body, HttpSession httpSession) throws InvocationTargetException, IllegalAccessException {
        final String login = body.getLogin();
        final String password = body.getPassword();
        final UserProfile currentUser = accountService.getUserByLogin(login);
        if(currentUser != null) {
            if (passwordEncoder.matches(password, currentUser.getPassword())) {
                httpSession.setAttribute("Login", login);
                return new ResponseEntity<>(new OkResponse(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new ErrorResponse("Wrong login or password"), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("api/user")
    public ResponseEntity<?> getUser (HttpSession httpSession){
        final String login = (String) httpSession.getAttribute("Login");
        if( login == null)
            return new ResponseEntity<>(new ErrorResponse("User not found"), HttpStatus.CONFLICT);
        return new ResponseEntity<>(new LoginResponse(login), HttpStatus.OK);
    }

    @GetMapping("api/logout")
    public ResponseEntity<?> logout( HttpSession httpSession)  {
        if(httpSession.getAttribute("Login") == null)
            return new ResponseEntity<>(new ErrorResponse("User is not authorized"), HttpStatus.CONFLICT);
        httpSession.removeAttribute("Login");
        return new ResponseEntity<>(new OkResponse(), HttpStatus.OK);
    }

    @PostMapping("api/settings")
    public ResponseEntity<?> editUser(@RequestBody GetBody body,  HttpSession httpSession) throws NullPointerException {
        final String login = (String) httpSession.getAttribute("Login");
        final String password = body.getPassword()==null ? "1" : body.getPassword();
        final String result = accountService.changeUser(login, body.getLogin(), body.getMail(),
                passwordEncoder.encode(password));
        if(result == null)
            return new ResponseEntity<>(new ErrorResponse("User not found"), HttpStatus.NOT_FOUND);
        if(result.equals("Login already exist"))
            return new ResponseEntity<>(new ErrorResponse("Login already exist"), HttpStatus.CONFLICT);
        if(result.equals("Mail already exist"))
            return new ResponseEntity<>(new ErrorResponse("Mail already exist"), HttpStatus.CONFLICT);
        return new ResponseEntity<>(new OkResponse(), HttpStatus.OK);
    }

    @PostMapping("api/setscore")
    public ResponseEntity<?> setScore(@RequestBody GetScoreBody body,  HttpSession httpSession)  {
        final String login = (String) httpSession.getAttribute("Login");
        if(login == null)
            return new ResponseEntity<>(new ErrorResponse("Empty user"), HttpStatus.NOT_FOUND);
        final UserProfile currentUser = accountService.getUserByLogin(login);
        if(currentUser == null)
            return new ResponseEntity<>(new ErrorResponse("User not found"), HttpStatus.NOT_FOUND);
        final Integer score = body.getScore();
        if(score == null)
            return new ResponseEntity<>(new ErrorResponse("Empty score"), HttpStatus.NOT_FOUND);
        accountService.changeScore(currentUser, body.getScore());
        return new ResponseEntity<>(new OkResponse(), HttpStatus.OK);
    }

    @GetMapping("api/getscore")
    public ResponseEntity<?> getScore (HttpSession httpSession){
        final String login = (String) httpSession.getAttribute("Login");
        if(login == null)
            return new ResponseEntity<>(new ErrorResponse("Empty user"), HttpStatus.NOT_FOUND);
        final UserProfile currentUser = accountService.getUserByLogin(login);
        if(currentUser == null)
            return new ResponseEntity<>(new ErrorResponse("User not found"), HttpStatus.NOT_FOUND);
        final int score = accountService.getScore(login);
        return new ResponseEntity<>(new ScoreResponse(score), HttpStatus.OK);
    }

    @GetMapping("api/users")
    public ResponseEntity<?> getUsers(HttpSession httpSession) {
        final LinkedList<UserToInfo> users = accountService.getAllUsers();
        return new ResponseEntity<>(new UsersListResponse(users), HttpStatus.OK);
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
        GetBody(@JsonProperty("login") String login, @JsonProperty("password") String password,
                @JsonProperty("mail") String mail ) {
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

    private static final class GetScoreBody {
        private final Integer score;

        @JsonCreator
        @SuppressWarnings({"unused", "null"})
        GetScoreBody(@JsonProperty("score") Integer score) {
            this.score = score;
        }

        public Integer getScore() {
            return score;
        }
    }


}
