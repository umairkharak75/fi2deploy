package zaslontelecom.esk.backend.api.DAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.Model.CertifikatExt;

import java.util.Optional;

@Service
@Repository
public interface CertifikatExtDAO extends PagingAndSortingRepository<CertifikatExt, Long>, JpaRepository<CertifikatExt, Long> {

    Optional<CertifikatExt> getByIdCertifikat(Long idCertifikat);
}