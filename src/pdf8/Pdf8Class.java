package pdf8;

import java.sql.Date;

public class Pdf8Class {
    int noFolio,noDoc;
    String docName,inst,addresse;
    Date receptionDate;

    public Pdf8Class(int noFolio, int noDoc, String docName,Date receptionDate, String inst, String addresse) {
        this.noFolio = noFolio;
        this.noDoc = noDoc;
        this.docName = docName;
        this.inst = inst;
        this.addresse = addresse;
        this.receptionDate = receptionDate;
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

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getInst() {
        return inst;
    }

    public void setInst(String sender) {
        this.inst = inst;
    }

    public String getAddresse() {
        return addresse;
    }

    public void setAddresse(String addresse) {
        this.addresse = addresse;
    }

    public Date getReceptionDate() {
        return receptionDate;
    }

    public void setReceptionDate(Date receptionDate) {
        this.receptionDate = receptionDate;
    }

    @Override
    public String toString() {
        return "Pdf8Class{" +
                "noFolio=" + noFolio +
                ", noDoc=" + noDoc +
                ", docName='" + docName + '\'' +
                ", inst='" + inst + '\'' +
                ", addresse='" + addresse + '\'' +
                ", receptionDate=" + receptionDate +
                '}';
    }
}