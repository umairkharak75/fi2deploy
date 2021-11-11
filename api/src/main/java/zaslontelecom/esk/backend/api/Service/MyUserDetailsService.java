package zaslontelecom.esk.backend.api.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.Model.MyUserPrincipal;
import zaslontelecom.esk.backend.api.Model.UserWithPermissions;
import zaslontelecom.esk.backend.api.Security.UserActivityList;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserActivityList activityList;

    @Override
    public MyUserPrincipal loadUserByUsername(String username) {
        UserWithPermissions user = activityList.getUserActivity(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return new MyUserPrincipal(user);
    }
}