package zaslontelecom.esk.backend.api.Service;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.DAO.ZakonskaPodlagaDAO;
import zaslontelecom.esk.backend.api.Model.ZakonskaPodlaga;
import zaslontelecom.esk.backend.api.Utils.MyAntiSamy;
import zaslontelecom.esk.backend.api.Utils.Utils;

import java.util.Optional;

@Service
public class ZakonskaPodlagaService extends BaseService{

    @Autowired
    private ZakonskaPodlagaDAO dbService;

    @Autowired
    private MyAntiSamy antiSamy;

    public Iterable<ZakonskaPodlaga> list(){
        return dbService.findAll();
    }

    public Optional<ZakonskaPodlaga> get(long id){
        return dbService.findById(id);
    }

    public ZakonskaPodlaga insert(ZakonskaPodlaga item) throws ScanException, PolicyException {
        item.setSpremenil(GetCurrentUserId());
        item.setVsebina(antiSamy.getCleanHTML(item.getVsebina()));
        return dbService.save(item);
    }

    public ZakonskaPodlaga update(ZakonskaPodlaga item) throws ScanException, PolicyException {
        boolean existing = dbService.existsById(item.getId());
        if (!existing)
            throw new IllegalArgumentException("ITEM_NOT_EXISTS");
        item.setVsebina(antiSamy.getCleanHTML(item.getVsebina()));
        item.setSpremenil(GetCurrentUserId());
        dbService.save(item);

        return item;
    }

    public void delete(long id) throws PolicyException, ScanException {
        ZakonskaPodlaga item = get(id).get();
        item.setSpremenil(GetCurrentUserId());
        update(item);
        dbService.deleteById(id);
    }

    public Page<ZakonskaPodlaga> findByQuery(int page, int resultPerPage, String query){
        Pageable pageSortById = PageRequest.of(page, resultPerPage, Sort.by("id").descending());
        return dbService.findByStevilkaContainingIgnoreCaseOrVsebinaContainingIgnoreCase(query, query, pageSortById);
    }

    public Optional<ZakonskaPodlaga> getByStevilka(String stevilka) {
        return this.dbService.getByStevilka(stevilka);
    }
}