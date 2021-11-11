package zaslontelecom.esk.backend.api.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.DAO.ShemaDAO;
import zaslontelecom.esk.backend.api.Model.Shema;
import zaslontelecom.esk.backend.api.Utils.Utils;

import java.util.Optional;

@Service
public class ShemaService extends BaseService {

    @Autowired
    private ShemaDAO dbService;

    public Iterable<Shema> list(){
        return dbService.findAll();
    }

    public Optional<Shema> get(long id){
        return dbService.findById(id);
    }

    public Shema insert(Shema item){
        item.setSpremenil(GetCurrentUserId());
        return dbService.save(item);
    }

    public void save(Shema item){
        item.setSpremenil(GetCurrentUserId());
        dbService.save(item);
    }

    public Shema update(Shema item){
        boolean existing = dbService.existsById(item.getId());
        if (!existing)
            throw new IllegalArgumentException("ITEM_NOT_EXISTS");
        item.setSpremenil(GetCurrentUserId());
        dbService.save(item);

        return item;
    }

    public void delete(long id){
        Shema item = get(id).get();
        item.setSpremenil(GetCurrentUserId());
        update(item);
        dbService.deleteById(id);
    }

    public Page<Shema> findByQuery(int page, int resultPerPage, String query){
        Pageable pageSortById = PageRequest.of(page, resultPerPage == 0 ? 1000 : resultPerPage, Sort.by("naziv").descending());
        if (Utils.isNullOrEmpty(query))
            return dbService.findAll(pageSortById);
        else
            return dbService.findAllByNazivContainingIgnoreCase(query, pageSortById);
    }

    public Optional<Shema> getByNaziv(String name){
        return dbService.findByNazivIgnoreCaseEquals(name);
    }
}