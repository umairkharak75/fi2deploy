package zaslontelecom.esk.backend.api.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import zaslontelecom.esk.backend.api.Model.UserWithPermissions;
import zaslontelecom.esk.backend.api.Service.AuthenticationService;


@Component
// @CrossOrigin(origins = "*")
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    AuthenticationService auth;

    @Autowired
    UserActivityList activityList;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        String username = authentication.getName();
        char[] password = authentication.getCredentials() == null ? new char[]{} : (char[])authentication.getCredentials();
        UserWithPermissions user = auth.login(username, password);

        if (user != null) {
            this.activityList.setUserActivity(user);
            return new UsernamePasswordAuthenticationToken(username, password);
        } else {
            throw new BadCredentialsException("INVALID");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}