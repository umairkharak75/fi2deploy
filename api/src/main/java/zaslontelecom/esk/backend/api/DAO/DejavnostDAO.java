package zaslontelecom.esk.backend.api.DAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.Model.Dejavnost;

import java.util.Optional;

@Service
@Repository
public interface DejavnostDAO extends PagingAndSortingRepository<Dejavnost, Long>, JpaRepository<Dejavnost, Long> {

    Page<Dejavnost> findAllByNazivContainingIgnoreCase(String naziv, Pageable pageable);

    Optional<Dejavnost> getByNazivIgnoreCase(String dejavnost);

    @Query(value = "select d.* from ESK_DATA.CERTIFIKAT_DEJAVNOST cd inner join ESK_DATA.DEJAVNOST d on cd.ID_DEJAVNOST = d.ID where cd.ID_CERTIFIKAT = :id_certifikat", nativeQuery = true)
    Iterable<Dejavnost> getByIdCertifikat(@Param("id_certifikat") long id);
}