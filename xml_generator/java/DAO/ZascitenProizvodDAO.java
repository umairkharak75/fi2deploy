package zaslontelecom.esk.backend.api.DAO;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.Model.ZascitenProizvod;

@Service
@Repository
public interface ZascitenProizvodDAO extends CrudRepository<ZascitenProizvod, Long> {

}