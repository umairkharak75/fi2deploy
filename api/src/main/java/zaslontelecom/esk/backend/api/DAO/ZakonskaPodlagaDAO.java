package zaslontelecom.esk.backend.api.DAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.Model.ZakonskaPodlaga;

import java.util.Optional;

@Service
@Repository
public interface ZakonskaPodlagaDAO extends PagingAndSortingRepository<ZakonskaPodlaga, Long>, JpaRepository<ZakonskaPodlaga, Long> {

    Page<ZakonskaPodlaga> findByStevilkaContainingIgnoreCaseOrVsebinaContainingIgnoreCase(String stevilka, String vsebina, Pageable pageable);

    Optional<ZakonskaPodlaga> getByStevilka(String stevilka);
}