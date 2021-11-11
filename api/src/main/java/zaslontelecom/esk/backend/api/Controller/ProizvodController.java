package zaslontelecom.esk.backend.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import zaslontelecom.esk.backend.api.Controller.Requests.PagedQuery;
import zaslontelecom.esk.backend.api.Controller.Requests.ProizvodByZascitenProizvodQuery;
import zaslontelecom.esk.backend.api.Controller.Response.PagedQueryResult;
import zaslontelecom.esk.backend.api.Model.Proizvod;
import zaslontelecom.esk.backend.api.Service.ProizvodService;
import zaslontelecom.esk.backend.api.Utils.HandledException;

import java.util.Optional;

@RestController
@RequestMapping(path = "/Proizvod")
public class ProizvodController extends BaseController{
    @Autowired
    ProizvodService service;

    @RequestMapping(value = "/list", method = RequestMethod.GET, headers = "Accept=application/json")
    @CacheEvict(value = "first", allEntries = true)
    public PagedQueryResult list(PagedQuery request) throws HandledException {
        this.checkPermission("PROIZVOD_PREGLED");
        PagedQueryResult<Proizvod> response = new PagedQueryResult<>();
        Page<Proizvod> resultPage = this.service.findByQuery(request.getPageNumber(), request.getResultPerPage(), request.getQuery());
        response.setResult(resultPage.getContent());
        response.setResultLength(resultPage.getTotalElements());
        // delete unbinded shema and zascitenProizvod
        service.clearUnusedData();
        return response;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Optional<Proizvod> get(long id) throws HandledException {
        this.checkPermission("PROIZVOD_PREGLED");
        return service.get(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public Proizvod post(@RequestBody Proizvod item) throws HandledException {
        this.checkPermission("PROIZVOD_AZURIRAJ");

        service.save(item);
        return item;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Proizvod put(@RequestBody Proizvod item) throws HandledException {
        this.checkPermission("PROIZVOD_AZURIRAJ");

        service.save(item);
        service.clearUnusedData();
        return item;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public boolean delete(long id) throws HandledException {
        this.checkPermission("PROIZVOD_AZURIRAJ");

        service.delete(id);
        service.clearUnusedData();
        return true;
    }

    @RequestMapping(value = "/ByZascitenProizvod", method = RequestMethod.GET, headers = "Accept=application/json")
    public PagedQueryResult byZascitenProizvod(long id) throws HandledException {
        this.checkPermission("PROIZVOD_PREGLED");
        PagedQueryResult<Proizvod> response = new PagedQueryResult<>();
        Page<Proizvod> resultPage = this.service.findByZascitenProizvod(0, 1000, id);
        response.setResult(resultPage.getContent());
        response.setResultLength(resultPage.getTotalElements());

        return response;
    }
}