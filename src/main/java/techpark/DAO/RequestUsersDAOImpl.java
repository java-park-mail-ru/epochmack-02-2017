package techpark.DAO;

import org.jetbrains.annotations.Nullable;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
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
public class RequestUsersDAOImpl {

    private JdbcTemplate jdbcTemplate;

    public RequestUsersDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean addUser(@NotNull String mail, @NotNull String login, @NotNull String password)
            throws DataAccessException {
        try {
            jdbcTemplate.update("INSERT INTO Users (login, email, password) VALUES (?,?,?)",
                    login,
                    mail,
                    password);
            return true;
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean verifyMail(@NotNull String mail) throws DataAccessException {
        try {
            jdbcTemplate.queryForObject("SELECT email FROM Users WHERE lower(email) = lower(?)",
                    String.class,
                    mail);
            return true;
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Nullable
    public UserProfile getUserByLogin(@NotNull String login) throws DataAccessException {
        try {
            return jdbcTemplate.queryForObject("SELECT U.* FROM Users AS U WHERE lower(login)=lower(?)",
                    (result, num) -> new UserProfile(result.getString(2),
                            result.getString(1),
                            result.getString(3),
                            result.getInt(4)),
                    login
            );
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }


    public boolean changeUser(@NotNull UserProfile user, @Nullable String login, @Nullable String mail,
                             @Nullable String password) throws DataAccessException  {
        try {
            jdbcTemplate.update("UPDATE Users SET (login,email,password) = (?,?,?)" +
                            "WHERE lower(login) = lower(?)",
                    login == null ? user.getLogin() : login,
                    mail == null ? user.getMail() : mail,
                    password == null ? user.getPassword() : password,
                    user.getLogin());
            return true;
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void changeScore(@NotNull Integer score, @NotNull String login)
            throws DataAccessException  {
        jdbcTemplate.update("UPDATE Users SET hightScore = ? WHERE lower(login) = lower(?)",
                score,
                login);
    }

    public int getScore(@NotNull String login)  throws DataAccessException  {
        return jdbcTemplate.queryForObject("SELECT hightScore FROM Users WHERE lower(login) = lower(?)",
                Integer.class,
                login);
    }

    public ArrayList<UserToInfo> getBestUsers() throws DataAccessException {
        final ArrayList<UserToInfo> userList = new ArrayList<>();
        jdbcTemplate.query("SELECT login, hightScore FROM Users ORDER BY hightScore DESC LIMIT 100",
                (result, num) ->
                        userList.add(new UserToInfo(result.getString(1), result.getInt(2))));
        return userList;
    }
}
