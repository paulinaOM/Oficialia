package pdf4;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class Pdf4DAO {
    Connection conn;
    public Pdf4DAO(Connection conn){
        this.conn= conn;
    }

    public List<Pdf4> getPDF4(String toDate) {
        ObservableList<Pdf4> pdList= FXCollections.observableArrayList();
        Pdf4 registro=null;
        try {
            String query="select d.noFolio,d.noDoc,d.nomDoc,i.descripcion,d2.nombreRecibe,d2.fechaEntrega\n" +
                    "from documento d \n" +
                    "join procedencia p on d.cveProcedencia = p.cveProcedencia\n" +
                    "join institucion i on p.cveInstitucion = i.cveInstitucion\n" +
                    "join destinatario d2 on d.cveDestinatario = d2.cveDestinatario\n" +
                    "where fechaEntrega='"+toDate+"';";//noEmp es la clave primaria de la tabla EN LA BD
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            while(rs.next()){
                registro=new Pdf4(rs.getString("nomDoc"),rs.getString("descripcion"),rs.getString("nombreRecibe"),rs.getInt("noFolio"),rs.getInt("noDoc"),rs.getDate("fechaEntrega"));
                System.out.println(registro);
                pdList.add(registro);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return pdList;
    }
}
