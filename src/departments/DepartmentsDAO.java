package departments;
import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class DepartmentsDAO {
    Connection conn;
    public DepartmentsDAO(Connection conn){
        this.conn=conn;
    }

    public ObservableList<Departments> fetchAllDepartments(){
        ObservableList<Departments> depList= FXCollections.observableArrayList();
        Departments dep=null;
        try {
            String query="select * from areasayuntamiento;";
            Statement st=conn.createStatement();
            ResultSet rs= st.executeQuery(query);
            while (rs.next()){
                dep=new Departments(rs.getString(1),rs.getString(2));
                depList.add(dep);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return depList;
    }

    public Departments fetchDepartment(String areaCode){
        Departments dept=new Departments("","");
        return dept;
    }

    public Boolean insertDep(Departments dept){
        try {
            String query = "insert into areasayuntamiento"
                    + " (cveArea, descripcion)" //NOMBRE DE LOS CAMPOS DE LA TABLA EN LA BD
                    + " values (?, ?)"; //LOS QUE IRÁ RECUPERANDO DEL OBJETO TIPO (clase) Departments(.java)
            PreparedStatement st =  conn.prepareStatement(query);
            st.setString(   1, dept.getAreaCode());
            st.setString(  2, dept.getAreaName());
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Boolean updateDep(String deptCode, String deptName){
        try {
            String query = "update areasayuntamiento "
                    + " set descripcion = ? where cveArea=?";
            PreparedStatement st =  conn.prepareStatement(query);

            st.setString(1, deptName);
            st.setString(2, deptCode);
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return false;
    }
    public Boolean deleteDept(String deptCode){
        try {
            String query = "delete from areasayuntamiento where cveArea= ?";//noEmp es la clave primaria de la tabla EN LA BD
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, deptCode);
            st.execute();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean checkAreaCode(String areaCode){//Verfica que no haya claves primarias duplicadas
        int total=0;
        try {
            String query="select count(*) as total from areasayuntamiento where cveArea='"+areaCode+"'";
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            rs.next();
            total=rs.getInt("total");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return (total>=1)?true:false;
    }

    public boolean checkForeignKey(String areaCode){ //Verifica que no sea una clave foranea para poder borrar el departamennto
        int total=0;
        try {
            String query="select count(*) as total from destinatario where cveArea='"+areaCode+"'";
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            rs.next();
            total=rs.getInt("total");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return (total>=1)?true:false;
    }
}