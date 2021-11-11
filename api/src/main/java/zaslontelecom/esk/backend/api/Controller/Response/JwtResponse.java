package zaslontelecom.esk.backend.api.Controller.Response;

import org.springframework.security.core.userdetails.UserDetails;
import zaslontelecom.esk.backend.api.Model.UserWithPermissions;

public class JwtResponse  {
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDetails getUser() {
        return user;
    }

    public void setUser(UserDetails user) {
        this.user = user;
    }

    String token;
    UserDetails user;
}
