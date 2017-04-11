package techpark.DAO;

import org.jetbrains.annotations.Nullable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import techpark.user.UserProfile;
import techpark.user.UserToInfo;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

/**
 * Created by Варя on 28.03.2017.
 */

@SuppressWarnings("SqlResolve")
@Repository
public class RequestUsersDAO {

    private JdbcTemplate jdbcTemplate;

    public RequestUsersDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addUser(@NotNull String mail, @NotNull String login, @NotNull String password) {
        jdbcTemplate.update("INSERT INTO Users (login, email, password) VALUES (?,?,?)",
                    login,
                    mail,
                    password);
    }

    public void verifyMail(@NotNull String mail) {
            jdbcTemplate.queryForObject("SELECT email FROM Users WHERE lower(email) = lower(?)",
                    String.class,
                    mail);
    }

    @Nullable
    public UserProfile getUserByLogin(@NotNull String login){
            return jdbcTemplate.queryForObject("SELECT U.* FROM Users AS U WHERE lower(login)=lower(?)",
                    (result, num) -> new UserProfile(result.getString(2),
                            result.getString(1),
                            result.getString(3),
                            result.getInt(4)),
                    login
            );
    }


    public void changeUser(@NotNull UserProfile user, @Nullable String login, @Nullable String mail,
                             @Nullable String password)  {
            jdbcTemplate.update("UPDATE Users SET (login,email,password) = (?,?,?)" +
                            "WHERE lower(login) = lower(?)",
                    login == null ? user.getLogin() : login,
                    mail == null ? user.getMail() : mail,
                    password == null ? user.getPassword() : password,
                    user.getLogin());
    }

    public void changeScore(@NotNull Integer score, @NotNull String login) {
        jdbcTemplate.update("UPDATE Users SET hightScore = ? WHERE lower(login) = lower(?)",
                score,
                login);
    }

    public int getScore(@NotNull String login)  {
        return jdbcTemplate.queryForObject("SELECT hightScore FROM Users WHERE lower(login) = lower(?)",
                Integer.class,
                login);
    }

    public ArrayList<UserToInfo> getBestUsers() {
        final ArrayList<UserToInfo> userList = new ArrayList<>();
        jdbcTemplate.query("SELECT login, hightScore FROM Users ORDER BY hightScore DESC LIMIT 100",
                (result, num) ->
                        userList.add(new UserToInfo(result.getString(1), result.getInt(2))));
        return userList;
    }
}
