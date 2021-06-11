package pdf3;

public class Pdf3 {
    String nomDoc,descripcion;
    int noFolio,noDoc;

    public Pdf3(String nomDoc, int noFolio, int noDoc,String descripcion) {

        this.nomDoc = nomDoc;
        this.noFolio = noFolio;
        this.noDoc = noDoc;
        this.descripcion=descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNomDoc() {
        return nomDoc;
    }

    public void setNomDoc(String nomDoc) {
        this.nomDoc = nomDoc;
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
}
