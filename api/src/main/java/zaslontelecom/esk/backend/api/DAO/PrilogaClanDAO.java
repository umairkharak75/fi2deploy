package zaslontelecom.esk.backend.api.DAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.Model.PrilogaClan;

import java.util.Optional;

@Service
@Repository
public interface PrilogaClanDAO extends JpaRepository<PrilogaClan, Long> {

    void deleteAllByIdPriloga(long id);
}