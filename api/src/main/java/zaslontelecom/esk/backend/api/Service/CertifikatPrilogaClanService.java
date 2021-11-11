package zaslontelecom.esk.backend.api.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.Controller.Requests.CertifikatSearchCriteriaRequest;
import zaslontelecom.esk.backend.api.Controller.Requests.PagedQuery;
import zaslontelecom.esk.backend.api.DAO.CertifikatPrilogaClanDAO;
import zaslontelecom.esk.backend.api.DAO.PrilogaClanDAO;
import zaslontelecom.esk.backend.api.DAO.PrilogaDAO;
import zaslontelecom.esk.backend.api.DAO.SubjektDAO;
import zaslontelecom.esk.backend.api.Model.*;
import zaslontelecom.esk.backend.api.Utils.Utils;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CertifikatPrilogaClanService extends BaseService{

    @Autowired
    private CertifikatPrilogaClanDAO dao;

    @Autowired
    private PrilogaDAO prilogaDao;

    @Autowired
    private PrilogaClanDAO prilogaClanDao;

    @Autowired
    private SubjektService subjektService;

    @Autowired
    EntityManager em;

    public Iterable<CertifikatPrilogaClan> list(){
        return dao.findAll();
    }

    public Optional<CertifikatPrilogaClan> get(long id){
        return dao.findById(id);
    }

    public CertifikatPrilogaClan insert(CertifikatPrilogaClan item){
        item.setSpremenil(GetCurrentUserId());
        return dao.save(item);
    }

    public CertifikatPrilogaClan update(CertifikatPrilogaClan item){
        boolean existing = dao.existsById(item.getId());
        if (!existing)
            throw new IllegalArgumentException("ITEM_NOT_EXISTS");
        item.setSpremenil(GetCurrentUserId());
        dao.save(item);

        return item;
    }

    public void delete(long id){
        CertifikatPrilogaClan item = get(id).get();
        item.setSpremenil(GetCurrentUserId());
        update(item);
        dao.deleteById(id);
    }

    public void saveAll(long idCertifikat, Collection<CertifikatPrilogaClan> certifikatPrilogaClanCollection) {
        if (certifikatPrilogaClanCollection == null || certifikatPrilogaClanCollection.isEmpty()){
            return;
        }

        for (CertifikatPrilogaClan certifikatPrilogaClan : certifikatPrilogaClanCollection)
        {
            certifikatPrilogaClan.setIdCertifikat(idCertifikat);
            Priloga priloga = certifikatPrilogaClan.getPriloga();
            certifikatPrilogaClan.setSpremenil(GetCurrentUserId());
            prilogaDao.save(priloga); // SAVE Priloga

            // reimport data HACK
            boolean reimport = true;

            for (PrilogaClan prilogaClan : priloga.getPrilogaClan())
            {
                if (prilogaClan.getId() != 0)
                {
                    reimport = false;
                }
                Subjekt clan = this.subjektService.getAndSaveBeforeIfNecessary(prilogaClan.getClan());
                prilogaClan.setIdSubjekt(clan.getId());
                prilogaClan.setIdPriloga(priloga.getId());
            }
            if (reimport) {
                this.deleteAllPrilogaClan(priloga);
            }
            priloga.setSpremenil(GetCurrentUserId());
            prilogaClanDao.saveAll(priloga.getPrilogaClan()); // SAVE PrilogaClan
            certifikatPrilogaClan.setIdPriloga(priloga.getId());
            certifikatPrilogaClan.setSpremenil(GetCurrentUserId());
        }
        dao.saveAll(certifikatPrilogaClanCollection);// SAVE CertifikatPrilogaClan
    }

    public void deleteAllByIdCertifikat(long id) {
        Iterable<CertifikatPrilogaClan> certifikatPrilogaClanList = dao.getAllByIdCertifikat(id);
        for (CertifikatPrilogaClan certifikatPrilogaClanItem : certifikatPrilogaClanList) {
            Priloga priloga = certifikatPrilogaClanItem.getPriloga();
            prilogaClanDao.deleteAllByIdPriloga(priloga.getId());
        }
        dao.deleteAllByIdCertifikat(id);
    }

    public Page<CertifikatPrilogaClan> getLastByIdCertifikat(long id) {
        Pageable pageSortById = PageRequest.of(0, 1, Sort.by("id").descending());
        return dao.findAllByIdCertifikat(id, pageSortById);
    }

    public Optional<CertifikatPrilogaClan> getLastOptionalByIdCertifikat(long id) {
        Optional<CertifikatPrilogaClan> data = Optional.empty();

        Page<CertifikatPrilogaClan> result = getLastByIdCertifikat(id);
        if (result.hasContent()){
            data = Optional.of(result.getContent().get(0));
        }

        return data;
    }

    public List<Object> findByParams(PagedQuery request, String org) {
        StoredProcedureQuery sp = em.createStoredProcedureQuery("ESK_DATA.ESK_MGMT.getPrilogaClanList");
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

    private void deleteAllPrilogaClan(Priloga priloga) {

        if (priloga.getId() == 0)
        {
            return;
        }

        StoredProcedureQuery sp = em.createStoredProcedureQuery("ESK_DATA.ESK_MGMT.deleteAttachmentMembers");
        // register parameters
        sp.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
        sp.setParameter(1,priloga.getId());
        sp.registerStoredProcedureParameter(2, Long.class, ParameterMode.IN);
        sp.setParameter(2, GetCurrentUserId());
        sp.execute();
    }

}