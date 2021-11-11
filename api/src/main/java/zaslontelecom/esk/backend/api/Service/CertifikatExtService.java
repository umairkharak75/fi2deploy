package zaslontelecom.esk.backend.api.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.DAO.CertifikatExtDAO;
import zaslontelecom.esk.backend.api.Model.CertifikatExt;

import java.util.Optional;

@Service
public class CertifikatExtService extends BaseService{

    @Autowired
    private CertifikatExtDAO dao;
    

    public Optional<CertifikatExt> getByIdCertifikat(long id){
        return dao.getByIdCertifikat(id);
    }

    public void save(CertifikatExt item){
        item.setSpremenil(GetCurrentUserId());
        dao.save(item);
    }
}