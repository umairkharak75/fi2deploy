package zaslontelecom.esk.backend.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import zaslontelecom.esk.backend.api.Controller.Requests.PagedQuery;
import zaslontelecom.esk.backend.api.Controller.Response.PagedQueryResult;
import zaslontelecom.esk.backend.api.Model.Zascitniznak;
import zaslontelecom.esk.backend.api.Service.SubjektService;
import zaslontelecom.esk.backend.api.Service.ZascitniznakService;
import zaslontelecom.esk.backend.api.Utils.HandledException;

import java.util.Optional;

@RestController
@RequestMapping(path = "/ZascitniZnak")
public class ZascitniznakController extends BaseController {
    @Autowired
    ZascitniznakService service;

    @Autowired
    SubjektService subjektService;

    @RequestMapping(value = "/list", method = RequestMethod.GET, headers = "Accept=application/json")
    public PagedQueryResult list(PagedQuery request) throws HandledException {
        this.checkPermission("ZZNAK_PREGLED");
        PagedQueryResult<Zascitniznak> response = new PagedQueryResult<>();
        Page<Zascitniznak> resultPage = this.service.findByQuery(request.getPageNumber(), request.getResultPerPage(), request.getQuery());
        response.setResult(resultPage.getContent());
        response.setResultLength(resultPage.getTotalElements());

        return response;
    }


    @RequestMapping(method = RequestMethod.GET)
    public Optional<Zascitniznak> get(long id) throws HandledException {
        this.checkPermission("ZZNAK_PREGLED");
        return service.get(id);
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST)
    public Zascitniznak post(@RequestBody Zascitniznak zznak) throws HandledException {
        this.checkPermission("ZZNAK_AZURIRAJ");
        service.validateObject(zznak);
        zznak.setImetnik(subjektService.getAndSaveBeforeIfNecessary(zznak.getImetnik()));
        Zascitniznak result = service.insert(zznak);
        return result;
    }
    @RequestMapping(method = RequestMethod.PUT)
    public Zascitniznak put(@RequestBody Zascitniznak zznak) throws HandledException {
        this.checkPermission("ZZNAK_AZURIRAJ");
        service.validateObject(zznak);
        zznak.setImetnik(subjektService.getAndSaveBeforeIfNecessary(zznak.getImetnik()));
        service.update(zznak);
        return zznak;
    }

    @Transactional
    @RequestMapping(method = RequestMethod.DELETE)
    public boolean delete(long id) throws HandledException {
        this.checkPermission("ZZNAK_AZURIRAJ");
        service.delete(id);
        return true;
    }
}