package techpark.mechanics;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import techpark.Application;
import techpark.exceptions.AccountServiceDBException;
import techpark.exceptions.AccountServiceDDException;
import techpark.game.GameMechanics;
import techpark.game.GameSession;
import techpark.game.avatar.GameUser;
import techpark.game.avatar.Square;
import techpark.game.base.ClientSnap;
import techpark.service.AccountService;
import techpark.user.UserProfile;
import techpark.websocket.RemotePointService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by Варя on 27.04.2017.
 */


@SuppressWarnings({"MagicNumber", "SpringJavaAutowiredMembersInspection"})
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class GameMechanicsTest {

    @SuppressWarnings("unused")
    @MockBean
    private RemotePointService remotePointService;
    @Autowired
    private GameMechanics gameMechanics;
    @Autowired
    private AccountService accountService;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private UserProfile user1;
    private UserProfile user2;


    @Before
    public void setUp ()  {
        when(remotePointService.isConnected(any())).thenReturn(true);
        try {
            try {
                accountService.register("mail1", "login1", "password1");
                accountService.register("mail2", "login2", "password2");
            }
            catch (AccountServiceDDException e){
                logger.warn("dublicate value", e);
            }
            Assert.assertNotNull(user1 = accountService.getUserByLogin("login1"));
            Assert.assertNotNull(user2 = accountService.getUserByLogin("login2"));
        }
        catch (AccountServiceDBException q){
            logger.warn("failed connect to db", q);
        }
    }

    @Test
    public void gameStartedTest () throws InterruptedException, ExecutionException {
        final GameSession gameSession = startGame();
        endGame(gameSession);
    }

    @Test
    public void putTowerTest() throws InterruptedException, ExecutionException {
        final GameSession gameSession = startGame();
        final GameUser userProfile1 = gameSession.getUsers().get(0);
        final GameUser userProfile2 = gameSession.getUsers().get(1);
        final ClientSnap snap1 = new ClientSnap();
        snap1.setSquare(new Square(8, 3));
        final ClientSnap snap2 = new ClientSnap();
        snap2.setSquare(new Square(5, 3));
        gameMechanics.addClientSnapshot(userProfile1.getUser(), snap1);
        gameMechanics.addClientSnapshot(userProfile2.getUser(), snap2).get();
        Assert.assertNotEquals(gameSession.field.getMap()[8][3], 'o');
        Assert.assertNotEquals(gameSession.field.getMap()[5][3], 'o');
        endGame(gameSession);
    }

    @Test
    public void putWrongTowerTest() throws InterruptedException, ExecutionException {
        final GameSession gameSession = startGame();
        final GameUser user = gameSession.getUsers().get(0);
        final ClientSnap snap1 = new ClientSnap();
        snap1.setSquare(new Square(2, 2));
        gameMechanics.addClientSnapshot(user.getUser(), snap1).get();
        Assert.assertEquals(gameSession.field.getMap()[2][2], 'o');
        endGame(gameSession);
    }

    @Test
    public void encloseTowerTest() throws InterruptedException, ExecutionException {
        final GameSession gameSession = startGame();
        final GameUser user = gameSession.getUsers().get(0);
        final ClientSnap snap1 = new ClientSnap();
        snap1.setSquare(new Square(5, 4));
        gameMechanics.addClientSnapshot(user.getUser(), snap1);
        final ClientSnap snap2 = new ClientSnap();
        snap2.setSquare(new Square(5, 6));
        gameMechanics.addClientSnapshot(user.getUser(), snap2);
        final ClientSnap snap3 = new ClientSnap();
        snap3.setSquare(new Square(4, 5));
        gameMechanics.addClientSnapshot(user.getUser(), snap3);
        final ClientSnap snap4 = new ClientSnap();
        snap4.setSquare(new Square(6, 5));
        gameMechanics.addClientSnapshot(user.getUser(), snap4).get();
        Assert.assertEquals(gameSession.field.getMap()[6][5], 'o');
        endGame(gameSession);
    }


    @Test
    public void waveTest() throws InterruptedException, ExecutionException {
        final GameSession gameSession = startGame();
        final UserProfile us1 = gameSession.getUsers().get(0).getUser();
        final UserProfile us2 = gameSession.getUsers().get(1).getUser();
        final ClientSnap snap1 = new ClientSnap();
        snap1.setSquare(new Square(1, 1));
        gameMechanics.addClientSnapshot(us1, snap1);
        final ClientSnap snap2 = new ClientSnap();
        snap2.setSquare(new Square(1, 2));
        gameMechanics.addClientSnapshot(us1, snap2);
        final ClientSnap snap3 = new ClientSnap();
        snap3.setSquare(new Square(1, 3));
        gameMechanics.addClientSnapshot(us1, snap3);
        final ClientSnap snap7 = new ClientSnap();
        snap7.setSquare(new Square(1, 2));
        gameMechanics.addClientSnapshot(us1, snap7);


        final ClientSnap snap11 = new ClientSnap();
        snap11.setSquare(new Square(3, 1));
        gameMechanics.addClientSnapshot(us2, snap11);
        final ClientSnap snap22 = new ClientSnap();
        snap22.setSquare(new Square(3, 2));
        gameMechanics.addClientSnapshot(us2, snap22);
        final ClientSnap snap33 = new ClientSnap();
        snap33.setSquare(new Square(3, 3));
        gameMechanics.addClientSnapshot(us2, snap33);
        final ClientSnap snap77 = new ClientSnap();
        snap77.setSquare(new Square(3, 2));
        gameMechanics.addClientSnapshot(us2, snap77).get();
        Assert.assertEquals(gameSession.getPoints(), 10);
        Assert.assertEquals(gameSession.getWave(), 1);
        Assert.assertEquals(gameSession.field.getMap()[3][3], '#');
        Assert.assertEquals(gameSession.field.getMap()[1][3], '#');
        endGame(gameSession);
    }

    private GameSession startGame() throws InterruptedException, ExecutionException {
        gameMechanics.addPlayer(user1);
        gameMechanics.addPlayer(user2).get();
        final GameSession gameSession = gameMechanics.getSessionFor(user1);
        Assert.assertNotNull(gameSession);
        final List<UserProfile> users = new ArrayList<>();
        for(GameUser gamer: gameSession.getUsers())
            users.add(gamer.getUser());
        Assert.assertEquals(users, Arrays.asList(user1, user2));
        return gameSession;
    }

    private void endGame(GameSession gameSession){
        gameMechanics.endGameFor(gameSession);
    }
}
