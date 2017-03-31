package techpark.DAO;

import org.jetbrains.annotations.Nullable;
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

    @Override
    public void addUser(String login, String mail, String password) {
        try{
            SelectQuery.execute("INSERT INTO game.\"User\" VALUES (?,?,?)",
                    preparedStatement -> {
                preparedStatement.setString(1, login);
                preparedStatement.setString(2, mail);
                preparedStatement.setString(3, password);
                preparedStatement.executeUpdate();
                return null;
                    });
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean verifyMail(String mail) {
        try {
            return SelectQuery.execute("SELECT email FROM game.\"User\" WHERE lower(email) = lower(?)",
                    preparedStatement -> {
                preparedStatement.setString(1, mail);
                final ResultSet resultSet = preparedStatement.executeQuery();
                if(!resultSet.next())
                    throw new SQLException("Not found");
                return true;
                    });
        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
        }

    }

    @Nullable
    @Override
    public UserProfile getUserByLogin(String login) {
        try {
            return SelectQuery.execute("SELECT * FROM game.\"User\" WHERE lower(login) = lower(login)",
                    preparedStatement -> {
                preparedStatement.setString(1, login);
                final ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                return new UserProfile(resultSet.getString(2),
                                resultSet.getString(1),
                                resultSet.getString(3));
                    });
        }
        catch (SQLException e){
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
            return SelectQuery.execute("UPDATE game.\"User\" SET (login,email,password) = (?,?,?)" +
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
                    });
        }
        catch (SQLException e){
            e.printStackTrace();
            return (e.getMessage());
        }
    }

    @Override
    public boolean changeScore(Integer score, String login) {
        try{
            return SelectQuery.execute("UPDATE game.\"User\" SET hightScore = ? WHERE lower(login) = lower(?)",
                    preparedStatement -> {
                preparedStatement.setInt(1, score);
                preparedStatement.setString(2, login);
                preparedStatement.executeUpdate();
                return true;
                    });
        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    @Nullable
    @Override
    public Integer getScore(String login){
        try {
            return SelectQuery.execute("SELECT score FROM gemr.\"User\" WHERE lower(login) = lower(?)",
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
        }
    }

    @Override
    public LinkedList<UserToInfo> getBestUsers() {
        final LinkedList<UserToInfo> userList = new LinkedList<>();
        try{
            SelectQuery.execute("SELECT login, hightScore FROM game.\"User\" ORDER BY hightScore DESC LIMIT 100",
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
        return userList;
    }
}
