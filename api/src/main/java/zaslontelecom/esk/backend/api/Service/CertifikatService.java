package zaslontelecom.esk.backend.api.Service;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.Controller.Requests.CertifikatSearchCriteriaRequest;
import zaslontelecom.esk.backend.api.Controller.Response.RefreshResponse;
import zaslontelecom.esk.backend.api.DAO.CertifikatDAO;
import zaslontelecom.esk.backend.api.Model.*;
import zaslontelecom.esk.backend.api.Utils.*;
import zaslontelecom.esk.backend.api.Utils.Imis.ImisDms;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.security.auth.login.LoginException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.text.Collator;
import java.util.*;

@Service
public class CertifikatService extends BaseService {

    @Autowired
    private CertifikatDAO dao;

    @Autowired
    private CertifikatPrilogaProizvodService certifikatPrilogaProizvodService;

    @Autowired
    CertifikatPrilogaClanService certifikatPrilogaClanService;

    @Autowired
    CertifikatProizvodService certifikatProizvodService;

    @Autowired
    CertifikatDejavnostService certifikatDejavnostService;


    @Autowired
    private SubjektService subjektService;

    @Autowired
    private PrilogaService prilogaService;

    @Autowired
    private DejavnostService dejavnostService;

    @Autowired
    private ShemaService shemaService;

    @Autowired
    private ZakonskaPodlagaService zakonskaPodlagaService;

    @Autowired
    private ZascitenProizvodService zascitenProizvodService;

    @Autowired
    private CertifikatExtService extService;

    @Autowired
    private ProizvodService proizvodService;

    @Autowired
    EntityManager em;

    @Autowired
    Settings settings;

    @Autowired
    ImisDms imisService;

    @Autowired
    private KoService koService;

    public Optional<Certifikat> get(long id){
        return dao.findById(id);
    }

    public Optional<Certifikat> getByStevilka(String stevilka) {
        return dao.getByStevilka(stevilka);
    }

    public boolean checkIfCertificateAlreadyExists(Long id, Long idImetnik, Long idZascitenProizvod){
        return dao.checkIfAlreadyExists(idImetnik, idZascitenProizvod, id) > 0;
    }

    public boolean checkIfCertificateAlreadyExists2(Long id, Long idImetnik, Long idZascitenProizvod){
        return dao.checkIfAlreadyExists2(idImetnik, idZascitenProizvod, id) > 0;
    }

    public Certifikat save(Certifikat certifikat, boolean fullValidation, UserWithPermissions user) throws HandledException, ScanException, PolicyException {
        // VALIDATE CERTIFICATE
        validateCertifikat(certifikat, fullValidation);

        // get existing or create new Person. THIS IS REQUEST FROM OUR CLIENT!!!!
        certifikat.setImetnik(subjektService.getAndSaveBeforeIfNecessary(certifikat.getImetnik()));
        certifikat.setSpremenil(user.getId());

        // SECOND VALIDATION - after imetnik is set
        // check for constraint id_imetnik, id_zasciten_proizvod
        if (certifikat.getIdParent() == null){
            String findDuplicates  = this.settings.getFindDuplicates();

            if (findDuplicates.equals("WITH_VALIDATION_DATE")) {
                    if (checkIfCertificateAlreadyExists(certifikat.getId(), certifikat.getImetnik().getId(), certifikat.getZascitenProizvod().getId())) {
                    throw new HandledException("Za danega nosilca in proizvod, že obstaja veljaven certifikat. Postopka ne morete nadaljevati.");
                }
            }

            if (findDuplicates.equals("IGNORE_VALIDATION_DATE")) {
                if (checkIfCertificateAlreadyExists2(certifikat.getId(), certifikat.getImetnik().getId(), certifikat.getZascitenProizvod().getId())) {
                    throw new HandledException("Za danega nosilca in proizvod, že obstaja veljaven certifikat. Postopka ne morete nadaljevati.");
                }
            }
        }

        // FOR NOW ALL DATA IS RESET IN FRONTEND
        // close old certificates if current is finished
        if (certifikat.getStatus().equals("Veljaven") && certifikat.getIdParent() != null){
            // THIRD VALIDATION - datIzdaje MUST exists in this case
            if (certifikat.getDatIzdaje() == null) {
                throw new HandledException("Datum izdaje certifikata je obvezen podatek. Datum veljavnosti starega certifikata se mora nastaviti na dan izdaje novega.");
            }
            // cancel old certificate
            closeOldCertificate(certifikat);
        }

        // when insert new record we need to set time and user
        if (certifikat.getId() == 0) {
            certifikat.setUporabnik(user);
            certifikat.setDatVnosa(Utils.getCurrentSqlDate());
        }

        Collection<CertifikatPrilogaProizvod> cpp = certifikat.getCertifikatPrilogaProizvod();
        Collection<CertifikatPrilogaClan> cpc = certifikat.getCertifikatPrilogaClan();
        // first save certificate
        dao.save(certifikat);

        // save product list
        saveProizvodList(certifikat);

        // save product list
        saveDejavnostList(certifikat);

        certifikat.setCertifikatPrilogaProizvod(cpp);
        certifikat.setCertifikatPrilogaClan(cpc);
        // save product attachment
        if (hasProductAttachment(certifikat)){
            certifikatPrilogaProizvodService.save(certifikat.getId(), cpp);
            // set status to "Neveljaven" is called in closeOldCertificate method before that action.
        }
        // save members attachment, if group certificate
        if (certifikat.getTip().equals("S")){
            certifikatPrilogaClanService.saveAll(certifikat.getId(), cpc);
            // set status to "Neveljaven" is called in closeOldCertificate method before that action.
        }

        // return certificate
        return certifikat;
    }

    private void closeOldCertificate(Certifikat certifikat) throws HandledException {
        Optional<Certifikat> found = get(certifikat.getIdParent());
        if (!found.isPresent()){
            certifikat.setIdParent(null); // če ga ne najdemo, pomeni  da ga je nekdo izbrisal in smo s tem izgubili sled za njim
            return;
        }

        Certifikat old = found.get();
        old.setDatVelj(certifikat.getDatIzdaje());
        old.setSpremenil(GetCurrentUserId());
        dao.save(old);

        updateStatusCascade(old.getId(), "Neveljaven");
    }

    public Certifikat update(Certifikat item){
        boolean existing = dao.existsById(item.getId());
        if (!existing)
            throw new IllegalArgumentException("ITEM_NOT_EXISTS");

        item.setSpremenil(GetCurrentUserId());
        dao.save(item);

        return item;
    }

