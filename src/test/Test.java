package test;

import java.awt.*;

public class Test {
    public static void main(String[] args) {
        System.out.println(new Color(0x7C68E8).getRGB());
    }
}

//package test;
//
////import org.apache.poi.hssf.usermodel.*;
////import org.apache.poi.hssf.util.HSSFColor;
////import org.apache.poi.ss.usermodel.*;
////
////import java.io.File;
////import java.io.FileOutputStream;
////import java.io.IOException;
//
///**
// * Created by Vladimir on 02/03/18.
// **/
//public class Test {
//    public static void main(String[] args) throws IOException {
//        System.out.println(new Color(-1));
//        /// /
////        HSSFWorkbook workbook = new HSSFWorkbook();
////        HSSFPalette palette = workbook.getCustomPalette();
//////        HSSFColor color = palette.addColor((byte) 10, (byte) 10, (byte) 10);
////        HSSFSheet sheet = workbook.createSheet("Графік навчання");
////        HSSFRow row = sheet.createRow(0);
////        HSSFCellStyle style = workbook.createCellStyle();
//////        style.setFillBackgroundColor();
////        style.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
////        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
////        style.setBorderBottom(BorderStyle.THIN);
////        style.setBorderRight(BorderStyle.THIN);
////        style.setRotation((short)90);
////        for (int i = 0; i < 52; i++) {
////            Cell cell = row.createCell(i);
////            cell.setCellStyle(style);
////            cell.setCellValue(i*i);
////        }
////        CellStyle styleBottom = workbook.createCellStyle();
////        styleBottom.setBorderRight(BorderStyle.THIN);
////        styleBottom.setBorderBottom(BorderStyle.THIN);
////        styleBottom.setFillPattern(FillPatternType.SOLID_FOREGROUND);
////        for (int i = 1; i < 5; i++) {
////            Row rows = sheet.createRow(i);
////            for (int j = 0; j < 52; j++) {
////                CellStyle cellStyle = workbook.createCellStyle();
////                cellStyle.cloneStyleFrom(styleBottom);
////                cellStyle.setFillForegroundColor((short) (j));
////                Cell cell = rows.createCell(j);
////                cell.setCellStyle(cellStyle);
////                cell.setCellValue(i*j);
////            }
////        }
////        for (int i = 0; i < 52; i++) {
////            sheet.autoSizeColumn(i);
////        }
////
////        workbook.write(new FileOutputStream(new File("D:/test1.xls")));
////        workbook.close();
////
////
//////        for (IndexedColors colors : IndexedColors.values()) {
//////            System.out.println(colors);
//////            System.out.println(colors.getIndex());
//////        }
//    }
//}
