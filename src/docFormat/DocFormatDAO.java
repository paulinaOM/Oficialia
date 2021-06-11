package docFormat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class DocFormatDAO {
    Connection conn;
    public DocFormatDAO(Connection conn)
    {
        this.conn = conn;
    }

    public ObservableList<DocFormat> fetchAll() {
        ObservableList<DocFormat> docformat = FXCollections.observableArrayList();
        try {
            String query = "select * from formato";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            DocFormat p = null;
            while(rs.next()) {//Va generando un arreglo de empleados
                p = new DocFormat( //CON NOMBRE DE CAMPOS DE LA TABLA DE LA BD
                        rs.getString("cveFormato"), rs.getString("descripcion")
                );
                docformat.add(p);
            }
            rs.close();//cierra resultSet
            st.close();//Cierra sentencia;

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return docformat;
    }

    public int validateFormat(String cveFormato){
        Boolean ban=true;
        int cont=0;
        try {
            String query = "SELECT * FROM documento where cveFormato='"+cveFormato+"';";
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

    public Boolean delete(String cveformato) {
        try {
            String query = "delete from formato where cveFormato = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, cveformato);
            st.execute();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Boolean insert(DocFormat docformat) {
        try {
            String query = "insert into formato "
                    + " (cveFormato,descripcion)" //NOMBRE DE LOS CAMPOS DE LA TABLA EN LA BD
                    + " values (?, ?)"; //LOS QUE IRÁ RECUPERANDO DEL OBJETO TIPO (clase) Employee (.java)
            PreparedStatement st =  conn.prepareStatement(query);
            st.setString(1, String.valueOf(docformat.getCveFormato()));
            st.setString(2, String.valueOf(docformat.getDescripcion()));
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return false;
    }

    public Boolean update(DocFormat docformat) {
        try {
            String query = "update formato "
                    + " set descripcion=?" //NOMBRE DE LOS CAMPOS DE LA TABLA EN LA BD
                    + " where cveFormato=?"; //noEmp es la clave primaria de la tabla EN LA BD
            PreparedStatement st =  conn.prepareStatement(query);
            st.setString(1,docformat.getDescripcion());
            st.setString(2,docformat.getCveFormato());
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return false;
    }

    public boolean checkKey(String key){
        boolean ban=false;
        try {
            String query="select * from formato where cveFormato='"+key+"';";
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            while (rs.next()){
                ban=true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ban;
    }


}