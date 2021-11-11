package zaslontelecom.esk.backend.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import zaslontelecom.esk.backend.api.Controller.Requests.CertifikatSearchCriteriaRequest;
import zaslontelecom.esk.backend.api.Controller.Requests.PagedQuery;
import zaslontelecom.esk.backend.api.Controller.Response.PagedQueryResult;
import zaslontelecom.esk.backend.api.Controller.Response.RefreshResponse;
import zaslontelecom.esk.backend.api.Model.*;
import zaslontelecom.esk.backend.api.Service.CertifikatPrilogaClanService;
import zaslontelecom.esk.backend.api.Service.CertifikatPrilogaProizvodService;
import zaslontelecom.esk.backend.api.Service.CertifikatService;
import zaslontelecom.esk.backend.api.Service.PrilogaClanService;
import zaslontelecom.esk.backend.api.Utils.HandledException;
import zaslontelecom.esk.backend.api.Utils.Utils;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class CertifikatBaseController extends BaseController {
    @Autowired
    CertifikatService service;

    @RequestMapping(value = "/list", method = RequestMethod.GET, headers = "Accept=application/json")
    public PagedQueryResult list(PagedQuery request) throws HandledException {
        PagedQueryResult<Certifikat> response = new PagedQueryResult<>();
        if (this.isAuthenticated()){
            boolean hasPermissions = this.hasPermission("CERTIFIKAT_PREG_VSE") || this.hasPermission("CERTIFIKAT_PREG_MOJA_ORG");
            if (!hasPermissions){
                this.checkPermission("CERTIFIKAT_PREG_MOJA_ORG");
            }
        }

        String org = !this.isAuthenticated() || this.hasPermission("CERTIFIKAT_PREG_VSE") ? null : Utils.getValueOrNull(this.getUser().getOrgSif());
        Page<Certifikat> resultPage = this.service.findByQuery(request.getPageNumber(), request.getResultPerPage(), request.getQuery(), this.isAuthenticated(), org);
        response.setResult(resultPage.getContent());
        response.setResultLength(resultPage.getTotalElements());

        return response;
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public PagedQueryResult search(@RequestBody CertifikatSearchCriteriaRequest request) {
        PagedQueryResult<Object> response = new PagedQueryResult<>();
        String org = this.hasPermission("CERTIFIKAT_PREG_VSE") ? Utils.getValueOrNull(request.getOrganizacija()) : Utils.getValueOrNull(this.getUser().getOrgSif());
        Iterable<Object> resultPage = this.service.findByParams(request, org);
        response.setResult(resultPage);
        response.setResultLength(resultPage.spliterator().getExactSizeIfKnown());

        return response;
    }
}