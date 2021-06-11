package pdf7;

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

public class Pdf7Activity {
    public void createPdf(String dest, ObservableList<Pdf7Class> activitiesList) throws IOException {//Recibe la carpeta de destino y la lista de empleados autilizar para la
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
        Paragraph p = new Paragraph("Bitácora de seguimiento de Actividad");
        // Add Paragraph to document
        document.add(p);

        Table table = new Table(new float[]{2, 4,4,3,2});//Indicalas columnas que va a generar, ancho de la columna
        table.addHeaderCell(new Cell().add(new Paragraph("id Usuario").setFont(bold)));//Agrega celda de encabezdo y dentro agrega párrafos
        table.addHeaderCell(new Cell().add(new Paragraph("Nombre de Usuario").setFont(bold)));
        table.addHeaderCell(new Cell().add(new Paragraph("Actividad").setFont(bold)));
        table.addHeaderCell(new Cell().add(new Paragraph("Fecha").setFont(bold)));
        table.addHeaderCell(new Cell().add(new Paragraph("Hora").setFont(bold)));

        System.out.println("Cabecera creada");

        for (Pdf7Class reg : activitiesList){
            process(table, reg,font);
        }
        //br.close();
        document.add(table);

        //Close document
        document.close();
    }

    public void process(Table table, Pdf7Class record, PdfFont font) {
        table.addCell(new Cell().add(new Paragraph(""+record.getId()).setFont(font)));
        table.addCell(new Cell().add(new Paragraph(""+record.getUsername()).setFont(font)));
        table.addCell(new Cell().add(new Paragraph(""+record.getActivity()).setFont(font)));
        table.addCell(new Cell().add(new Paragraph(""+record.getDate()).setFont(font)));
        table.addCell(new Cell().add(new Paragraph(""+record.getTime()).setFont(font)));
    }

}