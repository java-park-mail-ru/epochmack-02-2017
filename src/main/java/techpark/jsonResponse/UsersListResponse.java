package techpark.jsonResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import techpark.user.UserProfile;
import techpark.user.UserToInfo;

import java.util.LinkedList;

/**
 * Created by yaches on 09.03.17.
 */
public class UsersListResponse extends Response {

    public final LinkedList<UserToInfo> users;

    public UsersListResponse(LinkedList<UserToInfo> users) {
        super("ok");
        this.users = users;
    }
}
