package zaslontelecom.esk.backend.api.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.DAO.PrilogaDAO;
import zaslontelecom.esk.backend.api.Model.Priloga;

import java.util.Optional;

@Service
public class PrilogaService extends BaseService{

    @Autowired
    private PrilogaDAO dbService;

    public Iterable<Priloga> list(){
        return dbService.findAll();
    }

    public Optional<Priloga> get(long id){
        return dbService.findById(id);
    }

    public Priloga insert(Priloga item){
        item.setSpremenil(GetCurrentUserId());
        return dbService.save(item);
    }

    public Priloga update(Priloga item){
        boolean existing = dbService.existsById(item.getId());
        if (!existing)
            throw new IllegalArgumentException("ITEM_NOT_EXISTS");
        item.setSpremenil(GetCurrentUserId());
        dbService.save(item);

        return item;
    }

    public void delete(long id){
        Priloga item = get(id).get();
        item.setSpremenil(GetCurrentUserId());
        update(item);
        dbService.deleteById(id);
    }
}