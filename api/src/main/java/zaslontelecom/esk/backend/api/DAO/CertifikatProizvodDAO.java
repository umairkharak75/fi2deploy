package zaslontelecom.esk.backend.api.DAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zaslontelecom.esk.backend.api.Model.CertifikatProizvod;

@Service
@Repository
public interface CertifikatProizvodDAO extends JpaRepository<CertifikatProizvod, Long> {

    Iterable<CertifikatProizvod> findAllByIdCertifikat(long id);

    void deleteAllByIdCertifikat(long id);
}