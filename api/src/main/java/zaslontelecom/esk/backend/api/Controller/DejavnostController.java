package zaslontelecom.esk.backend.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import zaslontelecom.esk.backend.api.Controller.Requests.PagedQuery;
import zaslontelecom.esk.backend.api.Controller.Response.PagedQueryResult;
import zaslontelecom.esk.backend.api.Model.Dejavnost;
import zaslontelecom.esk.backend.api.Service.DejavnostService;
import zaslontelecom.esk.backend.api.Utils.HandledException;

import java.util.Optional;

@RestController
// @CrossOrigin(origins = "*")
@RequestMapping(path = "/Dejavnost")
public class DejavnostController extends BaseController  {
    @Autowired
    DejavnostService service;

    @RequestMapping(value = "/list", method = RequestMethod.GET, headers = "Accept=application/json")
    public PagedQueryResult list(PagedQuery request) throws HandledException {
        this.checkPermission("DEJAVNOST_PREGLED");
        PagedQueryResult<Dejavnost> response = new PagedQueryResult<>();
        Page<Dejavnost> resultPage = this.service.findByQuery(request.getPageNumber(), request.getResultPerPage(), request.getQuery());
        response.setResult(resultPage.getContent());
        response.setResultLength(resultPage.getTotalElements());

        return response;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Optional<Dejavnost> get(long id) throws HandledException {
        this.checkPermission("DEJAVNOST_PREGLED");
        return service.get(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Dejavnost post(@RequestBody Dejavnost item) throws HandledException {
        this.checkPermission("DEJAVNOST_AZURIRAJ");
        return service.insert(item);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Dejavnost put(@RequestBody Dejavnost item) throws HandledException {
        this.checkPermission("DEJAVNOST_AZURIRAJ");
        return service.update(item);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public boolean delete(long id) throws HandledException {
        this.checkPermission("DEJAVNOST_AZURIRAJ");
        service.delete(id);
        return true;
    }
}