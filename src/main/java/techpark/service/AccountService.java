package techpark.service;


import jdk.nashorn.internal.scripts.JD;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
import techpark.DAO.RequestUsersDAO;
import techpark.DAO.RequestUsersDAOImpl;
import techpark.DBconnect.DBConnect;
import techpark.user.UserProfile;
import techpark.user.UserToInfo;

import javax.sql.DataSource;

/**
 * Created by Fedorova on 20/02/2017.
 */

@Transactional
@Service
public class AccountService {

    private final RequestUsersDAOImpl usersDAO;

    //@Autowired
    public AccountService(JdbcTemplate jdbcTemplate) {
        this.usersDAO = new RequestUsersDAOImpl(jdbcTemplate);
    }


    public int register(@NotNull  String mail, @NotNull  String login, @NotNull  String password) {
        return usersDAO.addUser(mail, login, password);
    }

    public boolean verifyMail(@NotNull String mail) {
        return ( usersDAO.verifyMail(mail));
    }

    public UserProfile getUserByLogin(@NotNull String login){
       // return userNameToUserProfile.get(login);
        return usersDAO.getUserByLogin(login);
    }

    public String changeUser (@NotNull String oldLogin, @Nullable String login, @Nullable String  mail, @Nullable String password){
        return usersDAO.changeUser(oldLogin, login, mail, password);
    }

    public void changeScore (@NotNull UserProfile currentUser, @NotNull Integer score){
        if(currentUser.getScore() < score)
            usersDAO.changeScore(score, currentUser.getLogin());
    }

    public Integer getScore (@NotNull String login){return usersDAO.getScore(login);}

    public LinkedList<UserToInfo> getAllUsers() {
        return usersDAO.getBestUsers();
    }

}
