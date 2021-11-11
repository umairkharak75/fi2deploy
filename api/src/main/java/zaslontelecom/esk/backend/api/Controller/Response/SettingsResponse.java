package zaslontelecom.esk.backend.api.Controller.Response;

import zaslontelecom.esk.backend.api.Model.Nastavitve;
import zaslontelecom.esk.backend.api.Utils.Settings;

public class SettingsResponse extends Nastavitve {
    private String appPurpose;

    public String getAppPurpose() {
        return appPurpose;
    }

    public void setAppPurpose(String appPurpose) {
        this.appPurpose = appPurpose;
    }

    public SettingsResponse(Nastavitve nast, Settings appSettings){
        this.setAppName(nast.getAppName());
        this.setVerzija(nast.getVerzija());
        this.setId(nast.getId());
    }
}
