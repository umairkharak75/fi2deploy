package zaslontelecom.esk.backend.api.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.DAO.CertifikatDejavnostDAO;
import zaslontelecom.esk.backend.api.Model.CertifikatDejavnost;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.util.Collection;


@Service
public class CertifikatDejavnostService extends BaseService{

    @Autowired
    EntityManager em;

    public void save(CertifikatDejavnost item) {
        StoredProcedureQuery sp = em.createStoredProcedureQuery("ESK_DATA.ESK_MGMT.saveActivity");
        // register parameters
        sp.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
        sp.setParameter(1, item.getIdCertifikat());
        sp.registerStoredProcedureParameter(2, Long.class, ParameterMode.IN);
        sp.setParameter(2, item.getIdDejavnost());
        sp.registerStoredProcedureParameter(3, Long.class, ParameterMode.IN);
        sp.setParameter(3, GetCurrentUser().getEskUser().getId());
        sp.registerStoredProcedureParameter(4, Long.class, ParameterMode.OUT);
        sp.execute();
        Object result = sp.getOutputParameterValue(4);
        item.setId(Long.valueOf(result.toString()));
    }

}