    public void delete(long id) throws HandledException {
        Optional<Certifikat> found = get(id);

        if (!found.isPresent()){
            throw new HandledException("Certifikat ne obstaja!");
        }

        Certifikat cert = found.get();

        if (!cert.getStatus().equals("Vnos")){
            throw new HandledException("Brišete lahko samo certifikate v statusu Vnos.");
        }

        cert.setSpremenil(GetCurrentUserId());
        dao.save(cert);

        certifikatPrilogaClanService.deleteAllByIdCertifikat(id);
        certifikatPrilogaProizvodService.deleteAllByIdCertifikat(id);
        certifikatProizvodService.deleteAllByIdCertifikat(id);
        dao.deleteById(id);
    }

    // Method return certificates against if user is authenticated || user is a part of organisation (not public user)
    public Page<Certifikat> findByQuery(int pageNumber, int resultPerPage, String query, boolean isAuthenticated, String org) {
        Pageable pageSortById = PageRequest.of(pageNumber, resultPerPage == 0 ? 100000 : resultPerPage, Sort.by("id").descending());
        List<String> statusList = new ArrayList<>();
        statusList.add("Veljaven");
        if (isAuthenticated){
            statusList.add("Vnos");
        }

        if (!Utils.isNullOrEmpty(query)){
            return dao.findAllCertificatesWithPagination(query.toLowerCase(), statusList, Utils.getValueOrDefault(org), pageSortById);
        } else {
            return Utils.isNullOrEmpty(org) ? dao.findAllByStatusIn(statusList, pageSortById) : dao.findAllByStatusInAndOrganizacija(statusList, org, pageSortById);
        }
    }

