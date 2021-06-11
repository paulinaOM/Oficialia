package pdf4;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import login.MySQL;
import user.UserDAO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.util.ResourceBundle;

public class Pdf4Controller implements Initializable{
    @FXML
    DatePicker Date;
    @FXML
    Button btnGenerate;

    Pdf4DAO pdf4DAO=new Pdf4DAO(MySQL.getConnection());
    String DEST1="oficialia/reports/documentosFecha.pdf",creationTime;

    UserDAO userDAO=new UserDAO(MySQL.getConnection()); //----------------------------------Para invocar metodos de insertar actividad e insertar reporte
    int id=userDAO.searchActive(); //Busca al usuario que actualmente está activo


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnGenerate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                java.util.Date date = new java.util.Date();

                File file = new File(DEST1);
                file.getParentFile().mkdirs();

                try {
                    if(pdf4DAO.getPDF4(Date.getValue().toString()).size()!=0) {
                        System.out.println(pdf4DAO.getPDF4(Date.getValue().toString()));
                        new Pdf().createPdf(DEST1, pdf4DAO.getPDF4(Date.getValue().toString()));
                        System.out.println("Si se puede crear el documento");
                        Alert al = new Alert(Alert.AlertType.INFORMATION);
                        al.setHeaderText("");
                        al.setContentText("Archivo generado exitosamente");
                        al.show();

                        creationTime=date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
                        System.out.println("creation time "+ Time.valueOf(creationTime));
                        userDAO.insertAction("Generó Pdf4", Time.valueOf(creationTime),id); //Inserta la acción en la bitácora de seguimientp
                        int lastAccess=userDAO.getLastAccess();
                        userDAO.insertReportCreation(lastAccess,4,Time.valueOf(creationTime));
                    }else{
                        System.out.println("No se puede crear el documento");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
