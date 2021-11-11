package zaslontelecom.esk.backend.api.Security;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFailureListener
        implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent ev) {
        String username = ev.getAuthentication().getName();
    }
}