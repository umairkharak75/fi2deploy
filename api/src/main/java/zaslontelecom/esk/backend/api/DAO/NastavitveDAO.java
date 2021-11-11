package zaslontelecom.esk.backend.api.DAO;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.Model.Nastavitve;

import java.util.Optional;

@Service
@Repository
public interface NastavitveDAO extends CrudRepository<Nastavitve, Long> {

    @Query(value="select * from ESK_DATA.nastavitve where rownum = 1", nativeQuery = true)
    Optional<Nastavitve> getFirst();
}