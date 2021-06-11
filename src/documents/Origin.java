package documents;

public class Origin { //Procedencia
    String sender,affair,position, addressee,observations,institutionCode;

    public Origin(String sender, String affair, String position, String addressee, String observations, String institutionCode) {
        this.sender = sender;
        this.affair = affair;
        this.position = position;
        this.addressee = addressee;
        this.observations = observations;
        this.institutionCode = institutionCode;
    }

    public Origin(){

    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getAffair() {
        return affair;
    }

    public void setAffair(String affair) {
        this.affair = affair;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAddressee() {
        return addressee;
    }

    public void setAddressee(String addressee) {
        this.addressee = addressee;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getInstitutionCode() {
        return institutionCode;
    }

    public void setInstitutionCode(String institutionCode) {
        this.institutionCode = institutionCode;
    }

    @Override
    public String toString() {
        return "Origin{" +
                "sender='" + sender + '\'' +
                ", affair='" + affair + '\'' +
                ", position='" + position + '\'' +
                ", addressee='" + addressee + '\'' +
                ", observations='" + observations + '\'' +
                ", institutionCode='" + institutionCode + '\'' +
                '}';
    }
}
