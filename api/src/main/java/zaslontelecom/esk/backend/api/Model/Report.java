package zaslontelecom.esk.backend.api.Model;

public class Report {
    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    String report;
    String name;
    Double value;
    String unit;
}
