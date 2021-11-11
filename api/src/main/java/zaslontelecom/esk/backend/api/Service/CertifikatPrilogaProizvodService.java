package zaslontelecom.esk.backend.api.Service;

import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.Controller.Requests.PagedQuery;
import zaslontelecom.esk.backend.api.DAO.CertifikatPrilogaProizvodDAO;
import zaslontelecom.esk.backend.api.DAO.PrilogaDAO;
import zaslontelecom.esk.backend.api.Model.CertifikatPrilogaProizvod;
import zaslontelecom.esk.backend.api.Model.Priloga;
import zaslontelecom.esk.backend.api.Utils.MyAntiSamy;
import zaslontelecom.esk.backend.api.Utils.Utils;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CertifikatPrilogaProizvodService extends BaseService{

    @Autowired
    private CertifikatPrilogaProizvodDAO dao;

    @Autowired
    private PrilogaDAO prilogaDao;

    @Autowired
    EntityManager em;

    public Iterable<CertifikatPrilogaProizvod> list(){
        return dao.findAll();
    }

    public Optional<CertifikatPrilogaProizvod> get(long id){
        return dao.findById(id);
    }

    public CertifikatPrilogaProizvod insert(CertifikatPrilogaProizvod item) throws PolicyException, ScanException {
        item.setSpremenil(GetCurrentUserId());

        Priloga priloga = item.getPriloga();
        priloga.setVsebina(antiSamy.getCleanHTML(priloga.getVsebina()));
        priloga.setSpremenil(GetCurrentUserId());
        return dao.save(item);
    }

    @Autowired
    private MyAntiSamy antiSamy;

    public CertifikatPrilogaProizvod update(CertifikatPrilogaProizvod item) throws PolicyException, ScanException {
        boolean existing = dao.existsById(item.getId());
        if (!existing)
            throw new IllegalArgumentException("ITEM_NOT_EXISTS");
        item.setSpremenil(GetCurrentUserId());

        Priloga priloga = item.getPriloga();
        priloga.setVsebina(antiSamy.getCleanHTML(priloga.getVsebina()));
        priloga.setSpremenil(GetCurrentUserId());
        dao.save(item);

        return item;
    }

    public void delete(long id) throws ScanException, PolicyException {
        CertifikatPrilogaProizvod item = get(id).get();
        item.setSpremenil(GetCurrentUserId());
        update(item);
        dao.deleteById(id);
    }

    public void save(long idCertifikat, Collection<CertifikatPrilogaProizvod> attachments) throws PolicyException, ScanException {
        if (attachments == null || attachments.isEmpty()){
            return;
        }
        for (CertifikatPrilogaProizvod var : attachments)
        {
            var.setIdCertifikat(idCertifikat);
            Priloga priloga = var.getPriloga();
            priloga.setVsebina(antiSamy.getCleanHTML(priloga.getVsebina()));
            priloga.setSpremenil(GetCurrentUserId());
            prilogaDao.save(priloga);
            var.setIdPriloga(priloga.getId());
        }
        dao.saveAll(attachments);
    }

    public void deleteAllByIdCertifikat(long id) {
        Iterable<CertifikatPrilogaProizvod> certifikatPrilogaProizvodList = dao.getAllByIdCertifikat(id);
        dao.deleteAllByIdCertifikat(id);
        for (CertifikatPrilogaProizvod item : certifikatPrilogaProizvodList) {
            long idPriloga = item.getPriloga().getId();
            prilogaDao.deleteById(idPriloga);
        }
    }

    public Page<CertifikatPrilogaProizvod> getLastByIdCertifikat(long id) {
        Pageable pageSortById = PageRequest.of(0, 1, Sort.by("id").descending());
        return dao.findAllByIdCertifikat(id, pageSortById);
    }

    public Optional<CertifikatPrilogaProizvod> getLastOptionalByIdCertifikat(long id) {
        Optional<CertifikatPrilogaProizvod> data = Optional.empty();

        Page<CertifikatPrilogaProizvod> result = getLastByIdCertifikat(id);
        if (result.hasContent()){
            data = Optional.of(result.getContent().get(0));
        }

        return data;
    }

    public List<Object> findByParams(PagedQuery request, String org) {
        StoredProcedureQuery sp = em.createStoredProcedureQuery("ESK_DATA.ESK_MGMT.getPrilogaProizvodList");
        // register parameters
        sp.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
        sp.setParameter(1, request.getQuery());
        sp.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
        sp.setParameter(2,org);
        sp.registerStoredProcedureParameter(3, Object.class, ParameterMode.REF_CURSOR);
        sp.execute();
        List<Object> resultList = sp.getResultList();

        return resultList;
    }
}