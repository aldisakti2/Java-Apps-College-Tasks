import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileOutputStream;
import java.nio.file.Path;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;


public class ExportTable {
    public void writeToExcell(JTable table, Path path) throws FileNotFoundException, IOException {
        //new WorkbookFactory();
        Workbook wb = new XSSFWorkbook(); //Excell workbook
        Sheet sheet = wb.createSheet(); //WorkSheet
        Row row = sheet.createRow(2); //Row created at line 3
        TableModel model = table.getModel(); //Table model


        Row headerRow = sheet.createRow(0); //Create row at line 0
        for(int headings = 0; headings < model.getColumnCount(); headings++){ //For each column
            headerRow.createCell(headings).setCellValue(model.getColumnName(headings));//Write column name
        }

        for(int rows = 0; rows < model.getRowCount(); rows++){ //For each table row
            for(int cols = 0; cols < table.getColumnCount(); cols++){ //For each table column
                row.createCell(cols).setCellValue(model.getValueAt(rows, cols).toString()); //Write value
            }

            //Set the row to the next one in the sequence
            row = sheet.createRow((rows + 3));
        }
        wb.write(new FileOutputStream(path.toString()));//Save the file
    }
}
