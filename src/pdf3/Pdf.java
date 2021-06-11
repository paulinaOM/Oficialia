package pdf3;

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

import java.io.IOException;
import java.util.List;

public class Pdf {
    public void createPdf(String dest, List<Pdf3> pdfList) throws IOException {//Recibe la carpeta de destino y la lista de empleados autilizar para la
        //...creación del PDF
        //Initialize PDF writer
        PdfWriter writer = new PdfWriter(dest);

        //Initialize PDF document
        PdfDocument pdf = new PdfDocument(writer);

        // Initialize document
        com.itextpdf.layout.Document document = new Document(pdf, PageSize.A4.rotate());
        document.setMargins(20, 20, 20, 20);
        PdfFont font = PdfFontFactory.createFont(FontConstants.COURIER);
        PdfFont bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        Paragraph p = new Paragraph("Documentos por Institución de Procedencia");
        // Add Paragraph to document
        document.add(p);

        Table table = new Table(new float[]{4, 3, 3,4});//Indicalas columnas que va a generar, ancho de la columna
        table.addHeaderCell(new Cell().add(new Paragraph("No Folio").setFont(bold)));//Agrega celda de encabezdo y dentro agrega párrafos
        table.addHeaderCell(new Cell().add(new Paragraph("No Doc").setFont(bold)));
        table.addHeaderCell(new Cell().add(new Paragraph("Nombre Documento").setFont(bold)));
        table.addHeaderCell(new Cell().add(new Paragraph("Institución").setFont(bold)));

        System.out.println("Cabecera creada");

        for (Pdf3 reg : pdfList){
            process(table, reg,font);
        }
        //br.close();
        document.add(table);

        //Close document
        document.close();
    }

    public void process(Table table, Pdf3 registro, PdfFont font) {
        table.addCell(new Cell().add(new Paragraph(""+registro.getNoFolio()).setFont(font)));
        table.addCell(new Cell().add(new Paragraph(""+registro.getNoDoc()).setFont(font)));
        table.addCell(new Cell().add(new Paragraph(registro.getNomDoc()).setFont(font)));
        table.addCell(new Cell().add(new Paragraph(registro.getDescripcion()).setFont(font)));


    }
}
