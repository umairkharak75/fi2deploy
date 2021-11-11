package zaslontelecom.esk.backend.api.Controller.Requests;

public class ReportRequest {
    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    String report;
    Long year;
}
