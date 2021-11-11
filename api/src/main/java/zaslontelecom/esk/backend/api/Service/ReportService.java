package zaslontelecom.esk.backend.api.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zaslontelecom.esk.backend.api.Model.Report;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {
    @Autowired
    EntityManager em;

    public ArrayList<Report> getReportData(String report, Long year) {
        StoredProcedureQuery sp = em.createStoredProcedureQuery("ESK_DATA.ESK_MGMT.getReportData");
        // register parameters
        sp.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
        sp.setParameter(1, report);
        sp.registerStoredProcedureParameter(2, Long.class, ParameterMode.IN);
        sp.setParameter(2, year);
        sp.registerStoredProcedureParameter(3, Object.class, ParameterMode.REF_CURSOR);
        sp.execute();
        ArrayList<Report> repList = new ArrayList<>();
        try {
            List<Object> result = sp.getResultList();
            if (result != null) {
                for (Object obj : result) {
                    Object[] item = (Object[]) obj;
                    Report row = new Report();
                    row.setName(String.valueOf(item[1]));
                    row.setReport(String.valueOf(item[0]));
                    row.setUnit(String.valueOf(item[3]));
                    row.setValue(Double.valueOf(item[2].toString()));
                    repList.add(row);
                }
            }
        }catch(Exception ex){

        }

        return repList;
    }

    public String getRpt(String rpt, ArrayList<Report> data) {
        String rptString = "";
        for (Report row : data) {
            if (row.getReport().equals(rpt)){
                String tds = "<td>" + row.getName() +"</td>";
                tds += "<td>" + (int)Math.round(row.getValue()) +"</td>";
                if (!row.getUnit().equals("null")) {
                    tds += "<td>" + row.getUnit() + "</td>";
                }
                rptString += "<tr>" + tds + "</tr>";
            }
        }
        return rptString;
    }
}
