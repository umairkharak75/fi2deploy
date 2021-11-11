package zaslontelecom.esk.backend.api.DAO;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.Model.Subjekt;

@Service
@Repository
public interface SubjektDAO extends CrudRepository<Subjekt, Long> {

}