package techpark.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import techpark.Application;
import techpark.user.UserProfile;
import techpark.user.UserToInfo;

import javax.sql.DataSource;
import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * Created by Варя on 30.03.2017.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;


    @Before
    public void createTestUsers(){
        final UserProfile user = new UserProfile("mail1", "login1", "password1", null);
        final UserProfile user1 = new UserProfile("mail2", "login2", "password2", null);
        final UserProfile user2 = new UserProfile("mail3", "login3", "password3", null);
        accountService.register(user.getMail(), user.getLogin(), user.getPassword());
        accountService.register(user1.getMail(), user1.getLogin(), user1.getPassword());
        accountService.register(user2.getMail(), user2.getLogin(), user2.getPassword());
    }

    @Test
    public void testRegister(){
        final UserProfile user = new UserProfile("mail4", "login4", "password4", null);
        assertNotNull(user);
        final int row = accountService.register(user.getMail(), user.getLogin(), user.getPassword());
        assertEquals(1, row);
        final UserProfile userByLogin = accountService.getUserByLogin("login4");
        assertNotNull(userByLogin);
        assertEquals(user.getLogin(),userByLogin.getLogin());
        assertEquals(user.getMail(),userByLogin.getMail());
    }

    @Test
    public void testVerifyLogin(){
        final UserProfile user = new UserProfile("mail4", "login4", "password4", null);
        assertNotNull(user);
        accountService.register(user.getMail(), user.getLogin(), user.getPassword());
        assertTrue(accountService.verifyMail(user.getMail()));
    }

    @Test
    public void testGetUserByLogin(){
        final UserProfile user = new UserProfile("mail5", "login5", "password5", null);
        assertNotNull(user);
        accountService.register(user.getMail(), user.getLogin(), user.getPassword());
        final UserProfile userByLogin = accountService.getUserByLogin("login5");
        assertNotNull(userByLogin);
        assertEquals(user.getLogin(),userByLogin.getLogin());
        assertEquals(user.getMail(),userByLogin.getMail());
    }

    @Test
    public void testChangeUser(){
        final UserProfile user = new UserProfile("mail6", "login6", "password6", null);
        assertNotNull(user);
        accountService.register(user.getMail(), user.getLogin(), user.getPassword());
        assertEquals("Ok", accountService.changeUser(user.getLogin(), "newlogin", "newmail", "newpassword"));
        final UserProfile changeUser = accountService.getUserByLogin("newlogin");
        assertNotNull(changeUser);
        assertEquals("newlogin", changeUser.getLogin());
        assertEquals("newmail", changeUser.getMail());
        assertTrue(accountService.verifyMail("newmail"));
        assertFalse(accountService.verifyMail("mail6"));
    }

    @Test
    public void testChangeUserNotAll(){
        final UserProfile user = new UserProfile("mail7", "login7", "password7", null);
        assertNotNull(user);
        accountService.register(user.getMail(), user.getLogin(), user.getPassword());
        assertEquals("Ok", accountService.changeUser(user.getLogin(), "newlogin1", null, "newpassword1"));
        final UserProfile changeUser = accountService.getUserByLogin("newlogin1");
        assertNotNull(changeUser);
        assertEquals("newlogin1", changeUser.getLogin());
        assertEquals("mail7", changeUser.getMail());
        assertTrue(accountService.verifyMail("mail7"));
    }

    @Test
    public void testChangeScore(){
        final UserProfile user = accountService.getUserByLogin("login1");
        assertNotNull(user);
        accountService.changeScore(user, 6);
        assertEquals(6, accountService.getScore("login1").longValue());
    }

    @Test
    public void testChangeScoreToLowest(){
        UserProfile user = accountService.getUserByLogin("login1");
        assertNotNull(user);
        accountService.changeScore(user, 6);
        assertEquals(6, accountService.getScore("login1").longValue());
        user = accountService.getUserByLogin("login1");
        System.out.println(user.getScore());
        accountService.changeScore(user, 5);
        assertEquals(6, accountService.getScore("login1").longValue());
    }

    @Test
    public void testGetScore(){
        final UserProfile user = accountService.getUserByLogin("login2");
        assertNotNull(user);
        accountService.changeScore(user, 10);
        assertEquals(10, accountService.getScore("login2").longValue());
    }

    @Test
    public void testGetAllUsers(){
        /*final UserProfile user1 = accountService.getUserByLogin("login1");
        final UserProfile user2 = accountService.getUserByLogin("login2");
        final UserProfile user3 = accountService.getUserByLogin("login3");
        assertNotNull(user1);
        assertNotNull(user2);
        assertNotNull(user3);
        accountService.changeScore(user1, 100);
        accountService.changeScore(user2, 110);
        accountService.changeScore(user3, 120);*/
        LinkedList<UserToInfo> bestUsers = accountService.getAllUsers();
        System.out.println(bestUsers.getFirst().login);
        System.out.println(bestUsers.getLast().login);
        /*assertEquals(user3, bestUsers.get(1));
        assertEquals(user2, bestUsers.get(2));
        assertEquals(user1, bestUsers.get(3));*/
    }
}
