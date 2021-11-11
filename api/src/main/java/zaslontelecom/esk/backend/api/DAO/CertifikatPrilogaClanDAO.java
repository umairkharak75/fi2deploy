
package zaslontelecom.esk.backend.api.DAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.Model.CertifikatPrilogaClan;

import java.util.Collection;

@Service
@Repository
public interface CertifikatPrilogaClanDAO extends PagingAndSortingRepository<CertifikatPrilogaClan, Long>, CrudRepository<CertifikatPrilogaClan, Long> {
    Page<CertifikatPrilogaClan> findAllByIdCertifikat(long id, Pageable pageSortById);
    void deleteAllByIdCertifikat(long id);

    Iterable<CertifikatPrilogaClan> getAllByIdCertifikat(long id);
}