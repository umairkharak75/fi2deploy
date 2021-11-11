package zaslontelecom.esk.backend.api.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import zaslontelecom.esk.backend.api.Model.MyUserPrincipal;

public class BaseService {

    public MyUserPrincipal GetCurrentUser(){
        return (MyUserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public long GetCurrentUserId(){
        return  GetCurrentUser().getEskUser().getId();
    }

    Logger logger = LoggerFactory.getLogger(BaseService.class);
}
