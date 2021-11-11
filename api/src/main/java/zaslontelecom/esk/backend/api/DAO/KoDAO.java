package zaslontelecom.esk.backend.api.DAO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.Model.Ko;

import java.util.Optional;

@Service
@Repository
public interface KoDAO extends CrudRepository<Ko, Long> {
    Optional<Ko> getBySif(String sif);
    Optional<Ko> getByExtSif(String exSif);
}