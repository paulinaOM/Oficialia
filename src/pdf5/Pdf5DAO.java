package pdf5;

import documents.Document;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Pdf5DAO {
    Connection conn;
    public Pdf5DAO(Connection conn){
        this.conn= conn;
    }

    public ObservableList<Pdf5Class> fetchAllAccess(){
        ObservableList<Pdf5Class> accessList= FXCollections.observableArrayList();
        Pdf5Class access=null;
        try {
            String query="select a.*, u.username from accesos a inner join usuarios u on a.id = u.id order by fecha";
            Statement st= conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            while (rs.next()){
                access= new Pdf5Class(rs.getDate("fecha"),rs.getTime("horaInicio"),rs.getTime("horaFin"),rs.getInt("a.id"),rs.getString("u.username"));
                accessList.add(access);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accessList;
    }
}