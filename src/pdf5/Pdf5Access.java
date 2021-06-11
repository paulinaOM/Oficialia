package pdf5;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import javafx.collections.ObservableList;
import java.io.IOException;


public class Pdf5Access {
    public void createPdf(String dest, ObservableList<Pdf5Class> accessList) throws IOException {//Recibe la carpeta de destino y la lista de empleados autilizar para la
        //...creación del PDF
        //Initialize PDF writer
        PdfWriter writer = new PdfWriter(dest);

        //Initialize PDF document
        PdfDocument pdf = new PdfDocument(writer);

        // Initialize document
        Document document = new Document(pdf, PageSize.A4.rotate());
        document.setMargins(20, 20, 20, 20);
        PdfFont font = PdfFontFactory.createFont(FontConstants.COURIER);
        PdfFont bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        Paragraph p = new Paragraph("Bitácora de accesos al Sistema");
        // Add Paragraph to document
        document.add(p);

        Table table = new Table(new float[]{4, 3,3,2,3});//Indicalas columnas que va a generar, ancho de la columna
        table.addHeaderCell(new Cell().add(new Paragraph("id Usuario").setFont(bold)));//Agrega celda de encabezdo y dentro agrega párrafos
        table.addHeaderCell(new Cell().add(new Paragraph("Nombre de Usuario").setFont(bold)));
        table.addHeaderCell(new Cell().add(new Paragraph("Fecha").setFont(bold)));
        table.addHeaderCell(new Cell().add(new Paragraph("Inicio de Sesión").setFont(bold)));
        table.addHeaderCell(new Cell().add(new Paragraph("Fin de la Sesión").setFont(bold)));

        System.out.println("Cabecera creada");

        for (Pdf5Class reg : accessList){
            process(table, reg,font);
        }
        //br.close();
        document.add(table);

        //Close document
        document.close();
    }

    public void process(Table table, Pdf5Class record, PdfFont font) {
        table.addCell(new Cell().add(new Paragraph(""+record.getId()).setFont(font)));
        table.addCell(new Cell().add(new Paragraph(""+record.getUsername()).setFont(font)));
        table.addCell(new Cell().add(new Paragraph(""+record.getDateAccess()).setFont(font)));
        table.addCell(new Cell().add(new Paragraph(""+record.getStartTime()).setFont(font)));
        table.addCell(new Cell().add(new Paragraph(""+record.getEndingTime()).setFont(font)));
    }
}