package techpark.service;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import org.springframework.transaction.annotation.Transactional;
import techpark.DAO.RequestUsersDAOImpl;
import techpark.user.UserProfile;
import techpark.user.UserToInfo;


/**
 * Created by Fedorova on 20/02/2017.
 */

@Transactional
@Service
public class AccountService {

    private final RequestUsersDAOImpl usersDAO;

    public AccountService(JdbcTemplate jdbcTemplate) {
        this.usersDAO = new RequestUsersDAOImpl(jdbcTemplate);
    }


    public boolean register(@NotNull String mail, @NotNull String login, @NotNull String password)
            throws DataAccessException {
        return usersDAO.addUser(mail, login, password);
    }

    public boolean verifyMail(@NotNull String mail)
            throws DataAccessException {
        return (usersDAO.verifyMail(mail));
    }

    @Nullable
    public UserProfile getUserByLogin(@NotNull String login)
            throws DataAccessException {
        return usersDAO.getUserByLogin(login);
    }

    public boolean changeUser(@NotNull UserProfile user, @Nullable String login, @Nullable String mail,
                             @Nullable String password) throws DataAccessException  {
        return usersDAO.changeUser(user, login, mail, password);
    }

    public void changeScore(@NotNull UserProfile currentUser, @NotNull Integer score)
            throws DataAccessException  {
        if (currentUser.getScore() < score)
            usersDAO.changeScore(score, currentUser.getLogin());
    }

    public int getScore(@NotNull String login) {
        return usersDAO.getScore(login);
    }

    public ArrayList<UserToInfo> getAllUsers() throws DataAccessException   {
        return usersDAO.getBestUsers();
    }

}
