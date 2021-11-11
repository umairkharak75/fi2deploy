package zaslontelecom.esk.backend.api.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.Model.UserWithPermissions;
import zaslontelecom.esk.backend.api.Utils.Settings;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserActivityList {
    List<UserWithPermissions> users = new ArrayList<>();

    @Autowired
    Settings settings;

    public UserWithPermissions getUserActivity(String username){
        for (UserWithPermissions user: users) {
            if (user.getUserName().equals(username))
                return user;
        }

        return null;
    }

    public void removeUserActivity(String username){
        UserWithPermissions find = getUserActivity(username);

        if (find != null){
            this.users.remove(find);
        }
    }

    public void setUserActivity(UserWithPermissions user){
        UserWithPermissions find = getUserActivity(user.getUserName());

        if (find == null){
            user.setActivity(System.currentTimeMillis());
            this.users.add(user);
        } else {
            find.setActivity(System.currentTimeMillis());
        }
    }

    public boolean isActive(String username){
        UserWithPermissions find = getUserActivity(username);

        if (find == null){
            return false;
        }
        long activityPeriod = System.currentTimeMillis() - find.getActivity();
        if (activityPeriod > settings.getMaxInactivityTimeInMs()){
            return false;
        }

        return true;
    }
}
