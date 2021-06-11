package pdf6;

import java.sql.Date;
import java.sql.Time;

public class Pdf6Class {
    String reportName,username;
    int idReport, userId;
    Date date;
    Time time;

    public Pdf6Class(int idReport,String reportName, int userId, String username,  Date date, Time time) {
        this.reportName = reportName;
        this.username = username;
        this.idReport = idReport;
        this.userId = userId;
        this.date = date;
        this.time = time;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getIdReport() {
        return idReport;
    }

    public void setIdReport(int idReport) {
        this.idReport = idReport;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Pdf6Class{" +
                "reportName='" + reportName + '\'' +
                ", username='" + username + '\'' +
                ", idReport=" + idReport +
                ", userId=" + userId +
                ", date=" + date +
                ", time=" + time +
                '}';
    }
}