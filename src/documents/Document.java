package documents;

import java.sql.Date;
import java.util.Arrays;

public class Document {
    int noFolio, noDoc,originCode,addresseeCode;
    String formatCode, typeCode,docName;
    byte[] docPDF;
    Date docDate,receptionDate;

    public Document(int noFolio, int noDoc, String docName, Date docDate, Date receptionDate,  byte[] docPDF, int originCode, int addresseeCode, String formatCode, String typeCode) {
        this.noFolio = noFolio;
        this.noDoc = noDoc;
        this.originCode = originCode;
        this.addresseeCode = addresseeCode;
        this.formatCode = formatCode;
        this.typeCode = typeCode;
        this.docName = docName;
        this.docPDF = docPDF;
        this.docDate = docDate;
        this.receptionDate = receptionDate;
    }

    public Document() {
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
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

    public int getOriginCode() {
        return originCode;
    }

    public void setOriginCode(int originCode) {
        this.originCode = originCode;
    }

    public int getAddresseeCode() {
        return addresseeCode;
    }

    public void setAddresseeCode(int addresseeCode) {
        this.addresseeCode = addresseeCode;
    }

    public String getFormatCode() {
        return formatCode;
    }

    public void setFormatCode(String formatCode) {
        this.formatCode = formatCode;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public byte[] getDocPDF() {
        return docPDF;
    }

    public void setDocPDF(byte[] docPDF) {
        this.docPDF = docPDF;
    }

    public Date getDocDate() {
        return docDate;
    }

    public void setDocDate(Date docDate) {
        this.docDate = docDate;
    }

    public Date getReceptionDate() {
        return receptionDate;
    }

    public void setReceptionDate(Date receptionDate) {
        this.receptionDate = receptionDate;
    }

    @Override
    public String toString() {
        return "Document{" +
                "noFolio=" + noFolio +
                ", noDoc=" + noDoc +
                ", originCode=" + originCode +
                ", addresseeCode=" + addresseeCode +
                ", formatCode='" + formatCode + '\'' +
                ", typeCode='" + typeCode + '\'' +
                ", docName='" + docName + '\'' +
                ", docPDF=" + Arrays.toString(docPDF) +
                ", docDate=" + docDate +
                ", receptionDate=" + receptionDate +
                '}';
    }
}