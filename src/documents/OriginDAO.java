package documents;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class OriginDAO {
    Connection conn;
    public OriginDAO(Connection conn){
        this.conn= conn;
    }

    //Obtiene el nombre de las instituciones de procedencia(para el cmbInstituteOrigin)
    public ObservableList fetchAllInstOrigin(){
        ObservableList institutionsList= FXCollections.observableArrayList();
        String registry="";
        try {
            String query="select descripcion from institucion";
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            while (rs.next()){
                registry=rs.getString("descripcion");
                institutionsList.add(registry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return institutionsList;
    }

    //Obtiene la clave de la institución según el nombre seleccionado en el combo
    public String instCode(String instName){
        String instCode="";
        try {
            String query="select cveInstitucion from institucion where descripcion ='"+instName+"'";
            Statement st= conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            while (rs.next()){
                instCode=rs.getString("cveInstitucion");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instCode;
    }

    public Boolean insert(Origin origin) {
        try {
            String query = "insert into procedencia"
                    + " (remitente, asunto, puesto, destinatario,observaciones,cveInstitucion)" //NOMBRE DE LOS CAMPOS DE LA TABLA EN LA BD
                    + " values (?, ?, ?, ?,?,?)"; //LOS QUE IRÁ RECUPERANDO DEL OBJETO TIPO (clase) Addressee (.java)
            PreparedStatement st =  conn.prepareStatement(query);
            st.setString(   1,origin.getSender());
            st.setString(  2, origin.getAffair());
            st.setString(  3, origin.getPosition());
            st.setString(  4, origin.getAddressee());
            st.setString(  5, origin.getObservations());
            st.setString(  6, origin.getInstitutionCode());
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return false;
    }

    public int getLastCodeOrigin(){ //Obtiene el código del último registro que se dio de alta en la tabla procedencia
        int code=0;
        try{
            String query="SELECT MAX(cveProcedencia) lastcode FROM procedencia";
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            rs.next();
            code=rs.getInt("lastCode");
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return code;
    }

}
