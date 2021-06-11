package pdf8;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pdf6.Pdf6Class;

import java.sql.*;

public class Pdf8DAO {
    Connection conn;
    public Pdf8DAO(Connection conn){
        this.conn= conn;
    }

    public ObservableList<Pdf8Class> fetchAllReports(String date){
        ObservableList<Pdf8Class> reportsList= FXCollections.observableArrayList();
        Pdf8Class report=null;
        try {
            String query="select d.noFolio,d.noDoc,d.nomDoc,d.fechaRecep, i.descripcion,p.destinatario from documento d " +
                    " inner join procedencia p on d.cveProcedencia = p.cveProcedencia" +
                    " join institucion i on p.cveInstitucion = i.cveInstitucion" +
                    " where cveDestinatario in(select cveDestinatario from destinatario" +
                    "                                where status='No')" +
                    " and  fechaRecep ='"+date+"' order by d.noFolio,d.noDoc;";
            Statement st= conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            while (rs.next()){
                report= new Pdf8Class(rs.getInt("d.noFolio"),rs.getInt("d.noDoc"),rs.getString("d.nomDoc"),
                        rs.getDate("d.fechaRecep"),rs.getString("i.descripcion"),rs.getString("p.destinatario"));
                reportsList.add(report);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reportsList;
    }

    public boolean updateStatus(int noFolio,int noDoc, String name, Date date){
        try {
            String query = "update destinatario "
                    + " set status = 'Sí',nombreRecibe=?,fechaEntrega=?  where cveDestinatario in (select cveDestinatario from documento" +
                    " where noFolio=? and noDoc=?)";
            PreparedStatement st =  conn.prepareStatement(query);
            st.setString(1,name);
            st.setDate(2,date);
            st.setInt(  3, noFolio);
            st.setInt(  4, noDoc);
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return false;
    }
}