package zaslontelecom.esk.backend.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import zaslontelecom.esk.backend.api.Controller.Requests.PagedQuery;
import zaslontelecom.esk.backend.api.Controller.Response.PagedQueryResult;
import zaslontelecom.esk.backend.api.Model.Ko;
import zaslontelecom.esk.backend.api.Service.KoService;
import zaslontelecom.esk.backend.api.Utils.HandledException;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping(path = "/Ko")
public class KoController extends BaseController  {
    @Autowired
    KoService service;

    @RequestMapping(value = "/list", method = RequestMethod.GET, headers = "Accept=application/json")
    public Iterable<Ko> list() throws HandledException {
        Iterable<Ko> result = this.service.list();
        return result;
    }

    @RequestMapping(value = "/getByExtSif", method = RequestMethod.GET)
    public Optional<Ko> getBySif(String sifra) throws HandledException {
        return service.getByExtSif(sifra);
    }
}