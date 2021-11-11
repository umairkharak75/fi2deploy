package zaslontelecom.esk.backend.api.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.DAO.KoDAO;
import zaslontelecom.esk.backend.api.Model.Ko;

import java.util.Optional;

@Service
public class KoService extends BaseService{

    @Autowired
    private KoDAO dbService;

    public Iterable<Ko> list(){
        return dbService.findAll();
    }

    public Optional<Ko> getBySif(String ko){
        return dbService.getBySif(ko);
    }

    public Optional<Ko> getByExtSif(String ko){
        return dbService.getByExtSif(ko);
    }
}