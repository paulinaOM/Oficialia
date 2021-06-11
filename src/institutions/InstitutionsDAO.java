package institutions;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class InstitutionsDAO {
    Connection conn;
    public InstitutionsDAO(Connection conn)
    {
        this.conn = conn;
    }

    public ObservableList<Institutions> fetchAll(){
        ObservableList<Institutions> institutions = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM institucion";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            Institutions i = null;
            while(rs.next()) {
                i = new Institutions(
                        rs.getString("cveInstitucion"), rs.getString("descripcion")
                );
                institutions.add(i);
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return institutions;
    }

    public Boolean delete(String cveInstitution) {
        try {
            String query = "delete from institucion where cveInstitucion = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, cveInstitution);
            st.execute();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Boolean insert(Institutions institutions) {
        try {
            String query = "insert into institucion "
                    + " (cveInstitucion,descripcion)"
                    + " values (?, ?)";
            PreparedStatement st =  conn.prepareStatement(query);
            st.setString(1, institutions.getCveInstitution());
            st.setString(2, institutions.getDescription());
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return false;
    }
    public int validateInstitution(String cveInstitution){
        Boolean ban=true;
        int cont=0;
        try {
            String query = "select * from documento d " +
                    "inner join procedencia p on d.cveProcedencia=p.cveProcedencia " +
                    "inner join institucion i on p.cveInstitucion=i.cveInstitucion " +
                    "where p.cveInstitucion='"+cveInstitution+"';";
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

    public Boolean update(Institutions institutions) {
        try {
            String query = "update institucion "
                    + " set descripcion = ? where cveInstitucion = ?";
            PreparedStatement st =  conn.prepareStatement(query);
            st.setString(  1, institutions.getDescription());
            st.setString(  2, institutions.getCveInstitution());
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return false;
    }
    public boolean checkCveInstitution(String cveInstitution){//Verfica que no haya claves primarias duplicadas
        int total=0;
        try {
            String query="select count(*) as total from institucion where cveInstitucion='"+cveInstitution+"'";
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            rs.next();
            total=rs.getInt("total");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return (total>=1)?true:false;
    }
    public ObservableList<String> fetch() {
        ObservableList<String> institutions = FXCollections.observableArrayList();
        try {
            String query = "select descripcion from institucion";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {//Va generando un arreglo de empleados
                institutions.add(rs.getString(1));
            }
            rs.close();//cierra resultSet
            st.close();//Cierra sentencia;

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return institutions;
    }
}
