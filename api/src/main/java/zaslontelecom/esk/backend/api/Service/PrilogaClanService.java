package zaslontelecom.esk.backend.api.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.DAO.PrilogaClanDAO;
import zaslontelecom.esk.backend.api.DAO.PrilogaDAO;
import zaslontelecom.esk.backend.api.Model.Dejavnost;
import zaslontelecom.esk.backend.api.Model.Priloga;
import zaslontelecom.esk.backend.api.Model.PrilogaClan;

import java.util.Optional;

@Service
public class PrilogaClanService extends BaseService {

    @Autowired
    private PrilogaClanDAO dbService;

    public Iterable<PrilogaClan> list(){
        return dbService.findAll();
    }

    public Optional<PrilogaClan> get(long id){
        return dbService.findById(id);
    }

    public PrilogaClan insert(PrilogaClan item){
        item.setSpremenil(GetCurrentUserId());
        return dbService.save(item);
    }

    public PrilogaClan update(PrilogaClan item){
        boolean existing = dbService.existsById(item.getId());
        if (!existing)
            throw new IllegalArgumentException("ITEM_NOT_EXISTS");
        item.setSpremenil(GetCurrentUserId());
        dbService.save(item);

        return item;
    }

    public void delete(long id){
        PrilogaClan item = get(id).get();
        item.setSpremenil(GetCurrentUserId());
        update(item);
        dbService.deleteById(id);
    }
}