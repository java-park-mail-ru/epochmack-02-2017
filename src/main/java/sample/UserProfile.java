package sample;

import javax.validation.constraints.NotNull;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Fedorova on 20/02/2017.
 */
public class UserProfile {

    private String mail, login, password;
    private Integer hightScore;
    private long id;

    private static final AtomicLong ID_GENETATOR = new AtomicLong(0);

    public UserProfile(@NotNull String mail, @NotNull String login, @NotNull String password){
        this.mail = mail;
        this.login = login;
        this.password = password;
        this.id = ID_GENETATOR.getAndIncrement();
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

    public long getId(){
        return this.id;
    }
    //some missing fields here
}
