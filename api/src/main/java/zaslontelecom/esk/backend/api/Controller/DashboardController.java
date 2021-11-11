package zaslontelecom.esk.backend.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zaslontelecom.esk.backend.api.Controller.Response.DashboardResponse;
import zaslontelecom.esk.backend.api.Service.CertifikatService;
import zaslontelecom.esk.backend.api.Utils.Utils;

import java.util.Optional;

@RestController
// @CrossOrigin(origins = "*")
@RequestMapping(path = "/dashboard")
public class DashboardController extends BaseController{
    @Autowired
    CertifikatService service;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public Optional<DashboardResponse> getByUser(long id) {
        DashboardResponse response = new DashboardResponse();
        String org = this.hasPermission("CERTIFIKAT_PREG_VSE") ? null : Utils.getValueOrNull(this.getUser().getOrgSif());
        response.setVeljavniTotal(service.countVeljavni(org));
        response.setvIzteku(service.countVIzteku(org));
        response.setVnosTotal(service.countVnosTotal(org));
        return Optional.ofNullable(response);
    }
}