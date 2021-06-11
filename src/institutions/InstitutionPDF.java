package institutions;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import java.io.IOException;
import com.itextpdf.layout.Document;


import java.util.List;

public class InstitutionPDF {
    boolean ban=true;

    public void createPdf(String dest, List<Institutions> institutions) throws IOException {
        //Initialize PDF writer
        PdfWriter writer = new PdfWriter(dest);

        //Initialize PDF document
        PdfDocument pdf = new PdfDocument(writer);

        // Initialize document
        Document document = new Document(pdf, PageSize.A4.rotate());
        document.setMargins(20, 20, 20, 20);

        PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);
        PdfFont bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        Table table = new Table(new float[]{4, 1});

        //Indicalas columnas que va a generar, ancho de la columna
        //table.setWidthPercent(100);
        //process(table, line, bold, true);//el método process escribe sobre el archivo
        // tiene una bandera que indica que es el encabezado

        ban=true;
        for(int i=0; i<institutions.size();i++){
            process(table,institutions.get(i),font,ban,bold);
        }


        document.add(new Paragraph("Instituciones de Procedencia")).setFont(bold);
        document.add(new Paragraph(""));
        document.add(table);

        //Close document
        document.close();
    }

    public void process(Table table,Institutions i, PdfFont font, boolean isHeader,PdfFont bold) {
        if(isHeader) {
            table.addHeaderCell(new Cell().add(new Paragraph("cveInstitucion").setFont(bold)));//Agrega celda de encabezdo y dentro agrega párrafos
            table.addHeaderCell(new Cell().add(new Paragraph("Nombre").setFont(bold)));
            table.addCell(new Cell().add(new Paragraph(i.getCveInstitution()).setFont(font)));
            table.addCell(new Cell().add(new Paragraph(i.getDescription()).setFont(font)));
            ban=false;

        }
        else{
            table.addCell(new Cell().add(new Paragraph(i.getCveInstitution()).setFont(font)));
            table.addCell(new Cell().add(new Paragraph(i.getDescription()).setFont(font)));
        }
    }

}