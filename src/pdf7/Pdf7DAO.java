package pdf7;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Pdf7DAO {
    Connection conn;
    public Pdf7DAO(Connection conn){
        this.conn= conn;
    }

    public ObservableList<Pdf7Class> fetchAllAcivities(){
        ObservableList<Pdf7Class> activitiesList= FXCollections.observableArrayList();
        Pdf7Class activity=null;
        try {
            String query="select b.*, u.username from bitacoraseguimiento b inner join usuarios u on b.idAcceso=u.id order by fecha";
            Statement st= conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            while (rs.next()){
                activity= new Pdf7Class(rs.getInt("b.idAcceso"),rs.getString("u.username"),rs.getString("accion"),rs.getDate("fecha"),rs.getTime("hora"));
                activitiesList.add(activity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return activitiesList;
    }
}