    public List<Object> findByParams(CertifikatSearchCriteriaRequest request, String org) {
        StoredProcedureQuery sp = em.createStoredProcedureQuery("ESK_DATA.ESK_MGMT.getCertificateList");
        // register parameters
        sp.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
        sp.setParameter(1, null);
        sp.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
        sp.setParameter(2, request.getStevilka());
        sp.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
        sp.setParameter(3, Utils.toYYYYMMDDD(request.getDatIzdajeOd()));
        sp.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
        sp.setParameter(4, Utils.toYYYYMMDDD(request.getDatKontroleOd()));
        sp.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);
        sp.setParameter(5, Utils.toYYYYMMDDD(request.getDatVeljOd()));
        sp.registerStoredProcedureParameter(6, String.class, ParameterMode.IN);
        sp.setParameter(6,  Utils.toYYYYMMDDD(request.getDatVnosaOd()));
        sp.registerStoredProcedureParameter(7, String.class, ParameterMode.IN);
        sp.setParameter(7, Utils.toYYYYMMDDD(request.getDatIzdajeDo()));
        sp.registerStoredProcedureParameter(8, String.class, ParameterMode.IN);
        sp.setParameter(8, Utils.toYYYYMMDDD(request.getDatKontroleDo()));
        sp.registerStoredProcedureParameter(9, String.class, ParameterMode.IN);
        sp.setParameter(9, Utils.toYYYYMMDDD(request.getDatVeljDo()));
        sp.registerStoredProcedureParameter(10, String.class, ParameterMode.IN);
        sp.setParameter(10, Utils.toYYYYMMDDD(request.getDatVnosaDo()));
        sp.registerStoredProcedureParameter(11, String.class, ParameterMode.IN);
        sp.setParameter(11, request.getKontrolor());
        sp.registerStoredProcedureParameter(12, String.class, ParameterMode.IN);
        sp.setParameter(12, null); // User is not important in this query, we use organisation instead.
        sp.registerStoredProcedureParameter(13, String.class, ParameterMode.IN);
        sp.setParameter(13, request.getStatus());
        sp.registerStoredProcedureParameter(14, String.class, ParameterMode.IN);
        sp.setParameter(14, request.getDejavnost() == null || Utils.isNullOrEmpty(request.getDejavnost().getNaziv()) ? null : request.getDejavnost().getNaziv());
        sp.registerStoredProcedureParameter(15, String.class, ParameterMode.IN);
        sp.setParameter(15, request.getZascitenProizvod() == null || Utils.isNullOrEmpty(request.getZascitenProizvod().getNaziv()) ? null : request.getZascitenProizvod().getNaziv());
        sp.registerStoredProcedureParameter(16, String.class, ParameterMode.IN);
        sp.setParameter(16, Utils.getCommaSeparatedProductNames(request.getProizvod()));
        sp.registerStoredProcedureParameter(17, String.class, ParameterMode.IN);
        sp.setParameter(17, request.getShema() == null || Utils.isNullOrEmpty(request.getShema().getNaziv()) ? null : request.getShema().getNaziv());
        sp.registerStoredProcedureParameter(18, String.class, ParameterMode.IN);
        sp.setParameter(18, request.getSubKmgmid());
        sp.registerStoredProcedureParameter(19, String.class, ParameterMode.IN);
        sp.setParameter(19, request.getSubNaziv());
        sp.registerStoredProcedureParameter(20, String.class, ParameterMode.IN);
        sp.setParameter(20, request.getSubDavcna());
        sp.registerStoredProcedureParameter(21, String.class, ParameterMode.IN);
        sp.setParameter(21, request.getSubMaticna());
        sp.registerStoredProcedureParameter(22, String.class, ParameterMode.IN);
        sp.setParameter(22, request.getSubIdPoste());
        sp.registerStoredProcedureParameter(23, String.class, ParameterMode.IN);
        sp.setParameter(23, request.getSubPosta());
        sp.registerStoredProcedureParameter(24, String.class, ParameterMode.IN);
        sp.setParameter(24, request.getSubObcina());
        sp.registerStoredProcedureParameter(25, String.class, ParameterMode.IN);
        sp.setParameter(25, org);
        sp.registerStoredProcedureParameter(26, CertView.class, ParameterMode.REF_CURSOR);
        sp.execute();
        return sp.getResultList();
    }

    public void validateCertifikat(Certifikat certifikat, boolean fullValidation) throws HandledException {
        if (fullValidation && certifikat.getDatIzdaje() == null){
            throw new HandledException("Datum izdaje mora biti izpolnjen.");
        }

        if (fullValidation && certifikat.getDatVelj() == null){
            throw new HandledException("Datum veljavnosti mora biti izpolnjen.");
        }

        if (!(certifikat.getOrganizacija().equals("KONCERT") || certifikat.getOrganizacija().equals("IKC") || certifikat.getOrganizacija().equals("BUREAU"))){
            throw new HandledException("Neveljaven certifikacijski organ.");
        }

        if (fullValidation && Utils.dateComparer(certifikat.getDatIzdaje(), Utils.DateCompareEnum.isHigher, certifikat.getDatVelj())){
            throw new HandledException("Datum izdaje mora biti manjši ali enak od datuma veljavnosti.");
        }

        if (fullValidation && Utils.dateComparer(certifikat.getDatIzdaje(), Utils.DateCompareEnum.isHigher, Utils.sqlToday())){
            throw new HandledException("Datum izdaje certifikata mora biti enak ali manjši od datuma vnosa certifikata.");
        }

        if (fullValidation && Utils.dateComparer(certifikat.getDatVelj(), Utils.DateCompareEnum.isLowerOrEquals, Utils.sqlToday())){
            throw new HandledException("Datum veljavnosti certifikata mora biti večji od datuma vnosa certifikata.");
        }

        if (Utils.notExistsDejavnost(certifikat.getDejavnost())){
            throw new HandledException("Dejavnost mora biti izpolnjena.");
        }

        if (Utils.notExistsSubjekt(certifikat.getImetnik())){
            throw new HandledException("Nosilec certifikata ne obstaja.");
        }

        if (Utils.isNullOrEmpty(certifikat.getStevilka())){
            throw new HandledException("Številka certifikata ni določena.");
        }

        // If insert then we need to check certificate number if already exists.
        if (certifikat.getIdParent() == null) {
            List<String> list = new ArrayList<>();
            list.add("Veljaven");
            list.add("Vnos");
            int found = dao.countByStevilkaAndStatusInAndIdNot(certifikat.getStevilka(), list, certifikat.getId());
            if (found > 0) {
                throw new HandledException("Certifikat s to številko že obstaja!");
            }
        }

        if (Utils.notExistsZascitenProizvod(certifikat.getZascitenProizvod())){
            throw new HandledException("Proizvod ni določen.");
        }else{
            long length = certifikat.getCertifikatProizvod() == null ? 0 : certifikat.getCertifikatProizvod().spliterator().getExactSizeIfKnown();
            long idZascitenProizvod = certifikat.getZascitenProizvod().getId();

            long countByZascitenProizvod = proizvodService.countByIdZascitenProizvod(idZascitenProizvod);

            if (length == 0 && countByZascitenProizvod > 0) {
                throw new HandledException("Proizvod ni določen (SK - zbiranje podatkov).");
            }
        }

        if (fullValidation && Utils.notExistsPrilogaProizvod(certifikat.getCertifikatPrilogaProizvod())){
            throw new HandledException("Priloga proizvoda ne obstaja!");
        }

        if (Utils.isNullOrEmpty(certifikat.getTip())){
            throw new HandledException("Tip certifikata iz neznanih razlogov ni določen.");
        }

        if (!(certifikat.getTip().equals("I") || certifikat.getTip().equals("S"))){
            throw new HandledException("Tip certifikata mora biti S ali I.");
        }

        if (Utils.existsPrilogaProizvod(certifikat.getCertifikatPrilogaProizvod())) {
            Priloga prilogaProizvodov = certifikat.getCertifikatPrilogaProizvod().stream().findFirst().get().getPriloga();
            if (Utils.hasData(prilogaProizvodov)) {
                validatePriloga(certifikat, prilogaProizvodov, 0, fullValidation);
            } else {
                certifikat.setCertifikatPrilogaProizvod(null); // this hack do the trick, if empty attachment is posted
            }
        }

        if (certifikat.getTip().equals("S")) {
            if (Utils.notExistsPrilogaClan(certifikat.getCertifikatPrilogaClan())) {
                return;
            }

            Priloga prilogaClanov = certifikat.getCertifikatPrilogaClan().stream().findFirst().get().getPriloga();
            validatePriloga(certifikat, prilogaClanov, 1, fullValidation);

            Collection<PrilogaClan> claniCollection = prilogaClanov.getPrilogaClan();

            if (claniCollection == null || claniCollection.spliterator().getExactSizeIfKnown() == 0) {
                throw new HandledException("Na prilogi članov je potrebno navesti najmanj enega člana.");
            }
        }
    }

    public void validatePriloga(Certifikat certifikat, Priloga priloga, int type, boolean fullValidation) throws HandledException {
        if (fullValidation) {
            if (priloga.getDatIzdaje() == null) {
                throw new HandledException("Datum izdaje priloge mora biti izpolnjen.");
            }

            if (priloga.getDatVelj() == null) {
                throw new HandledException("Datum veljavnosti priloge mora biti izpolnjen.");
            }

            if (Utils.dateComparer(priloga.getDatIzdaje(), Utils.DateCompareEnum.isHigher, priloga.getDatVelj())) {
                throw new HandledException("Datum izdaje priloge mora biti manjši od datuma veljavnosti.");
            }

            if (Utils.dateComparer(priloga.getDatVelj(), Utils.DateCompareEnum.isHigher, certifikat.getDatVelj())) {
                throw new HandledException("Datum veljavnosti priloge mora biti manjši ali enak od datuma veljavnosti certifikata.");
            }

            if (Utils.dateComparer(priloga.getDatIzdaje(), Utils.DateCompareEnum.isLower, certifikat.getDatIzdaje())) {
                throw new HandledException("Datum izdaje priloge mora biti večji ali enak od datuma izdaje certifikata.");
            }
        }

        if (Utils.isNullOrEmpty(priloga.getStevilka())){
            throw new HandledException("Številka priloge ni določena.");
        }

        if (type == 0 && Utils.isNullOrEmpty(priloga.getVsebina())){
            throw new HandledException("Priloga proizvodov ne sme biti prazna.");
        }

        if (type == 1 &&  (priloga.getPrilogaClan() == null || priloga.getPrilogaClan().size() == 0)){
            throw new HandledException("Priloga članov ne sme biti prazna.");
        }
    }

    public Iterable<CertifikatProizvod> getProizvodiIdList(long id) {
        return this.certifikatProizvodService.findAllByIdCertifikat(id);
    }

    public Iterable<Proizvod> getProizvodi(long id) {
        return this.proizvodService.getByIdCertifikat(id);
    }

    public void saveProizvodList(Certifikat cert) {
        deleteAllCertifikatProizvod(cert.getId());
        if (cert.getCertifikatProizvod() == null || cert.getCertifikatProizvod().spliterator().getExactSizeIfKnown() == 0){
            return;
        }

        for (CertifikatProizvod el: cert.getCertifikatProizvod()) {
            el.setIdCertifikat(cert.getId());
            el.setSpremenil(GetCurrentUserId());
            this.certifikatProizvodService.save(el);
        }
    }

    public void saveDejavnostList(Certifikat cert) {
        deleteAllCertifikatActivities(cert.getId());

        for (Dejavnost dej: cert.getDejavnosti()) {
            CertifikatDejavnost cd = new CertifikatDejavnost();
            cd.setIdCertifikat(cert.getId());
            cd.setIdDejavnost(dej.getId());
            cd.setSpremenil(GetCurrentUserId());
            this.certifikatDejavnostService.save(cd);
        }
    }

    public boolean hasProductAttachment(Certifikat certifikat){
        if (certifikat.getCertifikatPrilogaProizvod() == null){
            return false;
        }
        Priloga prilogaProizvodov = certifikat.getCertifikatPrilogaProizvod().stream().findFirst().get().getPriloga();
        return Utils.hasData(prilogaProizvodov);
    }

    public void finish(Certifikat cert, UserWithPermissions user) throws HandledException, IOException, LoginException, PolicyException, ScanException {
        logger.info("[FINISH]: Saving certificate...");
        save(cert, true, user);
        logger.info("[FINISH]: Execute takeover...");
        executeTakeover(cert.getId());
        logger.info("[FINISH]: Update status cascade...");
        updateStatusCascade(cert.getId(), "Veljaven");
        logger.info("[FINISH]: Update certificate attachments...");
        updateCertificateAttachments(cert.getId());
        logger.info("[FINISH]: Generate PDF document...");
        generatePdfDocument(cert);
        logger.info("[FINISH]: Certificate finished...");
    }

    public void generatePdfDocument(Certifikat cert) throws HandledException, IOException, LoginException, PolicyException, ScanException {
        String folder = settings.getPdfTemplatePath();
        String htmlFile = getPdfName(cert);
        List<String> files = new ArrayList<String>();

        double marginCert = getBottomMarginCert(cert.getOrganizacija());
        Optional<Ko> ko = this.koService.getBySif(cert.getOrganizacija());

        if (!ko.isPresent()){
            throw new HandledException("Neveljavna organizacija " + cert.getOrganizacija() + "!");
        }
        Ko org = ko.get();

        boolean akreditiran = !((cert.getZascitenProizvod().getShema().getNaziv().toLowerCase().equals("izbrana kakovost") || cert.getZascitenProizvod().getShema().getNaziv().toLowerCase().equals("višja kakovost")));
        String certString = Utils.readFile(folder + htmlFile + ".html");

        if (certString == null){
            throw new HandledException("Ne morem prebrati HTML predloge!");
        }

        certString = getPdfString(cert, settings, certString, akreditiran, org.getPodpisnik(), org.getPodpisnikVloga());

        // adding data from previous certificate
        boolean certPrej = cert.getIdParent() != null;
        if (certPrej) {
            Optional<Certifikat> optCert = dao.findById(cert.getIdParent());

            if (!optCert.isPresent()){
                throw new HandledException("Predhodni certifikat ne obstaja!");
            }

            String prejString = "<p><div>Ta certifikat zamenjuje predhodni certifikat št.: <strong class=\"green\">[STEVILKA_PREJ]</strong>.</div></p>";
            prejString = prejString.replace("[STEVILKA_PREJ]", optCert.get().getStevilka());
            prejString = prejString.replace("[DAT_IZDAJE_PREJ]",  Utils.toDateString(optCert.get().getDatIzdaje(), "dd. MM. yyyy"));
            certString = certString.replace("[CERTIFIKAT_PREJ]", prejString);

            marginCert += 1.5;
        }else{
            certString = certString.replace("[CERTIFIKAT_PREJ]", "");
        }

        if (Utils.existsPrilogaProizvod(cert.getCertifikatPrilogaProizvod()) || Utils.existsPrilogaClan(cert.getCertifikatPrilogaClan())){
            String hasAtt = "<p class=\"center\" style=\"margin-top:100px\"><i>Priloga je sestavni del tega certifikata</i></p>";
            certString = certString.replace("[IMA_PRILOGE]", hasAtt);
        } else {
            certString = certString.replace("[IMA_PRILOGE]", "");
        }

        certString = certString.replace("[MARGIN-BOTTOM-CERTIFIKAT]", String.valueOf(marginCert));
        certString = Utils.parseToXHtml(certString);
        String certFile = (settings.getWorkingPath() + htmlFile + "-" + cert.getStevilka() + "-" + cert.getId() +  "-CERT.pdf").replaceAll("[^a-zA-Z0-9\\._]+", "_");
        PdfTools.generatePDF(certString, certFile, settings);
        files.add(certFile);


        // is grouped cert
        if (cert.getTip().equals("S")) {
            double marginAttachment = getBootomMarginAttachments(cert.getOrganizacija());
            String clanString = Utils.readFile(folder + htmlFile + "-CLAN.html");

            if (clanString == null){
                throw new HandledException("Ne morem prebrati HTML predloge članov!");
            }
            String prevAttachString = this.getPrevAttachmentString(cert, 1);
            clanString = clanString.replace("[CLANI]", getClaniAsStringLines(cert.getCertifikatPrilogaClan()));
            Priloga priloga = cert.getCertifikatPrilogaClan().stream().findFirst().get().getPriloga();
            clanString = clanString.replace("[STEVILKA_CLAN]", priloga.getStevilka());
            clanString = clanString.replace("[DAT_VELJ_CLAN]", Utils.toDateString(priloga.getDatVelj(), "d. M. yyyy"));
            clanString = clanString.replace("[DAT_IZDAJE_CLAN]", Utils.toDateString(priloga.getDatIzdaje(), "d. M. yyyy"));
            clanString = clanString.replace("[PRILOGA_CLAN_PREJ]", prevAttachString);

            if (!Utils.isNullOrEmpty(prevAttachString)){
                marginAttachment += 1.5;
            }
            clanString = clanString.replace("[MARGIN-BOTTOM-PRILOGA]", String.valueOf(marginAttachment));

            clanString = getPdfString(cert, settings, clanString, akreditiran, org.getPodpisnik(), org.getPodpisnikVloga());

            String clanFile = (settings.getWorkingPath() + htmlFile + "-" + cert.getStevilka() + "-" + cert.getId() +  "-CLAN.pdf").replaceAll("[^a-zA-Z0-9\\._]+", "_");
            clanString = Utils.parseToXHtml(clanString);
            PdfTools.generatePDF(clanString, clanFile, settings);
            files.add(clanFile);
        }

        // has product attachments
        if (Utils.existsPrilogaProizvod(cert.getCertifikatPrilogaProizvod())){
            double marginAttachment = getBootomMarginAttachments(cert.getOrganizacija());
            String proizvodString = Utils.readFile(folder + htmlFile + "-PROIZVOD.html");
            if (proizvodString == null){
                throw new HandledException("Ne morem prebrati HTML predloge proizvodov podrobno!");
            }
            Priloga priloga = cert.getCertifikatPrilogaProizvod().stream().findFirst().get().getPriloga();
            proizvodString = proizvodString.replace("[STEVILKA_PROIZVOD]", priloga.getStevilka());
            proizvodString = proizvodString.replace("[DAT_VELJ_PROIZVOD]", Utils.toDateString(priloga.getDatVelj(), "d. M. yyyy"));
            proizvodString = proizvodString.replace("[DAT_IZDAJE_PROIZVOD]", Utils.toDateString(priloga.getDatIzdaje(), "d. M. yyyy"));
            proizvodString = proizvodString.replace("[PROIZVOD_PODROBNO]", priloga.getVsebina());

            // get previous attachments
            String prevAttachString = this.getPrevAttachmentString(cert, 0);
            proizvodString = proizvodString.replace("[PRILOGA_PROIZVOD_PREJ]", prevAttachString);
            proizvodString = getPdfString(cert, settings, proizvodString, akreditiran, org.getPodpisnik(), org.getPodpisnikVloga());

            if (!Utils.isNullOrEmpty(prevAttachString)){
                marginAttachment += 1.5;
            }
            proizvodString = proizvodString.replace("[MARGIN-BOTTOM-PRILOGA]", String.valueOf(marginAttachment));
            proizvodString = Utils.parseToXHtml(proizvodString);
            String proizvodFile = (settings.getWorkingPath() + htmlFile + "-" + cert.getStevilka() + "-" + cert.getId() +  "-PROIZVOD.pdf").replaceAll("[^a-zA-Z0-9\\._]+", "_");
            PdfTools.generatePDF(proizvodString, proizvodFile, settings);
            files.add(proizvodFile);
        }

        String pdfFile = getPdfFilename(cert);
        PdfTools.merge(files, pdfFile);


        String idImis = String.valueOf(cert.getId());
        String blob = null;
        if (settings.getImisEnabled()) {
            idImis = this.imisService.documentStore(pdfFile);
        }else {
            blob = Utils.BinToHex(Utils.readFileAsByteArray(pdfFile));
        }

        Optional<CertifikatExt> foundExt = extService.getByIdCertifikat(cert.getId());
        CertifikatExt ext = foundExt.isPresent() ? foundExt.get() :  new CertifikatExt();
        ext.setIdCertifikat(cert.getId());
        ext.setIdImis(idImis);
        ext.setPdf(blob);
        ext.setSpremenil(cert.getSpremenil());
        this.extService.save(ext);

        // delete file from local folder, when file is uploaded correctly
        Utils.deleteFile(pdfFile);
    }

    private double getBottomMarginCert(String organizacija) {
        if (organizacija.equals("IKC")){
            return this.settings.getBottomMarginCert(organizacija, 7);
        }

        if (organizacija.equals("KONCERT")){
            return this.settings.getBottomMarginCert(organizacija, 7);
        }

        if (organizacija.equals("BUREAU")){
            return this.settings.getBottomMarginCert(organizacija, 7);
        }

        return 0;
    }

    private double getBootomMarginAttachments(String organizacija) {
        if (organizacija.equals("IKC")){
            return this.settings.getBottomMarginCert(organizacija, 9);
        }

        if (organizacija.equals("KONCERT")){
            return this.settings.getBottomMarginCert(organizacija, 7.5);
        }

        if (organizacija.equals("BUREAU")){
            return this.settings.getBottomMarginCert(organizacija, 7.0);
        }

        return 0;
    }

    private String getPrevAttachmentString(Certifikat cert, int type) {

        Long prev = this.getPrevAttachment(cert.getId(), type);

        if (prev == 0){
            return "";
        }

        Priloga prevAtt;
        Certifikat prevCert;

        String prejString;
        if (type == 0) {
            Optional<CertifikatPrilogaProizvod> cpp = certifikatPrilogaProizvodService.get(prev);
            if (cpp.get().getIdCertifikat() == cert.getId()){
                prevCert = cert;
            }else{
                prevCert = this.get(cpp.get().getIdCertifikat()).get();
            }
            prevAtt = this.prilogaService.get(cpp.get().getIdPriloga()).get();
            prejString = "<div>Ta priloga zamenjuje predhodno prilogo seznam certificiranih proizvodov št.: <strong class=\"green\">[STEVILKA_PREJ]</strong>, k certifikatu <strong class=\"green\">[CERTIFIKAT_PREJ]</strong>.</div>";
        } else {
            Optional<CertifikatPrilogaClan> cpp = certifikatPrilogaClanService.get(prev);
            if (cpp.get().getIdCertifikat() == cert.getId()){
                prevCert = cert;
            }else{
                prevCert = this.get(cpp.get().getIdCertifikat()).get();
            }
            prevAtt = this.prilogaService.get(cpp.get().getIdPriloga()).get();
            prejString = "<div>Ta priloga zamenjuje predhodno prilogo seznam članov št.: <strong class=\"green\">[STEVILKA_PREJ]</strong>, k certifikatu <strong class=\"green\">[CERTIFIKAT_PREJ]</strong>.</div>";
        }

        prejString = prejString.replace("[STEVILKA_PREJ]", prevAtt.getStevilka());
        prejString = prejString.replace("[CERTIFIKAT_PREJ]", prevCert.getStevilka());
        prejString = prejString.replace("[DAT_IZDAJE_PREJ]", Utils.toDateString(prevAtt.getDatIzdaje(), "dd. MM. yyyy"));

        return prejString;
    }

    public String getPdfFilename(Certifikat cert) throws HandledException {
        String folder = settings.getDownloadPath();
        String htmlFile = getPdfName(cert);

        return (folder + htmlFile + "-" + cert.getStevilka() + "-" + cert.getId() +  ".pdf").replaceAll("[^a-zA-Z0-9\\._]+", "_");
    }

    private String getPdfName(Certifikat cert) throws HandledException {
        return cert.getOrganizacija();
    }

    private String getClaniAsStringLines(Collection<CertifikatPrilogaClan> certifikatPrilogaClan) {
        Priloga priloga = certifikatPrilogaClan.stream().findFirst().get().getPriloga();
        String claniString = "";
        int index = 1;

        List<String> lines= new ArrayList<>();
        Collection<PrilogaClan> prilogaList = priloga.getPrilogaClan();
        for (PrilogaClan prilogaClan: prilogaList) {
            Subjekt clan = prilogaClan.getClan();
            String line = "[No]. " + (clan.getNaziv() == null ? clan.getPriimek() + " " + clan.getIme() : clan.getNaziv()) + ", " + clan.getNaslov() + "<br/>";
            lines.add(line);
        }

        Locale locale = new Locale("sl","SI");
        Collator colSi = Collator.getInstance(locale);
        colSi.setStrength(Collator.PRIMARY);
        Collections.sort(lines, colSi);

        for (String line: lines) {
            claniString += line.replace("[No]", String.valueOf(index));
            index++;
        }

        return claniString;
    }

    private String getPdfString(Certifikat cert, Settings settings, String certString, boolean zasciteniProizvodi, String podpisnik, String podpisnikVloga) throws IOException, HandledException, PolicyException, ScanException {
        String org = getPdfName(cert);

        if (certString.contains("[BACKGROUND]")) {
            String logoPath = settings.getPdfTemplatePath() + "images/" + org  + (zasciteniProizvodi ? "-akred" : "") + "-background.png";
            certString = certString.replace("[BACKGROUND]", Utils.image2Base64(logoPath));
        }

        String imetnik = cert.getImetnik().getNaziv() == null ? cert.getImetnik().getIme() + " " + cert.getImetnik().getPriimek() : cert.getImetnik().getNaziv();
        certString = certString.replace("[IMETNIK]", imetnik);
        String imetnikNaslov = cert.getImetnik().getNaslov();
        certString = certString.replace("[IMETNIK_NASLOV]", imetnikNaslov);

        certString = certString.replace("[ZASCITEN_PROIZVOD]", zasciteniProizvodi ? "" : getZascitenProizvodAsHtmlString(cert.getZascitenProizvod().getNaziv()));
        certString = certString.replace("[STEVILKA]", cert.getStevilka());
        certString = certString.replace("[DAT_VELJ]", Utils.toDateString(cert.getDatVelj(), "d. M. yyyy"));
        certString = certString.replace("[DAT_IZDAJE]", Utils.toDateString(cert.getDatIzdaje(), "d. M. yyyy"));
        String zp = getPravnaPodlaga(cert.getId());
        certString = certString.replace("[PRAVNA_PODLAGA]", Utils.isNullOrEmpty(zp)? "" : zp);
        certString = certString.replace("[DEJAVNOST]", !zasciteniProizvodi ? getDejavnostiAsHtmlString(cert) : "");
        certString = certString.replace("[PODPISNIK]", Utils.isNullOrEmpty(podpisnik) ? "" : podpisnik);
        certString = certString.replace("[PODPISNIK_VLOGA]", Utils.isNullOrEmpty(podpisnikVloga) ? "" : podpisnikVloga);
        String fontsize = "35";
        if (imetnik.length() >= 80 && imetnik.length() < 120){
            fontsize = "28";
        }
        if (imetnik.length() >= 120){
            fontsize = "21";
        }
        certString = certString.replace("[IMETNIK_FONT_SIZE]", fontsize);


        return certString;
    }

    private String getZascitenProizvodAsHtmlString(String naziv) {
        return "<p class=\"center\">Proizvod: <strong class=\"green\">" + naziv + "</strong></p>";
    }

    private String getDejavnostiAsHtmlString(Certifikat cert) {
        String result = cert.getDejavnost().getNaziv();
        for (Dejavnost dejavnost: cert.getDejavnosti()) {
            if (!cert.getDejavnost().getNaziv().equals(dejavnost.getNaziv())) {
                result += ", " + dejavnost.getNaziv();
            }
        }

        return "<p class=\"center\">Dejavnost: <strong class=\"green\">" + result + "</strong></p>" ;
    }

    private void executeTakeover(long id_cert) {
        StoredProcedureQuery sp = em.createStoredProcedureQuery("ESK_DATA.ESK_MGMT.takeoverData");
        // register parameters
        sp.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
        sp.setParameter(1, id_cert);
        sp.registerStoredProcedureParameter(2, Long.class, ParameterMode.IN);
        sp.setParameter(2, GetCurrentUserId());
        sp.execute();
    }

    public void cancellation(Certifikat cert, UserWithPermissions user) throws HandledException, PolicyException, ScanException {
        save(cert, false, user);
        updateStatusCascade(cert.getId(), "Razveljavljen");
    }

    void updateStatusCascade(long id, String status) throws HandledException {
        Optional<Certifikat> found = get(id);
        if (!found.isPresent()){
            throw new HandledException("Certifikat ne obstaja!");
        }

        Certifikat cert = found.get();
        cert.setStatus(status);
        if (status.equals("Razveljavljen")){
            cert.setDatVelj(Utils.sqlToday());
        }

        Optional<CertifikatPrilogaClan> foundCpc = certifikatPrilogaClanService.getLastOptionalByIdCertifikat(id);
        if (foundCpc.isPresent()){
            CertifikatPrilogaClan cpc = foundCpc.get();

            Priloga priloga = cpc.getPriloga();
            priloga.setStatus(status);

            prilogaService.update(priloga);
        }

        Optional<CertifikatPrilogaProizvod> foundCpp = certifikatPrilogaProizvodService.getLastOptionalByIdCertifikat(id);
        if (foundCpp.isPresent()){
            CertifikatPrilogaProizvod cpp = foundCpp.get();

            Priloga priloga = cpp.getPriloga();
            priloga.setStatus(status);

            prilogaService.update(priloga);
        }

        dao.save(cert);
    }
    void deleteAllCertifikatProizvod(long id_cert){
        StoredProcedureQuery sp = em.createStoredProcedureQuery("ESK_DATA.ESK_MGMT.deleteCertificateProducts");
        // register parameters
        sp.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
        sp.setParameter(1, id_cert);
        sp.registerStoredProcedureParameter(2, Long.class, ParameterMode.IN);
        sp.setParameter(2, GetCurrentUserId());
        sp.execute();
    }

    void deleteAllCertifikatActivities(long id_cert){
        StoredProcedureQuery sp = em.createStoredProcedureQuery("ESK_DATA.ESK_MGMT.deleteCertificateActivities");
        // register parameters
        sp.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
        sp.setParameter(1, id_cert);
        sp.registerStoredProcedureParameter(2, Long.class, ParameterMode.IN);
        sp.setParameter(2, GetCurrentUserId());
        sp.execute();
    }

    private void updateCertificateAttachments(long id_cert) {
        StoredProcedureQuery sp = em.createStoredProcedureQuery("ESK_DATA.ESK_MGMT.updateCertificateAttachments");
        // register parameters
        sp.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
        sp.setParameter(1, id_cert);
        sp.registerStoredProcedureParameter(2, Long.class, ParameterMode.IN);
        sp.setParameter(2, GetCurrentUserId());
        sp.execute();
    }

    private Long getPrevAttachment(long idCertifikat, int pType) {
        StoredProcedureQuery sp = em.createStoredProcedureQuery("ESK_DATA.ESK_MGMT.getPrevAttachment");
        // register parameters
        sp.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
        sp.setParameter(1, idCertifikat);

        sp.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
        sp.setParameter(2, pType);
        sp.registerStoredProcedureParameter(3, Long.class, ParameterMode.OUT);
        sp.execute();

        return Long.valueOf(String.valueOf(sp.getOutputParameterValue(3)));
    }

    public String getPravnaPodlaga(long id_cert){
        StoredProcedureQuery sp = em.createStoredProcedureQuery("ESK_DATA.ESK_MGMT.getPravnaPodlaga");
        // register parameters
        sp.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);
        sp.setParameter(1, id_cert);
        sp.registerStoredProcedureParameter(2, String.class, ParameterMode.OUT);
        sp.execute();

        Long res = Long.valueOf(String.valueOf(sp.getOutputParameterValue(2)));

        if (res > 0){
            Optional<ZakonskaPodlaga> value = this.zakonskaPodlagaService.get(res);
            return "<p>" + value.get().getVsebina() + "</p>";
        }
        return "";
    }

    public int countVeljavni(String org) {
        if (Utils.isNullOrEmpty(org))
            return dao.countByStatus("Veljaven");
        else
            return dao.countByStatusAndOrganizacija("Veljaven", org);
    }

    public int countVIzteku(String org) {
        if (Utils.isNullOrEmpty(org))
            return dao.countByStatusAndDatVeljBetween("Veljaven", Utils.now(), Utils.addDays(Utils.now(), 30));
        else
            return dao.countByStatusAndDatVeljBetweenAndOrganizacija("Veljaven", Utils.now(), Utils.addDays(Utils.now(), 30), org);
    }

    public int countVnosTotal(String org) {
        if (Utils.isNullOrEmpty(org))
            return dao.countByStatus("Vnos");
        else
            return dao.countByStatusAndOrganizacija("Vnos", org);
    }

    @Transactional
    public void importFromExcel(InputStream inputStream, UserWithPermissions user) throws IOException, InvalidFormatException, HandledException {
        // STEVILKA	TIP	DAT_VELJ	DAT_IZDAJE	TEL_ST	EMAIL	KONTROLOR	DAT_KONTROLE	DEJAVNOST	SHEMA_PROIZVODA	ZP_IK	NOSILEC_KMGMID	NOSILEC_MATICNA	CLAN_KMGMID	CLAN_MATICNA PRILOGA_STEVILKA	PRILOGA_DAT_VELJ	PRILOGA_DAT_VELJ
        ArrayList<ArrayList<String>> excel = ExcelReader.read(inputStream, true);
        String errors = "";
        int index = 1;
        String current = "";
        for (ArrayList<String> row: excel) {
            try {
                index++;

                Certifikat cert = new Certifikat();
                String stevilka = row.get(0);
                if (stevilka.equals(current)){
                    continue;
                }
                System.out.print("Obdelujem certifikat " + stevilka + "\t");
                // get data into namely variables
                String tip = row.get(1);
                String dat_izdaje = row.get(2);
                String dat_velj = row.get(3);
                String tel_st = row.get(4);
                String email = row.get(5);
                String kontrolor = row.get(6);
                String dat_kontrole = row.get(7);
                String dejavnost = row.get(8);
                String shema = row.get(9);
                String zp = row.get(10);
                String kmgmid = row.get(11);
                String maticna = row.get(12);
                String priloga_stevilka = row.get(15);
                String priloga_dat_izdaje = row.get(16);
                String priloga_dat_velj = row.get(17);
                String org = row.get(18);

                // first try to get existing certificate in a case of update
                Optional<Certifikat> existCert = this.getByStevilka(stevilka);

                if (existCert.isPresent())
                {
                    cert = existCert.get();
                }

                // populate data
                cert.setDatVelj(Utils.toSqlDate(dat_velj));
                cert.setStatus("Arhiv");
                cert.setDatVnosa(Utils.getCurrentSqlDate());
                cert.setDatIzdaje(Utils.toSqlDate(dat_izdaje));
                cert.setKontrolor(kontrolor);
                cert.setDatKontrole(Utils.toSqlDate(dat_kontrole));
                cert.setEmail(email);
                cert.setTelSt(tel_st);
                cert.setOrganizacija(org);
                cert.setUporabnik(user);
                cert.setSpremenil(GetCurrentUserId());
                cert.setStevilka(stevilka);
                cert.setTip(tip);

                Optional<Dejavnost> existDej = this.dejavnostService.getByNaziv(dejavnost);
                if (!existDej.isPresent()){
                    throw new HandledException("Dejavnost ne obstaja");
                }

                cert.setDejavnost(existDej.get());

                Subjekt subjekt = this.subjektService.getSubjektForImport(kmgmid, maticna);
                cert.setImetnik(this.subjektService.getAndSaveBeforeIfNecessary(subjekt));

                Optional<Shema> shemaExists = this.shemaService.getByNaziv(shema);

                if (!shemaExists.isPresent()){
                    throw new HandledException("Shema ne obstaja.");
                }

                Optional<ZascitenProizvod> zpExists = this.zascitenProizvodService.getByIdShemaAndNaziv(shemaExists.get().getId(), zp);

                if (!zpExists.isPresent()){
                    throw new HandledException("Zaščiten proizvod ali proizvod IK ne obstaja.");
                }

                ZascitenProizvod zpValue = zpExists.get();
                zpValue.setShema(shemaExists.get());
                cert.setZascitenProizvod(zpValue);
                dao.save(cert);

                if (tip.equals("S")){
                    // delete all attachments, because we create new one
                    if (cert.getId() > 0 && (cert.getCertifikatPrilogaClan() != null && cert.getCertifikatPrilogaClan().spliterator().getExactSizeIfKnown() > 0)){
                        this.certifikatPrilogaClanService.deleteAllByIdCertifikat(cert.getId());
                    }

                    if (Utils.isNullOrEmpty(priloga_dat_izdaje)){
                        throw new HandledException("Datum izdaje priloge ne obstaja.");
                    }

                    if (Utils.isNullOrEmpty(priloga_dat_velj)){
                        throw new HandledException("Datum veljavnosti priloge ne obstaja.");
                    }

                    if (Utils.isNullOrEmpty(priloga_stevilka)){
                        throw new HandledException("Številka priloge ne obstaja.");
                    }

                    // create new valid attachments
                    Priloga priloga = new Priloga();
                    priloga.setStatus("Arhiv");
                    priloga.setDatIzdaje(Utils.toSqlDate(priloga_dat_izdaje));
                    priloga.setDatVelj(Utils.toSqlDate(priloga_dat_velj));
                    priloga.setStevilka(priloga_stevilka);
                    priloga.setSpremenil(GetCurrentUserId());

                    CertifikatPrilogaClan cpc = new CertifikatPrilogaClan();
                    cpc.setCertifikat(cert);
                    cpc.setPriloga(priloga);
                    cpc.setSpremenil(GetCurrentUserId());

                    Collection<PrilogaClan> pcList = new ArrayList<>();

                    for (ArrayList<String> clanRow: this.subjektService.getMembersFromExcel(stevilka, index, excel) ) {
                        String kmgmid_clan = clanRow.get(13);
                        String maticna_clan = clanRow.get(14);
                        try {
                            Subjekt clan = this.subjektService.getAndSaveBeforeIfNecessary(this.subjektService.getSubjektForImport(kmgmid_clan, maticna_clan));
                            PrilogaClan pc = new PrilogaClan();
                            pc.setPriloga(priloga);
                            pc.setClan(clan);

                            pcList.add(pc);
                        }catch (Exception ex){
                            errors += "Vrstica " + index + ": " + ex.getMessage() + System.lineSeparator();
                        }
                    }

                    priloga.setPrilogaClan(pcList);

                    Collection<CertifikatPrilogaClan> cpcList = new ArrayList<>();
                    cpcList.add(cpc);

                    certifikatPrilogaClanService.saveAll(cert.getId(), cpcList);
                }

                // This is only for a case, if we manage grouped certificates. Every certificate is executed only ones.
                current = stevilka;
            } catch (Exception e) {
                errors += "Vrstica " + index + ": " +  e.getMessage() + System.lineSeparator();
                e.printStackTrace();
            }
        }

        if (!Utils.isNullOrEmpty(errors)){
            throw new HandledException(errors);
        }
    }

    public void invalidate() throws IOException {
        StoredProcedureQuery sp = em.createStoredProcedureQuery("ESK_DATA.ESK_MGMT.invalidateCertificates");
        sp.execute();

        Utils.deleteAllFilesFromFolder(settings.getWorkingPath());
        Utils.deleteAllFilesFromFolder(settings.getDownloadPath());
    }

    public String getImisId(long idCertifikat) throws HandledException {
        Optional<CertifikatExt> found = this.extService.getByIdCertifikat(idCertifikat);

        if (!found.isPresent()){
            throw new HandledException("Certifikat nima ustreznega zapisa v Imis dokumentnem sistemu.");
        }

        return found.get().getIdImis();
    }

    public RefreshResponse refresh(Certifikat cert) throws HandledException {
        // HOW IT WORKS?
        //
        // Method return whole set of members and owner, if there were any changed made in view data.

        RefreshResponse result = new RefreshResponse();

        boolean notMembers = (cert.getCertifikatPrilogaClan() == null || cert.getCertifikatPrilogaClan().spliterator().getExactSizeIfKnown() == 0);
        if (cert.getImetnik() == null && notMembers)
        {
            throw new HandledException("Najprej določite nosilca certifikata!");
        }

        // get existing or create new Person. THIS IS REQUEST FROM OUR CLIENT!!!!
        Optional<Subjekt> newImetnik = subjektService.getLastAndSaveBeforeIfNecessary(cert.getImetnik().getSubjId());

        if (newImetnik.get().getId() != cert.getImetnik().getId()) {
            result.setImetnik(newImetnik.get());
        }

        if (!notMembers){
            boolean changed = false;
            // delete all attachments, because we create new one
            if (cert.getId() > 0 && cert.getCertifikatPrilogaClan().spliterator().getExactSizeIfKnown() == 0){
                return result;
            }

            ArrayList<Subjekt> members = new ArrayList<>();

            if (cert.getCertifikatPrilogaClan().spliterator().getExactSizeIfKnown() != 1){
                throw new HandledException("Ne morem določiti prave priloge o članih!");
            }

            Priloga priloga = cert.getCertifikatPrilogaClan().iterator().next().getPriloga(); // priloga mora biti ena in edina

            for (PrilogaClan prilogaClan : priloga.getPrilogaClan())
            {
                Optional<Subjekt> newMember = this.subjektService.getLastAndSaveBeforeIfNecessary(prilogaClan.getClan().getSubjId());

                if (!newMember.isPresent()){
                    throw new HandledException("Ne najdem člana s subjID=" +  prilogaClan.getClan().getSubjId());
                }

                members.add(newMember.get());
                if (newMember.get().getId() != prilogaClan.getClan().getId()) {
                    changed = true;
                }
            }

            if (changed)  {
                result.setClani(members);
            }
        }

        return result;
    }

    public Iterable<Dejavnost> getDejavnosti(long id) {
        return this.dejavnostService.getByIdCertifikat(id);
    }

    public void generatePDF(long certId, String filename) throws HandledException {
        Optional<CertifikatExt> foundExt = extService.getByIdCertifikat(certId);

        if (!foundExt.isPresent()){
            throw new HandledException("Ne najdem ustreznega certifikata");
        }

        Utils.saveFileFromByteArray(filename, Utils.HexToBin(foundExt.get().getPdf()));
    }
}