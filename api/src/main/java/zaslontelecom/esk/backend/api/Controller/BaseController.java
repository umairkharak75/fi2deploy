package zaslontelecom.esk.backend.api.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import zaslontelecom.esk.backend.api.Model.MyUserPrincipal;
import zaslontelecom.esk.backend.api.Model.UserWithPermissions;
import zaslontelecom.esk.backend.api.Utils.HandledException;
import zaslontelecom.esk.backend.api.Utils.Utils;

public class BaseController {

    public UserWithPermissions getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((MyUserPrincipal)authentication.getPrincipal()).getEskUser();
    }

    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication != null;
    }

    public void checkPermission(String permission) throws HandledException {
        if (!hasPermission(permission)){
            throw new HandledException("Nimate pravic za izvajanje! (" + permission + ")");
        }
    }

    public boolean hasPermission(String permission) {
        String permissions = getUser().getPravice();

        if (Utils.isNullOrEmpty(permissions)){
            return false;
        }

        for (String element: permissions.split(",")) {
            if (element.equals(permission)){
                return true;
            }
        }

        return false;
    }
}
