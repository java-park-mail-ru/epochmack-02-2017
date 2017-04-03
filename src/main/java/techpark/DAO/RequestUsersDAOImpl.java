package techpark.DAO;

import org.jetbrains.annotations.Nullable;
import org.omg.PortableInterceptor.ORBInitInfoPackage.DuplicateName;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import techpark.user.UserProfile;
import techpark.user.UserToInfo;

import javax.validation.constraints.NotNull;
import java.util.LinkedList;

/**
 * Created by Варя on 28.03.2017.
 */

@SuppressWarnings("SqlResolve")
@Repository
public class RequestUsersDAOImpl implements RequestUsersDAO {

    private JdbcTemplate jdbcTemplate;

    public RequestUsersDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int addUser(String mail, String login, String password) {
        return jdbcTemplate.update("INSERT INTO Users (login, email, password) VALUES (?,?,?)",
                login,
                mail,
                password);
    }

    @Override
    public boolean verifyMail(String mail) {
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
    @Override
    public UserProfile getUserByLogin(String login) {
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

    @Nullable
    @Override
    public String changeUser(@NotNull String oldLogin, @Nullable String login, @Nullable String mail, @Nullable String password) {
        final UserProfile user = getUserByLogin(oldLogin);
        if (user == null)
            return null;
        try {
            if (login != null && getUserByLogin(login) != null)
                throw new DuplicateName("Login already exist");
            if (mail != null && verifyMail(mail))
                throw new DuplicateName("E-mail already exist");

            jdbcTemplate.update("UPDATE Users SET (login,email,password) = (?,?,?)" +
                            "WHERE lower(login) = lower(?)",
                    login == null ? user.getLogin() : login,
                    mail == null ? user.getMail() : mail,
                    password == null ? user.getPassword() : password,
                    oldLogin);
            return "Ok";
        } catch (DuplicateName e) {
            e.printStackTrace();
            return (e.getMessage());
        }
    }

    @Override
    public void changeScore(Integer score, String login) {
        jdbcTemplate.update("UPDATE Users SET hightScore = ? WHERE lower(login) = lower(?)",
                score,
                login);
    }

    @Override
    public int getScore(String login) {
        return jdbcTemplate.queryForObject("SELECT hightScore FROM Users WHERE lower(login) = lower(?)",
                Integer.class,
                login);
    }

    @Override
    public LinkedList<UserToInfo> getBestUsers() {
        final LinkedList<UserToInfo> userList = new LinkedList<>();
        jdbcTemplate.query("SELECT login, hightScore FROM Users ORDER BY hightScore DESC LIMIT 100",
                (result, num) ->
                        userList.add(new UserToInfo(result.getString(1), result.getInt(2))));
        return userList;
    }
}
