import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.*;

public class ExcelReportGenerator<T> implements ReportGenerator<T> {
    ClassExtractor<T> extractor;

    public ExcelReportGenerator(ClassExtractor<T> extractor) throws IllegalAccessException {
        if (extractor == null) {
            throw new IllegalAccessException("Class extractor not be null");
        }
        this.extractor = extractor;
    }

    @Override
    public Report generate(List<T> entities) {
        if (entities == null) {
            throw new RuntimeException("List entities not be null");
        }
        List<String> fieldsNames = extractor.getFieldsNames();
        List<List<String>> fieldsValues = extractor.getFieldsValues(entities);
        return new XlsReport(generateWorkbook(fieldsNames, fieldsValues));
    }

    private Workbook generateWorkbook(List<String> fieldsNames,  List<List<String>> fieldsValues) {
        Workbook book = new HSSFWorkbook();
        Sheet sheet = book.createSheet("Report");
        generateNamesRow(sheet, fieldsNames);
        generateValuesRows(sheet, fieldsValues);
        return book;
    }

    private void generateNamesRow(Sheet sheet, List<String> fieldsNames) {
        Row namesRow = sheet.createRow(0);
        for (int i = 0; i < fieldsNames.size(); i++) {
            namesRow.createCell(i).setCellValue(fieldsNames.get(i));
            sheet.autoSizeColumn(i);
        }
    }

    private void generateValuesRows(Sheet sheet, List<List<String>> fieldsValues) {
        for (int i = 0; i < fieldsValues.size(); i++) {
            Row valuesRow = sheet.createRow(i + 1);
            for (int j = 0; j < fieldsValues.get(i).size(); j++) {
                valuesRow.createCell(j).setCellValue(fieldsValues.get(i).get(j));
            }
        }
    }
}
