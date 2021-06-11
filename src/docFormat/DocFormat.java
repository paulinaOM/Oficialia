package docFormat;

public class DocFormat {
    private String cveFormato,descripcion;

    public DocFormat(String cveFormato, String descripcion) {
        this.cveFormato = cveFormato;
        this.descripcion = descripcion;
    }

    public String getCveFormato() {
        return cveFormato;
    }

    public void setCveFormato(String cveFormato) {
        this.cveFormato = cveFormato;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
