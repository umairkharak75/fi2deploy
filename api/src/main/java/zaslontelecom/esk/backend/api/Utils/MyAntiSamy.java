package zaslontelecom.esk.backend.api.Utils;

import org.owasp.validator.html.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyAntiSamy {
    Policy policy;
    AntiSamy as;

    @Autowired
    Settings settings;

    public String getCleanHTML(String dirtyInput) throws PolicyException, ScanException {
        if (this.policy == null) {
            this.policy = Policy.getInstance(settings.getAntiSamyPath());
        }
        if (this.as == null) {
            this.as = new AntiSamy();
        }

        CleanResults cr = this.as.scan(dirtyInput, policy);
        return cr.getCleanHTML(); // some custom function
    }
}
