package zaslontelecom.esk.backend.api.Model;


import java.util.Date;

public class UserWithPermissions extends EskUsers {
    public String getPravice() {
        return pravice;
    }

    public void setPravice(String pravice) {
        this.pravice = pravice;
    }

    String pravice;

    public Long getActivity() {
        return activity;
    }

    public void setActivity(Long activity) {
        this.activity = activity;
    }

    Long activity;
}
