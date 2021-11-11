package zaslontelecom.esk.backend.api.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import zaslontelecom.esk.backend.api.Controller.BaseController;
import zaslontelecom.esk.backend.api.DAO.CertifikatPrilogaProizvodDAO;
import zaslontelecom.esk.backend.api.DAO.CertifikatProizvodDAO;
import zaslontelecom.esk.backend.api.DAO.PrilogaDAO;
import zaslontelecom.esk.backend.api.Model.CertifikatPrilogaProizvod;
import zaslontelecom.esk.backend.api.Model.CertifikatProizvod;
import zaslontelecom.esk.backend.api.Model.Priloga;
import zaslontelecom.esk.backend.api.Model.PrilogaClan;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class CertifikatProizvodService extends BaseService {

    @Autowired
    private CertifikatProizvodDAO dbService;

    @Autowired
    EntityManager em;

    public void deleteAllByIdCertifikat(long id) {
        dbService.deleteAllByIdCertifikat(id);
    }

    public void save(CertifikatProizvod item) {
        StoredProcedureQuery sp = em.createStoredProcedureQuery("ESK_DATA.ESK_MGMT.saveProduct");
        // register parameters
        sp.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
        sp.setParameter(1, item.getIdCertifikat());
        sp.registerStoredProcedureParameter(2, Long.class, ParameterMode.IN);
        sp.setParameter(2, item.getIdProizvod());
        sp.registerStoredProcedureParameter(3, Long.class, ParameterMode.IN);
        sp.setParameter(3, GetCurrentUserId());
        sp.registerStoredProcedureParameter(4, Long.class, ParameterMode.OUT);
        sp.execute();
        Object result = sp.getOutputParameterValue(4);
        item.setId(Long.valueOf(result.toString()));
    }

    public Iterable<CertifikatProizvod> findAllByIdCertifikat(long id) {
        return this.dbService.findAllByIdCertifikat(id);
    }
}