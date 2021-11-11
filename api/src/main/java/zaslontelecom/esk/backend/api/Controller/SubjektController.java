package zaslontelecom.esk.backend.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import zaslontelecom.esk.backend.api.Controller.Requests.PagedQuery;
import zaslontelecom.esk.backend.api.Controller.Response.PagedQueryResult;
import zaslontelecom.esk.backend.api.Model.Evsubjekt;
import zaslontelecom.esk.backend.api.Model.Subjekt;
import zaslontelecom.esk.backend.api.Service.SubjektService;

import java.util.Collection;
import java.util.Optional;

@RestController
// @CrossOrigin(origins = "*")
@RequestMapping(path = "/Subjekt")
public class SubjektController {
    @Autowired
    SubjektService service;

    @RequestMapping(value = "/list", method = RequestMethod.GET, headers = "Accept=application/json")
    public PagedQueryResult list(PagedQuery request) {

        PagedQueryResult<Evsubjekt> response = new PagedQueryResult<>();

        Collection<Evsubjekt> resultPage = this.service.findByQuery(request.getQuery());
        response.setResult(resultPage);
        response.setResultLength(resultPage.size());

        return response;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Optional<Subjekt> get(long id) {
        return service.get(id);
    }
}