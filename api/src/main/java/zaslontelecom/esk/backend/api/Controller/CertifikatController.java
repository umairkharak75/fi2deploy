package zaslontelecom.esk.backend.api.Controller;

import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import zaslontelecom.esk.backend.api.Controller.Response.PagedQueryResult;
import zaslontelecom.esk.backend.api.Controller.Response.RefreshResponse;
import zaslontelecom.esk.backend.api.Model.*;
import zaslontelecom.esk.backend.api.Service.*;
import zaslontelecom.esk.backend.api.Utils.HandledException;
import zaslontelecom.esk.backend.api.Utils.Utils;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@RestController
@RequestMapping(path = "/certifikat")
public class CertifikatController extends CertifikatBaseController {
    @Autowired
    CertifikatService service;

    @Autowired
    CertifikatPrilogaProizvodService certifikatPrilogaProizvodService;

    @Autowired
    CertifikatPrilogaClanService certifikatPrilogaClanService;

    @Autowired
    PrilogaClanService prilogaClanService;

    @RequestMapping(method = RequestMethod.GET)
    public Optional<Certifikat> get(long id) {
        return service.get(id);
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST)
    public Certifikat post(@RequestBody Certifikat certifikat) throws HandledException, PolicyException, ScanException {
        this.checkPermission("CERTIFIKAT_AZURIRAJ");
        service.save(certifikat, false, this.getUser());
        return certifikat;
    }

    @Transactional
    @RequestMapping(method = RequestMethod.PUT)
    public Certifikat put(@RequestBody Certifikat certifikat) throws HandledException, PolicyException, ScanException {
        this.checkPermission("CERTIFIKAT_AZURIRAJ");
        service.save(certifikat, false, this.getUser());
        return certifikat;
    }

    @Transactional
    @RequestMapping(method = RequestMethod.DELETE)
    public boolean delete(long id) throws HandledException {
        this.checkPermission("CERTIFIKAT_IZBRIS");
        service.delete(id);
        return true;
    }

    @RequestMapping(value = "/proizvodiIdList", method = RequestMethod.GET)
    public PagedQueryResult proizvodiIdList(long id) {
        PagedQueryResult<CertifikatProizvod> response = new PagedQueryResult<>();
        Iterable<CertifikatProizvod> resultPage = this.service.getProizvodiIdList(id);
        response.setResult(resultPage);
        response.setResultLength(resultPage.spliterator().getExactSizeIfKnown());

        return response;
    }

    @RequestMapping(value = "/proizvodi", method = RequestMethod.GET)
    public PagedQueryResult proizvodi(long id) {
        PagedQueryResult<Proizvod> response = new PagedQueryResult<>();
        Iterable<Proizvod> resultPage = this.service.getProizvodi(id);
        response.setResult(resultPage);
        response.setResultLength(resultPage.spliterator().getExactSizeIfKnown());

        return response;
    }

    @RequestMapping(value = "/dejavnosti", method = RequestMethod.GET)
    public PagedQueryResult dejavnosti(long id) {
        PagedQueryResult<Dejavnost> response = new PagedQueryResult<>();
        Iterable<Dejavnost> resultPage = this.service.getDejavnosti(id);
        response.setResult(resultPage);
        response.setResultLength(resultPage.spliterator().getExactSizeIfKnown());

        return response;
    }

    @RequestMapping(value = "/certifikatPrilogaProizvod", method = RequestMethod.GET)
    public PagedQueryResult certifikatPrilogaProizvod(long id) {
        PagedQueryResult<CertifikatPrilogaProizvod> response = new PagedQueryResult<>();
        Page<CertifikatPrilogaProizvod> resultPage = this.certifikatPrilogaProizvodService.getLastByIdCertifikat(id);
        response.setResult(resultPage.getContent());
        response.setResultLength(resultPage.getContent().spliterator().getExactSizeIfKnown());

        return response;
    }

    @RequestMapping(value = "/certifikatPrilogaClan", method = RequestMethod.GET)
    public PagedQueryResult certifikatPrilogaClan(long id) {
        PagedQueryResult<CertifikatPrilogaClan> response = new PagedQueryResult<>();
        Page<CertifikatPrilogaClan> resultPage = this.certifikatPrilogaClanService.getLastByIdCertifikat(id);
        response.setResult(resultPage.getContent());
        response.setResultLength(resultPage.getContent().spliterator().getExactSizeIfKnown());

        return response;
    }

    @Transactional
    @RequestMapping(value = "/finish", method = RequestMethod.POST)
    public Certifikat finish(@RequestBody Certifikat certifikat) throws HandledException, IOException, LoginException, SQLException, ScanException, PolicyException {
        this.checkPermission("CERTIFIKAT_ZAKLJUCI");

        certifikat.setStatus("Veljaven");
        if (Utils.existsPrilogaProizvod(certifikat.getCertifikatPrilogaProizvod())){
            for (CertifikatPrilogaProizvod cp: certifikat.getCertifikatPrilogaProizvod()) {
                cp.getPriloga().setStatus("Veljaven");
            }
        }
        if (Utils.existsPrilogaClan(certifikat.getCertifikatPrilogaClan())){
            for (CertifikatPrilogaClan cp: certifikat.getCertifikatPrilogaClan()) {
                cp.getPriloga().setStatus("Veljaven");
            }
        }
        service.finish(certifikat, this.getUser());
        return certifikat;
    }

    @Transactional
    @RequestMapping(value = "/cancellation", method = RequestMethod.POST)
    public Certifikat cancellation(@RequestBody Certifikat certifikat) throws HandledException, ScanException, PolicyException {
        this.checkPermission("CERTIFIKAT_RAZVELJAVI");
        certifikat.setStatus("Razveljavljen");
        if (Utils.existsPrilogaProizvod(certifikat.getCertifikatPrilogaProizvod())){
            for (CertifikatPrilogaProizvod cp: certifikat.getCertifikatPrilogaProizvod()) {
                cp.getPriloga().setStatus("Razveljavljen");
            }
        }
        if (Utils.existsPrilogaClan(certifikat.getCertifikatPrilogaClan())) {
            for (CertifikatPrilogaClan cp : certifikat.getCertifikatPrilogaClan()) {
                cp.getPriloga().setStatus("Razveljavljen");
            }
        }
        service.cancellation(certifikat, this.getUser());
        return certifikat;
    }
    
    @Transactional
    @RequestMapping(value = "/removeClan", method = RequestMethod.POST)
    public boolean removeClan(@RequestBody PrilogaClan prilogaClan) throws HandledException {

        this.checkPermission("CERTIFIKAT_CLANI_IZBRISI");
        this.prilogaClanService.delete(prilogaClan.getId());

        return true;
    }

    @Transactional
    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    public RefreshResponse refreshCertificate(@RequestBody Certifikat cert) throws HandledException {
        this.checkPermission("CERTIFIKAT_OSVEZI_PARTNERJE");
        return service.refresh(cert);
    }

    @Transactional
    @RequestMapping(value = "/generatePdf", method = RequestMethod.POST)
    public boolean generatePdf(@RequestBody long id) throws HandledException, IOException, LoginException, ScanException, PolicyException {
        this.checkPermission("CERTIFIKAT_PREG_VSE");

        Optional<Certifikat> c = service.get(id);

        service.generatePdfDocument(c.get());

        return true;
    }
}