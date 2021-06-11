package documents;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class AddresseeDAO { //DestinatarioDAO
    Connection conn;
    public AddresseeDAO(Connection conn){
        this.conn= conn;
    }

    //Obtiene el nombre de las Areas del Ayuntamiento (para el cmbTownHall)
    public ObservableList fetchAllAreas(){
        ObservableList areasList= FXCollections.observableArrayList();
        String registry="";
        try {
            String query="select descripcion from areasAyuntamiento";
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            while (rs.next()){
                registry=rs.getString(1); //Lo que obtiene de la consulta en el índice de columna 1 (descripcion)
                areasList.add(registry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return areasList;
    }

    //Obtiene la clave del ayuntamiento según el nombre seleccionado en el combo
    public String areaCode(String areaName){
        String areaCode="";
        try {
            String query="select cveArea from areasAyuntamiento where descripcion ='"+areaName+"'";
            Statement st= conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            while (rs.next()){
                areaCode=rs.getString("cveArea");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return areaCode;
    }

    //Obtiene el nombre de las instrucciones (para el cmbInstruction)
    public ObservableList fetchAllInstructions(){
        ObservableList instructList= FXCollections.observableArrayList();
        String registry="";
        try {
            String query="select descripcion from instruccion";
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            while (rs.next()){
                registry=rs.getString("descripcion");
                instructList.add(registry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instructList;
    }

    //Obtiene la clave de la instrucción según el nombre seleccionado en el combo
    public String instructionCode(String instructionName){
        String instructCode="";
        try {
            String query="select cveInstruccion from instruccion where descripcion ='"+instructionName+"'";
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            while (rs.next()){
                instructCode=rs.getString("cveInstruccion");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instructCode;
    }

    //Obtiene descripción de la prioridad (para el cmbPriority)
    public ObservableList fetchAllPriority(){
        ObservableList priorityList= FXCollections.observableArrayList();
        String prioridad="";
        try {
            String query="select descripcion from prioridad";
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            while (rs.next()){
                prioridad=rs.getString("descripcion");
                priorityList.add(prioridad);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return priorityList;
    }

    //Obtiene la clave de la instrucción según el nombre seleccionado en el combo
    public String priorityCode(String priorityName){
        String priorityCode="";
        try {
            String query="select cvePrioridad from prioridad where descripcion ='"+priorityName+"'";
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            while (rs.next()){
                priorityCode=rs.getString("cvePrioridad");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return priorityCode;
    }

    public Boolean insert(Addressee addressee) {
        try {
            String query = "insert into destinatario"
                    + " (nombreRecibe, fechaEntrega, fechaLimite, status,cveArea,cveInstruccion,cvePrioridad)" //NOMBRE DE LOS CAMPOS DE LA TABLA EN LA BD
                    + " values (?, ?, ?, ?,?,?,?)"; //LOS QUE IRÁ RECUPERANDO DEL OBJETO TIPO (clase) Addressee (.java)
            PreparedStatement st =  conn.prepareStatement(query);
            st.setString(   1, addressee.getReceive());
            st.setDate(  2, addressee.getDeliverDate());
            st.setDate(  3, addressee.getLimitDate());
            st.setString(  4, addressee.getStatus());
            st.setString(  5, addressee.getAreaCode());
            st.setString(  6, addressee.getInstructionCode());
            st.setString(  7, addressee.getPriorityCode());
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return false;
    }

    public int getLastCodeAddressee(){ //Obtiene el código del último destinatario que se dio de alta
        int code=0;
        try{
            String query="SELECT MAX(cveDestinatario) FROM destinatario";
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            rs.next();
            code=rs.getInt(1);
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return code;
    }
}
