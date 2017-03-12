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

//    @Override
//    public ResponseEntity<?> getMessage() {
//        String body = "{\"users\": [";
//
//        LinkedList<UserProfile> users = this.users;
////        for (UserProfile user : users) {
//        for (int i = 0; i < users.size(); ++i) {
//            UserProfile user = users.get(i);
//            body += "{";
//            body += "\"login\": \"" + user.getLogin() + "\",";
//            body += "\"score\": \"" + user.getScore() + "\"";
//            body += "}";
//            if (i != users.size() - 1) {
//                body += ",";
//            }
//        }
//
//        body += "] }";
//        return new ResponseEntity<>(body, HttpStatus.OK);
//    }
}
