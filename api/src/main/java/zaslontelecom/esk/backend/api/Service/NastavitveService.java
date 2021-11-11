package zaslontelecom.esk.backend.api.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.DAO.NastavitveDAO;
import zaslontelecom.esk.backend.api.Model.Nastavitve;
import zaslontelecom.esk.backend.api.Utils.HandledException;

import java.util.Optional;

@Service
public class NastavitveService {

    @Autowired
    private NastavitveDAO dbService;

    public Nastavitve get() throws HandledException {

        Optional<Nastavitve> found = this.dbService.getFirst();

        if (!found.isPresent()){
            throw new HandledException("Nastavitve ne obstajajo.");
        }

        return found.get();
    }

}