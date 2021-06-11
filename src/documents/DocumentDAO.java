package documents;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.sql.*;

public class DocumentDAO {
    Connection conn;
    public DocumentDAO(Connection conn){
        this.conn= conn;
    }

    //Obtiene el nombre de los formatos  (para el cmbFormat)
    public ObservableList fetchAllFormats(){
        ObservableList formatsList= FXCollections.observableArrayList();
        String registry="";
        try {
            String query="select descripcion from formato";
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            while (rs.next()){
                registry=rs.getString(1); //Lo que obtiene de la consulta en el índice de columna 1 (descripcion)
                formatsList.add(registry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return formatsList;
    }

    //Obtiene la clave del formato según el nombre seleccionado en el combo
    public String formatCode(String formatName){
        String formatCode="";
        try {
            String query="select cveFormato from formato where descripcion ='"+formatName+"'";
            Statement st= conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            while (rs.next()){
                formatCode=rs.getString("cveFormato");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return formatCode;
    }

    //Obtiene el nombre de los tipos de documento (para el cmbType)
    public ObservableList fetchAllTypes(){
        ObservableList typesList= FXCollections.observableArrayList();
        String registry="";
        try {
            String query="select descripcion from tipoDoc";
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            while (rs.next()){
                registry=rs.getString(1); //Lo que obtiene de la consulta en el índice de columna 1 (descripcion)
                typesList.add(registry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return typesList;
    }

    //Obtiene la clave de tipo de documento según el nombre seleccionado en el combo
    public String typeCode(String typeName){
        String typeCode="";
        try {
            String query="select cveTipo from tipoDoc where descripcion ='"+typeName+"'";
            Statement st= conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            while (rs.next()){
                typeCode=rs.getString("cveTipo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return typeCode;
    }

    //Verifica que no se duplique la clave primaria
    public boolean checkFolio(int noFolio, int noDoc){
        boolean ban=false;
        try {
            String query="select * from documento where noFolio="+noFolio+" and noDoc="+noDoc;
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

    //Insertar en la tabla documento
    public void insert(Document doc) {
        String sql = "insert into documento (noFolio, noDoc, nomDoc, fechaDoc, fechaRecep, archivoDoc,cveProcedencia,cveDestinatario,cveFormato,cveTipo) values (?, ?, ?, ?, ?, ?, ?, ?,?,?);";
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(sql);
            st.setInt(1, doc.getNoFolio());
            st.setInt(2, doc.getNoDoc());
            st.setString(3,doc.getDocName());
            st.setDate(4, doc.getDocDate());
            st.setDate(5, doc.getReceptionDate());
            st.setBytes(6, doc.getDocPDF());
            st.setInt(7, doc.getOriginCode());
            st.setInt(8, doc.getAddresseeCode());
            st.setString(9, doc.getFormatCode());
            st.setString(10, doc.getTypeCode());
            st.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("SQL insertar");
            System.out.println(ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Exception insertar");
            System.out.println(ex.getMessage());
        }
    }

    public byte[] getDocument(int noFolio, int noDoc){
        byte[] doc=null;
        try {
            String query="select archivoDoc from documento where noFolio="+noFolio+" and noDoc="+noDoc;
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            rs.next();
            doc=rs.getBytes("archivoDoc");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doc;
    }

}