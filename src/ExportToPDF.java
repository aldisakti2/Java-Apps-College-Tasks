
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.Date;


public class ExportToPDF {
    private static TextAlignment getTextAlignmentForColumn(int column) {
        if (column == 0) {
            return TextAlignment.CENTER;
        } else if (column == 1) {
            return TextAlignment.LEFT;
        } else {
            return TextAlignment.CENTER;
        }
    }

    public void writeToPDF(JTable table, Path path) throws FileNotFoundException {
        try {
            //Date Time
            Date date = new Date(); // the date instance
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            String dest = path.toString();
            PdfWriter writer = new PdfWriter(dest);

            //Adding to Template
            PdfReader reader = new PdfReader("./template/TEMPLATE.pdf");

            // Creating a PdfDocument object
            PdfDocument pdf = new PdfDocument(reader, writer);
            Document doc = new Document(pdf);

            //Table
            Table pdfTable = new Table(table.getColumnCount());


            //Font Style
            PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);

            pdfTable.setDocument(doc);
            //pdfTable.setWidthPercent(90);
            pdfTable.setHorizontalAlignment(HorizontalAlignment.CENTER);


            //Adding table headers
            for (int i = 0; i < table.getColumnCount(); i++) {
                Cell cell = new Cell();
                cell.add(table.getColumnName(i));
                cell.setFont(font);
                cell.setTextAlignment(TextAlignment.CENTER).setBold();
                pdfTable.addHeaderCell(cell);
            }

            //Extracting data from the JTable and inserting it to PdfTable
            for (int rows = 0; rows < table.getRowCount(); rows++) {
                for (int cols = 0; cols < table.getColumnCount(); cols++) {
                    Cell cell = new Cell();
                    cell.add(table.getValueAt(rows,cols).toString());
                    cell.setFont(font);
                    cell.setFontSize(8);
                    cell.setTextAlignment(getTextAlignmentForColumn(cols));
                    pdfTable.addCell(cell);
                }
            }
            //doc.setMargins(3,3,3,4);
            doc.add(new Paragraph("Tabel Pendataan Ternak Desa Babakan Tahun "+calendar.get(Calendar.YEAR)).setBold().setMarginLeft(4).setFont(font).setFontSize(14).setTextAlignment(TextAlignment.CENTER).setPaddings(175,0,10,0));
            doc.add(pdfTable);
            doc.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
