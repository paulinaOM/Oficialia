package pdf5;

import java.sql.Time;
import java.util.Date;

public class Pdf5Class {
    Date dateAccess;
    Time startTime, endingTime;
    int id;
    String username;

    public Pdf5Class(Date dateAccess, Time startTime, Time endingTime, int id, String username) {
        this.dateAccess = dateAccess;
        this.startTime = startTime;
        this.endingTime = endingTime;
        this.id = id;
        this.username = username;
    }

    public Date getDateAccess() {
        return dateAccess;
    }

    public void setDateAccess(Date dateAccess) {
        this.dateAccess = dateAccess;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(Time endingTime) {
        this.endingTime = endingTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Pdf5Class{" +
                "dateAccess=" + dateAccess +
                ", startTime=" + startTime +
                ", endingTime=" + endingTime +
                ", id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}