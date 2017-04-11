package techpark.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import techpark.exceptions.AccountServiceDBException;
import techpark.exceptions.AccountServiceDDException;
import techpark.user.UserProfile;
import techpark.user.UserToInfo;


/**
 * Created by Варя on 10.04.2017.
 */
public interface AccountService {


    void register(@NotNull String mail, @NotNull String login, @NotNull String password)
            throws AccountServiceDBException, AccountServiceDDException;

    boolean verifyMail(@NotNull String mail)
            throws AccountServiceDBException;

    @Nullable
    UserProfile getUserByLogin(@NotNull String login)
            throws AccountServiceDBException;

    void changeUser(@NotNull UserProfile user, @Nullable String login, @Nullable String mail,
                           @Nullable String password) throws AccountServiceDBException, AccountServiceDDException;

    void changeScore(@NotNull UserProfile currentUser, @NotNull Integer score)
            throws AccountServiceDBException;

    int getScore(@NotNull String login) throws AccountServiceDBException;

    @NotNull
    ArrayList<UserToInfo> getAllUsers() throws AccountServiceDBException;

}
