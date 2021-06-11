package user;

//import boss.Boss;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class UserDAO {
    Connection conn;
    public UserDAO(Connection conn)
    {
        this.conn = conn;
    }


    //Obtiene todos los usuarios vigentes
    public ObservableList<User> fetchAllUser() {
        ObservableList<User> users = FXCollections.observableArrayList();
        try {
            String query = "select u.*, r.descripcion  from usuarios u inner join roles r on u.cveRol=r.cveRol where vigencia='Actual';";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            User u = null;
            while(rs.next()) {
                u = new User(
                        rs.getInt("id"), rs.getString("username"),
                        rs.getString("password"), rs.getString("r.descripcion")
                );
                users.add(u);
            }
            rs.close();
            st.close();
        } catch (SQLException ex){
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return users;
    }
    //Recupera la descripción de cada Rol para el combo
    public ObservableList<String> fetchAllRol() {
        ObservableList<String> rol = FXCollections.observableArrayList();
        try {
            String query = "select descripcion from roles;";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            while(rs.next()) {
                rol.add(rs.getString("descripcion"));
            }
            rs.close();
            st.close();
        } catch (SQLException ex){
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return rol;
    }
    //Recupera la clave del Rol
    public String fetchCveRol(String rol){
        String cveRol="";
        try {
            String query = "SELECT cveRol FROM roles where descripcion='"+rol+"'";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                cveRol= rs.getString("cveRol");
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return cveRol;
    }
    //Se encarga de dar de baja un usuario
    public Boolean delete(User user) {
        try {
            String query = "update usuarios "
                    + " set vigencia='Baja' where id = ?";
            PreparedStatement st =  conn.prepareStatement(query);
            st.setInt(1,user.getId());
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return false;
    }
    public Boolean insert(User user) {
        try {
            String query = "insert into usuarios "
                    + " (username,password,cveRol,vigencia)"
                    + " values (?, md5(?), ?, 'Actual')";
            PreparedStatement st =  conn.prepareStatement(query);
            st.setString(1, user.getUsername());
            st.setString(2, user.getPassword());
            st.setString(3, user.getRol());
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return false;
    }

    public Boolean update(User user) {
        try {
            String query = "update usuarios "
                    + " set username = ?, password= md5(?), cveRol= ? where id = ?";
            PreparedStatement st =  conn.prepareStatement(query);
            st.setString(  1, user.getUsername());
            st.setString(  2, user.getPassword());
            st.setString(  3, user.getRol());
            st.setInt(4,user.getId());
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return false;
    }

    //Comprueba que usuario y contraseña coincidan para iniciar sesión, y regresa el id del usuario
    public int validUser(String user,String password){
        ResultSet rs = null;
        int id=0;
        try {
            String query = "SELECT id " +
                    "FROM usuarios " +
                    "where username = '" + user + "'"+
                    "and password = md5('"+password+"') and vigencia='Actual'";

            Statement st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()){
                id = rs.getInt("id");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return id;
    }
    //Busca al usuario activo
    public int searchActive(){
        ResultSet rs = null;
        int id=0;
        try {
            String query = "SELECT id " +
                    "FROM usuarios " +
                    "where status = 'Activo'";

            Statement st = conn.createStatement();
            rs = st.executeQuery(query);
            rs.next();
            id = rs.getInt("id");

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return id;
    }
    //Obtiene el Rol del usuario Activo
    public String searchActiveRol(){
        ResultSet rs = null;
        String rol="";
        try {
            String query = "select cveRol from usuarios where status='Activo';";

            Statement st = conn.createStatement();
            rs = st.executeQuery(query);
            rs.next();
            rol = rs.getString("cveRol");

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return rol;
    }



    //Pone el status como activo cuando se inicia sesion
    public void activateStatus(int id) {
        try {
            System.out.println(id);
            String query = "update usuarios "
                    + " set status = ? where id ="+id;
            PreparedStatement st =  conn.prepareStatement(query);
            st.setString(  1, "Activo");
            st.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Cambia el status a inactivo cuando sale de la aplicación
    public void desactivateStatus(int id) {
        try {
            System.out.println(id);
            String query = "update usuarios "
                    + " set status = ? where id ="+id;
            PreparedStatement st =  conn.prepareStatement(query);
            st.setString(  1, "Inactivo");
            st.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //------------------------------------------------------------------------------------------------
    public boolean insertAccess(Time startTime,int id) {
        try {
            String query = "insert into accesos "
                    + " (fecha,horaInicio,horaFin,id)"
                    + " values (now(), ?, '00:00:00',?)";
            PreparedStatement st =  conn.prepareStatement(query);
            //st.setDate(1,date);
            st.setTime(1,startTime);
            st.setInt(2,id);
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean updateAccess(int idAccess, Time endingTime){
        try {
            String query = "update accesos set horaFin=? where idAcceso="+idAccess;
            PreparedStatement st =  conn.prepareStatement(query);
            st.setTime(1,endingTime);
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return false;
    }

    public int getLastAccess(){ //Obtiene id del ultimo acceso
        int id=0;
        try {
            String query = "select idAcceso from accesos order by idAcceso desc limit 1";
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            rs.next();
            id=rs.getInt("idAcceso");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return id;
    }

    public boolean checkUserName(String username){//Verfica que no haya claves primarias duplicadas
        int total=0;
        try {
            String query="select count(*) as total from usuarios where username='"+username+"'";
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(query);
            rs.next();
            total=rs.getInt("total");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return (total>=1)?true:false;
    }

    public boolean insertAction(String action, Time time, int id){ //Bitacora de seguimiento
        try {
            String query = "insert into bitacoraseguimiento"
                    + " (accion,hora,fecha,idAcceso)"
                    + " values (?, ?, curdate(),?)";
            PreparedStatement st =  conn.prepareStatement(query);
            st.setString(1,action);
            st.setTime(2,time);
            st.setInt(3,id);
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean insertReportCreation(int idAccess,int idReport, Time time){ //Inserta un registro en la tabla de generación de reportes
        try {
            String query = "insert into bitacorareportes"
                    + " (idAcceso, idReporte, fecha, hora)"
                    + " values (?, ?, curdate(),?)";
            PreparedStatement st =  conn.prepareStatement(query);
            st.setInt(1,idAccess);
            st.setInt(2,idReport);
            st.setTime(3,time);
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return false;
    }


}