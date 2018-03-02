package test;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Vladimir on 02/03/18.
 **/
public class Test {
    public static void main(String[] args) throws IOException {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("Графік навчання");
        Row row = sheet.createRow(0);
        for (int i = 0; i < 52; i++) {
            Cell cell = row.createCell(i);
            CellStyle style = workbook.createCellStyle();
            style.setRotation((short)90);
            cell.setCellStyle(style);
            cell.setCellValue("12/12-12/12");
        }
        for (int i = 1; i < 5; i++) {
            Row rows = sheet.createRow(i);
            for (int j = 0; j < 52; j++) {
                Cell cell = rows.createCell(j);
                cell.setCellValue(i*j);
            }
        }

        workbook.write(new FileOutputStream(new File("D:/test1.xls")));
        workbook.close();
    }
}
