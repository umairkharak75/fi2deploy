package zaslontelecom.esk.backend.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zaslontelecom.esk.backend.api.Controller.Requests.PagedQuery;
import zaslontelecom.esk.backend.api.Controller.Response.PagedQueryResult;
import zaslontelecom.esk.backend.api.Model.Priloga;
import zaslontelecom.esk.backend.api.Service.CertifikatPrilogaProizvodService;
import zaslontelecom.esk.backend.api.Service.PrilogaService;

import java.util.Optional;

@RestController
// @CrossOrigin(origins = "*")
@RequestMapping(path = "/priloga")

public class PrilogaController extends BaseController {
    @Autowired
    PrilogaService service;
    @RequestMapping(method = RequestMethod.GET)
    public Optional<Priloga> get(long id) {
        Optional<Priloga> priloga = service.get(id);
        return priloga;
    }
}