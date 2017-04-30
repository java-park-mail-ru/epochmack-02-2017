package techpark.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import techpark.Application;
import techpark.exceptions.AccountServiceDBException;
import techpark.exceptions.AccountServiceDDException;
import techpark.user.UserProfile;
import techpark.user.UserToInfo;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Варя on 30.03.2017.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class AccountServiceTest {

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private AccountService accountService;


    @Before
    public void createTestUsers() throws AccountServiceDBException, AccountServiceDDException {
        final UserProfile user = new UserProfile("mail1", "login1", "password1");
        final UserProfile user1 = new UserProfile("mail2", "login2", "password2");
        final UserProfile user2 = new UserProfile("mail3", "login3", "password3");
        accountService.register(user.getMail(), user.getLogin(), user.getPassword());
        accountService.register(user1.getMail(), user1.getLogin(), user1.getPassword());
        accountService.register(user2.getMail(), user2.getLogin(), user2.getPassword());
    }

    @Test
    public void testRegister() throws AccountServiceDBException, AccountServiceDDException {
        final UserProfile user = new UserProfile("mail4", "login4", "password4");
        accountService.register(user.getMail(), user.getLogin(), user.getPassword());
        final UserProfile userByLogin = accountService.getUserByLogin("login4");
        assertNotNull(userByLogin);
        assertEquals(user.getLogin(),userByLogin.getLogin());
        assertEquals(user.getMail(),userByLogin.getMail());
    }

    @Test
    public void testVerifyLogin() throws AccountServiceDBException, AccountServiceDDException {
        final UserProfile user = new UserProfile("mail4", "login4", "password4");
        accountService.register(user.getMail(), user.getLogin(), user.getPassword());
        assertTrue(accountService.verifyMail(user.getMail()));
    }

    @Test
    public void testGetUserByLogin() throws AccountServiceDBException, AccountServiceDDException {
        final UserProfile user = new UserProfile("mail5", "login5", "password5");
        accountService.register(user.getMail(), user.getLogin(), user.getPassword());
        final UserProfile userByLogin = accountService.getUserByLogin("login5");
        assertNotNull(userByLogin);
        assertEquals(user.getLogin(),userByLogin.getLogin());
        assertEquals(user.getMail(),userByLogin.getMail());
    }

    @Test
    public void testChangeUser() throws AccountServiceDBException, AccountServiceDDException {
        final UserProfile user = new UserProfile("mail6", "login6", "password6");
        accountService.register(user.getMail(), user.getLogin(), user.getPassword());
        accountService.changeUser(user, "newlogin", "newmail", "newpassword");
        final UserProfile changeUser = accountService.getUserByLogin("newlogin");
        assertNotNull(changeUser);
        assertEquals("newlogin", changeUser.getLogin());
        assertEquals("newmail", changeUser.getMail());
        assertTrue(accountService.verifyMail("newmail"));
        assertFalse(accountService.verifyMail("mail6"));
    }

    @Test
    public void testChangeUserNotAll() throws AccountServiceDBException, AccountServiceDDException {
        final UserProfile user = new UserProfile("mail7", "login7", "password7");
        accountService.register(user.getMail(), user.getLogin(), user.getPassword());
        accountService.changeUser(user, "newlogin1", null, "newpassword1");
        final UserProfile changeUser = accountService.getUserByLogin("newlogin1");
        assertNotNull(changeUser);
        assertEquals("newlogin1", changeUser.getLogin());
        assertEquals("mail7", changeUser.getMail());
        assertTrue(accountService.verifyMail("mail7"));
    }

    @Test
    public void testChangeScore() throws AccountServiceDBException {
        final UserProfile user = accountService.getUserByLogin("login1");
        assertNotNull(user);
        accountService.changeScore(user, 6);
        assertEquals(6, accountService.getScore("login1"));
    }

    @Test
    public void testChangeScoreToLowest() throws AccountServiceDBException {
        UserProfile user = accountService.getUserByLogin("login1");
        assertNotNull(user);
        accountService.changeScore(user, 6);
        assertEquals(6, accountService.getScore("login1"));
        user = accountService.getUserByLogin("login1");
        assertNotNull(user);
        accountService.changeScore(user, 5);
        assertEquals(6, accountService.getScore("login1"));
    }

    @Test
    public void testGetScore() throws AccountServiceDBException {
        final UserProfile user = accountService.getUserByLogin("login2");
        assertNotNull(user);
        accountService.changeScore(user, 10);
        assertEquals(10, accountService.getScore("login2"));
    }

    @SuppressWarnings({"ConstantConditions", "MagicNumber"})
    @Test
    public void testGetAllUsers() throws AccountServiceDBException {
        UserProfile user1 = accountService.getUserByLogin("login1");
        UserProfile user2 = accountService.getUserByLogin("login2");
        UserProfile user3 = accountService.getUserByLogin("login3");
        assertNotNull(user1);
        assertNotNull(user2);
        assertNotNull(user3);
        accountService.changeScore(user1, 100);
        accountService.changeScore(user2, 110);
        accountService.changeScore(user3, 120);
        user1 = accountService.getUserByLogin("login1");
        user2 = accountService.getUserByLogin("login2");
        user3 = accountService.getUserByLogin("login3");
        final ArrayList<UserToInfo> bestUsers = accountService.getAllUsers();
        assertEquals(user3.getLogin(), bestUsers.get(0).login);
        assertEquals(user3.getScore().intValue(), bestUsers.get(0).score);
        assertEquals(user2.getLogin(), bestUsers.get(1).login);
        assertEquals(user2.getScore().intValue(), bestUsers.get(1).score);
        assertEquals(user1.getLogin(), bestUsers.get(2).login);
        assertEquals(user1.getScore().intValue(), bestUsers.get(2).score);
    }
}
