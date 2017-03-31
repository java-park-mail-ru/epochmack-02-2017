package techpark.DAO;

import org.jetbrains.annotations.Nullable;
import org.omg.PortableInterceptor.ORBInitInfoPackage.DuplicateName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import techpark.DBconnect.SelectQuery;
import techpark.user.UserProfile;
import techpark.user.UserToInfo;

import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Created by Варя on 28.03.2017.
 */

@Repository
public class RequestUsersDAOImpl implements RequestUsersDAO {

    private JdbcTemplate jdbcTemplate;

    public RequestUsersDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int addUser(String mail, String login, String password) {

        //try{
            return jdbcTemplate.update("INSERT INTO Users (login, email, password) VALUES (?,?,?)",
                    login,
                    mail,
                    password);
        /*}
        catch (EmptyResultDataAccessException e){
            e.printStackTrace();
            return 0;
        }*/
    }

    @Override
    public boolean verifyMail(String mail) {
        try {
            jdbcTemplate.queryForObject("SELECT email FROM Users WHERE lower(email) = lower(?)",
                    String.class,
                    mail);
            return true;
            /*return SelectQuery.execute("SELECT email FROM User WHERE lower(email) = lower(?)",
                    preparedStatement -> {
                preparedStatement.setString(1, mail);
                final ResultSet resultSet = preparedStatement.executeQuery();
                if(!resultSet.next())
                    throw new SQLException("Not found");
                return true;
                    });*/
        }
        catch (EmptyResultDataAccessException e){
            e.printStackTrace();
            return false;
        }

    }

    @Nullable
    @Override
    public UserProfile getUserByLogin(String login) {
        try {
            return jdbcTemplate.queryForObject("SELECT U.* FROM Users AS U WHERE lower(login)=lower(?)",
                    //new Object[]{login},
                    (result, num) -> new UserProfile(result.getString(2),
                            result.getString(1),
                            result.getString(3),
                            result.getInt(4)),
                    login
                    );
            /*return SelectQuery.execute("SELECT * FROM User WHERE lower(login) = lower(login)",
                    preparedStatement -> {
                preparedStatement.setString(1, login);
                final ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                return new UserProfile(resultSet.getString(2),
                                resultSet.getString(1),
                                resultSet.getString(3));
                    });*/
        }
        catch (EmptyResultDataAccessException e){
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    @Override
    public String changeUser(@NotNull String oldLogin, @Nullable String login, @Nullable String mail, @Nullable String password) {
        final UserProfile user = getUserByLogin(oldLogin);
        if(user == null)
            return null;
        try{
            if(login != null && getUserByLogin(login)!=null)
                throw new DuplicateName("Login already exist");
            if(mail != null && verifyMail(mail))
                throw new DuplicateName("E-mail already exist");

            jdbcTemplate.update("UPDATE Users SET (login,email,password) = (?,?,?)" +
                    "WHERE lower(login) = lower(?)",
                    login == null ? user.getLogin() : login,
                    mail == null ? user.getMail() : mail,
                    password == null ? user.getPassword() : password,
                    oldLogin);
            return "Ok";
            /*return SelectQuery.execute("UPDATE User SET (login,email,password) = (?,?,?)" +
                    "WHERE lower(login) = lower(?)",
                    preparedStatement -> {
                if(login != null ){
                    final UserProfile userByLogin = getUserByLogin(login);
                    if(userByLogin == null)
                        preparedStatement.setString(1, login);
                    else throw new SQLException("Login already exist");
                }
                else preparedStatement.setString(1, user.getLogin());
                if(mail != null){
                    if(!verifyMail(mail))
                        preparedStatement.setString(2, login);
                    else throw new SQLException("Mail already exist");
                }
                else preparedStatement.setString(2, user.getMail());
                if(password != null)preparedStatement.setString(3, password);
                else preparedStatement.setString(3, user.getPassword());
                return "Ok";
                    });*/
        }
        catch (DuplicateName e){
            e.printStackTrace();
            return (e.getMessage());
        }
    }

    @Override
    public void changeScore(Integer score, String login) {
       // try{
            jdbcTemplate.update("UPDATE Users SET hightScore = ? WHERE lower(login) = lower(?)",
                    score,
                    login);
            /*return SelectQuery.execute("UPDATE User SET hightScore = ? WHERE lower(login) = lower(?)",
                    preparedStatement -> {
                preparedStatement.setInt(1, score);
                preparedStatement.setString(2, login);
                preparedStatement.executeUpdate();
                return true;
                    });*/
       // }
        /*catch (SQLException e){
            e.printStackTrace();
            return false;
        }*/
    }

    @Nullable
    @Override
    public Integer getScore(String login){
       // try {
           return jdbcTemplate.queryForObject("SELECT hightScore FROM Users WHERE lower(login) = lower(?)",
                    Integer.class,
                    login);
         /*   return SelectQuery.execute("SELECT score FROM gemr.User WHERE lower(login) = lower(?)",
                    preparedStatement->{
                preparedStatement.setString(1, login);
                final ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                return resultSet.getInt(1);
                    });
        }
        catch (SQLException e){
            e.printStackTrace();
            return null;
        }*/
    }

    @Override
    public LinkedList<UserToInfo> getBestUsers() {
        final LinkedList<UserToInfo> userList = new LinkedList<>();
        jdbcTemplate.query("SELECT login, hightScore FROM Users ORDER BY hightScore DESC LIMIT 100",
                (result, num) ->
                    userList.add(new UserToInfo(result.getString(1), result.getInt(2))));
        return userList;
        /*try{
            SelectQuery.execute("SELECT login, hightScore FROM User ORDER BY hightScore DESC LIMIT 100",
                    preparedStatement -> {
                final ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    userList.add(new UserToInfo(resultSet.getString(1), resultSet.getInt(2)));
                }
                return null;
                    });
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return userList;*/
    }
}
