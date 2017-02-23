package sample;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.jetty.server.Authentication;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpSession;
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

        UserProfile currentUser = new UserProfile(mail, login, password);
        userNameToUserProfile.put(login, currentUser);

        return currentUser;
    }

    public boolean login(@NotNull String login, @NotNull  String password) {
        if( userNameToUserProfile.containsKey(login) & userNameToUserProfile.get(login).getPassword() == password) {
            return true;
        }
        else return false;
    }

    public UserProfile getUserByLogin(@NotNull String login){
        return userNameToUserProfile.get(login);
    }

    public UserProfile getUserByMail(@NotNull String mail){
        for(UserProfile user: userNameToUserProfile.values()){
           if (user.getMail() == mail)
               return user;
        }
        return null;
    }

    public void changeUser (@NotNull UserProfile currentUser, @NotNull String type, @NotNull String value){
        if (type == "Login"){
            userNameToUserProfile.remove(currentUser.getLogin(),currentUser);
            currentUser.setLogin(value);
            userNameToUserProfile.put(value, currentUser);
        }
        else if (type == "Mail"){
            currentUser.setMail(value);
        }
        else if (type == "Password")
            currentUser.setPassword(value);
    }

    public void changeScore (@NotNull UserProfile currentUser, @NotNull Integer score){
        if(currentUser.getScore() < score)
            currentUser.setScore(score);
    }

   /* public Map<String, Integer> bestPlayers(){
        Map<String, Integer> userLoginToUserScore = new HashMap<>();
        for(UserProfile user: userNameToUserProfile.values()){
            userLoginToUserScore.put(user.getLogin(), user.getScore());
        }
        if(!userLoginToUserScore.isEmpty())
            userLoginToUserScore.
    }*/
}
