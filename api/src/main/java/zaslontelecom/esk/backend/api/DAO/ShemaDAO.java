package zaslontelecom.esk.backend.api.DAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.Model.Shema;

import java.util.Optional;

@Service
@Repository
public interface ShemaDAO extends PagingAndSortingRepository<Shema, Long>, JpaRepository<Shema, Long> {

    Page<Shema> findAllByNazivContainingIgnoreCase(String naziv, Pageable pageable);

    Optional<Shema> findByNazivIgnoreCaseEquals(String name);
}