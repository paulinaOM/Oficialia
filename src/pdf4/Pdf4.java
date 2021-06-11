package pdf4;

import java.sql.Date;

public class Pdf4 {
    String nomDoc,descripcion,nombreRecibe;
    int noFolio,noDoc;
    Date fechaEntrega;

    public Pdf4(String nomDoc, String descripcion, String nombreRecibe, int noFolio, int noDoc, Date fechaEntrega) {
        this.nomDoc = nomDoc;
        this.descripcion = descripcion;
        this.nombreRecibe = nombreRecibe;
        this.noFolio = noFolio;
        this.noDoc = noDoc;
        this.fechaEntrega = fechaEntrega;
    }

    public String getNomDoc() {
        return nomDoc;
    }

    public void setNomDoc(String nomDoc) {
        this.nomDoc = nomDoc;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombreRecibe() {
        return nombreRecibe;
    }

    public void setNombreRecibe(String nombreRecibe) {
        this.nombreRecibe = nombreRecibe;
    }

    public int getNoFolio() {
        return noFolio;
    }

    public void setNoFolio(int noFolio) {
        this.noFolio = noFolio;
    }

    public int getNoDoc() {
        return noDoc;
    }

    public void setNoDoc(int noDoc) {
        this.noDoc = noDoc;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }
}
