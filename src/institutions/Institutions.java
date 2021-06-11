package institutions;

public class Institutions {
    String cveInstitution, description;

    public Institutions(String cveInstitution, String description) {
        this.cveInstitution = cveInstitution;
        this.description = description;
    }

    public String getCveInstitution() {
        return cveInstitution;
    }

    public void setCveInstitution(String cveInstitution) {
        this.cveInstitution = cveInstitution;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Institutions{" +
                "cveInstitution='" + cveInstitution + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
