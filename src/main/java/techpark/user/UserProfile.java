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

    public UserProfile(@NotNull String mail, @NotNull String login, @NotNull String password,
                       @Nullable Integer score){
        this.mail = mail;
        this.login = login;
        this.password = password;
        this.hightScore = score == null ? 0 :  score;
    }

    public String getMail(){
        return mail;
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

    public Integer getScore(){
        return hightScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final UserProfile userProfile = (UserProfile) o;
        if (!login.equals(userProfile.login)) return false;
        if (!mail.equals(userProfile.mail)) return false;
        return password.equals(userProfile.password);
    }

    @Override
    public int hashCode() {
        final int result = login.hashCode() + mail.hashCode() + password.hashCode();
        return result ^ (result >>> 31);
    }

}
