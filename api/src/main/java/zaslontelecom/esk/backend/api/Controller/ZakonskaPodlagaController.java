package zaslontelecom.esk.backend.api.Controller;

import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import zaslontelecom.esk.backend.api.Controller.Requests.PagedQuery;
import zaslontelecom.esk.backend.api.Controller.Response.PagedQueryResult;
import zaslontelecom.esk.backend.api.Model.ZakonskaPodlaga;
import zaslontelecom.esk.backend.api.Service.ZakonskaPodlagaService;
import zaslontelecom.esk.backend.api.Utils.HandledException;

import java.util.Optional;

@RestController
// @CrossOrigin(origins = "*")
@RequestMapping(path = "/ZakonskaPodlaga")
public class ZakonskaPodlagaController extends BaseController {
    @Autowired
    ZakonskaPodlagaService service;

    @RequestMapping(value = "/list", method = RequestMethod.GET, headers = "Accept=application/json")
    public PagedQueryResult list(PagedQuery request) throws HandledException {
        this.checkPermission("ZAK_PODL_PREGLED");
        PagedQueryResult<ZakonskaPodlaga> response = new PagedQueryResult<>();
        Page<ZakonskaPodlaga> resultPage = this.service.findByQuery(request.getPageNumber(), request.getResultPerPage(), request.getQuery());
        response.setResult(resultPage.getContent());
        response.setResultLength(resultPage.getTotalElements());

        return response;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Optional<ZakonskaPodlaga> get(long id) throws HandledException {
        this.checkPermission("ZAK_PODL_PREGLED");
        return service.get(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ZakonskaPodlaga post(@RequestBody ZakonskaPodlaga item) throws HandledException, PolicyException, ScanException {
        this.checkPermission("ZAK_PODL_AZURIRAJ");
        return service.insert(item);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ZakonskaPodlaga put(@RequestBody ZakonskaPodlaga item) throws HandledException, PolicyException, ScanException {
        this.checkPermission("ZAK_PODL_AZURIRAJ");
        return service.update(item);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public boolean delete(long id) throws HandledException, ScanException, PolicyException {
        this.checkPermission("ZAK_PODL_AZURIRAJ");
        service.delete(id);
        return true;
    }
}