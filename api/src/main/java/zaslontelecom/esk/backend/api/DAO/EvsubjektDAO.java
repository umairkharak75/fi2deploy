package zaslontelecom.esk.backend.api.DAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.Model.Evsubjekt;

import java.util.Collection;
import java.util.Optional;

@Service
@Repository
public interface EvsubjektDAO extends JpaRepository<Evsubjekt, Long> {
    @Query(value = "select * from ESK_DATA.EVSUBJEKT WHERE INSTR(lower(naziv), :naziv) > 0 and rownum < 11", nativeQuery = true)
    Collection<Evsubjekt> findAllByName(@Param("naziv") String naziv);

    @Query(value = "select * from ESK_DATA.EVSUBJEKT WHERE maticna = :maticna", nativeQuery = true)
    Optional<Evsubjekt> findLastByMaticna(@Param("maticna") String maticna);

    @Query(value = "select * from ESK_DATA.EVSUBJEKT WHERE davcna = :davcna", nativeQuery = true)
    Optional<Evsubjekt> findLastByDavcna(@Param("davcna") String davcna);

    @Query(value = "select * from ESK_DATA.EVSUBJEKT WHERE KMGMID = :kmgmid", nativeQuery = true)
    Optional<Evsubjekt> findLastByKmgmid(@Param("kmgmid") String kmgmid);

    @Query(value = "select * from ESK_DATA.EVSUBJEKT WHERE subj_id = :subjId", nativeQuery = true)
    Optional<Evsubjekt> findLastBySubjId(@Param("subjId") long subjId);

    @Query(value = "select * from ESK_DATA.EVSUBJEKTALL WHERE INSTR(lower(naziv), :naziv) > 0 and rownum < 11", nativeQuery = true)
    Collection<Evsubjekt> listAllByName(@Param("naziv") String naziv);

    @Query(value = "select * from ESK_DATA.EVSUBJEKTALL WHERE maticna = :maticna", nativeQuery = true)
    Optional<Evsubjekt> listAllLastByMaticna(@Param("maticna") String maticna);

    @Query(value = "select * from ESK_DATA.EVSUBJEKTALL WHERE davcna = :davcna", nativeQuery = true)
    Optional<Evsubjekt> listAllLastByDavcna(@Param("davcna") String davcna);

    @Query(value = "select * from ESK_DATA.EVSUBJEKTALL WHERE KMGMID = :kmgmid", nativeQuery = true)
    Optional<Evsubjekt> listAllLastByKmgmid(@Param("kmgmid") String kmgmid);

    @Query(value = "select * from ESK_DATA.EVSUBJEKTALL WHERE subj_id = :subjId", nativeQuery = true)
    Optional<Evsubjekt> listAllLastBySubjId(@Param("subjId") long subjId);

}