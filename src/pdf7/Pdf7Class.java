package pdf7;

import java.sql.Date;
import java.sql.Time;

public class Pdf7Class {
    int id;
    String username,activity;
    Date date;
    Time time;

    public Pdf7Class(int id, String username, String activity, Date date, Time time) {
        this.id = id;
        this.username = username;
        this.activity = activity;
        this.date = date;
        this.time = time;
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

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
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
        return "Pdf7Activity{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", activity='" + activity + '\'' +
                ", date=" + date +
                ", time=" + time +
                '}';
    }
}