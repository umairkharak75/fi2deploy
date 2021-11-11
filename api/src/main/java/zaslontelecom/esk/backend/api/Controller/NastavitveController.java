package zaslontelecom.esk.backend.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import zaslontelecom.esk.backend.api.Controller.Requests.PagedQuery;
import zaslontelecom.esk.backend.api.Controller.Response.PagedQueryResult;
import zaslontelecom.esk.backend.api.Controller.Response.SettingsResponse;
import zaslontelecom.esk.backend.api.Model.Nastavitve;
import zaslontelecom.esk.backend.api.Service.NastavitveService;
import zaslontelecom.esk.backend.api.Utils.HandledException;
import zaslontelecom.esk.backend.api.Utils.Settings;

import java.util.Optional;

@RestController
// @CrossOrigin(origins = "*")
@RequestMapping(path = "/settings")
public class NastavitveController {
    @Autowired
    NastavitveService service;

    @Autowired
    Settings appSettings;

    @RequestMapping(method = RequestMethod.GET)
    public SettingsResponse get(long id) throws HandledException {
        // ID is dummy in this case, because of frontend generic implementation.
        SettingsResponse mySettings = new SettingsResponse(service.get(), this.appSettings);

        return mySettings;
    }
}