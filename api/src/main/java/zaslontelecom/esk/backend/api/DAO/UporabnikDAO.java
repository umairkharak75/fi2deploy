package zaslontelecom.esk.backend.api.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.Model.EskUsers;

@Service
@Repository
public interface UporabnikDAO extends JpaRepository<EskUsers, Long> {

}