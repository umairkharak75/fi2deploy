package zaslontelecom.esk.backend.api.Model;

import org.springframework.security.core.GrantedAuthority;

public class EskPermission implements GrantedAuthority {

    String perm = null;

    public EskPermission(String permisson){
        this.perm = permisson;
    }

    @Override
    public String getAuthority() {
        return perm;
    }
}
