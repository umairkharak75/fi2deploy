package zaslontelecom.esk.backend.api.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.DAO.DejavnostDAO;
import zaslontelecom.esk.backend.api.Model.Dejavnost;

import java.util.Optional;

@Service
public class DejavnostService extends BaseService {

    @Autowired
    private DejavnostDAO dbService;

    public Iterable<Dejavnost> list(){
        return dbService.findAll();
    }

    public Optional<Dejavnost> get(long id){
        return dbService.findById(id);
    }

    public Dejavnost insert(Dejavnost item){
        item.setSpremenil(GetCurrentUserId());
        return dbService.save(item);
    }

    public Dejavnost update(Dejavnost item){
        boolean existing = dbService.existsById(item.getId());
        if (!existing)
            throw new IllegalArgumentException("ITEM_NOT_EXISTS");
        item.setSpremenil(GetCurrentUserId());
        dbService.save(item);

        return item;
    }

    public void delete(long id){
        Dejavnost item = get(id).get();
        item.setSpremenil(GetCurrentUserId());
        update(item);
        dbService.deleteById(id);
    }

    public Page<Dejavnost> findByQuery(int page, int resultPerPage, String query){
        Pageable pageSortById = PageRequest.of(page, resultPerPage, Sort.by("naziv").descending());
        return dbService.findAllByNazivContainingIgnoreCase(query, pageSortById);
    }

    public Optional<Dejavnost> getByNaziv(String dejavnost) {
        return this.dbService.getByNazivIgnoreCase(dejavnost);
    }

    public Iterable<Dejavnost> getByIdCertifikat(long id) {
        return dbService.getByIdCertifikat(id);
    }
}