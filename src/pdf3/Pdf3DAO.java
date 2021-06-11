package pdf3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.List;

public class Pdf3DAO {
    Connection conn;
    public Pdf3DAO(Connection conn){
        this.conn= conn;
    }

    public List<Pdf3> getPDF3(String name) {
        ObservableList<Pdf3> pdList= FXCollections.observableArrayList();
        Pdf3 registro=null;
        try {
            String query="select d.noFolio,d.noDoc,d.nomDoc,i.descripcion from documento d\n" +
                    "join procedencia p on d.cveProcedencia=p.cveProcedencia\n" +
                    "join institucion i on p.cveInstitucion = i.cveInstitucion\n" +
                    "where i.descripcion='"+name+"';";//noEmp es la clave primaria de la tabla EN LA BD
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            while(rs.next()){
                registro=new Pdf3(rs.getString("nomDoc"),rs.getInt("noFolio"),rs.getInt("noDoc"),rs.getString("descripcion"));
                System.out.println(registro);
                pdList.add(registro);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return pdList;
    }

    /*
    public ObservableList<Pdf3> getPDF3() {
        InstitutionsDAO institutionsDAO=new InstitutionsDAO(MySQL.getConnection());
        ObservableList List;
        List=institutionsDAO.fetch();
        System.out.println(List);
        ObservableList<Pdf3> pdList= FXCollections.observableArrayList();
        Pdf3 registro=null;
        for(int i=0;i<List.size();i++) {
            System.out.println(List.get(i).toString());
            try{
                String query="select d.noFolio,d.noDoc,d.nomDoc,i.descripcion from documento d\n" +
                        "join procedencia p on d.cveProcedencia=p.cveProcedencia\n" +
                        "join institucion i on p.cveInstitucion = i.cveInstitucion\n" +
                        "where i.descripcion='"+List.get(i).toString()+"';";
                Statement st=conn.createStatement();
                ResultSet rs=st.executeQuery(query);
                while(rs.next()){
                    registro=new Pdf3(rs.getString("nomDoc"),rs.getInt("noFolio"),rs.getInt("noDoc"),rs.getString("descripcion"));
                    System.out.println(registro);
                    pdList.add(registro);
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }

        return pdList;
    }
    */

    public List<Pdf3> getPDF3All(String name) {
        ObservableList<Pdf3> pdList= FXCollections.observableArrayList();
        Pdf3 registro=null;
        try {
            String query="select d.noFolio,d.noDoc,d.nomDoc,i.descripcion from documento d\n" +
                    "join procedencia p on d.cveProcedencia=p.cveProcedencia\n" +
                    "join institucion i on p.cveInstitucion = i.cveInstitucion;";//noEmp es la clave primaria de la tabla EN LA BD
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            while(rs.next()){
                registro=new Pdf3(rs.getString("nomDoc"),rs.getInt("noFolio"),rs.getInt("noDoc"),rs.getString("descripcion"));
                System.out.println(registro);
                pdList.add(registro);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return pdList;
    }
/*
    public ObservableList<Pdf3> fetch() {
        ObservableList<Pdf3> pdf3 = FXCollections.observableArrayList();
        try {
            String query = "select noFolio,noDoc,nomDoc from documento";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            Pdf3 p = null;
            while(rs.next()) {//Va generando un arreglo de empleados
                p = new Pdf3( //CON NOMBRE DE CAMPOS DE LA TABLA DE LA BD
                        rs.getString(""),rs.getString(""),rs.getString(3),rs.getInt(1), rs.getInt(2)
                );
                pdf3.add(p);
            }
            rs.close();//cierra resultSet
            st.close();//Cierra sentencia;

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return pdf3;
    }
*/

}
