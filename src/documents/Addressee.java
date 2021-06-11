package documents;

import java.sql.Date;

public class Addressee { //Destinatario
    String receive,status,areaCode,instructionCode,priorityCode;
    Date deliverDate, limitDate;

    public Addressee(String receive, Date deliverDate, Date limitDate, String status, String areaCode, String instructionCode, String priorityCode) {
        this.receive = receive;
        this.status = status;
        this.areaCode = areaCode;
        this.instructionCode = instructionCode;
        this.priorityCode = priorityCode;
        this.deliverDate = deliverDate;
        this.limitDate = limitDate;
    }

    public Addressee(){

    }

    public String getReceive() {
        return receive;
    }

    public void setReceive(String receive) {
        this.receive = receive;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getInstructionCode() {
        return instructionCode;
    }

    public void setInstructionCode(String instructionCode) {
        this.instructionCode = instructionCode;
    }

    public String getPriorityCode() {
        return priorityCode;
    }

    public void setPriorityCode(String priorityCode) {
        this.priorityCode = priorityCode;
    }

    public Date getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(Date deliverDate) {
        this.deliverDate = deliverDate;
    }

    public Date getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(Date limitDate) {
        this.limitDate = limitDate;
    }

    @Override
    public String toString() {
        return "Addressee{" +
                "receive='" + receive + '\'' +
                ", status='" + status + '\'' +
                ", areaCode='" + areaCode + '\'' +
                ", instructionCode='" + instructionCode + '\'' +
                ", priorityCode='" + priorityCode + '\'' +
                ", deliverDate=" + deliverDate +
                ", limitDate=" + limitDate +
                '}';
    }
}
