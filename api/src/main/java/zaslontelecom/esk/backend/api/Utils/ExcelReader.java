package zaslontelecom.esk.backend.api.Utils;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ExcelReader {

    public static ArrayList<ArrayList<String>> read(InputStream stream, boolean hasHeader) throws IOException, InvalidFormatException, HandledException {

        ArrayList<ArrayList<String>>  result = new ArrayList<>();
        try{
        // Creating a Workbook from an Excel file (.xls or .xlsx)
        Workbook workbook = WorkbookFactory.create(stream);

        // Getting the Sheet at index zero
        Sheet sheet = workbook.getSheetAt(0);

        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();

        int columnNumber = 0;
        DataFormatter formatter = new DataFormatter();
        // read into string values
        for (Row row : sheet) {
            if (hasHeader){
                hasHeader = false;
                columnNumber = row.getLastCellNum();
                continue;
            }

            ArrayList<String> rowString = new ArrayList<>();

            for (int cn = 0; cn < columnNumber; cn++) {
                String cellValue = row.getCell(cn) == null ? null: formatter.formatCellValue(row.getCell(cn));
                rowString.add(Utils.isNullOrEmpty(cellValue) ? null : cellValue.trim());
            }

            result.add(rowString);
        }

        // Closing the workbook
        workbook.close();
        }catch (Exception ex)
        {
            throw new HandledException(ex.getMessage());
        }

        return result;
    }
}
