package techpark.user;

import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;

/**
 * Created by Fedorova on 20/02/2017.
 */
@SuppressWarnings("unnused")
public class UserProfile {

    private String mail;
    private String login;
    private String password;
    private Integer hightScore;

    public UserProfile(@NotNull String mail, @NotNull String login, @NotNull String password){
        this.mail = mail;
        this.login = login;
        this.password = password;
        this.hightScore = 0;
    }

    public void setScore(Integer score){
        this.hightScore = score;
    }

    public String getMail(){
        return mail;
    }

    public void setMail(String mail){
        this.mail = mail;
    }

    public String getLogin(){
        return login;
    }

    public void setLogin(String login){
        this.login = login;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public Integer getScore(){
        return hightScore;
    }

}
