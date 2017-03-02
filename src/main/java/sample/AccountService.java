package sample;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Fedorova on 20/02/2017.
 */
@Service
public class AccountService {

    private Map<String, UserProfile> userNameToUserProfile = new HashMap<>();

    @NotNull
    public UserProfile register(@NotNull  String mail, @NotNull  String login, @NotNull  String password) {

        final UserProfile currentUser = new UserProfile(mail, login, password);
        userNameToUserProfile.put(login, currentUser);

        return currentUser;
    }

    public boolean verifylogin(@NotNull String login) {
        return ( userNameToUserProfile.containsKey(login));
    }

    public UserProfile getUserByLogin(@NotNull String login){
        return userNameToUserProfile.get(login);
    }

    @Nullable
    public UserProfile getUserByMail(@NotNull String mail){
        for(UserProfile user: userNameToUserProfile.values()){
           if (mail.equals(user.getMail()))
               return user;
        }
        return null;
    }

    public void changeUser (@NotNull UserProfile currentUser, @NotNull String type, @NotNull String value){
        switch (type){
            case "login":
                userNameToUserProfile.remove(currentUser.getLogin(),currentUser);
                currentUser.setLogin(value);
                userNameToUserProfile.put(currentUser.getLogin(), currentUser);
                break;
            case"mail":
                currentUser.setMail(value);
                break;
            case "password":
                currentUser.setPassword(value);
                break;
            default: break;
        }
    }

    public void changeScore (@NotNull UserProfile currentUser, @NotNull Integer score){
        if(currentUser.getScore() < score)
            currentUser.setScore(score);
    }

}
