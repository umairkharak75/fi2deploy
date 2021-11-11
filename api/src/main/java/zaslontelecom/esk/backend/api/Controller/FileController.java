package zaslontelecom.esk.backend.api.Controller;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import zaslontelecom.esk.backend.api.Model.Certifikat;
import zaslontelecom.esk.backend.api.Model.UserWithPermissions;
import zaslontelecom.esk.backend.api.Service.*;
import zaslontelecom.esk.backend.api.Utils.HandledException;
import zaslontelecom.esk.backend.api.Utils.Imis.ImisDms;
import zaslontelecom.esk.backend.api.Utils.PdfTools;
import zaslontelecom.esk.backend.api.Utils.Settings;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/files")
public class FileController extends BaseController {

    @Autowired
    CertifikatService certifikatService;

    @Autowired
    ProizvodService proizvodService;

    @Autowired
    SubjektService subjektService;

    @Autowired
    ZascitniznakService zznakService;

    @Autowired
    ProizvodKolicineService kolicineService;

    @Autowired
    ImisDms imisService;

    @Autowired
    FileService fileService;

    @Autowired
    Settings settings;

    @RequestMapping(value = "/pdf/{filename}", method = RequestMethod.GET, headers = "Accept=application/pdf", produces = "application/pdf; charset=utf-8")
    public Resource pdf(@PathVariable String filename, HttpServletResponse response) throws HandledException {
        try {

            Optional<Certifikat> cert = certifikatService.get(Long.valueOf(filename));

            if (!cert.isPresent()){
                throw new HandledException("Certifikat ne obstaja!");
            }

            filename = certifikatService.getPdfFilename(cert.get());


            String idImis = this.certifikatService.getImisId(cert.get().getId());
            if (!idImis.equals(String.valueOf(cert.get().getId()))) {
                // CERT from IMIS storage
                this.imisService.documentRetrieve(idImis, filename);
            }else {
                // CERT from Oracle database
                this.certifikatService.generatePDF(cert.get().getId(), filename);
            }

            // adding watermark
            if (!isAuthenticated()) {
                String logoPath = settings.getPdfTemplatePath() + "images/" + "copy.png";
                PdfTools.generateWatermark(filename, logoPath);
            }

            return fileService.getFileSystem(filename, response);
        }catch(Exception ex){
            return null;
        }
    }

    @PostMapping(value = "/upload")
    @ResponseStatus(HttpStatus.OK)
    public Object handleFileUpload(@RequestParam("file") MultipartFile file, String action) throws IOException, InvalidFormatException, HandledException {
        switch(action){
            case "PROIZVOD": { this.importProducts(file.getInputStream()); return HttpStatus.OK;}
            case "KOLICINA": { this.importQuantities(file.getInputStream()); return HttpStatus.OK;}
            case "CERTIFIKAT": { this.importCertificates(file.getInputStream()); return HttpStatus.OK;}
            case "ZZNAK": { this.importTrademarks(file.getInputStream()); return HttpStatus.OK;}
            case "CLAN": { return this.importMembers(file.getInputStream()); }
            default: {throw new HandledException("Naložene datoteke ne razumem!");}
        }
    }

    private void importQuantities(InputStream inputStream) throws HandledException, InvalidFormatException, IOException {
        this.checkPermission("UVOZ_KOLICINE");
        this.kolicineService.importFromExcel(inputStream);
    }

    private void importTrademarks(InputStream inputStream) throws IOException, InvalidFormatException, HandledException {
        this.checkPermission("UVOZ_ZZNAK");
        this.zznakService.importFromExcel(inputStream);
    }

    private Object importMembers(InputStream inputStream) throws IOException, InvalidFormatException, HandledException {
        this.checkPermission("UVOZ_CLANI");
        return this.subjektService.importFromExcel(inputStream);
    }

    private void importProducts(InputStream inputStream) throws IOException, InvalidFormatException, HandledException {
        this.checkPermission("UVOZ_PROIZVODI");
        this.proizvodService.importFromExcel(inputStream);
    }

    private void importCertificates(InputStream inputStream) throws IOException, InvalidFormatException, HandledException {
        this.checkPermission("UVOZ_CERTIFIKATI");
        this.certifikatService.importFromExcel(inputStream, this.getUser());
    }
}