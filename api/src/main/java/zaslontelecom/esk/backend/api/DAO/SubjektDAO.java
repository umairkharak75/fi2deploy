package zaslontelecom.esk.backend.api.DAO;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.Model.Subjekt;

import java.sql.Date;
import java.util.Optional;

@Service
@Repository
public interface SubjektDAO extends CrudRepository<Subjekt, Long> {
    Optional<Subjekt> findFirstBySubjIdAndDatZsGreaterThanEqualOrderByIdDesc(long subj_id, Date datZs);
}