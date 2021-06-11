package DocumentTypes;

public class DocumentType {
    String cveType, description;

    public DocumentType(String cveType, String description) {
        this.cveType = cveType;
        this.description = description;
    }

    public String getCveType() {
        return cveType;
    }

    public void setCveType(String cveType) {
        this.cveType = cveType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
