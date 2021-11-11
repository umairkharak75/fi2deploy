package zaslontelecom.esk.backend.api.DAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.Model.Proizvod;

import java.util.Optional;

@Service
@Repository
public interface ProizvodDAO extends PagingAndSortingRepository<Proizvod, Long>, JpaRepository<Proizvod, Long> {

    Page<Proizvod> findAllByNazivContainingIgnoreCaseOrZascitenProizvodNazivContainingIgnoreCaseOrZascitenProizvodShemaNazivContainingIgnoreCase(String naziv, String naziv1, String naziv2, Pageable pageable);
    Page<Proizvod> findAllByZascitenProizvodIdEqualsAndNazivIsNotNull(long idShema, Pageable pageable);


    boolean existsByZascitenProizvodIdEqualsAndNazivIsNull(long id);

    boolean existsByZascitenProizvodIdEqualsAndNazivIgnoreCaseEquals(long id, String naziv);

    @Procedure(procedureName = "ESK_DATA.ESK_MGMT.deleteUnusedProizvodData")
    void clearUnusedData();

    Iterable<Proizvod> findAllByZascitniznakProizvodZascitniznakIdEquals(long id);

    Optional<Proizvod> getByZascitenProizvodIdAndNazivIsNull(Long idZascitenProizvod);

    Optional<Proizvod> getByZascitenProizvodIdAndNazivIgnoreCaseEquals(Long idZascitenProizvod, String naziv);

    @Query(
            value = "select count(*) from ESK_DATA.proizvod p where p.id_zasciten_proizvod = :idZascitenProizvod and length(TRIM(p.naziv)) > 0",
            nativeQuery = true
    )
    long countByZascitenProizvodId(@Param("idZascitenProizvod") long idZascitenProizvod);


    @Query(
            value = "select p.* from ESK_DATA.certifikat_proizvod cp inner join ESK_DATA.proizvod p on cp.id_proizvod = p.id where cp.id_certifikat = :id_certifikat",
            nativeQuery = true
    )
    Iterable<Proizvod> getByIdCertifikat(@Param("id_certifikat") long id);
}