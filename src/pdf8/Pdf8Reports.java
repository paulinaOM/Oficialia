package pdf8;

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

public class Pdf8Reports {
    public void createPdf(String dest, ObservableList<Pdf8Class> reportsList) throws IOException {//Recibe la carpeta de destino y la lista de empleados autilizar para la
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
        Paragraph p = new Paragraph("Bitácora de Reportes no entregados");
        // Add Paragraph to document
        document.add(p);


        Table table = new Table(new float[]{1,1,2,2,2,2,2,2});//Indicalas columnas que va a generar, ancho de la columna
        table.addHeaderCell(new Cell().add(new Paragraph("Folio").setFont(bold)));//Agrega celda de encabezdo y dentro agrega párrafos
        table.addHeaderCell(new Cell().add(new Paragraph("No. Doc.").setFont(bold)));
        table.addHeaderCell(new Cell().add(new Paragraph("Nombre").setFont(bold)));
        table.addHeaderCell(new Cell().add(new Paragraph("Fecha Recepción").setFont(bold)));
        table.addHeaderCell(new Cell().add(new Paragraph("Inst. Procedencia").setFont(bold)));
        table.addHeaderCell(new Cell().add(new Paragraph("Destinatario").setFont(bold)));
        table.addHeaderCell(new Cell().add(new Paragraph("Recibido por                 ").setFont(bold)));
        table.addHeaderCell(new Cell().add(new Paragraph("Firma                           ").setFont(bold)));
        System.out.println("Cabecera creada");

        for (Pdf8Class reg : reportsList){
            process(table, reg,font);
        }
        //br.close();
        document.add(table);

        //Close document
        document.close();
    }

    public void process(Table table, Pdf8Class record, PdfFont font) {
        table.addCell(new Cell().add(new Paragraph(""+record.getNoFolio()).setFont(font)));
        table.addCell(new Cell().add(new Paragraph(""+record.getNoDoc()).setFont(font)));
        table.addCell(new Cell().add(new Paragraph(""+record.getDocName()).setFont(font)));
        table.addCell(new Cell().add(new Paragraph(""+record.getReceptionDate()).setFont(font)));
        table.addCell(new Cell().add(new Paragraph(""+record.getInst()).setFont(font)));
        table.addCell(new Cell().add(new Paragraph(""+record.getAddresse()).setFont(font)));
        table.addCell(new Cell().add(new Paragraph("")));
        table.addCell(new Cell().add(new Paragraph("")));
    }
}