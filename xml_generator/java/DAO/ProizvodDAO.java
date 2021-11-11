package zaslontelecom.esk.backend.api.DAO;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.Model.Proizvod;

@Service
@Repository
public interface ProizvodDAO extends CrudRepository<Proizvod, Long> {

}