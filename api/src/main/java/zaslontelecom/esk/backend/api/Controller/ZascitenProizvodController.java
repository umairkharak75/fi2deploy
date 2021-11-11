package zaslontelecom.esk.backend.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import zaslontelecom.esk.backend.api.Controller.Requests.PagedQuery;
import zaslontelecom.esk.backend.api.Controller.Requests.ZascitenProizvodByIdShemaQuery;
import zaslontelecom.esk.backend.api.Controller.Response.PagedQueryResult;
import zaslontelecom.esk.backend.api.Model.ZascitenProizvod;
import zaslontelecom.esk.backend.api.Service.ZascitenProizvodService;

import java.util.Optional;

@RestController
// @CrossOrigin(origins = "*")
@RequestMapping(path = "/ZascitenProizvod")
public class ZascitenProizvodController {
    @Autowired
    ZascitenProizvodService service;

    @RequestMapping(value = "/list", method = RequestMethod.GET, headers = "Accept=application/json")
    public PagedQueryResult list(PagedQuery request) {
        PagedQueryResult<ZascitenProizvod> response = new PagedQueryResult<>();
        Page<ZascitenProizvod> resultPage = this.service.findByQuery(request.getPageNumber(), request.getResultPerPage(), request.getQuery());
        response.setResult(resultPage.getContent());
        response.setResultLength(resultPage.getTotalElements());

        return response;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Optional<ZascitenProizvod> get(long id) {
        return service.get(id);
    }

    @RequestMapping(value = "/ByIdShema", method = RequestMethod.GET, headers = "Accept=application/json")
    public PagedQueryResult byIdShema(ZascitenProizvodByIdShemaQuery request) {
        PagedQueryResult<ZascitenProizvod> response = new PagedQueryResult<>();
        Page<ZascitenProizvod> resultPage = this.service.findByIdShema(request.getPageNumber(), request.getResultPerPage(), request.getIdShema());
        response.setResult(resultPage.getContent());
        response.setResultLength(resultPage.getTotalElements());

        return response;
    }
}