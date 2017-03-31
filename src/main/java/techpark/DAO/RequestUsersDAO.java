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

    public int addUser(String login, String mail, String password);

    public boolean verifyMail(String mail);

    public UserProfile getUserByLogin(String login);

    public String changeUser(@NotNull String oldLogin, @Nullable String login, @Nullable String mail, @Nullable String password);

    public void changeScore(Integer score, String login);

    public Integer getScore(String login);

    public LinkedList<UserToInfo> getBestUsers();
}
