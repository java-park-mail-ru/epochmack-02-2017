package techpark.controller;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import techpark.exceptions.AccountServiceDBException;
import techpark.exceptions.AccountServiceDDException;
import techpark.service.AccountServiceImpl;
import techpark.user.UserProfile;
import techpark.jsonresponse.*;
import techpark.user.UserToInfo;


import javax.servlet.http.HttpSession;
import java.util.ArrayList;

/**
 * Created by Fedorova on 20/02/2017.
 */

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://gem-td.herokuapp.com", "http://gem-td.herokuapp.com"})
@SuppressWarnings("unused")
public class UserController {

    @NotNull
    private final AccountServiceImpl accountService;
    private final PasswordEncoder passwordEncoder;

    @ExceptionHandler(AccountServiceDBException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ResponseBody
    Response
    handleDBException() {
        return new ErrorResponse("Server error");
    }

    @SuppressWarnings("OverlyComplexMethod")
    @PostMapping("api/registration")
    public ResponseEntity<Response> registration(@RequestBody GetBody body, HttpSession httpSession)
            throws AccountServiceDBException {
        final String login = body.getLogin();
        final String password = body.getPassword();
        final String mail = body.getMail();

        if (login == null || mail == null || password == null)
            return new ResponseEntity<>(new ErrorResponse("Request param not found"), HttpStatus.NOT_FOUND);
        if (login.length() < 5 || mail.isEmpty() || password.length() < 4)
            return new ResponseEntity<>(new ErrorResponse("Wrong data"), HttpStatus.BAD_REQUEST);
        try {
            accountService.register(mail, login, passwordEncoder.encode(password));
            httpSession.setAttribute("Login", login);
            return new ResponseEntity<>(new OkResponse(), HttpStatus.OK);

        } catch (AccountServiceDDException e) {
            if (accountService.getUserByLogin(login) != null)
                return new ResponseEntity<>(new ErrorResponse("Login already exist"), HttpStatus.CONFLICT);
            if (accountService.verifyMail(body.getMail()))
                return new ResponseEntity<>(new ErrorResponse("E-mail already exist"), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(new ErrorResponse("Server error"), HttpStatus.EXPECTATION_FAILED);
    }

    @PostMapping("api/login")
    public ResponseEntity<Response> login(@RequestBody GetBody body, HttpSession httpSession)
            throws AccountServiceDBException {
        final String login = body.getLogin();
        final String password = body.getPassword();
        final UserProfile currentUser = accountService.getUserByLogin(login);
        if (currentUser != null) {
            if (passwordEncoder.matches(password, currentUser.getPassword())) {
                httpSession.setAttribute("Login", login);
                return new ResponseEntity<>(new OkResponse(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new ErrorResponse("Wrong login or password"), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("api/user")
    public ResponseEntity<Response> getUser(HttpSession httpSession) {
        final String login = (String) httpSession.getAttribute("Login");
        if (login == null)
            return new ResponseEntity<>(new ErrorResponse("User not found"), HttpStatus.CONFLICT);
        return new ResponseEntity<>(new LoginResponse(login), HttpStatus.OK);
    }

    @GetMapping("api/logout")
    public ResponseEntity<Response> logout(HttpSession httpSession) {
        if (httpSession.getAttribute("Login") == null)
            return new ResponseEntity<>(new ErrorResponse("User is not authorized"), HttpStatus.CONFLICT);
        httpSession.removeAttribute("Login");
        return new ResponseEntity<>(new OkResponse(), HttpStatus.OK);
    }

    @PostMapping("api/settings")
    public ResponseEntity<Response> editUser(@RequestBody GetBody body, HttpSession httpSession)
            throws AccountServiceDBException {
        final String login = (String) httpSession.getAttribute("Login");
        final String password = body.getPassword() == null ? "1" : body.getPassword();
        final UserProfile user;
        if ((user = accountService.getUserByLogin(login)) == null)
            return new ResponseEntity<>(new ErrorResponse("User not found"), HttpStatus.NOT_FOUND);
        try {
            accountService.changeUser(user, body.getLogin(), body.getMail(),
                    passwordEncoder.encode(password));
            return new ResponseEntity<>(new OkResponse(), HttpStatus.OK);
        } catch (AccountServiceDDException e) {
            if (accountService.getUserByLogin(login) != null)
                return new ResponseEntity<>(new ErrorResponse("Login already exist"), HttpStatus.CONFLICT);
            if (accountService.verifyMail(body.getMail()))
                return new ResponseEntity<>(new ErrorResponse("E-mail already exist"), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(new ErrorResponse("Server error"), HttpStatus.EXPECTATION_FAILED);
    }

    @PostMapping("api/setscore")
    public ResponseEntity<Response> setScore(@RequestBody GetScoreBody body, HttpSession httpSession)
            throws AccountServiceDBException {
        final String login = (String) httpSession.getAttribute("Login");
        if (login == null)
            return new ResponseEntity<>(new ErrorResponse("Empty user"), HttpStatus.NOT_FOUND);
        final UserProfile currentUser = accountService.getUserByLogin(login);
        if (currentUser == null)
            return new ResponseEntity<>(new ErrorResponse("User not found"), HttpStatus.NOT_FOUND);
        final Integer score = body.getScore();
        if (score == null)
            return new ResponseEntity<>(new ErrorResponse("Empty score"), HttpStatus.NOT_FOUND);
        accountService.changeScore(currentUser, body.getScore());
        return new ResponseEntity<>(new OkResponse(), HttpStatus.OK);

    }

    @GetMapping("api/getscore")
    public ResponseEntity<Response> getScore(HttpSession httpSession)
            throws AccountServiceDBException {
        final String login = (String) httpSession.getAttribute("Login");
        if (login == null)
            return new ResponseEntity<>(new ErrorResponse("Empty user"), HttpStatus.NOT_FOUND);
        final UserProfile currentUser = accountService.getUserByLogin(login);
        if (currentUser == null)
            return new ResponseEntity<>(new ErrorResponse("User not found"), HttpStatus.NOT_FOUND);
        final int score = accountService.getScore(login);
        return new ResponseEntity<>(new ScoreResponse(score), HttpStatus.OK);
    }

    @GetMapping("api/users")
    public ResponseEntity<Response> getUsers(HttpSession httpSession) throws AccountServiceDBException {
        final ArrayList<UserToInfo> users = accountService.getAllUsers();
        return new ResponseEntity<>(new UsersListResponse(users), HttpStatus.OK);
    }


    public UserController(@NotNull AccountServiceImpl accountService, @NotNull PasswordEncoder passwordEncoder) {
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
                @JsonProperty("mail") String mail) {
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
