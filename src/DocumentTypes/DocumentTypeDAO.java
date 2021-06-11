package DocumentTypes;

import institutions.Institutions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class DocumentTypeDAO {
    Connection conn;

    public DocumentTypeDAO(Connection conn)
    {
        this.conn = conn;
    }

    public ObservableList<DocumentType> fetchAll(){
        ObservableList<DocumentType> documentTypes = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM tipoDoc";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            DocumentType dt = null;
            while(rs.next()) {
                dt = new DocumentType(
                        rs.getString("cveTipo"), rs.getString("descripcion")
                );
                documentTypes.add(dt);
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return documentTypes;
    }
    public boolean checkCveType(String cveType){//Verfica que no haya claves primarias duplicadas
        int total=0;
        try {
            String query="select count(*) as total from tipoDoc where cveTipo='"+cveType+"'";
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            rs.next();
            total=rs.getInt("total");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return (total>=1)?true:false;
    }

    public Boolean delete(String cveType) {
        try {
            String query = "delete from tipoDoc where cveTipo = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, cveType);
            st.execute();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    public int validateType(String cveType){
        Boolean ban=true;
        int cont=0;
        try {
            String query = "SELECT * FROM documento where cveTipo='"+cveType+"';";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                cont++;
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return cont;
    }

    public Boolean insert(DocumentType type) {
        try {
            String query = "insert into tipoDoc "
                    + " (cveTipo,descripcion)"
                    + " values (?, ?)";
            PreparedStatement st =  conn.prepareStatement(query);
            st.setString(1, type.getCveType());
            st.setString(2, type.getDescription());
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return false;
    }

    public Boolean update(DocumentType type) {
        try {
            String query = "update tipoDoc "
                    + " set descripcion = ? where cveTipo = ?";
            PreparedStatement st =  conn.prepareStatement(query);
            st.setString(  1, type.getDescription());
            st.setString(  2, type.getCveType());
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return false;
    }

}
