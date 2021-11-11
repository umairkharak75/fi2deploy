package zaslontelecom.esk.backend.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import zaslontelecom.esk.backend.api.Controller.Requests.PagedQuery;
import zaslontelecom.esk.backend.api.Controller.Response.PagedQueryResult;
import zaslontelecom.esk.backend.api.Model.ProizvodKolicine;
import zaslontelecom.esk.backend.api.Model.Report;
import zaslontelecom.esk.backend.api.Service.ProizvodKolicineService;
import zaslontelecom.esk.backend.api.Utils.HandledException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/ProizvodKolicine")
public class ProizvodKolicineController extends BaseController {
    @Autowired
    ProizvodKolicineService service;

    @RequestMapping(value = "/search", method = RequestMethod.GET, headers = "Accept=application/json")
    public List<ProizvodKolicine> search(PagedQuery request) throws HandledException {
        this.checkPermission("KOLICINE_PREGLED");
        return this.service.findByParams(request);
    }
}