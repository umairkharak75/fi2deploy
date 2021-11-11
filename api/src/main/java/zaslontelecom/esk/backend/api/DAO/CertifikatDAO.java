package zaslontelecom.esk.backend.api.DAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.Model.Certifikat;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Repository
public interface CertifikatDAO extends PagingAndSortingRepository<Certifikat, Long>, JpaRepository<Certifikat, Long>, JpaSpecificationExecutor<Certifikat> {

    Page<Certifikat> findAllByStatusInAndOrganizacija(List<String> statusList, String org, Pageable pageSortById);
    Page<Certifikat> findAllByStatusIn(List<String> statusList, Pageable pageSortById);

    int countByStevilkaAndStatusInAndIdNot(String stevilka, List<String> status,Long id);

    int countByStatus(String status);

    int countByStatusAndDatVeljBetween(String veljaven, Date now, Date addDays);

    @Query(
            value = "SELECT x.* FROM ESK_DATA.CERTIFIKAT x WHERE x.id in ( SELECT c.id FROM ESK_DATA.CERTIFIKAT c inner join ESK_DATA.SUBJEKT S on c.ID_IMETNIK = S.ID INNER JOIN ESK_DATA.ZASCITENPROIZVOD zp on c.ID_ZASCITEN_PROIZVOD = zp.id INNER JOIN ESK_DATA.SHEMA S2 on zp.ID_SHEMA = S2.ID LEFT JOIN ESK_DATA.CERTIFIKAT_PROIZVOD cp ON c.id = cp.id_certifikat LEFT JOIN ESK_DATA.PROIZVOD p ON cp.id_proizvod = p.id LEFT JOIN ESK_DATA.CERTIFIKAT_PRILOGA_CLAN cpp ON cpp.id_certifikat = c.id LEFT JOIN ESK_DATA.PRILOGA_CLAN pc ON cpp.id_priloga = pc.id_priloga LEFT JOIN ESK_DATA.SUBJEKT clan ON pc.id_subjekt = clan.id WHERE (:org is null or c.ORGANIZACIJA = :org) and c.STATUS IN :statusList and ( INSTR(lower(c.stevilka), :query) > 0 or  INSTR(lower(s.naziv), :query) > 0 or INSTR(lower(clan.naziv), :query) > 0 or  s.id_poste = :query or clan.id_poste = :query or  INSTR(lower(s.posta), :query) > 0 or INSTR(lower(clan.posta), :query) > 0 or  INSTR(lower(zp.naziv), :query) > 0 or  INSTR(lower(s2.naziv), :query) > 0 or  (REGEXP_INSTR(:query, '[[:digit:]]') > 0 and (s.maticna = :query or s.davcna =:query or lower(s.KMGMID)=:query))))",
            countQuery = "SELECT COUNT(x.id) FROM ESK_DATA.CERTIFIKAT x WHERE x.id in ( SELECT c.id FROM ESK_DATA.CERTIFIKAT c inner join ESK_DATA.SUBJEKT S on c.ID_IMETNIK = S.ID INNER JOIN ESK_DATA.ZASCITENPROIZVOD zp on c.ID_ZASCITEN_PROIZVOD = zp.id INNER JOIN ESK_DATA.SHEMA S2 on zp.ID_SHEMA = S2.ID LEFT JOIN ESK_DATA.CERTIFIKAT_PROIZVOD cp ON c.id = cp.id_certifikat LEFT JOIN ESK_DATA.PROIZVOD p ON cp.id_proizvod = p.id LEFT JOIN ESK_DATA.CERTIFIKAT_PRILOGA_CLAN cpp ON cpp.id_certifikat = c.id LEFT JOIN ESK_DATA.PRILOGA_CLAN pc ON cpp.id_priloga = pc.id_priloga LEFT JOIN ESK_DATA.SUBJEKT clan ON pc.id_subjekt = clan.id WHERE (:org is null or c.ORGANIZACIJA = :org) and c.STATUS IN :statusList and ( INSTR(lower(c.stevilka), :query) > 0 or  INSTR(lower(s.naziv), :query) > 0 or INSTR(lower(clan.naziv), :query) > 0 or  s.id_poste = :query or clan.id_poste = :query or  INSTR(lower(s.posta), :query) > 0 or INSTR(lower(clan.posta), :query) > 0 or  INSTR(lower(zp.naziv), :query) > 0 or  INSTR(lower(s2.naziv), :query) > 0 or  (REGEXP_INSTR(:query, '[[:digit:]]') > 0 and (s.maticna = :query or s.davcna =:query or lower(s.KMGMID)=:query))))",
            nativeQuery = true)
    Page<Certifikat> findAllCertificatesWithPagination(@Param("query") String query, @Param("statusList") List<String> statusList, @Param("org") String org, Pageable pageable);

    @Query( value="select count(*) from ESK_DATA.CERTIFIKAT where id != :id and ID_IMETNIK = :idImetnik and id_zasciten_proizvod = :idZascitenProizvod and (status in ('Veljaven', 'Vnos') or dat_velj > trunc(sysdate))", nativeQuery = true)
    int checkIfAlreadyExists(@Param("idImetnik") Long idImetnik, @Param("idZascitenProizvod") Long idZascitenProizvod, @Param("id") Long id);

    @Query( value="select count(*) from ESK_DATA.CERTIFIKAT where id != :id and ID_IMETNIK = :idImetnik and id_zasciten_proizvod = :idZascitenProizvod and (status in ('Veljaven', 'Vnos'))", nativeQuery = true)
    int checkIfAlreadyExists2(@Param("idImetnik") Long idImetnik, @Param("idZascitenProizvod") Long idZascitenProizvod, @Param("id") Long id);

    Optional<Certifikat> getByStevilka(String stevilka);

    int countByStatusAndOrganizacija(String veljaven, String org);

    int countByStatusAndDatVeljBetweenAndOrganizacija(String veljaven, Date now, Date addDays, String org);
}