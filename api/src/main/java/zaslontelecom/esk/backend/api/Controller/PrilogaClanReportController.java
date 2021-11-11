package zaslontelecom.esk.backend.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zaslontelecom.esk.backend.api.Controller.Requests.PagedQuery;
import zaslontelecom.esk.backend.api.Controller.Response.PagedQueryResult;
import zaslontelecom.esk.backend.api.Service.CertifikatPrilogaClanService;
import zaslontelecom.esk.backend.api.Utils.HandledException;

@RestController
// @CrossOrigin(origins = "*")
@RequestMapping(path = "/PrilogeClanovReport")
public class PrilogaClanReportController extends BaseController {
    @Autowired
    CertifikatPrilogaClanService service;

    @RequestMapping(value = "/search", method = RequestMethod.GET, headers = "Accept=application/json")
    public PagedQueryResult search(PagedQuery request) throws HandledException {
        this.checkPermission("PRILOGE_CLAN_PREGLED");
        PagedQueryResult<Object> response = new PagedQueryResult<>();
        String org = this.hasPermission("CERTIFIKAT_PREG_VSE") ? null : this.getUser().getOrgSif();
        Iterable<Object> resultPage = this.service.findByParams(request, org);
        response.setResult(resultPage);
        response.setResultLength(resultPage.spliterator().getExactSizeIfKnown());

        return response;
    }
}