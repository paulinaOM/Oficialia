package configuration;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import official.Official;

import java.sql.*;

@SuppressWarnings("Duplicates")

public class ConfigurationDAO {
    Connection conn;
    public ConfigurationDAO(Connection conn)
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


}
