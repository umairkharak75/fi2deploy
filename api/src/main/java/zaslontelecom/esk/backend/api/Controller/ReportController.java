package zaslontelecom.esk.backend.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import zaslontelecom.esk.backend.api.Controller.Requests.ReportRequest;
import zaslontelecom.esk.backend.api.Model.Priloga;
import zaslontelecom.esk.backend.api.Model.Report;
import zaslontelecom.esk.backend.api.Service.FileService;
import zaslontelecom.esk.backend.api.Service.PrilogaService;
import zaslontelecom.esk.backend.api.Service.ReportService;
import zaslontelecom.esk.backend.api.Utils.HandledException;
import zaslontelecom.esk.backend.api.Utils.PdfTools;
import zaslontelecom.esk.backend.api.Utils.Settings;
import zaslontelecom.esk.backend.api.Utils.Utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/report")
public class ReportController extends BaseController{
    @Autowired
    ReportService service;

    @Autowired
    Settings settings;

    @Autowired
    FileService fileService;

    @RequestMapping(value = "/list", method = RequestMethod.GET, headers = "Accept=application/json")
    public ArrayList<Report> list(ReportRequest request) throws HandledException {
        this.checkPermission("POIZVEDBA_PRIPRAVLJENA_POROCILA");
        ArrayList<Report> response = this.service.getReportData(request.getReport(), request.getYear());

        return response;
    }

    @RequestMapping(value = "/pdf/{year}", method = RequestMethod.GET, headers = "Accept=application/pdf", produces = "application/pdf; charset=utf-8")
    public Resource pdfReport(@PathVariable String year, HttpServletResponse response) throws HandledException, IOException {
        this.checkPermission("POIZVEDBA_PRIPRAVLJENA_POROCILA");
        // 1. get data from DB
        ArrayList<Report> data = this.service.getReportData("RPTALL", Long.valueOf(year));

        // 2. generate html table rows for each report
        String rpt1 = service.getRpt("RPT1", data);
        String rpt2 = service.getRpt("RPT2", data);
        String rpt3 = service.getRpt("RPT3", data);
        String rpt4 = service.getRpt("RPT4", data);
        String rpt5 = service.getRpt("RPT5", data);
        String rpt6 = service.getRpt("RPT6", data);

        // 3. inject reults into HTML
        String folder = settings.getPdfTemplatePath();
        String reportString = Utils.readFile(folder + "MKGP.html");
        reportString = reportString.replace("[RPT1]", rpt1);
        reportString = reportString.replace("[RPT2]", rpt2);
        reportString = reportString.replace("[RPT3]", rpt3);
        reportString = reportString.replace("[RPT4]", rpt4);
        reportString = reportString.replace("[RPT5]", rpt5);
        reportString = reportString.replace("[RPT6]", rpt6);
        reportString = reportString.replace("[LETO]", year);

        String logoPath = settings.getPdfTemplatePath() + "images/" + "MKGP2.png";
        reportString = reportString.replace("[BASE_64_LOGO]", Utils.image2Base64(logoPath));

        // 4. generate PDF file from HTML report
        String reportFile = settings.getWorkingPath() + "reportESK.pdf";
        PdfTools.generatePDF(reportString, reportFile, settings);

        // 5.read and return newly generated PDF file to user
        return fileService.getFileSystem(reportFile, response);
    }
}