package techpark.jsonresponse;

import techpark.user.UserToInfo;

import java.util.ArrayList;

/**
 * Created by yaches on 09.03.17.
 */
public class UsersListResponse extends Response {

    public final ArrayList<UserToInfo> users;

    public UsersListResponse(ArrayList<UserToInfo> users) {
        super("ok");
        this.users = users;
    }

}
