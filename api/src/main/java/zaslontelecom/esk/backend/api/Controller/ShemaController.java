package zaslontelecom.esk.backend.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import zaslontelecom.esk.backend.api.Controller.Requests.PagedQuery;
import zaslontelecom.esk.backend.api.Controller.Response.PagedQueryResult;
import zaslontelecom.esk.backend.api.Model.Shema;
import zaslontelecom.esk.backend.api.Service.ShemaService;

import java.util.Optional;

@RestController
// @CrossOrigin(origins = "*")
@RequestMapping(path = "/Shema")
public class ShemaController  {
    @Autowired
    ShemaService service;

    @RequestMapping(value = "/list", method = RequestMethod.GET, headers = "Accept=application/json")
    public PagedQueryResult list(PagedQuery request) {
        PagedQueryResult<Shema> response = new PagedQueryResult<>();
        Page<Shema> resultPage = this.service.findByQuery(request.getPageNumber(), request.getResultPerPage(), request.getQuery());
        response.setResult(resultPage.getContent());
        response.setResultLength(resultPage.getTotalElements());

        return response;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Optional<Shema> get(long id) {
        return service.get(id);
    }
}