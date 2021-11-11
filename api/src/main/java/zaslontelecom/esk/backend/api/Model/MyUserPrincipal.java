package zaslontelecom.esk.backend.api.Model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MyUserPrincipal implements UserDetails {
    private UserWithPermissions user;

    public MyUserPrincipal(UserWithPermissions user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<EskPermission> res = new ArrayList<>();
        if (this.user.getPravice() == null){
            return res;
        }

        List<String> permissionList = new ArrayList<String>(Arrays.asList(this.user.getPravice().split(",")));
        for (String permission: permissionList ) {
            res.add(new EskPermission(permission.trim()));
        }
        return res;
    }

    @Override
    public String getPassword() {
        return "****************";
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public UserWithPermissions getEskUser(){
        return user;
    }
}