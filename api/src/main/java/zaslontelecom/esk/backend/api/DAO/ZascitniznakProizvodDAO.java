package zaslontelecom.esk.backend.api.DAO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.Model.ZascitniznakProizvod;

@Service
@Repository
public interface ZascitniznakProizvodDAO extends CrudRepository<ZascitniznakProizvod, Long> {

    void removeAllByIdZascitniznak(long id);
}