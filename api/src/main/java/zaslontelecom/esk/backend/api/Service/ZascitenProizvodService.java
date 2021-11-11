package zaslontelecom.esk.backend.api.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.DAO.ZascitenProizvodDAO;
import zaslontelecom.esk.backend.api.Model.ZascitenProizvod;
import zaslontelecom.esk.backend.api.Utils.Utils;

import java.util.Optional;

@Service
public class ZascitenProizvodService extends BaseService{

    @Autowired
    private ZascitenProizvodDAO dbService;

    public Iterable<ZascitenProizvod> list(){
        return dbService.findAll();
    }

    public Optional<ZascitenProizvod> get(long id){
        return dbService.findById(id);
    }

    public ZascitenProizvod insert(ZascitenProizvod item){
        item.setSpremenil(GetCurrentUserId());
        return dbService.save(item);
    }

    public void save(ZascitenProizvod item){
        item.setSpremenil(GetCurrentUserId());
        dbService.save(item);
    }

    public ZascitenProizvod update(ZascitenProizvod item){
        boolean existing = dbService.existsById(item.getId());
        if (!existing)
            throw new IllegalArgumentException("ITEM_NOT_EXISTS");
        item.setSpremenil(GetCurrentUserId());
        dbService.save(item);

        return item;
    }

    public void delete(long id){
        ZascitenProizvod item = get(id).get();
        item.setSpremenil(GetCurrentUserId());
        update(item);
        dbService.deleteById(id);
    }

    public Page<ZascitenProizvod> findByQuery(int page, int resultPerPage, String query){
        Pageable pageSortById = PageRequest.of(page, resultPerPage == 0 ? 1000 : resultPerPage, Sort.by("id").descending());
        if (Utils.isNullOrEmpty(query))
            return dbService.findAll(pageSortById);
        else
            return dbService.findAllByNazivContainingIgnoreCase(query, pageSortById);
    }

    public Page<ZascitenProizvod> findByIdShema(int page, int resultPerPage, long idShema){
        Pageable pageSortById = PageRequest.of(page, resultPerPage == 0 ? 1000 : resultPerPage, Sort.by("naziv").descending());
        return dbService.findAllByShemaIdEquals(idShema, pageSortById);
    }

    public Optional<ZascitenProizvod> getByIdShemaAndNaziv(long idShema, String naziv) {
        return dbService.findByShemaIdEqualsAndNazivIgnoreCaseEquals(idShema, naziv);
    }
}