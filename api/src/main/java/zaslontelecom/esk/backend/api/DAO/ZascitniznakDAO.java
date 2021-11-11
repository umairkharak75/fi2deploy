package zaslontelecom.esk.backend.api.DAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.Model.Zascitniznak;

import java.util.Optional;

@Service
@Repository
public interface ZascitniznakDAO extends PagingAndSortingRepository<Zascitniznak, Long>, JpaRepository<Zascitniznak, Long> {

    @Query(
            value = "SELECT c.* FROM ESK_DATA.zascitniznak c inner join ESK_DATA.SUBJEKT S on c.ID_IMETNIK = S.ID LEFT JOIN ESK_DATA.ZASCITENPROIZVOD zp on c.ID_ZASCITEN_PROIZVOD = zp.id LEFT JOIN ESK_DATA.SHEMA S2 on zp.ID_SHEMA = S2.ID where lower(c.zz_shema) = :query or INSTR(lower(c.naziv_proizvoda), :query) > 0 or INSTR(lower(c.cert_organ), :query) > 0 or lower(c.st_odl) = :query or INSTR(lower(s.naziv), :query) > 0 or s.maticna = :query or s.davcna = :query or s.id_poste = :query or INSTR(lower(s.posta), :query) > 0 or INSTR(lower(zp.naziv), :query) > 0 or INSTR(lower(s2.naziv), :query) > 0",
            countQuery = "SELECT count(*) FROM ESK_DATA.zascitniznak c inner join ESK_DATA.SUBJEKT S on c.ID_IMETNIK = S.ID LEFT JOIN ESK_DATA.ZASCITENPROIZVOD zp on c.ID_ZASCITEN_PROIZVOD = zp.id LEFT JOIN ESK_DATA.SHEMA S2 on zp.ID_SHEMA = S2.ID where lower(c.zz_shema) = :query or INSTR(lower(c.naziv_proizvoda), :query) > 0 or INSTR(lower(c.cert_organ), :query) > 0 or lower(c.st_odl) = :query or INSTR(lower(s.naziv), :query) > 0 or s.maticna = :query or s.davcna = :query or s.id_poste = :query or INSTR(lower(s.posta), :query) > 0 or INSTR(lower(zp.naziv), :query) > 0 or INSTR(lower(s2.naziv), :query) > 0",
            nativeQuery = true)
    Page<Zascitniznak> findAllByNazivProizvodaContainingIgnoreCase(@Param("query") String query, Pageable pageable);

    Optional<Zascitniznak> getByStOdl(String stOdl);

    int countByStevilka(String stevilka);

    Optional<Zascitniznak> getByStevilka(String stevilka);
}