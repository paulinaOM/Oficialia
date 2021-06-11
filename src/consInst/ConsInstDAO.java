package consInst;

import documents.Document;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConsInstDAO {
    Connection conn;
    public ConsInstDAO(Connection conn)
    {
        this.conn = conn;
    }

    public ObservableList<Document> fetchDoc(String institution){ //Devuelve todos los datos de los documento buscando por su institución de procedencia
        ObservableList<Document> documents= FXCollections.observableArrayList();
        try {
            String query="select * from documento d inner join procedencia p on d.cveProcedencia=p.cveProcedencia " +
                    "inner join institucion i on i.cveInstitucion=p.cveInstitucion where descripcion='"+institution+"';";
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            Document d=null;
                while (rs.next()){
                    d= new Document(rs.getInt("noFolio"),rs.getInt("noDoc"),rs.getString("nomDoc"),rs.getDate("fechaDoc"),
                            rs.getDate("fechaRecep"),rs.getBytes("archivoDoc"),rs.getInt("cveProcedencia"),
                            rs.getInt("cveDestinatario"),rs.getString("cveFormato"),rs.getString("cveTipo"));
                    documents.add(d);
                }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documents;
    }


}
