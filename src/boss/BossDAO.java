package boss;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
@SuppressWarnings("Duplicates")

public class BossDAO {
    Connection conn;
    public BossDAO(Connection conn)
    {
        this.conn = conn;
    }

    public Boolean insert(Boss boss) {
        try {
            String query = "insert into jefes(nombre,direccion,telefono,imagen)"
                    + " values (?, ?, ?,?)";
            PreparedStatement st =  conn.prepareStatement(query);
            st.setString(1, boss.getName());
            st.setString(2, boss.getAddress());
            st.setInt(3, boss.getPhone());
            st.setBytes(4, boss.getImg());
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return false;
    }


    public ObservableList<Boss> fetchAllBoss() {
        ObservableList<Boss> bosses = FXCollections.observableArrayList();
        try {
            String query = "select * from jefes;";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            Boss b = null;
            while(rs.next()) {
                b = new Boss(
                        rs.getInt("idJefe"),
                        rs.getString("nombre"), rs.getString("direccion"),
                        rs.getInt("telefono"), rs.getBytes("imagen")
                );
                bosses.add(b);
            }
            rs.close();
            st.close();
        } catch (SQLException ex){
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return bosses;
    }
    public int validateBoss(int idBoss){
        Boolean ban=true;
        int cont=0;
        try {
            String query = "SELECT * FROM oficialia where idJefe="+idBoss;
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
    public int fetchBoss(String nameBoss){
        int boss=0;
        try {
            String query = "SELECT idJefe FROM jefes where nombre='"+nameBoss+"'";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                boss= rs.getInt("idJefe");
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return boss;
    }
    public int countBoss(){
        int total=0;
        try {
            String query = "select count(idJefe) total from jefes;";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                total= rs.getInt("total");
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return total;
    }
    public Boss lastBoss(){
        Boss boss=new Boss();
        try {
            String query = "SELECT * FROM jefes ORDER by idJefe DESC LIMIT 1;";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                boss.setId(rs.getInt("idJefe"));
                boss.setName(rs.getString("nombre"));
                boss.setAddress(rs.getString("direccion"));
                boss.setPhone(rs.getInt("telefono"));
                boss.setImg(rs.getBytes("imagen"));
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return boss;
    }

    public Boss selectBoss(int idJefe){
        Boss boss=new Boss();
        try {
            String query = "SELECT * FROM jefes where idJefe="+idJefe+";";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                boss.setId(rs.getInt("idJefe"));
                boss.setName(rs.getString("nombre"));
                boss.setAddress(rs.getString("direccion"));
                boss.setPhone(rs.getInt("telefono"));
                boss.setImg(rs.getBytes("imagen"));
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return boss;
    }

    public Boolean update(Boss boss) {
        try {
            String query = "update jefes "
                    + " set nombre = ?, direccion = ?, telefono = ?, imagen = ? where idJefe = ?";
            PreparedStatement st =  conn.prepareStatement(query);
            st.setString(1, boss.getName());
            st.setString(2, boss.getAddress());
            st.setInt(3, boss.getPhone());
            st.setBytes(4, boss.getImg());
            st.setInt(5, boss.getId());
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return false;
    }
    public Boolean delete(int idBoss) {
        try {
            String query = "delete from jefes where idJefe = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, idBoss);
            st.execute();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
