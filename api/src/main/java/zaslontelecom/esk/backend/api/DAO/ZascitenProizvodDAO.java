package zaslontelecom.esk.backend.api.DAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.Model.ZascitenProizvod;

import java.util.Optional;

@Service
@Repository
public interface ZascitenProizvodDAO extends PagingAndSortingRepository<ZascitenProizvod, Long>, JpaRepository<ZascitenProizvod, Long> {

    Page<ZascitenProizvod> findAllByNazivContainingIgnoreCase(String naziv, Pageable pageable);
    Page<ZascitenProizvod> findAllByShemaIdEquals(long idShema, Pageable pageable);
    Optional<ZascitenProizvod> findByShemaIdEqualsAndNazivIgnoreCaseEquals(long idShema, String naziv);
}