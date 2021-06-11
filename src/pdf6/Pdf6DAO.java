package pdf6;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Pdf6DAO {
    Connection conn;
    public Pdf6DAO(Connection conn){
        this.conn= conn;
    }

    public ObservableList<Pdf6Class> fetchAllReports(){
        ObservableList<Pdf6Class> reportsList= FXCollections.observableArrayList();
        Pdf6Class report=null;
        try {
            String query="select b.*, u.id, u.username, r.nombre from bitacorareportes b inner join accesos a on b.idAcceso = a.idAcceso" +
                    " inner join usuarios u on a.id = u.id " +
                    " inner join reportes r on b.idReporte = r.idReporte" +
                    " order by fecha, hora";
            Statement st= conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            while (rs.next()){
                report= new Pdf6Class(rs.getInt("idReporte"),rs.getString("r.nombre"),rs.getInt("u.id"),rs.getString("u.username"),rs.getDate("fecha"),rs.getTime("hora"));
                reportsList.add(report);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reportsList;
    }

}