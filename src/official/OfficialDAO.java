package official;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import official.Official;

import java.sql.*;
@SuppressWarnings("Duplicates")

public class OfficialDAO {
    Connection conn;
    public OfficialDAO(Connection conn)
    {
        this.conn = conn;
    }

    public ObservableList<String> fetchState(){
        ObservableList<String> states = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM estados";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                states.add(rs.getString("nomEstado"));
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return states;
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


    public String fetchIdMun(String state, String mun){
        String municipality="";
        try {
            String query = "SELECT * FROM municipios m inner join estados e on m.idEdo=e.idEdo where nomEstado='"+state+"' and nomMunicipio='"+mun+"'";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                municipality= rs.getString("idMun");
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return municipality;
    }
    public ObservableList<String> fetchMunicipality(String state){
        ObservableList<String> municipality = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM municipios m inner join estados e on m.idEdo=e.idEdo where nomEstado='"+state+"' order by nomMunicipio";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                municipality.add(rs.getString("nomMunicipio"));
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return municipality;
    }
    public ObservableList<Official> fetchOfficial(String municipality) {
        ObservableList<Official> officials = FXCollections.observableArrayList();
        try {
            String query = "select * from oficialia o inner join municipios m on o.idMun = m.idMun " +
                    "inner join estados e on m.idEdo = e.idEdo inner join jefes j on o.idJefe = j.idJefe where nomMunicipio='"+municipality+"';";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            Official o = null;
            while(rs.next()) {
                o = new Official(
                        rs.getInt("cveOficialia"),
                        rs.getString("nomEstado"), rs.getString("nomMunicipio"),
                        rs.getString("direccion"), rs.getInt("telefono"),
                        rs.getString("nombre"), rs.getString("horario"),
                        rs.getInt("idMun"),rs.getInt("idJefe")
                );
                officials.add(o);
            }
            rs.close();
            st.close();
        } catch (SQLException ex){
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return officials;
    }
    //obtiene el nombre del jefe de la oficialia
    public String getBossName(int id){
        String name="";
        try {
            String query = "SELECT nombre FROM oficialia o inner join jefes j on j.idJefe=o.idJefe where cveOficialia="+id;
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                name= rs.getString("nombre");
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return name;

    }


    public Boolean delete(String cveOfficial) {
        try {
            String query = "delete from oficialia where cveOficialia = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, cveOfficial);
            st.execute();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Boolean insert(Official official) {
        try {
            String query = "insert into oficialia "
                    + " (direccion,horario,telefono,idMun,idJefe)"
                    + " values (?, ?, ?, ?, ?)";
            PreparedStatement st =  conn.prepareStatement(query);
            st.setString(1, official.getAddress());
            st.setString(2, official.getSchedule());
            st.setString(3, String.valueOf(official.getPhone()));
            st.setString(4, String.valueOf(official.getIdMun()));
            st.setString(5, String.valueOf(official.getIdBoss()));
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return false;
    }

    public Boolean update(Official official) {
        try {
            String query = "update oficialia "
                    + " set direccion = ?, horario = ?, telefono = ?, idMun = ?, idJefe = ? where cveOficialia = ?";
            PreparedStatement st =  conn.prepareStatement(query);
            st.setString(1, official.getAddress());
            st.setString(2, official.getSchedule());
            st.setInt(3, official.getPhone());
            st.setInt(4, official.getIdMun());
            st.setInt(5, official.getIdBoss());
            st.setInt(6, official.getId());

            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return false;
    }

}
