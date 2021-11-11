
package zaslontelecom.esk.backend.api.DAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.Model.CertifikatPrilogaProizvod;

@Service
@Repository
public interface CertifikatPrilogaProizvodDAO extends PagingAndSortingRepository<CertifikatPrilogaProizvod, Long>, CrudRepository<CertifikatPrilogaProizvod, Long> {
    void deleteAllByIdCertifikat(long id);

    Iterable<CertifikatPrilogaProizvod> getAllByIdCertifikat(long id);

    Page<CertifikatPrilogaProizvod> findAllByIdCertifikat(long id, Pageable pageSortById);
}