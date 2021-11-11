package zaslontelecom.esk.backend.api.DAO;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.Model.ZakonskaPodlaga;

@Service
@Repository
public interface ZakonskaPodlagaDAO extends CrudRepository<ZakonskaPodlaga, Long> {

}