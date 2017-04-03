package techpark.DAO;

import org.jetbrains.annotations.Nullable;
import techpark.user.UserProfile;
import techpark.user.UserToInfo;

import javax.validation.constraints.NotNull;
import java.util.LinkedList;

/**
 * Created by Варя on 28.03.2017.
 */
public interface RequestUsersDAO {

    int addUser(String login, String mail, String password);

    boolean verifyMail(String mail);

    UserProfile getUserByLogin(String login);

    String changeUser(@NotNull String oldLogin, @Nullable String login, @Nullable String mail, @Nullable String password);

    void changeScore(Integer score, String login);

    int getScore(String login);

    LinkedList<UserToInfo> getBestUsers();
}
